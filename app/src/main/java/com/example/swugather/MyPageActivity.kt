package com.example.swugather

import android.content.Context
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
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

        loadUserSchedules()
    }

    private fun loadUserSchedules() {
        val userId = sharedPreferences.getString("user_id", "unknown") ?: "unknown"

        if (userId == "unknown") {
            Log.e("MypageActivity", "로그인된 사용자 없음")
            return
        }

        val userSchedules = dbHelper.getUserSchedules(userId)

        val timetableSchedules = ArrayList<Schedule>()

        userSchedules.forEach { scheduleData ->
            val schedule = Schedule().apply {
                day = scheduleData.dayOfWeek - 1  // TimetableView는 0(월)~6(일)
                startTime.hour = scheduleData.startTime.split(":").getOrNull(0)?.toIntOrNull() ?: 9
                startTime.minute = 0
                endTime.hour = scheduleData.endTime.split(":").getOrNull(0)?.toIntOrNull() ?: 18
                endTime.minute = 0
                classTitle = "참여 일정"
                classPlace = "소모임"
                professorName = "그룹 ID: ${scheduleData.groupId}"
            }
        }

        timetableView.add(timetableSchedules)
    }
}