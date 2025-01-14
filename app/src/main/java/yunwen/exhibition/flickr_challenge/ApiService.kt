package yunwen.exhibition.flickr_challenge

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("services/feeds/photos_public.gne?")
    suspend fun fetchData(
        @Query("format") format: String = "json",
        @Query("nojsoncallback") nojsoncallback: Int = -1,
        @Query("tags") tags: String = ""
    ): FlickrData
}