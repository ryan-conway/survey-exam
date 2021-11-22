package com.example.nimblesurveys.survey

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.nimblesurveys.databinding.FragmentSurveyLoadingBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SurveyLoadingFragment : Fragment() {

    private lateinit var binding: FragmentSurveyLoadingBinding
    private val viewModel: SurveyLoadingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSurveyLoadingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.startShimmer()

        observeData()

        viewModel.fetchSurveys()
    }

    private fun observeData() {
        viewModel.eventSurveysFetched.observe(viewLifecycleOwner) {
            if (it == true) {
                Snackbar.make(binding.root, "Surveys fetched!", Snackbar.LENGTH_SHORT).show()
                viewModel.onDoneSurveysFetched()
            }
        }
        viewModel.eventError.observe(viewLifecycleOwner) {
            if (it == true) {
                Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT).show()
                viewModel.onDoneError()
            }
        }
    }
}