package id.syaafiqi.photo_gallery_app.core

import android.content.Context
import android.content.SharedPreferences
import id.syaafiqi.photo_gallery_app.BuildConfig

object CacheConfig {
  const val USER = "user"
}

fun Context.cache(): SharedPreferences =
    this.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)

fun SharedPreferences.clear() {
  this.edit().clear().apply()
}

inline operator fun <reified T : Any> SharedPreferences.get(key: String): T =
    when (T::class) {
      String::class -> getString(key, "") as T
      Int::class -> getInt(key, 0) as T
      Boolean::class -> getBoolean(key, false) as T
      Long::class -> getLong(key, 0L) as T
      else -> throw UnsupportedOperationException("Unsupported operator function")
    }

operator fun SharedPreferences.set(key: String, value: Any) {
  when (value) {
    is String -> edit { it.putString(key, value) }
    is Int -> edit { it.putInt(key, value) }
    is Boolean -> edit { it.putBoolean(key, value) }
    is Float -> edit { it.putFloat(key, value) }
    is Long -> edit { it.putLong(key, value) }
    else -> throw UnsupportedOperationException("Unsupported operator function")
  }
}

private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
  this.edit().apply(operation).apply()
}