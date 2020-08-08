package com.example.myapplication.ui.marcas

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.models.Marca
import com.example.myapplication.network.Service
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MarcasViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {

    private var viewModelJob = Job()
    override val coroutineContext: CoroutineContext = viewModelJob + Dispatchers.Main

    private val _marcas = MutableLiveData<List<Marca>>(ArrayList())

    val marcas: LiveData<List<Marca>> = _marcas

    var currentPage = 0
    var maxPage: Boolean = false

    private suspend fun fetchNextPageAsync() = withContext(Dispatchers.IO) {
        async {

            val qMarcas = Service.api.marcaGetAll(currentPage + 1).await()

            if (qMarcas.isEmpty()) {
                maxPage = true
            } else {
                currentPage += 1
                insertOrUpdateInMarcas(qMarcas)
            }

        }
    }

    private suspend fun insertOrUpdateInMarcas(marcasToAdd: List<Marca>){
        val marcas = _marcas.value
        if (marcas == null || marcas.isEmpty() || currentPage == 1) {
            withContext(Dispatchers.Main) { _marcas.value = marcasToAdd }
        } else {
            val newMarcas = marcas.toMutableList()
            newMarcas.addAll(marcasToAdd)
            withContext(Dispatchers.Main) { _marcas.value = newMarcas }
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

    fun insert(marca: Marca, callback: ((success: Boolean) -> Unit)?) {
        launch {
            try {
                val response = Service.api.marcaInsert(marca).await()
                callback?.let { it(response.code() == 201) }
            } catch (e: Exception) {
                Log.e("MarcasViewModel.insert", e.message ?: "error")
                callback?.let { it(false) }
            }
        }
    }

    fun update(marca: Marca, callback: ((success: Boolean) -> Unit)?) {
        launch {
            try {
                val response = Service.api.marcaUpdate(marca.marCodigo, marca).await()
                callback?.let { it(response.code() == 200) }
            } catch (e: Exception) {
                Log.e("MarcasViewModel.update", e.message ?: "error")
                callback?.let { it(false) }
            }
        }
    }

    fun delete(marca: Marca, callback: ((success: Boolean) -> Unit)?) {
        launch {
            try {
                val response = Service.api.marcaDelete(marca.marCodigo).await()
                callback?.let { it(response.code() == 200) }
            } catch (e: Exception) {
                Log.e("MarcasViewModel.delete", e.message ?: "error")
                callback?.let { it(false) }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}