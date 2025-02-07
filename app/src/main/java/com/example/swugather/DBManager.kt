package com.example.swugather

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.github.tlaabs.timetableview.Schedule
import com.example.swugather.UserSchedule

class DBManager(context: Context) : SQLiteOpenHelper(context, "GroupApp.db", null, 2) {
    override fun onCreate(db: SQLiteDatabase) {

        //회원가입 정보 저장 테이블
        db!!.execSQL("CREATE TABLE users(editTextId text primary key, editTextPW text, editTextRePW text, editTextPhone text)")

        db.execSQL("""
            CREATE TABLE Groups (
                id TEXT PRIMARY KEY,
                title TEXT NOT NULL,
                description TEXT,
                category TEXT,
                maxParticipants INTEGER
            )
        """)

        db.execSQL("""
            CREATE TABLE Schedules (
                id TEXT PRIMARY KEY,
                groupId TEXT NOT NULL,
                dayOfWeek INTEGER NOT NULL,
                startTime TEXT NOT NULL,
                endTime TEXT NOT NULL,
                FOREIGN KEY(groupId) REFERENCES Groups(id)
            )
        """)

        db.execSQL("""
    CREATE TABLE UserSchedules (
        id TEXT PRIMARY KEY,
        userId TEXT NOT NULL,
        groupId TEXT NOT NULL,
        dayOfWeek INTEGER NOT NULL,
        startTime TEXT NOT NULL,
        endTime TEXT NOT NULL,
        FOREIGN KEY(userId) REFERENCES users(editTextId),
        FOREIGN KEY(groupId) REFERENCES Groups(id)
    )
""")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("drop table if exists users")
        db.execSQL("DROP TABLE IF EXISTS Groups")
        db.execSQL("DROP TABLE IF EXISTS Schedules")
        onCreate(db)
    }



    //회원 정보 삽입
    fun insertData (editTextId: String?, editTextPW: String?, editTextPhone: String?): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("editTextId", editTextId)
        values.put("editTextPW", editTextPW)
        values.put("editTextPhone",editTextPhone)
        val result = db.insert("users", null, values)
        db.close()
        return if (result == -1L)false else true
    }

    //아이디와 비밀번호 확인
    fun connectCheck(editTextId: String, editTextPW: String): Boolean {
        val db = this.writableDatabase
        var res = true
        val cursor = db.rawQuery(
            "Select * from users where editTextId = ? and editTextPW = ?", arrayOf(editTextId,editTextPW)
        )
        if (cursor.count <= 0) res = false
        return res
    }


    companion object {
        const val DBNAME = "Login.db"
    }

    // 게시물 작성
    fun insertPost(title: String, description: String, category: String, maxParticipants: Int): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("id", System.currentTimeMillis().toString()) // 고유 ID 생성
            put("title", title)
            put("description", description)
            put("category", category)
            put("maxParticipants", maxParticipants)
        }
        val result = db.insert("Groups", null, values)
        db.close()
        return result != -1L
    }

    // 게시물 삭제
    fun deletePost(postId: Int): Boolean {
        val db = this.writableDatabase
        val result = db.delete("Posts", "id=?", arrayOf(postId.toString()))
        db.close()

        return result > 0
    }

    // 전체 게시물 불러오기
    fun getAllPosts(): List<Recruitment> {
        val db = this.readableDatabase
        val postList = mutableListOf<Recruitment>()

        val cursor = db.rawQuery(
            """
        SELECT Groups.id, Groups.title, Groups.description, Groups.category, Groups.maxParticipants,
               Schedules.dayOfWeek, Schedules.startTime, Schedules.endTime
        FROM Groups
        LEFT JOIN Schedules ON Groups.id = Schedules.groupId
        """, null
        )

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getString(cursor.getColumnIndexOrThrow("id"))
                val title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
                val description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
                val category = cursor.getString(cursor.getColumnIndexOrThrow("category"))
                val maxParticipants = cursor.getInt(cursor.getColumnIndexOrThrow("maxParticipants"))

                // 일정 정보
                val dayOfWeek = cursor.getInt(cursor.getColumnIndexOrThrow("dayOfWeek")).coerceIn(1, 7)
                val startTime = cursor.getString(cursor.getColumnIndexOrThrow("startTime")) ?: "시간 없음"
                val endTime = cursor.getString(cursor.getColumnIndexOrThrow("endTime")) ?: "시간 없음"

                val scheduleText = if (dayOfWeek in 1..7) {
                    "${convertDayToKorean(dayOfWeek)} $startTime~$endTime"
                } else {
                    "일정 없음"
                }

                postList.add(Recruitment(id, title, scheduleText, category, description, maxParticipants))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return postList
    }

    // 요일 숫자를 한국어로 변환
    private fun convertDayToKorean(dayOfWeek: Int): String {
        return when (dayOfWeek) {
            1 -> "월요일"
            2 -> "화요일"
            3 -> "수요일"
            4 -> "목요일"
            5 -> "금요일"
            6 -> "토요일"
            7 -> "일요일"
            else -> "요일 없음"
        }
    }

    //사용자가 참여한 모임의 일정만 가져오기
    fun getUserSchedules(userId: String): List<UserSchedule> {
        val db = this.readableDatabase
        val schedules = mutableListOf<UserSchedule>()
        val cursor = db.rawQuery(
            "SELECT * FROM UserSchedules WHERE userId = ?",
            arrayOf(userId)
        )

        while (cursor.moveToNext()) {
            schedules.add(
                UserSchedule(
                    cursor.getString(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("groupId")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("dayOfWeek")),
                    cursor.getString(cursor.getColumnIndexOrThrow("startTime")),
                    cursor.getString(cursor.getColumnIndexOrThrow("endTime"))
                )
            )
        }
        cursor.close()
        db.close()
        return schedules
    }
    fun getGroupTitle(groupId: String): String {
        val db = this.readableDatabase
        var title = "모임 제목 없음"  // 기본값

        val cursor = db.rawQuery("SELECT title FROM Groups WHERE id = ?", arrayOf(groupId))
        if (cursor.moveToFirst()) {
            title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
        }
        cursor.close()
        db.close()
        return title
    }
}