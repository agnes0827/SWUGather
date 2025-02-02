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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PostDetailActivity: AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 1
    private var imageUriList: MutableList<Uri?> = mutableListOf() // 저장된 이미지 URI
    private lateinit var imageViews: List<ImageView>
    private var currentIndex = 0 // 현재 업로드할 위치를 관리하는 인덱스

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

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