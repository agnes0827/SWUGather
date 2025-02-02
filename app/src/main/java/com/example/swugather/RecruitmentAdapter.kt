package com.example.swugather

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class Recruitment(val id: String, val title: String, val description: String, val category: String, val maxParticipants: Int)

class RecruitmentAdapter(private val recruitmentList: List<Recruitment>) :
    RecyclerView.Adapter<RecruitmentAdapter.RecruitmentViewHolder>() {

    class RecruitmentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.titleTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecruitmentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recruitment, parent, false)
        return RecruitmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecruitmentViewHolder, position: Int) {
        val recruitment = recruitmentList[position]
        holder.title.text = recruitment.title

        // 게시물 클릭 시 상세 페이지로 이동
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, PostDetailActivity::class.java).apply {
                putExtra("post_id", recruitment.id)  // 게시물 ID 전달
                putExtra("post_title", recruitment.title)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = recruitmentList.size
}