package com.example.swugather

import android.content.ContentValues
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.slider.RangeSlider
import java.util.UUID

class PostCreateActivity : AppCompatActivity() {

    private lateinit var dbHelper: SQLiteOpenHelper
    private lateinit var spinnerCategory: Spinner
    private lateinit var dayTextViews: List<TextView>
    private lateinit var rangeSliderTime: RangeSlider
    private lateinit var etMaxParticipants: EditText
    private lateinit var etTitle: EditText
    private lateinit var etDescription: EditText
    private lateinit var tvTimeRange: TextView

    private var selectedDayIndex: Int? = null // 선택된 요일

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_create)

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

        // 카테고리 데이터 정의
        val categories = listOf("운동", "독서", "여행", "스터디", "기타")

        // Spinner 어댑터 설정
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = adapter

        // RangeSlider 초기화
        val rangeSlider: RangeSlider = findViewById(R.id.rangeSliderTime)
        rangeSlider.setValues(9f, 18f) // 초기 값 설정 (9:00 ~ 18:00)

        // 값이 변경될 때 동작 정의
        rangeSlider.addOnChangeListener { slider, _, _ ->
            val selectedValues = slider.values // 선택된 값 리스트
            val startTime = selectedValues[0].toInt()
            val endTime = selectedValues[1].toInt()

            // 선택된 시간 표시 TextView 업데이트
            tvTimeRange.text = "선택된 시간: $startTime:00 ~ $endTime:00"
        }

        // 요일 클릭 이벤트
        setupDaySelector()

        // 시간 선택
        rangeSliderTime.addOnChangeListener { slider, _, _ ->
            val values = slider.values
            val startTime = "${values[0].toInt()}:00"
            val endTime = "${values[1].toInt()}:00"
            tvTimeRange.text = "선택된 시간: $startTime ~ $endTime"
        }

        // 저장 버튼 클릭 이벤트
        findViewById<Button>(R.id.btnSave).setOnClickListener {
            saveGroupToDatabase()
        }

    }
    private fun setupDaySelector() {
        dayTextViews.forEachIndexed { index, textView ->
            textView.setOnClickListener {
                // 이전 선택 초기화
                selectedDayIndex?.let { prevIndex ->
                    dayTextViews[prevIndex].setBackgroundResource(R.drawable.bg_day_default)
                }

                // 현재 선택 표시
                selectedDayIndex = index
                textView.setBackgroundResource(R.drawable.bg_day_selected)
            }
        }
    }

    private fun saveGroupToDatabase() {
        // 입력값 가져오기
        val title = etTitle.text.toString().trim()
        val description = etDescription.text.toString().trim()
        val category = spinnerCategory.selectedItem?.toString() ?: ""
        val maxParticipants = etMaxParticipants.text.toString().toIntOrNull()
        val dayOfWeek = selectedDayIndex
        val timeRange = rangeSliderTime.values
        val startTime = "${timeRange[0].toInt()}:00"
        val endTime = "${timeRange[1].toInt()}:00"


        // 유효성 검사
        if (title.isEmpty() || dayOfWeek == null || maxParticipants == null) {
            Toast.makeText(this, "필수 항목을 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        if (timeRange[0] >= timeRange[1]) {
            Toast.makeText(this, "종료 시간은 시작 시간 이후여야 합니다.", Toast.LENGTH_SHORT).show()
            return
        }

        // 데이터베이스에 저장
        val db = dbHelper.writableDatabase
        val groupId = UUID.randomUUID().toString()

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
            put("dayOfWeek", dayOfWeek + 1)
            put("startTime", startTime)
            put("endTime", endTime)
        }
        db.insert("Schedules", null, scheduleValues)

        Toast.makeText(this, "소모임이 저장되었습니다!", Toast.LENGTH_SHORT).show()
        finish()
    }

}