package com.example.myapplication.ui.clients

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.models.Client
import com.example.myapplication.network.Service
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ClientsViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {

    private var viewModelJob = Job()
    override val coroutineContext: CoroutineContext = viewModelJob + Dispatchers.Main

    private val _clients = MutableLiveData<List<Client>>(ArrayList())

    val clients: LiveData<List<Client>> = _clients

    var currentPage = 0
    var maxPage: Boolean = false

    private suspend fun fetchNextPageAsync() = withContext(Dispatchers.IO) {
        async {

            val qClients = Service.api.clientGetAll(currentPage + 1).await()

            if (qClients.isEmpty()) {
                maxPage = true
            } else {
                currentPage += 1

                val prevClients = _clients.value

                if (prevClients == null || prevClients.isEmpty() || currentPage == 1) {
                    withContext(Dispatchers.Main) { _clients.value = qClients }
                } else {
                    val newClients = prevClients.toMutableList()
                    newClients.addAll(qClients)
                    withContext(Dispatchers.Main) { _clients.value = newClients }
                }
            }

        }
    }

    fun fetchNextPage(callback: (() -> Unit)?) {
        launch {
            fetchNextPageAsync().await()
            callback?.let { it() }
        }
    }

    fun refresh(callback: (() -> Unit)?) {
        launch {
            currentPage = 0
            maxPage = false
            fetchNextPageAsync().await()
            callback?.let { it() }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}