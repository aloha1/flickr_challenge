package yunwen.exhibition.flickr_challenge.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import yunwen.exhibition.flickr_challenge.Constants.APP_TITLE
import yunwen.exhibition.flickr_challenge.Constants.CONTENT_SEARCH
import yunwen.exhibition.flickr_challenge.Constants.DEFAULT_DEBOUNCE_TIME
import yunwen.exhibition.flickr_challenge.R
import yunwen.exhibition.flickr_challenge.model.FlickrItem

@Composable
fun SearchScreen(
    viewModel: FlickrViewModel,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Column {
        TopBar()
        SearchBar(
            modifier = modifier,
            onSearchTextChanged = { query -> viewModel.search(query) }
        )
        LoadingScreenWithLazyColumn(
            uiState = uiState,
            navController = navController
        )
    }
}

@Composable
fun TopBar() {
    Text(
        text = APP_TITLE,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(color = Color.Black)
            .wrapContentHeight(Alignment.CenterVertically),
        color = Color.White,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )
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
fun LoadingScreenWithLazyColumn(uiState: UiState, navController: NavController) {
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
                GridItems(uiState.items, navController)
            }
        }
    }
}

@Composable
fun GridItems(flickrItem: List<FlickrItem>, navController: NavController) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(flickrItem.size) { index ->
            ThumbnailImage(flickrItem[index], navController, index)
        }
    }
}

@Composable
fun ThumbnailImage(flickrItem: FlickrItem, navController: NavController, index: Int) {
    Column(modifier = Modifier
        .padding(8.dp)
        .clickable(onClick = {navController.navigate("detail/${index}")})
        .semantics {
            contentDescription = flickrItem.title // Accessibility
        }) {
        Image(
            painter = rememberAsyncImagePainter(flickrItem.media.imageUrl),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .semantics {
                    contentDescription = flickrItem.title // Accessibility
                },
            contentScale = ContentScale.Crop
        )
    }
}