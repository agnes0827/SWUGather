package com.example.swugather

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBManager(context: Context) : SQLiteOpenHelper(context, "GroupApp.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
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
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Groups")
        db.execSQL("DROP TABLE IF EXISTS Schedules")
        onCreate(db)
    }
}