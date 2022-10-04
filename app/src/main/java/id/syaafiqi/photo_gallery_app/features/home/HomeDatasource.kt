package id.syaafiqi.photo_gallery_app.features.home

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface HomeDatasource {
    @GET("photos")
    fun getPhotosList(
        @Query("page") page: Int,
        @Query("per_page") limit: Int,
    ): Observable<List<HomeResponse>>

    @GET("search/photos")
    fun getPhotosListFiltered(
        @Query("page") page: Int,
        @Query("per_page") limit: Int,
        @Query("query") keyword: String
    ): Observable<SearchResponse>
}