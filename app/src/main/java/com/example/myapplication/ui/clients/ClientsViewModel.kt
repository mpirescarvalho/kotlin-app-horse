package com.example.myapplication.ui.clients

import android.app.Application
import android.database.sqlite.SQLiteQueryBuilder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.models.Client
import com.example.myapplication.network.Service
import com.example.myapplication.util.toIntOrDefault
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

data class ClientFilters(
    var query: String,
    var somenteAtivo: Boolean,
    var somentePessoaFisica: Boolean,
    var somenteIrece: Boolean
)

class ClientsViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {

    private var viewModelJob = Job()
    override val coroutineContext: CoroutineContext = viewModelJob + Dispatchers.Main

    private val _clients = MutableLiveData<List<Client>>(ArrayList())
    private var _clientFilters = MutableLiveData(ClientFilters("", false, false, false))

    val clients: LiveData<List<Client>> = _clients
    val clientFilters: LiveData<ClientFilters> = _clientFilters

    private var currentPage = 0
    var maxPage: Boolean = false

    private suspend fun fetchNextPageAsync() = withContext(Dispatchers.IO) {
        async {

            val where = buildWhere(_clientFilters.value)

            val qClients = Service.api.clientGetAll(where, currentPage + 1).await()

            if (qClients.isEmpty()) {
                maxPage = true
            }

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

    private fun buildWhere(filters: ClientFilters?): String? {
        return if (filters != null) {

            val whereList: MutableList<String> = ArrayList()
            val filterList: MutableList<String> = ArrayList()
            val queryList: MutableList<String> = ArrayList()

            with (queryList) {
                if (filters.query.isNotEmpty()) {
                    add("cli_codigo = ${filters.query.toIntOrDefault(-1)}")
                    add("documento like upper('%${filters.query}%')")
                    add("upper(cli_nome) like upper('%${filters.query}%')")
                    add("upper(cli_fantasia) like upper('%${filters.query}%')")
                    add("upper(cid_nome) like upper('%${filters.query}%')")
                }
            }

            with (filterList) {
                if (filters.somenteAtivo)
                    add("sit_permite_venda = 'S'")
                if (filters.somenteIrece)
                    add("cid_codigo = 2250")
                if (filters.somentePessoaFisica)
                    add("cli_pessoa = 'F'")
            }

            val query = queryList.joinToString(" OR ")
            val filter = filterList.joinToString(" AND ")

            with (whereList) {
                if (query.isNotEmpty())
                    add("(${query})")
                if (filter.isNotEmpty())
                    add("(${filter})")
            }

            val where = whereList.joinToString(" AND ")

            if (where.isNotEmpty()) {
                where
            } else {
                null
            }
        } else {
            null
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

    fun setFilters(filters: ClientFilters, callback: (() -> Unit)? = null) {
        _clientFilters.value = filters
        refresh { callback?.let { it() } }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}