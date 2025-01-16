package yunwen.exhibition.flickr_challenge

import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import yunwen.exhibition.flickr_challenge.Constants.INITIAL_STATE

class FlickrViewModelTest {

    @Test
    fun `test UI State Changes While Loading`() = runTest {
        val viewModel = FlickrViewModel()
        assertEquals(UiState.Error(INITIAL_STATE), viewModel.uiState.value)
        viewModel.search()
        delay(1000L)
        assertEquals(UiState.Loading, viewModel.uiState.value)
    }
}