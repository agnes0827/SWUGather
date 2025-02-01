package com.example.swugather

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity

class PostDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        val tvTitle = findViewById<TextView>(R.id.tvPostTitle)
        val tvDate = findViewById<TextView>(R.id.tvPostDate)
        val tvParticipants = findViewById<TextView>(R.id.tvPostParticipants)
        val btnJoin = findViewById<Button>(R.id.btnJoinPost)

        // Intent로부터 게시물 정보 받기
        val title = intent.getStringExtra("post_title") ?: "제목 없음"
        val date = intent.getStringExtra("post_date") ?: "날짜 없음"
        val participants = intent.getIntExtra("post_participants", 0)

        // 받은 데이터 화면에 표시
        tvTitle.text = title
        tvDate.text = "날짜: $date"
        tvParticipants.text = "참여자 수: ${participants}명"

        // 참여 버튼 클릭 이벤트
        btnJoin.setOnClickListener {
            // 참여 로직 (추후 Firebase 또는 DB 연동 가능)
        }
    }
}