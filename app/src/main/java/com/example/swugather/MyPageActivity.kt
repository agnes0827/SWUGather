package com.example.swugather

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.tlaabs.timetableview.Schedule
import com.github.tlaabs.timetableview.TimetableView

class MyPageActivity : AppCompatActivity() {

    private lateinit var timetableView: TimetableView
    private lateinit var dbHelper: DBManager
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)

        dbHelper = DBManager(this)
        timetableView = findViewById(R.id.timetable)

        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("user_id", null)

        if (userId.isNullOrEmpty()) {
            Toast.makeText(this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()

            return
        }

        loadUserSchedules(userId)
    }

    private fun loadUserSchedules(userId: String) {
        val userSchedules = dbHelper.getUserSchedules(userId)

        if (userSchedules.isEmpty()) {
            Toast.makeText(this, "등록된 일정이 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val timetableSchedules = ArrayList<Schedule>()

        userSchedules.forEach { scheduleData ->

            val groupTitle: String = dbHelper.getGroupTitle(scheduleData.groupId)

            val schedule = Schedule().apply {
                day = scheduleData.dayOfWeek - 1
                startTime.hour = scheduleData.startTime.split(":").getOrNull(0)?.toIntOrNull() ?: 9
                startTime.minute = 0
                endTime.hour = scheduleData.endTime.split(":").getOrNull(0)?.toIntOrNull() ?: 18
                endTime.minute = 0
                classTitle = groupTitle
                professorName = "그룹 ID: ${scheduleData.groupId}"
            }
            timetableSchedules.add(schedule)
        }

        // 일정이 있을 때만 추가
        if (timetableSchedules.isNotEmpty()) {
            timetableView.add(timetableSchedules)
        }
    }
}