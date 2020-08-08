package com.example.myapplication.util

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types.newParameterizedType

@SuppressLint("HardwareIds")
fun getAndroidID(context: Context): String {
    return Settings.Secure.getString(
        context.contentResolver,
        Settings.Secure.ANDROID_ID
    )
}

fun String?.toIntOrDefault(default: Int = 0): Int {
    return this?.toIntOrNull()?:default
}

fun String?.responseSucess(): Boolean {
    return if (this != null) {
        this.indexOf("\"ok\": true") >= 0
    } else {
        false
    }
}

inline fun <reified T: Any> List<T>.toJson(): String? {
    val moshi: Moshi = Moshi.Builder().build()
    val type = newParameterizedType(MutableList::class.java, T::class.java)
    val adapter: JsonAdapter<List<T>> = moshi.adapter(type)
    return adapter.toJson(this)
}

inline fun <reified T: Any> T.toJson(): String? {
    val moshi: Moshi = Moshi.Builder().build()
    val adapter: JsonAdapter<T> = moshi.adapter(T::class.java)
    return adapter.toJson(this)
}

inline fun <reified T: Any> String.toModel(): T? {
    val moshi: Moshi = Moshi.Builder().build()
    val adapter: JsonAdapter<T> = moshi.adapter<T>(T::class.java)
    return adapter.fromJson(this)
}