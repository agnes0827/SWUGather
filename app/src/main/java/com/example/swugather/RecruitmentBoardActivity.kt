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

class RecruitmentBoardActivity : AppCompatActivity() {

    private lateinit var dbManager: DBManager
    private lateinit var adapter: RecruitmentPagerAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recruitment_board)

        dbManager = DBManager(this)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)

        val categories = listOf("전체", "운동", "독서", "여행", "스터디", "기타")
        adapter = RecruitmentPagerAdapter(supportFragmentManager, lifecycle, categories)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = categories[position]
        }.attach()

        val postCreateLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    refreshFragments()
                }
            }

        val createPostButton: Button = findViewById(R.id.btnCreatePost)
        createPostButton.setOnClickListener {
            val intent = Intent(this, PostCreateActivity::class.java)
            postCreateLauncher.launch(intent)
        }

        val myPageButton: Button = findViewById(R.id.btnMyPage)
        myPageButton.text = "마이페이지"
        myPageButton.setOnClickListener {
            val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            val userId = sharedPreferences.getString("user_id", null)

            if (userId.isNullOrEmpty()) {
                Toast.makeText(this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
                val loginIntent = Intent(this, MainActivity::class.java)
                startActivity(loginIntent)
            } else {
                val myPageIntent = Intent(this, MyPageActivity::class.java)
                startActivity(myPageIntent)
            }
        }
    }

    private fun refreshFragments() {
        for (fragment in supportFragmentManager.fragments) {
            if (fragment is RecruitmentListFragment) {
                val category = fragment.arguments?.getString("category") ?: "전체"
                fragment.loadRecruitments(category)
            }
        }
    }
}