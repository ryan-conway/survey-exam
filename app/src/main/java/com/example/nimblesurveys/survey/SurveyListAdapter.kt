package com.example.nimblesurveys.survey

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nimblesurveys.databinding.ItemSurveyBinding
import com.example.nimblesurveys.domain.model.Survey
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class SurveyListAdapter : ListAdapter<Survey, SurveyListViewHolder>(SurveyDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SurveyListViewHolder.from(parent)

    override fun onBindViewHolder(holder: SurveyListViewHolder, position: Int) =
        holder.bind(getItem(position))
}

class SurveyListViewHolder(
    private val binding: ItemSurveyBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(survey: Survey?) {
        survey ?: return

        binding.tvDate.text = survey.activeAt.formatDate()
        binding.tvDateSubtitle.text = survey.activeAt.getDifference()
        binding.tvSurveyName.text = survey.title
        binding.tvSurveyDescription.text = survey.description

        Glide.with(binding.ivBackground)
            .load("${survey.coverImageUrl}l")
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
        fun from(parent: ViewGroup) = SurveyListViewHolder(
            ItemSurveyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
}

class SurveyDiffCallback : DiffUtil.ItemCallback<Survey>() {
    override fun areItemsTheSame(oldItem: Survey, newItem: Survey) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Survey, newItem: Survey) = oldItem == newItem
}