package com.example.swugather

import RecruitmentListFragment
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.example.swugather.RecruitmentPagerAdapter

/**
 * 모집 게시판 액티비티
 * 사용자가 모집(소모임) 목록을 확인하고, 모집을 생성하거나 마이페이지로 이동할 수 있는 화면
 */
class RecruitmentBoardActivity : AppCompatActivity() {

    // 데이터베이스 관리 객체
    private lateinit var dbManager: DBManager

    // ViewPager를 위한 어댑터
    private lateinit var adapter: RecruitmentPagerAdapter

    // RecyclerView (현재는 사용되지 않음)
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recruitment_board)

        // 데이터베이스 매니저 초기화
        dbManager = DBManager(this)

        // UI 요소 초기화
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)

        // 모집 카테고리 리스트
        val categories = listOf("전체", "운동", "독서", "여행", "스터디", "기타")

        // ViewPager 어댑터 설정
        adapter = RecruitmentPagerAdapter(supportFragmentManager, lifecycle, categories)
        viewPager.adapter = adapter

        // TabLayout과 ViewPager 연결 (탭과 페이지 연동)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = categories[position] // 각 탭의 이름 설정
        }.attach()

        // 모집 작성 액티비티 실행 후 결과를 받기 위한 런처
        val postCreateLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    refreshFragments() // 모집 목록 새로고침
                }
            }

        // "모집 작성" 버튼 클릭 시 PostCreateActivity 실행
        val createPostButton: Button = findViewById(R.id.btnCreatePost)
        createPostButton.setOnClickListener {
            val intent = Intent(this, PostCreateActivity::class.java)
            postCreateLauncher.launch(intent)
        }

        // "마이페이지" 버튼 설정
        val myPageButton: Button = findViewById(R.id.btnMyPage)
        myPageButton.text = "마이페이지"
        myPageButton.setOnClickListener {
            val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            val userId = sharedPreferences.getString("user_id", null)

            if (userId.isNullOrEmpty()) {
                // 로그인이 되어 있지 않으면 로그인 페이지(MainActivity)로 이동
                Toast.makeText(this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
                val loginIntent = Intent(this, MainActivity::class.java)
                startActivity(loginIntent)
            } else {
                // 로그인 상태라면 마이페이지(MyPageActivity)로 이동
                val myPageIntent = Intent(this, MyPageActivity::class.java)
                startActivity(myPageIntent)
            }
        }
    }

    /**
     * 모집 목록을 새로고침하는 메서드
     * 새로운 모집이 추가되었을 때 모든 모집 리스트를 다시 로드함
     */
    private fun refreshFragments() {
        for (fragment in supportFragmentManager.fragments) {
            if (fragment is RecruitmentListFragment) {
                val category = fragment.arguments?.getString("category") ?: "전체"
                fragment.loadRecruitments(category) // 해당 카테고리의 모집 목록 새로고침
            }
        }
    }
}
