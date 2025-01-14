package yunwen.exhibition.flickr_challenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import yunwen.exhibition.flickr_challenge.Constants.KEY_AUTHOR
import yunwen.exhibition.flickr_challenge.Constants.KEY_DESCRIPTION
import yunwen.exhibition.flickr_challenge.Constants.KEY_IMAGE
import yunwen.exhibition.flickr_challenge.Constants.KEY_PUBLISHED
import yunwen.exhibition.flickr_challenge.Constants.KEY_TITLE

class DetailActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val title = intent.getStringExtra(KEY_TITLE)
        val author = intent.getStringExtra(KEY_AUTHOR)
        val image = intent.getStringExtra(KEY_IMAGE)
        val description = intent.getStringExtra(KEY_DESCRIPTION)
        val published = intent.getStringExtra(KEY_PUBLISHED)

        setContent {
            ProductDetails(
                title = title.orEmpty(),
                author = author.orEmpty(),
                image = image.orEmpty(),
                description = description.orEmpty(),
                published = published.orEmpty()
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProductDetails(
    title: String,
    author: String,
    image: String,
    description: String,
    published: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        GlideImage(
            model = image,
            modifier = Modifier.fillMaxWidth().heightIn(max = 300.dp),
            contentDescription = "Big",
            contentScale = ContentScale.FillWidth
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(start = 12.dp, end = 12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
            )
            Text(
                text = author,
                style = MaterialTheme.typography.bodySmall,
            )
            Text(
                text = published,
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}


