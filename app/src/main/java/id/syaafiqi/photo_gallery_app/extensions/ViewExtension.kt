package id.syaafiqi.photo_gallery_app.extensions

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide

internal fun View.hideKeyboard() {
    val inputMethodManager: InputMethodManager =
        context.applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
}

internal fun View.show() {
    visibility = View.VISIBLE
}

internal fun View.hide() {
    visibility = View.GONE
}

internal fun View.invisible() {
    visibility = View.INVISIBLE
}

internal fun ImageView.load(url: String) {
    Glide.with(this.context).load(url).into(this)
}

internal fun View.showToast(any: Any) =
    Toast.makeText(context, any as String, Toast.LENGTH_SHORT).show()