package com.example.myapplication.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.myapplication.network.Service
import com.example.myapplication.network.dto.SessionPost
import com.example.myapplication.util.Memoria
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class LoginViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {

    private var viewModelJob = Job()
    override val coroutineContext: CoroutineContext = viewModelJob + Dispatchers.Main

    fun login(username: String, password: String, callback: (success: Boolean, error: String?) -> Unit) {
        launch {
            val response = Service.api.createSession(SessionPost(username, password)).await()
            if (response.code() == 200) {
                Memoria.session = response.body()
                callback(true, null)
            } else {
                callback(false, response.errorBody()?.string())
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}