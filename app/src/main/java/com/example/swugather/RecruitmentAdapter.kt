package com.example.swugather

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecruitmentAdapter(private var recruitmentList: List<Recruitment>) :
    RecyclerView.Adapter<RecruitmentAdapter.RecruitmentViewHolder>() {

    class RecruitmentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.titleTextView)
        val schedule: TextView = view.findViewById(R.id.scheduleTextView)
        val category: TextView = view.findViewById(R.id.categoryTextView)
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
        holder.schedule.text = recruitment.schedule // "요일 시작시간~종료시간" 표시
        holder.category.text = "카테고리: ${recruitment.category}"
        holder.participants.text = "모집 인원: ${recruitment.maxParticipants}명"

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, PostDetailActivity::class.java).apply {
                putExtra("post_id", recruitment.id)
                putExtra("post_title", recruitment.title)
                putExtra("post_schedule", recruitment.schedule)
                putExtra("post_category", recruitment.category)
                putExtra("post_description", recruitment.description)
                putExtra("post_maxParticipants", recruitment.maxParticipants)
            }
            context.startActivity(intent)
        }
    }
    override fun getItemCount(): Int = recruitmentList.size

    fun updateData(newList: List<Recruitment>) {
        recruitmentList = newList
        notifyDataSetChanged()
    }
}