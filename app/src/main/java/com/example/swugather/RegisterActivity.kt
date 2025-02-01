package com.example.swugather
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class RegisterActivity : AppCompatActivity() {
    var DB:DBManager?=null

    lateinit var editTextId : EditText
    lateinit var editTextPassword: EditText
    lateinit var editTextRepassword : EditText
    lateinit var editTextPhone: EditText
    lateinit var btnRegister : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        DB = DBManager(this)

        editTextId = findViewById(R.id.editTextId)
        editTextPassword = findViewById(R.id.editTextPW)
        editTextRepassword = findViewById(R.id.editTextRePW)
        editTextPhone = findViewById(R.id.editTextPhone)
        btnRegister = findViewById(R.id.btnRegister)


        //가입 버튼 클릭
        btnRegister.setOnClickListener {
            val user = editTextId.text.toString()
            val password = editTextPassword.text.toString()
            val repassword = editTextRepassword.text.toString()
            val phone = editTextPhone.text.toString()

            //빈칸이 있을 경우
            if (user == "" || password == "" || repassword == "" || phone == "") {
                Toast.makeText(this@RegisterActivity,
                    "빈칸을 모두 채우시오.", Toast.LENGTH_SHORT).show()
            }
            else{
                //비밀번호 재확인
                if (password == repassword) {
                    val insert = DB!!.insertData(user, password, phone)

                    //가입 성공
                    if (insert == true) {
                        Toast.makeText(this@RegisterActivity,"가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                    }

                    //가입 실패
                    else{
                        Toast.makeText(this@RegisterActivity, "가입을 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }
                //재확인 비번 틀림
                else{
                    Toast.makeText(this@RegisterActivity, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }
}