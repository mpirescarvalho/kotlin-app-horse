package com.example.myapplication.ui.marcas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.brouding.simpledialog.SimpleDialog
import com.brouding.simpledialog.builder.Custom
import com.brouding.simpledialog.builder.General
import com.brouding.simpledialog.extra.BtnAction
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentMarcasBinding
import com.example.myapplication.models.Marca
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MarcasFragment : Fragment() {

    private val viewModel: MarcasViewModel by viewModels()

    private lateinit var binding: FragmentMarcasBinding
    private lateinit var swipeRefresh: SwipeRefreshLayout

    private var isFetching: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMarcasBinding.inflate(inflater)

        swipeRefresh = binding.swipeRefresh
        swipeRefresh.setOnRefreshListener {
            viewModel.refresh {
                swipeRefresh.isRefreshing = false
            }
        }

        val adapter = MarcasAdapter(MarcasAdapter.Listener({
            showUpdateDialog(it)
        }, {
            showDeleteDialog(it)
        }))

        binding.listMarcas.adapter = adapter
        binding.listMarcas.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                checkFetch()
            }
        })

        viewModel.marcas.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        checkFetch()

        binding.fab.setOnClickListener {
            showInsertDialog()
        }

        return binding.root
    }

    private fun checkFetch() {
        if (shouldFetch(binding.listMarcas)) {
            isFetching = true
            swipeRefresh.isRefreshing = true
            binding.listMarcas.setPadding(0, 0, 0, 160)

            viewModel.fetchNextPage {
                isFetching = false
                swipeRefresh.isRefreshing = false
                if (viewModel.maxPage) {
                    binding.listMarcas.setPadding(0, 0, 0, 0)
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

    private fun showInsertDialog() {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.simple_input, null)
        val input = view.findViewById<TextInputEditText>(R.id.input)
        input.hint = "Descrição"

        SimpleDialog(Custom(requireContext())
            .applyGeneral {
                setTitle(context.getString(R.string.add_marca_title))
                setBtnConfirmText(context.getString(R.string.save))
                setBtnCancelText(context.getString(R.string.cancel))
            }
            .onBtnAction {
                if (it == BtnAction.CONFIRM) {
                    val description = input.text.toString().toUpperCase()
                    viewModel.insert(Marca(marDescricao = description)) { success ->
                        if (success) {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.marca_insert_success),
                                Toast.LENGTH_LONG
                            ).show()
                            swipeRefresh.isRefreshing = true
                            viewModel.refresh {
                                swipeRefresh.isRefreshing = false
                            }
                        } else {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.marca_insert_fail),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
            .setCustomView(view)
        ).show()
    }

    private fun showUpdateDialog(marca: Marca) {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.simple_input, null)
        val input = view.findViewById<TextInputEditText>(R.id.input)
        input.hint = "Descrição"

        input.setText(marca.marDescricao)

        SimpleDialog(Custom(requireContext())
            .applyGeneral {
                setTitle(context.getString(R.string.update_marca_title))
                setBtnConfirmText(context.getString(R.string.save))
                setBtnCancelText(context.getString(R.string.cancel))
            }
            .onBtnAction {
                if (it == BtnAction.CONFIRM) {
                    val description = input.text.toString().toUpperCase()
                    marca.marDescricao = description
                    viewModel.update(marca) { success ->
                        if (success) {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.marca_update_success),
                                Toast.LENGTH_LONG
                            ).show()
                            //TODO: recarregar apenas o item atualizado
                            swipeRefresh.isRefreshing = true
                            viewModel.refresh {
                                swipeRefresh.isRefreshing = false
                            }
                        } else {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.marca_update_fail),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
            .setCustomView(view)
        ).show()
    }

    private fun showDeleteDialog(marca: Marca) {
        SimpleDialog(
            General(requireContext())
                .setTitle(requireContext().getString(R.string.delete_marca_title))
                .setContent(requireContext().getString(R.string.confirmation_description))
                .setBtnConfirmText(requireContext().getString(R.string.delete))
                .setBtnCancelText(requireContext().getString(R.string.cancel))
                .onBtnAction {
                    if (it == BtnAction.CONFIRM) {
                        viewModel.delete(marca) { success ->
                            if (success) {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.marca_delete_success),
                                    Toast.LENGTH_LONG
                                ).show()
                                //TODO: recarregar apenas o item excluido
                                swipeRefresh.isRefreshing = true
                                viewModel.refresh {
                                    swipeRefresh.isRefreshing = false
                                }
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.marca_delete_fail),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
        ).show()
    }
}