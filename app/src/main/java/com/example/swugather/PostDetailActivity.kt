package com.example.swugather

import android.net.Uri
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.content.Intent
import android.content.SharedPreferences
import android.widget.Button
import android.widget.GridLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import java.util.UUID

class PostDetailActivity : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 1
    private var imageUriList: MutableList<Uri?> = mutableListOf() // 저장된 이미지 URI
    private lateinit var imageViews: List<ImageView>
    private var currentIndex = 0

    private lateinit var dbHelper: DBManager
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var btnJoinGroup: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        dbHelper = DBManager(this)
        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        val tvTitle = findViewById<TextView>(R.id.tvTitle)
        val tvCategory = findViewById<TextView>(R.id.tvCategory)
        val tvDescription = findViewById<TextView>(R.id.tvDescription)
        val tvParticipants = findViewById<TextView>(R.id.tvMaxParticipants)
        val tvSchedule = findViewById<TextView>(R.id.tvSchedule)
        btnJoinGroup = findViewById(R.id.btnJoinGroup)  // 참여하기 버튼

        val postId = intent.getStringExtra("post_id") ?: ""
        val postTitle = intent.getStringExtra("post_title") ?: "제목 없음"
        val postCategory = intent.getStringExtra("post_category") ?: "카테고리 없음"
        val postDescription = intent.getStringExtra("post_description") ?: "설명 없음"
        val postParticipants = intent.getIntExtra("post_maxParticipants", 0)
        val postDayOfWeek = intent.getIntExtra("post_dayOfWeek",  -1)

        val postStartTime = intent.getStringExtra("post_startTime") ?: "09:00"
        val postEndTime = intent.getStringExtra("post_endTime") ?: "18:00"

        val postSchedule = "${convertDayToKorean(postDayOfWeek)}, 시간: $postStartTime ~ $postEndTime"


        tvTitle.text = postTitle
        tvCategory.text = "카테고리: $postCategory"
        tvDescription.text = postDescription
        tvParticipants.text = "모집 인원: ${postParticipants}명"
        tvSchedule.text = postSchedule

        btnJoinGroup.setOnClickListener {
            joinGroup(postId, postDayOfWeek, postStartTime, postEndTime)
        }

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

    private fun joinGroup(groupId: String, dayOfWeek: Int, startTime: String, endTime: String) {
        val userId = sharedPreferences.getString("user_id", null)

        if (userId == null) {
            Toast.makeText(this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put("id", UUID.randomUUID().toString())
            put("userId", userId)  // 현재 로그인한 사용자 ID
            put("groupId", groupId)
            put("dayOfWeek", dayOfWeek)
            put("startTime", startTime)
            put("endTime", endTime)
        }

        val result = db.insert("UserSchedules", null, values)
        db.close()

        if (result != -1L) {
            Toast.makeText(this, "모임에 참여하였습니다!", Toast.LENGTH_SHORT).show()
            finish() // 현재 화면 종료
        } else {
            Toast.makeText(this, "참여 중 오류 발생", Toast.LENGTH_SHORT).show()
        }
    }

    private fun convertDayToKorean(dayOfWeek: Int): String {
        return when (dayOfWeek) {
            1 -> "월요일"
            2 -> "화요일"
            3 -> "수요일"
            4 -> "목요일"
            5 -> "금요일"
            6 -> "토요일"
            7 -> "일요일"
            else -> "알 수 없음"
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