package com.example.swugather

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class PostCreateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_create)

        val titleEditText = findViewById<EditText>(R.id.editPostTitle)
        val contentEditText = findViewById<EditText>(R.id.editPostContent)
        val submitButton = findViewById<Button>(R.id.btnSubmitPost)

        // 게시물 작성 완료 버튼 클릭 이벤트
        submitButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val content = contentEditText.text.toString()

            if (title.isNotEmpty() && content.isNotEmpty()) {
                // 게시물 저장 로직 추가 가능 (예: Firebase 연동)
                finish() // 작성 완료 후 이전 화면으로 돌아감
            } else {
                titleEditText.error = "제목을 입력하세요"
                contentEditText.error = "내용을 입력하세요"
            }
        }
    }
}