package yunwen.exhibition.flickr_challenge

import com.google.gson.annotations.SerializedName

data class Flickr(
    val items: List<ItemDetail>
)

data class ItemDetail(
    val title: String,
    val media: Media,
    val description: String,
    val published: String,
    val author: String
)

data class Media(
    @SerializedName("m")
    val mediaUrl: String
)