package com.example.swugather

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBManager(context: Context) : SQLiteOpenHelper(context, "GroupApp.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {

        //회원가입 정보 저장 테이블
        db!!.execSQL("CREATE TABLE users(editTextId text primary key, editTextPW text, editTextRePW text, editTextPhone text)")

        db.execSQL("""
            CREATE TABLE Groups (
                id TEXT PRIMARY KEY,
                title TEXT NOT NULL,
                description TEXT,
                category TEXT,
                maxParticipants INTEGER,
                author_id TEXT,
                FOREIGN KEY(author_id) REFERENCES users(editTextId) ON DELETE CASCADE
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
}