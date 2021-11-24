package com.example.nimblesurveys.login

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nimblesurveys.databinding.FragmentSplashBinding
import com.example.nimblesurveys.util.finishOnBackPressed
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        finishOnBackPressed()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()

        Handler(Looper.getMainLooper())
            .postDelayed({ viewModel.checkLoginState() }, 2000)
    }

    private fun observeData() {
        viewModel.isLoggedIn.observe(viewLifecycleOwner) {
            it?.let { isLoggedIn ->
                val action = if (isLoggedIn) {
                    SplashFragmentDirections.actionSplashToSurveyList()
                } else {
                    SplashFragmentDirections.actionSplashToLogin()
                }
                findNavController().navigate(action)
            }
        }
    }


}