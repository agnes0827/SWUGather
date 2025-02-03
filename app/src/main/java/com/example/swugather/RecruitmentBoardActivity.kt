package com.example.swugather

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class RecruitmentBoardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recruitment_board)

        // TabLayout과 ViewPager2 연결
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)

        // 카테고리 목록 설정
        val categories = listOf("전체", "운동", "독서", "맛집", "여행", "스터디", "기타")

        // RecruitmentPagerAdapter 생성
        val adapter = RecruitmentPagerAdapter(supportFragmentManager, lifecycle, categories)
        viewPager.adapter = adapter

        // TabLayout과 ViewPager2 연동
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = categories[position]
        }.attach()

        // 게시물 작성 버튼 클릭 시 작성 페이지로 이동
        val createPostButton: Button = findViewById<Button>(R.id.btnCreatePost)
        createPostButton.setOnClickListener {
            val intent = Intent(this, PostCreateActivity::class.java)
            startActivity(intent)
        }

        val dbManager = DBManager(this)
        val recruitmentList = dbManager.getAllPosts() // 데이터베이스에서 게시물 목록 가져오기
    }
}