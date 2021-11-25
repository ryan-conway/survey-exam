package com.example.nimblesurveys.survey

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nimblesurveys.R
import com.example.nimblesurveys.databinding.FragmentSurveyListBinding
import com.example.nimblesurveys.util.finishOnBackPressed
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SurveyListFragment : Fragment() {

    private lateinit var binding: FragmentSurveyListBinding
    private val viewModel: SurveyListViewModel by viewModels()

    private lateinit var adapter: SurveyListFragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        finishOnBackPressed()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSurveyListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
        observeData()
        viewModel.getSurveys()
    }

    private fun initUi() = activity?.let { activity ->
        binding.shimmer.root.startShimmer()

        val tabPosY = binding.viewpager.height - (
                resources.getDimension(R.dimen.activity_margin) +
                        (resources.getDimension(R.dimen.text_medium) * 2) +
                        (resources.getDimension(R.dimen.text_large) * 2.5f)
                )

        binding.tabLayout.y = tabPosY
        adapter = SurveyListFragmentAdapter(activity)
        binding.viewpager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewpager) { _, _ -> }.attach()

        binding.fab.setOnClickListener {
            viewModel.viewSurvey(binding.tabLayout.selectedTabPosition)
        }
    }

    private fun observeData() {
        viewModel.surveys.observe(viewLifecycleOwner) {
            it?.let {
                adapter.setItems(it.map { survey -> survey.toListItem() })
                viewModel.restorePage?.let { page ->
                    binding.viewpager.setCurrentItem(page, false)
                    viewModel.restorePage = null
                }
                binding.shimmer.root.visibility = View.GONE
                binding.fab.show()
            }
        }
        viewModel.eventError.observe(viewLifecycleOwner) {
            if (it == true) {
                Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT).show()
                viewModel.onDoneError()
            }
        }
        viewModel.eventViewSurvey.observe(viewLifecycleOwner) {
            it?.let {
                viewModel.restorePage = binding.tabLayout.selectedTabPosition
                val action = SurveyListFragmentDirections.actionSurveyListToDetails(it.id)
                findNavController().navigate(action)
                viewModel.onDoneViewSurvey()
            }
        }
    }
}