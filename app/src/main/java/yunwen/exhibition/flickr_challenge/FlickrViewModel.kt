package yunwen.exhibition.flickr_challenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import yunwen.exhibition.flickr_challenge.Constants.BASE_URL
import yunwen.exhibition.flickr_challenge.Constants.INITIAL_STATE
import yunwen.exhibition.flickr_challenge.Constants.UNKNOWN_ERROR
import yunwen.exhibition.flickr_challenge.model.FlickrItem
import yunwen.exhibition.flickr_challenge.network.FlickrApiService

class FlickrViewModel() : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Error(INITIAL_STATE))
    val uiState: StateFlow<UiState> = _uiState
    private val _items = MutableStateFlow<List<FlickrItem>>(emptyList())
    val items: StateFlow<List<FlickrItem>> = _items

    private val retrofit =
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .build()
    private val flickrApiService = retrofit.create(FlickrApiService::class.java)

    fun search(searchQuery: String = "") {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = UiState.Loading
            try {
                val response = flickrApiService.fetchDataByTags(tags = searchQuery)
                _uiState.value = UiState.Success(response.items)
                _items.value = response.items
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: UNKNOWN_ERROR)
            }
        }
    }
}

sealed class UiState {
    object Loading : UiState()
    data class Success(val items: List<FlickrItem>) : UiState()
    data class Error(val message: String) : UiState()
}