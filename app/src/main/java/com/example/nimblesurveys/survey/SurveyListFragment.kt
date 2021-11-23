package com.example.nimblesurveys.survey

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.nimblesurveys.databinding.FragmentSurveyBinding
import com.example.nimblesurveys.util.PagerDecorator
import com.example.nimblesurveys.util.PixelUtil
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SurveyListFragment : Fragment() {

    private lateinit var binding: FragmentSurveyBinding
    private val viewModel: SurveyListViewModel by viewModels()

    private lateinit var adapter: SurveyListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.finish()
                }
            })
    }

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

        initUi()
        observeData()
        viewModel.getSurveys()
    }

    private fun initUi() {
        binding.shimmer.root.startShimmer()
        adapter = SurveyListAdapter()
        binding.recycler.setHasFixedSize(true)
        binding.recycler.adapter = adapter
        PagerSnapHelper().attachToRecyclerView(binding.recycler)
        val pixelUtil = PixelUtil(binding.root.context)
        val pagerDecorator = PagerDecorator(
            pixelUtil.dpToPx(40f),
            pixelUtil.dpToPx(32F) + pixelUtil.spToPx(28f * 3f) + pixelUtil.spToPx(18f * 3f)
        )
        binding.recycler.addItemDecoration(pagerDecorator)
        binding.recycler.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) = Unit
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) = Unit
            override fun onInterceptTouchEvent(rv: RecyclerView,  motionEvent: MotionEvent) =
                pagerDecorator.isIndicatorPressing(motionEvent, rv)
        })

        binding.fab.setOnClickListener { viewModel.viewSurvey(pagerDecorator.activeIndicator) }
    }

    private fun observeData() {
        viewModel.surveys.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it)
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
                val action = SurveyListFragmentDirections.actionSurveyListToDetails(it.id)
                findNavController().navigate(action)
                viewModel.onDoneViewSurvey()
            }
        }
    }
}