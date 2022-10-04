package id.syaafiqi.photo_gallery_app.utils

import android.content.Context
import androidx.swiperefreshlayout.widget.CircularProgressDrawable

object Utility {
    fun createLoaderPlaceholder(context: Context): CircularProgressDrawable {
        val loaderPlaceholder = CircularProgressDrawable(context)
        with(loaderPlaceholder) {
            strokeWidth = 10f
            centerRadius = 50f
            start()
        }
        return loaderPlaceholder
    }
}