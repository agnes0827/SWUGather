package com.example.swugather

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    lateinit var btnLogin: Button
    lateinit var editTextId : EditText
    lateinit var editTextPassword : EditText
    lateinit var btnRegister : Button

    var DB:DBManager?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DB = DBManager(this)

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

                    //if문 넘어갈 시에 PostView로 이동
                    val passintent = Intent(this@MainActivity, TodoActivity::class.java)
                    startActivity(passintent)
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