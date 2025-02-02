package com.example.swugather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecruitmentListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecruitmentAdapter
    private val recruitmentList = mutableListOf<Recruitment>() // 모집글 목록
    private lateinit var dbManager: DBManager // DBManager 인스턴스

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recruitment_list, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = RecruitmentAdapter(recruitmentList)
        recyclerView.adapter = adapter

        // DBManager 초기화
        dbManager = DBManager(requireContext())

        // 카테고리별 데이터 로드
        val category = arguments?.getString("category") ?: "전체"
        loadRecruitmentData(category)

        return view
    }

    private fun loadRecruitmentData(category: String) {
        recruitmentList.clear()

        // DB에서 모집글 데이터 가져오기
        val allPosts = dbManager.getAllPosts()

        // 선택한 카테고리에 따라 필터링
        val filteredPosts = if (category == "전체") {
            allPosts
        } else {
            allPosts.filter { it.category == category }
        }

        recruitmentList.addAll(filteredPosts)
        adapter.notifyDataSetChanged() // RecyclerView 업데이트
    }

    companion object {
        fun newInstance(category: String): RecruitmentListFragment {
            val fragment = RecruitmentListFragment()
            val args = Bundle()
            args.putString("category", category)
            fragment.arguments = args
            return fragment
        }
    }
}