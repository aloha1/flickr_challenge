package yunwen.exhibition.flickr_challenge.ui

import android.content.Context
import android.content.Intent
import android.text.Html
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import yunwen.exhibition.flickr_challenge.Constants.SHARED_ME
import yunwen.exhibition.flickr_challenge.Constants.SHARE_VIA
import yunwen.exhibition.flickr_challenge.Constants.TEXT_AUTHOR
import yunwen.exhibition.flickr_challenge.Constants.TEXT_DATE_PUBLISHED
import yunwen.exhibition.flickr_challenge.Constants.TEXT_DESCRIPTION
import yunwen.exhibition.flickr_challenge.Constants.TEXT_TITLE
import yunwen.exhibition.flickr_challenge.model.FlickrItem

@Composable
fun DetailScreen(
    item: FlickrItem, navController: NavController
) {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Image(
                painter = rememberAsyncImagePainter(item.media.imageUrl),
                contentDescription = item.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(bottom = 12.dp)
            )
            Text(text = TEXT_TITLE + item.title, fontWeight = FontWeight.Bold)
            Text(text = TEXT_AUTHOR + item.author)
            Text(text = TEXT_DATE_PUBLISHED + item.published)

            val description = Html.fromHtml(item.description, Html.FROM_HTML_MODE_LEGACY).toString()
            Text(text = TEXT_DESCRIPTION + description)

            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = { shareImage(item, navController.context) }) {
                Text(text = SHARED_ME)
            }
        }
    }
}

fun shareImage(item: FlickrItem, context: Context) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(
            Intent.EXTRA_TEXT,
            "Share the picture!\n\nTitle: ${item.title}\nAuthor: ${item.author}\nPublished: ${item.published}\nURL: ${item.media.imageUrl}"
        )
    }
    context.startActivity(Intent.createChooser(shareIntent, SHARE_VIA))
}