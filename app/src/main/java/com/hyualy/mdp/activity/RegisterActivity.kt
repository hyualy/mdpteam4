package com.hyualy.mdp.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hyualy.mdp.R
import com.hyualy.mdp.manager.Wifi
import com.hyualy.mdp.util.LoginUtil

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        settingButton()
    }

    private fun settingButton() {
        val btnRegister = findViewById<Button>(R.id.register_register)
        btnRegister.setOnClickListener {

            val id = findViewById<EditText>(R.id.register_id).text.toString()
            val password = findViewById<EditText>(R.id.register_password).text.toString()
            val checkPassword = findViewById<EditText>(R.id.register_check_password).text.toString()
            val name = findViewById<EditText>(R.id.register_name).text.toString()
            val email = findViewById<EditText>(R.id.register_email).text.toString()

            if (!LoginUtil.lengthChecker(this, id.length, password.length, name.length, email.length)) {
                return@setOnClickListener
            }
            if (listOf(id, password, checkPassword, name, email).all { it.isNotEmpty() }) {
                if (password == checkPassword) {
                    Wifi.sendData("register/$id/$password/$name/$email")
                    Wifi.receiveData(this) { data ->
                        if (data == "register/yes") {
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            Toast.makeText(this, "회원가입 완료", Toast.LENGTH_SHORT).show()
                            finish()
                        } else if (data == "register/no") {
                            Toast.makeText(this, "이미 등록된 아이디입니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "비밀번호가 같지 않습니다.", Toast.LENGTH_SHORT).show()
                    println(password)
                    println(checkPassword)
                }
            } else {
                Toast.makeText(this, "모든 양식을 채워주십시오.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}