package id.syaafiqi.photo_gallery_app.features.home

data class PhotosModel(
    val id: String,
    val imageLink: String,
    val imageDescription: String? = null,
    val userName: String
)
