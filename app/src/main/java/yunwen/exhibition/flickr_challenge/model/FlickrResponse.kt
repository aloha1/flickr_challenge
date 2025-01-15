package yunwen.exhibition.flickr_challenge.model

import com.google.gson.annotations.SerializedName

data class FlickrResponse(
    val title: String,
    val link: String,
    val description: String?,
    val modified: String,
    val generator: String,
    val items: List<FlickrItem>
)

data class FlickrItem(
    val title: String,
    val link: String,
    val media: Media,
    @SerializedName("date_taken") val dateTaken: String,
    val description: String,
    val published: String,
    val author: String,
    @SerializedName("author_id") val authorId: String,
    val tags: String
)

data class Media(
    @SerializedName("m") val imageUrl: String
)