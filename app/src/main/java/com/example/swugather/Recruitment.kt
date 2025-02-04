package com.example.swugather

data class Recruitment(
    val id: String,
    val title: String,
    val schedule: String,  // 일정 정보 추가
    val category: String,
    val description: String,
    val maxParticipants: Int
)