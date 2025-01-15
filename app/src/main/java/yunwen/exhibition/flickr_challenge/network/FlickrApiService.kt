package yunwen.exhibition.flickr_challenge.network

import retrofit2.http.GET
import retrofit2.http.Query
import yunwen.exhibition.flickr_challenge.model.FlickrResponse

interface FlickrApiService {
    /**
     * Fetch flickr data based on given tags
     * @param tags the keyword to search for pictures, separated by commas
     * @return list of flickr data matching the search
     * */
    @GET("services/feeds/photos_public.gne?format=json&nojsoncallback=1")
    suspend fun fetchDataByTags(@Query("tags") tags: String): FlickrResponse
}