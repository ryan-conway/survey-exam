package com.example.nimblesurveys.survey

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.nimblesurveys.databinding.FragmentSurveyBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SurveyListFragment: Fragment() {

    private lateinit var binding: FragmentSurveyBinding
    private val viewModel: SurveyListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSurveyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()
        viewModel.getSurveys()
    }

    private fun observeData() {
        viewModel.surveys.observe(viewLifecycleOwner) {
            it?.let {
                Snackbar.make(binding.root, "Fetched ${it.size} survey/s", Snackbar.LENGTH_SHORT).show()
                viewModel.onDoneGetSurveys()
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