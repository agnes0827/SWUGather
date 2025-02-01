package com.example.swugather

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class Recruitment(val title: String, val date: String, val participants: Int)

class RecruitmentAdapter(private val recruitmentList: List<Recruitment>) :
    RecyclerView.Adapter<RecruitmentAdapter.RecruitmentViewHolder>() {

    class RecruitmentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.titleTextView)
        val date: TextView = view.findViewById(R.id.dateTextView)
        val participants: TextView = view.findViewById(R.id.participantsTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecruitmentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recruitment, parent, false)
        return RecruitmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecruitmentViewHolder, position: Int) {
        val recruitment = recruitmentList[position]
        holder.title.text = recruitment.title
        holder.date.text = "날짜: ${recruitment.date}"
        holder.participants.text = "참여자: ${recruitment.participants}명"

        // 게시물 클릭 시 상세 페이지로 이동
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, PostDetailActivity::class.java).apply {
                putExtra("post_title", recruitment.title)
                putExtra("post_date", recruitment.date)
                putExtra("post_participants", recruitment.participants)
            }
            context.startActivity(intent)
        }
    }
    override fun getItemCount(): Int = recruitmentList.size
}