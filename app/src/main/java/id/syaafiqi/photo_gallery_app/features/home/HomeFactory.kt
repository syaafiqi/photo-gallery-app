package id.syaafiqi.photo_gallery_app.features.home

import id.syaafiqi.photo_gallery_app.core.data.PagedRequest
import id.syaafiqi.photo_gallery_app.core.domain.DefaultObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class HomeFactory(private val instance: HomeDatasource) {
    fun getPhotos(pagedRequest: PagedRequest, callback: DefaultObserver<List<HomeResponse>>): Disposable {
        return instance.getPhotosList(pagedRequest.page, pagedRequest.limit)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    callback.onSuccess(it)
                },
                {
                    callback.onError(it)
                }
            )
    }

    fun getPhotosFiltered(pagedRequest: PagedRequest, callback: DefaultObserver<List<HomeResponse>>): Disposable {
        return instance.getPhotosListFiltered(pagedRequest.page, pagedRequest.limit, pagedRequest.keyword)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    callback.onSuccess(it.results)
                },
                {
                    callback.onError(it)
                }
            )
    }
}