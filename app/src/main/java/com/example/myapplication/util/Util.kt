package com.example.myapplication.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import com.example.myapplication.App
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types.newParameterizedType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

fun <T> debounce(
    waitMs: Long = 300L,
    coroutineScope: CoroutineScope,
    destinationFunction: (T) -> Unit
): (T) -> Unit {
    var debounceJob: Job? = null
    return { param: T ->
        debounceJob?.cancel()
        debounceJob = coroutineScope.launch {
            delay(waitMs)
            destinationFunction(param)
        }
    }
}

fun internetDisponivel(): Boolean {
    val connectivityManager =
        App.appContext!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val capabilities =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        } else {
            return true
        }

    if (capabilities != null) {
        when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                return true
            }
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                return true
            }
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                return true
            }
        }
    }
    return false
}

