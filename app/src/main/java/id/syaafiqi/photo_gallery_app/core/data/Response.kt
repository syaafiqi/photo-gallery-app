package id.syaafiqi.photo_gallery_app.core.data

import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("message") val message: String = "Something went wrong!"
)