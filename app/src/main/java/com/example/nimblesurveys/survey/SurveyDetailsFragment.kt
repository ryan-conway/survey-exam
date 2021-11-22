package com.example.nimblesurveys.survey

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.nimblesurveys.databinding.FragmentSurveyDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SurveyDetailsFragment : Fragment() {

    private lateinit var binding: FragmentSurveyDetailsBinding
    private val viewModel: SurveyDetailsViewModel by viewModels()
    private val args: SurveyDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSurveyDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
        observeData()
        viewModel.loadSurvey(args.surveyId)
    }

    private fun initUi() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.bStartSurvey.setOnClickListener {
            Toast.makeText(binding.root.context, "Coming soon!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeData() {
        viewModel.survey.observe(viewLifecycleOwner) {
            it?.let {
                binding.tvSurveyName.text = it.title
                binding.tvSurveyDescription.text = it.description
                Glide.with(binding.ivBackground)
                    .load("${it.coverImageUrl}l")
                    .into(binding.ivBackground)
            }
        }
        viewModel.eventError.observe(viewLifecycleOwner) {
            if (it == true) {
                Toast.makeText(binding.root.context, "Survey not found", Toast.LENGTH_SHORT).show()
                viewModel.onDoneError()
                findNavController().popBackStack()
            }
        }
    }

}