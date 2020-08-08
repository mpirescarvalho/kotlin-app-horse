package com.example.myapplication.ui.clients

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.ListItemClientsBinding
import com.example.myapplication.models.Client

class ClientsAdapter: ListAdapter<Client, ClientsAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ListItemClientsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_clients,
            parent,
            false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val client = getItem(position)
        holder.bind(client)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Client>() {
        override fun areItemsTheSame(oldItem: Client, newItem: Client): Boolean {
            return (oldItem.cliCodigo == newItem.cliCodigo) && (oldItem.empCodigo == newItem.empCodigo)
        }
        override fun areContentsTheSame(oldItem: Client, newItem: Client): Boolean {
            return (oldItem == newItem)
        }
    }

    class ViewHolder(var binding: ListItemClientsBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(client: Client) {
            binding.client = client
            binding.executePendingBindings()
        }
    }
}