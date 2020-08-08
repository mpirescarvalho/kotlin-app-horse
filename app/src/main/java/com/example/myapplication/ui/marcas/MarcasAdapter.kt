package com.example.myapplication.ui.marcas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.ListItemMarcasBinding
import com.example.myapplication.models.Marca

class MarcasAdapter(private val listener: Listener): ListAdapter<Marca, MarcasAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ListItemMarcasBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_marcas,
            parent,
            false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val marca = getItem(position)
        holder.bind(marca)

        holder.binding.root.setOnClickListener {
            listener.onItemClickListener(marca)
        }

        holder.binding.deleteButton.setOnClickListener {
            listener.onDeleteClickListener(marca)
        }

    }

    companion object DiffCallback : DiffUtil.ItemCallback<Marca>() {
        override fun areItemsTheSame(oldItem: Marca, newItem: Marca): Boolean {
            return (oldItem.marCodigo == newItem.marCodigo)
        }
        override fun areContentsTheSame(oldItem: Marca, newItem: Marca): Boolean {
//            return (oldItem == newItem)
            return false
            //TODO: verificar falha aqui (oldItem e newItem vindo sempre igual ao newItem)
        }
    }

    class ViewHolder(var binding: ListItemMarcasBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(marca: Marca) {
            binding.marca = marca
            binding.executePendingBindings()
        }
    }

    class Listener(
        val onItemClickListener: (marca: Marca) -> Unit,
        var onDeleteClickListener: (marca: Marca) -> Unit)
}