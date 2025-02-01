package com.example.swugather

import android.graphics.Paint
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TodoActivity : AppCompatActivity() {

    lateinit var todoList: ArrayList<String>
    lateinit var adapter: ArrayAdapter<String>
    lateinit var todoEdit: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)

            //초기화
        todoList=ArrayList()

        adapter = ArrayAdapter(this,R.layout.list_item,todoList)

        val listView: ListView = findViewById(R.id.list_view)
        val addBtn: Button = findViewById(R.id.add_btn)
        todoEdit = findViewById(R.id.todo_edit)

        listView.adapter = adapter

        addBtn.setOnClickListener {
            addItem()
        }

        //얘가 안 됨
        listView.setOnItemClickListener { adapterView, view, i, l ->
            val textView: TextView = view as TextView
            textView.paintFlags = if (textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG > 0) {
                textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv() // 취소
            } else {
                textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG // 취소선 추가
            }
        }

    }

    private fun addItem() {
        val todo: String = todoEdit.text.toString()

        if(todo.isNotEmpty()){
            todoList.add(todo)

            adapter.notifyDataSetChanged()
        }
        else{
            Toast.makeText(this,"일정",Toast.LENGTH_SHORT).show()
        }
    }
}