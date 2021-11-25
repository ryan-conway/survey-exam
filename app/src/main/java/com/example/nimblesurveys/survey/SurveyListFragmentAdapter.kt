package com.example.nimblesurveys.survey

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class SurveyListFragmentAdapter(
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {

    private var items = listOf<SurveyListItem>()

    override fun getItemCount(): Int = items.size

    override fun createFragment(position: Int): Fragment =
        SurveyListItemFragment.newInstance(items[position])

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<SurveyListItem>) {
        this.items = items
        notifyDataSetChanged()
    }

}