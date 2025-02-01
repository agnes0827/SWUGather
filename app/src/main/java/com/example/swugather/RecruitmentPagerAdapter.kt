package com.example.swugather

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class RecruitmentPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val categories: List<String> // 카테고리 목록
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = categories.size

    override fun createFragment(position: Int): Fragment {
        return RecruitmentListFragment.newInstance(categories[position])
    }
}