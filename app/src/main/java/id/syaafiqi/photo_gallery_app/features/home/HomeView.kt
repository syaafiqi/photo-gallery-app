package id.syaafiqi.photo_gallery_app.features.home

interface HomeView {
    fun onShowLoading()
    fun onHideLoading()
    fun onFetchSuccess(models: List<PhotosModel>)
    fun onFetchFailed()
    fun onLoadSuccess(models: List<PhotosModel>)
    fun onLoadFailed()

    var isLastPage: Boolean
    var isLoadMore: Boolean
}