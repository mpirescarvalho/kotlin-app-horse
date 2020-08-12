package com.example.myapplication.ui.login

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.IntentCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.myapplication.MainActivity
import com.example.myapplication.databinding.FragmentLoginBinding
import com.example.myapplication.util.Memoria


class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private val viewModel: LoginViewModel by viewModels()

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater)

        binding.login.setOnClickListener {
            val username = binding.username.text.toString()
            val password = binding.password.text.toString()

            if (username == "" || password == "") {
                Toast.makeText(context, "Preencha todos os campos para continuar", Toast.LENGTH_SHORT).show()
            } else {
              viewModel.login(username, password) { success, error ->
                  if (success) {
                      Memoria.persistSession(requireContext())
                      val intent = Intent(activity, MainActivity::class.java)
                      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK)
                      startActivity(intent)
                      ActivityCompat.finishAffinity(requireActivity())
                  } else {
                      Toast.makeText(context, "Erro: $error", Toast.LENGTH_LONG).show()
                  }
              }
            }
        }

        return binding.root
    }

}