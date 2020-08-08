package com.example.myapplication.ui.products

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.models.Product
import com.example.myapplication.network.Service
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ProductsViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {

    private var viewModelJob = Job()
    override val coroutineContext: CoroutineContext = viewModelJob + Dispatchers.Main

    private val _products = MutableLiveData<List<Product>>(ArrayList())

    val products: LiveData<List<Product>> = _products

    var currentPage = 0
    var maxPage: Boolean = false

    private suspend fun fetchNextPageAsync() = withContext(Dispatchers.IO) {
        async {

            val qProducts = Service.api.productGetAll(currentPage + 1).await()

            if (qProducts.isEmpty()) {
                maxPage = true
            } else {
                currentPage += 1

                val prevProducts = _products.value

                if (prevProducts == null || prevProducts.isEmpty() || currentPage == 1) {
                    withContext(Dispatchers.Main) { _products.value = qProducts }
                } else {
                    val newProducts = prevProducts.toMutableList()
                    newProducts.addAll(qProducts)
                    withContext(Dispatchers.Main) { _products.value = newProducts }
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