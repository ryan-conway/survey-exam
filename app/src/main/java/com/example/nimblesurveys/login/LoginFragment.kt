package com.example.nimblesurveys.login

import android.animation.AnimatorSet
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.example.nimblesurveys.R
import com.example.nimblesurveys.databinding.FragmentLoginBinding
import com.example.nimblesurveys.util.fade
import com.example.nimblesurveys.util.finishOnBackPressed
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        finishOnBackPressed()
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.move).apply {
                duration = 1000
            }

    }

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
            hideKeyboard(it)
            val email = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            viewModel.login(email, password)
        }
        val emailFadeIn = binding.tilUsername.fade(1000, toAlpha = 0.5f)
        val passwordFadeIn = binding.tilPassword.fade(1000, toAlpha = 0.5f)
        val loginButtonFadeIn = binding.bLogin.fade(1000)
        val fadeInSet = AnimatorSet().apply {
            play(emailFadeIn)
                .with(passwordFadeIn)
                .with(loginButtonFadeIn)
        }
        fadeInSet.start()

    }

    private fun observeData() {
        viewModel.eventSuccess.observe(viewLifecycleOwner) {
            if (it == true) {
                findNavController().navigate(R.id.action_login_to_surveyList)
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

    private fun hideKeyboard(view: View) {
        val inputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}