package id.syaafiqi.photo_gallery_app.features.home

import id.syaafiqi.photo_gallery_app.core.data.PagedRequest
import id.syaafiqi.photo_gallery_app.core.domain.DefaultObserver
import id.syaafiqi.photo_gallery_app.services.NetworkProvider
import io.reactivex.disposables.CompositeDisposable

class HomePresenter(
    private val view: HomeView,
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
) {
    private val factory: HomeFactory =
        HomeFactory(NetworkProvider.retrofitInstance.create(HomeDatasource::class.java))

    fun getPhotos(paging: PagedRequest) {
        view.onShowLoading()

        val callback = object : DefaultObserver<List<HomeResponse>> {
            override fun onSuccess(entity: List<HomeResponse>) {
                view.onHideLoading()
                if (entity.isEmpty()) view.isLastPage = true
                view.onLoadSuccess(entity.map { item ->
                    PhotosModel(
                        id = item.id,
                        imageLink = item.urls.thumb,
                        imageDescription = item.description,
                        userName = item.user.name
                    )
                })
            }

            override fun onError(exception: Throwable) {
                view.onHideLoading()
                view.onLoadFailed()
            }
        }

        val disposable = if (paging.keyword.isEmpty()) factory.getPhotos(
            paging,
            callback
        ) else factory.getPhotosFiltered(paging, callback)

        compositeDisposable.add(disposable)
    }

    fun fetchPhotos(paging: PagedRequest) {
        view.isLoadMore = true

        val callback =  object : DefaultObserver<List<HomeResponse>> {
            override fun onSuccess(entity: List<HomeResponse>) {
                view.isLoadMore = false
                if (entity.isEmpty()) view.isLastPage = true
                view.onFetchSuccess(entity.map { item ->
                    PhotosModel(
                        id = item.id,
                        imageLink = item.urls.thumb,
                        imageDescription = item.description,
                        userName = item.user.name
                    )
                })
            }

            override fun onError(exception: Throwable) {
                view.isLoadMore = false
                view.onFetchFailed()
            }
        }

        val disposable = if (paging.keyword.isEmpty()) factory.getPhotos(
            paging,
            callback
        ) else factory.getPhotosFiltered(paging, callback)

        compositeDisposable.add(disposable)
    }

    fun onDestroy() {
        compositeDisposable.clear()
    }
}