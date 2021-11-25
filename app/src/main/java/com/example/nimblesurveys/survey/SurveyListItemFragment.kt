package com.example.nimblesurveys.survey

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.nimblesurveys.databinding.FragmentSurveyListItemBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class SurveyListItemFragment: Fragment() {

    private lateinit var binding: FragmentSurveyListItemBinding
    private var surveyListItem: SurveyListItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        surveyListItem = arguments?.getParcelable(ARG_SURVEY_ITEM)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSurveyListItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val survey = surveyListItem ?: return

        binding.tvDate.text = survey.date.formatDate()
        binding.tvDateSubtitle.text = survey.date.getDifference()
        binding.tvSurveyName.text = survey.name
        binding.tvSurveyDescription.text = survey.description

        Glide.with(binding.ivBackground)
            .load(survey.coverImageThumbnailUrl)
            .thumbnail(Glide.with(binding.ivBackground).load(survey.coverImageUrl))
            .into(binding.ivBackground)
    }

    private fun String.formatDate(): String {
        val toFormat = SimpleDateFormat("EEEE, MMMM dd", Locale.getDefault())
        val date = getDate(this) ?: return this
        return toFormat.format(date)
    }

    private fun getDate(dateString: String): Date? {
        val fromFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.sss'Z'", Locale.getDefault())
        return try {
            fromFormat.parse(dateString)
        } catch (e: ParseException) {
            null
        }
    }

    //TODO replace this with accurate difference measurement
    private fun String.getDifference(): String {
        val date = getDate(this)?.time ?: return this
        val currentDate = System.currentTimeMillis()
        val difference = currentDate - date
        val days = TimeUnit.MILLISECONDS.toDays(difference)
        return when {
            days < 1L -> "Today"
            days < 7L -> "$days days ago"
            days < 31L -> "${days / 4} weeks ago"
            days < 365 -> "${days / 30} months ago"
            else -> "${days / 365} years ago"
        }
    }

    companion object {

        private const val ARG_SURVEY_ITEM = "ARG_SURVEY_ITEM"

        @JvmStatic
        fun newInstance(param: SurveyListItem) =
            SurveyListItemFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_SURVEY_ITEM, param)
                }
            }

    }
}