package com.example.swugather

data class UserSchedule(
    val id: String,
    val groupId: String,
    val dayOfWeek: Int,
    val startTime: String,
    val endTime: String
)