package id.syaafiqi.photo_gallery_app.core.domain

interface DefaultObserver<T : Any> {
    fun onSuccess(entity: T)
    fun onError(exception: Throwable)
}