package com.example.swugather

import android.net.Uri
import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.GridLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PostDetailActivity : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 1
    private var imageUriList: MutableList<Uri?> = mutableListOf() // 저장된 이미지 URI
    private lateinit var imageViews: List<ImageView>
    private var currentIndex = 0 // 현재 업로드할 위치를 관리하는 인덱스

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        // 데이터 받아오는 코드
        /*
        val tvTitle = findViewById<TextView>(R.id.tvTitle)
        val tvCategory = findViewById<TextView>(R.id.tvCategory)
        val tvDescription = findViewById<TextView>(R.id.tvDescription)
        val tvParticipants = findViewById<TextView>(R.id.tvMaxParticipants)
        val tvSchedule = findViewById<TextView>(R.id.tvSchedule)

        val postTitle = intent.getStringExtra("post_title") ?: "제목 없음"
        val postCategory = intent.getStringExtra("post_category") ?: "카테고리 없음"
        val postDescription = intent.getStringExtra("post_description") ?: "설명 없음"
        val postParticipants = intent.getIntExtra("post_participants", 0)
        val postDayOfWeek = intent.getStringExtra("post_dayOfWeek") ?: "요일 정보 없음"
        val postStartTime = intent.getStringExtra("post_startTime") ?: "시작 시간 없음"
        val postEndTime = intent.getStringExtra("post_endTime") ?: "종료 시간 없음"

        // UI에 데이터 설정
        tvTitle.text = postTitle
        tvCategory.text = "카테고리: $postCategory"
        tvDescription.text = postDescription
        tvParticipants.text = "모집 인원: $postParticipants 명"
        tvSchedule.text = "요일: $postDayOfWeek, 시간: $postStartTime ~ $postEndTime"
        */

        val uploadButton: Button = findViewById(R.id.uploadButton)
        val gridLayout: GridLayout = findViewById(R.id.imageGrid)
        // GridLayout에서 ImageView 리스트 가져오기
        imageViews = (0 until gridLayout.childCount).map { index ->
            gridLayout.getChildAt(index) as ImageView
        }
        // "사진 업로드" 버튼 클릭 이벤트
        uploadButton.setOnClickListener {
            openGallery()
        }
    }

    // 갤러리 열기
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true) // 이미지 선택
        }
        // 여러 이미지 선택 가능
        startActivityForResult(Intent.createChooser(intent, "사진 선택"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.let {
                if (it.clipData != null) {
                    // 여러 장의 사진 선택
                    val count = it.clipData!!.itemCount
                    for (i in 0 until count) {
                        val imageUri = it.clipData!!.getItemAt(i).uri
                        addImageToGrid(imageUri)
                    }
                } else if (it.data != null) {
                    // 단일 사진 선택
                    val imageUri = it.data
                    // ImageView에 사진 채우기
                    addImageToGrid(imageUri)
                }
            }
        }
    }

    private fun addImageToGrid(imageUri: Uri?) {
        // 현재 인덱스의 ImageView에 새 이미지를 설정
        imageViews[currentIndex].setImageURI(imageUri)
        // 기존 이미지 리스트에 업데이트
        if (currentIndex < imageUriList.size) {
            imageUriList[currentIndex] = imageUri
        } else {
            imageUriList.add(imageUri)
        }

        currentIndex = (currentIndex + 1) % imageViews.size // 인덱스 업데이트(순환 구조)
    }
}