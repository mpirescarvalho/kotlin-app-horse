package com.example.myapplication.ui.clients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.myapplication.databinding.FragmentClientsBinding

class ClientsFragment : Fragment() {

    private val viewModel: ClientsViewModel by viewModels()

    private lateinit var binding: FragmentClientsBinding
    private lateinit var swipeRefresh: SwipeRefreshLayout

    private var isFetching: Boolean = false

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding = FragmentClientsBinding.inflate(inflater)

        swipeRefresh = binding.swipeRefresh
        swipeRefresh.setOnRefreshListener {
            viewModel.refresh {
                swipeRefresh.isRefreshing = false
            }
        }

        val adapter = ClientsAdapter()
        binding.listClients.adapter = adapter
        binding.listClients.addOnScrollListener(object: OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                checkFetch()
            }
        })

        viewModel.clients.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        checkFetch()

        return binding.root
    }

    private fun checkFetch() {
        if (shouldFetch(binding.listClients)) {
            isFetching = true
            swipeRefresh.isRefreshing = true
            binding.listClients.setPadding(0, 0, 0, 160)

            viewModel.fetchNextPage {
                isFetching = false
                swipeRefresh.isRefreshing = false
                if (viewModel.maxPage) {
                    binding.listClients.setPadding(0, 0, 0, 0)
                }
            }
        }
    }

    private fun shouldFetch(recyclerView: RecyclerView): Boolean {
        val threshold = 15

        val manager = recyclerView.layoutManager as LinearLayoutManager
        val currentItems = manager.childCount
        val totalItems = manager.itemCount
        val scrolledOutItems = manager.findFirstVisibleItemPosition()

        return !viewModel.maxPage && !isFetching && (currentItems + scrolledOutItems >= (totalItems - threshold))

    }
}