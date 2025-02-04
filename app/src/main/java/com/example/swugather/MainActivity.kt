package com.example.swugather

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    lateinit var btnLogin: Button
    lateinit var editTextId : EditText
    lateinit var editTextPassword : EditText
    lateinit var btnRegister : Button
    private lateinit var sharedPreferences: SharedPreferences

    var DB:DBManager?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DB = DBManager(this)
        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        btnLogin = findViewById(R.id.btnLogin)
        editTextId = findViewById(R.id.editTextId)
        editTextPassword = findViewById(R.id.editTextPassword)
        btnRegister = findViewById(R.id.btnRegister)


        btnLogin.setOnClickListener{
            val id = editTextId.text.toString()
            val password = editTextPassword.text.toString()

            //아무것도 넣지 않았을 경우
            if(id == "" || password == "") {
                Toast.makeText(this@MainActivity, "아이디와 비밀번호를 다시 입력해주세요..",
                    Toast.LENGTH_SHORT).show()
                }
            else{
                val connectCheck = DB!!.connectCheck(id, password)

                if (connectCheck == true) {
                    Toast.makeText(this@MainActivity, "안녕하세요, " + id + "님", Toast.LENGTH_SHORT).show()

                    val editor = sharedPreferences.edit()
                    editor.putString("user_id", id)
                    editor.apply() // 저장 완료!

                    // 로그 확인
                    val savedUserId = sharedPreferences.getString("user_id", null)
                    Log.d("MainActivity", "저장된 user_id: $savedUserId")

                    //if문 넘어가면 게시물 목록 페이지로 이동
                    val passintent = Intent(this@MainActivity, RecruitmentBoardActivity::class.java)
                    startActivity(passintent)
                    finish()
                }

                else{
                    Toast.makeText(this@MainActivity, "아이디와 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        //회원가입 버튼 클릭
        btnRegister.setOnClickListener{
            val loginIntent = Intent(this@MainActivity,RegisterActivity::class.java)
            startActivity(loginIntent)
        }

    }
}