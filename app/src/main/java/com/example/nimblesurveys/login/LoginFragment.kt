package com.example.nimblesurveys.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nimblesurveys.R
import com.example.nimblesurveys.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment: Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
        observeData()
    }

    private fun initUi() {
        binding.bLogin.setOnClickListener {
            val email = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            viewModel.login(email, password)
        }
    }

    private fun observeData() {
        viewModel.eventSuccess.observe(viewLifecycleOwner) {
            if (it == true) {
                findNavController().navigate(R.id.action_login_to_first)
                viewModel.onDoneLoginSuccess()
            }
        }
        viewModel.eventError.observe(viewLifecycleOwner) {
            it?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
                viewModel.onDoneError()
            }
        }
        viewModel.eventLoading.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.groupLoginControls.visibility = View.GONE
                binding.groupSigningIn.visibility = View.VISIBLE
            } else {
                binding.groupLoginControls.visibility = View.VISIBLE
                binding.groupSigningIn.visibility = View.GONE
            }
        }
    }
}