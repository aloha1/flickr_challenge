package yunwen.exhibition.flickr_challenge

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import yunwen.exhibition.flickr_challenge.Constants.DEFAULT_DEBOUNCE_TIME
import yunwen.exhibition.flickr_challenge.ui.theme.FlickrColor

@Composable
fun SearchScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    onCLick: (Context, ItemDetail) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Column {
        SearchBar(
            modifier = modifier,
            onSearchTextChanged = { query ->
                viewModel.search(query)
            }
        )
        LoadingScreenWithLazyColumn(uiState = uiState, onCLick = onCLick)
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onSearchTextChanged: (String) -> Unit = {},
    debounceTimeMills: Long = DEFAULT_DEBOUNCE_TIME
) {
    val focusManager = LocalFocusManager.current
    var searchQuery by remember { mutableStateOf("") }
    var searchJob: Job? by remember { mutableStateOf(null) }
    val scope = rememberCoroutineScope()
    OutlinedTextField(
        value = searchQuery,
        onValueChange = { newText ->
            searchQuery = newText
            searchJob?.cancel()
            searchJob = scope.launch {
                delay(debounceTimeMills)
                onSearchTextChanged(newText)
            }
        },
        singleLine = true,
        keyboardActions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearchTextChanged(searchQuery)
                focusManager.clearFocus()
            }
        ),
        placeholder = {
            Text(
                text = "Enter to Search",
                modifier = Modifier.background(Color.White)
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = ""
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 25.dp, end = 25.dp, top = 16.dp)
    )
}

@Composable
fun LoadingScreenWithLazyColumn(uiState: UiState, onCLick: (Context, ItemDetail) -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
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

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MenuItemsList(item: Flickr, onClick: (Context, ItemDetail) -> Unit) {
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(
                        text = item.title, style = MaterialTheme.typography.headlineSmall
                    )
                    GlideImage(
                        model = item.media.mediaUrl,
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(12.dp))
                            .clickable(onClick = { onClick(context, item) }),
                        contentDescription = "Image",
                    )
                }
            }
        })
    }
}
