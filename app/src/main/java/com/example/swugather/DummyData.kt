package com.example.swugather

object DummyData {
    fun getRecruitmentsByCategory(category: String): List<Recruitment> {
        return when (category) {
            "전체" -> listOf(
                Recruitment("프로그래밍 스터디 모집", "2025-01-20", 5),
                Recruitment("독서 모임 모집", "2025-01-22", 8)
            )
            "어학" -> listOf(
                Recruitment("영어 회화 스터디", "2025-01-23", 6),
                Recruitment("일본어 공부 모임", "2025-01-25", 4)
            )
            else -> emptyList()
        }
    }
}