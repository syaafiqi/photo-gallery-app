package id.syaafiqi.photo_gallery_app.core.data

import com.google.gson.annotations.SerializedName

data class PagedRequest(
    var page: Int = 1,
    @SerializedName("per_page")
    var limit: Int = 10,
    @SerializedName("query")
    var keyword: String = ""
)