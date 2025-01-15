package yunwen.exhibition.flickr_challenge

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import yunwen.exhibition.flickr_challenge.Constants.CONTENT_SEARCH
import yunwen.exhibition.flickr_challenge.Constants.DEFAULT_DEBOUNCE_TIME
import yunwen.exhibition.flickr_challenge.ui.theme.FlickrColor

@Composable
fun SearchScreen(
    viewModel: MainViewModel, modifier: Modifier = Modifier, onCLick: (Context, ItemDetail) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Column {
        SearchBar(modifier = modifier, onSearchTextChanged = { query ->
            viewModel.search(query)
        })
        LoadingScreenWithLazyColumn(uiState = uiState, onCLick = onCLick)
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onSearchTextChanged: (String) -> Unit = {},
    debounceTimeMillis: Long = DEFAULT_DEBOUNCE_TIME
) {
    var searchQuery by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    var searchJob: Job? by remember { mutableStateOf(null) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    OutlinedTextField(
        value = searchQuery,
        onValueChange = { newText ->
            searchQuery = newText
            searchJob?.cancel()
            searchJob = scope.launch {
                delay(debounceTimeMillis)
                onSearchTextChanged(newText)
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text, imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(onSearch = {
            onSearchTextChanged(searchQuery)
            focusManager.clearFocus()
        }),
        placeholder = {
            Text(
                text = context.resources.getString(R.string.enter_to_search),
                modifier = Modifier.background(Color.White)
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search, contentDescription = CONTENT_SEARCH
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, top = 16.dp)
            .clip(shape = RoundedCornerShape(4.dp))
            .background(Color.White)
    )
}

@Composable
fun LoadingScreenWithLazyColumn(uiState: UiState, onCLick: (Context, ItemDetail) -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        when (uiState) {
            is UiState.Loading -> {
                CircularProgressIndicator()
            }

            is UiState.Error -> {
                Text(text = uiState.message)
            }

            is UiState.Success -> {
                MenuItemsList(uiState.data, onCLick)
            }
        }
    }
}

@Composable
fun MenuItemsList(item: FlickrData, onClick: (Context, ItemDetail) -> Unit) {
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .padding(top = 12.dp)
    ) {
        items(items = item.items, itemContent = { item ->
            Divider(
                modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                thickness = 1.dp,
                color = FlickrColor.yellow
            )
            Card {
                ListItem(headlineContent = {
                    Text(
                        text = item.title, style = MaterialTheme.typography.headlineSmall
                    )
                }, leadingContent = {
                    AsyncImage(
                        model = ImageRequest.Builder(context).data(item.media.mediaUrl)
                            .crossfade(true).build(),
                        modifier = Modifier.clip(shape = RoundedCornerShape(12.dp)),
                        contentDescription = item.title
                    )
                }, modifier = Modifier.clickable {
                    onClick(context, item)
                })
            }
        })
    }
}
