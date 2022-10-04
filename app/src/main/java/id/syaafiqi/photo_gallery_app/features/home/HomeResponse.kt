package id.syaafiqi.photo_gallery_app.features.home

import com.google.gson.annotations.SerializedName

data class HomeResponse(
    val id: String,
    val description: String? = null,
    val user: User,
    val urls: Urls,
    val links: Links
) {
    data class User(
        val id: String,
        val username: String,
        val name: String,
        @SerializedName("profile_image")
        val profileImage: ProfileImage
    ) {
        data class ProfileImage(
            val small: String,
            val medium: String
        )
    }

    data class Urls(
        val raw: String,
        val full: String,
        val regular: String,
        val small: String,
        val thumb: String
    )

    data class Links(
        val download: String,
        @SerializedName("download_location")
        val downloadLocation: String
    )
}

data class SearchResponse(
    val total: Int,
    @SerializedName("total_pages")
    val totalPages: Int,
    val results: List<HomeResponse>
)
