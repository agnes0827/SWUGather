package com.example.swugather

import RecruitmentListFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import android.os.Bundle

class RecruitmentPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val categories: List<String>
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = categories.size

    override fun createFragment(position: Int): Fragment {
        return RecruitmentListFragment.newInstance(categories[position])
    }
}