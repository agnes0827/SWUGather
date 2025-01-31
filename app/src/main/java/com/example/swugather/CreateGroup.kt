package com.example.swugather

import android.content.ContentValues
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.slider.RangeSlider
import java.util.UUID

class CreateGroup : AppCompatActivity() {

    private lateinit var dbHelper: SQLiteOpenHelper
    private lateinit var spinnerCategory: Spinner
    private lateinit var dayTextViews: List<TextView>
    private lateinit var rangeSliderTime: RangeSlider
    private lateinit var etMaxParticipants: EditText
    private lateinit var etTitle: EditText
    private lateinit var etDescription: EditText
    private lateinit var tvTimeRange: TextView

    private var selectedDayIndex: Int? = null // 선택된 요일
    private val dayNames = listOf("월", "화", "수", "목", "금", "토", "일") // 요일 리스트

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)

        dbHelper = DBManager(this)

        spinnerCategory = findViewById(R.id.spinnerCategory)
        rangeSliderTime = findViewById(R.id.rangeSliderTime)
        etMaxParticipants = findViewById(R.id.etMaxParticipants)
        etTitle = findViewById(R.id.etTitle)
        etDescription = findViewById(R.id.etDescription)
        tvTimeRange = findViewById(R.id.tvTimeRange)

        dayTextViews = listOf(
            findViewById(R.id.tvMonday),
            findViewById(R.id.tvTuesday),
            findViewById(R.id.tvWednesday),
            findViewById(R.id.tvThursday),
            findViewById(R.id.tvFriday),
            findViewById(R.id.tvSaturday),
            findViewById(R.id.tvSunday)
        )

        setupDaySelector() // 요일 선택 설정
        setupSpinner() // 카테고리 스피너 설정
        setupRangeSlider() // 시간 선택 슬라이더 설정

        // 저장 버튼 클릭 이벤트
        findViewById<Button>(R.id.btnSave).setOnClickListener {
            saveGroupToDatabase() // DB 저장
            navigateToPostView()  // PostView로 이동
        }
    }

    // 요일 선택 기능 설정
    private fun setupDaySelector() {
        dayTextViews.forEachIndexed { index, textView ->
            textView.setOnClickListener {
                // 이전 선택된 요일 초기화
                selectedDayIndex?.let { prevIndex ->
                    dayTextViews[prevIndex].setBackgroundResource(R.drawable.bg_day_default)
                }

                // 현재 선택한 요일 표시
                selectedDayIndex = index
                textView.setBackgroundResource(R.drawable.bg_day_selected)
            }
        }
    }

    // 카테고리 스피너 설정
    private fun setupSpinner() {
        val categories = listOf("운동", "독서", "여행", "스터디", "기타")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = adapter
    }

    // 시간 선택 슬라이더 설정
    private fun setupRangeSlider() {
        rangeSliderTime.setValues(9f, 18f) // 기본값 설정 (9:00 ~ 18:00)

        rangeSliderTime.addOnChangeListener { slider, _, _ ->
            val startTime = "${slider.values[0].toInt()}:00"
            val endTime = "${slider.values[1].toInt()}:00"
            tvTimeRange.text = "선택된 시간: $startTime ~ $endTime"
        }
    }

    // 데이터베이스에 그룹 정보 저장
    private fun saveGroupToDatabase() {
        val db = dbHelper.writableDatabase
        val groupId = UUID.randomUUID().toString()

        val title = etTitle.text.toString()
        val description = etDescription.text.toString()
        val category = spinnerCategory.selectedItem.toString()
        val maxParticipants = etMaxParticipants.text.toString()
        val selectedDay = selectedDayIndex?.let { dayNames[it] } ?: "요일 없음"
        val timeRange = "${rangeSliderTime.values[0].toInt()}:00 ~ ${rangeSliderTime.values[1].toInt()}:00"

        // Groups 테이블에 데이터 삽입
        val groupValues = ContentValues().apply {
            put("id", groupId)
            put("title", title)
            put("description", description)
            put("category", category)
            put("maxParticipants", maxParticipants)
        }
        db.insert("Groups", null, groupValues)

        // Schedules 테이블에 데이터 삽입
        val scheduleValues = ContentValues().apply {
            put("id", UUID.randomUUID().toString())
            put("groupId", groupId)
            put("dayOfWeek", selectedDay)
            put("startTime", timeRange.split("~")[0].trim()) // 시작 시간
            put("endTime", timeRange.split("~")[1].trim())   // 종료 시간
        }
        db.insert("Schedules", null, scheduleValues)
    }

    // PostView로 데이터 전달 및 이동
    private fun navigateToPostView() {
        val intent = Intent(this, PostView::class.java).apply {
            putExtra("GROUP_TITLE", etTitle.text.toString())
            putExtra("GROUP_DESCRIPTION", etDescription.text.toString())
            putExtra("GROUP_CATEGORY", spinnerCategory.selectedItem.toString())
            putExtra("GROUP_MAX_PARTICIPANTS", etMaxParticipants.text.toString())
            putExtra("GROUP_DAY", selectedDayIndex?.let { dayNames[it] } ?: "요일 없음") // 요일 전달
            putExtra("GROUP_TIME_RANGE", "${rangeSliderTime.values[0].toInt()}:00 ~ ${rangeSliderTime.values[1].toInt()}:00") // 시간 전달
        }
        startActivity(intent)
    }
}