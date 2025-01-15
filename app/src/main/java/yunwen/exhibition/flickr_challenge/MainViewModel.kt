package yunwen.exhibition.flickr_challenge

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import yunwen.exhibition.flickr_challenge.Constants.BASE_URL
import yunwen.exhibition.flickr_challenge.Constants.INITIAL_STATE
import yunwen.exhibition.flickr_challenge.Constants.UNKNOWN_ERROR
import yunwen.exhibition.flickr_challenge.Constants.KEY_AUTHOR
import yunwen.exhibition.flickr_challenge.Constants.KEY_DESCRIPTION
import yunwen.exhibition.flickr_challenge.Constants.KEY_IMAGE
import yunwen.exhibition.flickr_challenge.Constants.KEY_PUBLISHED
import yunwen.exhibition.flickr_challenge.Constants.KEY_TITLE

class MainViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Error(INITIAL_STATE))
    val uiState: StateFlow<UiState> = _uiState

    private val retrofit =
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .build()
    private val apiService = retrofit.create(ApiService::class.java)

    fun search(searchQuery: String = "") {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading
                val response = apiService.fetchData(tags = searchQuery)
                _uiState.value = UiState.Success(response)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: UNKNOWN_ERROR)
            }
        }
    }

    fun startProductActivity(context: Context, itemDetail: ItemDetail) {
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra(KEY_TITLE, itemDetail.title)
        intent.putExtra(KEY_IMAGE, itemDetail.media.mediaUrl)
        intent.putExtra(KEY_AUTHOR, itemDetail.author)
        intent.putExtra(KEY_DESCRIPTION, itemDetail.description)
        intent.putExtra(KEY_PUBLISHED, itemDetail.published)
        context.startActivity(intent)
    }
}

sealed class UiState {
    object Loading : UiState()
    data class Success(val data: FlickrData) : UiState()
    data class Error(val message: String) : UiState()
}