package com.example.boogilog

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {
    lateinit var inputEmail: EditText
    lateinit var inputPwd: EditText
    lateinit var loginBtn: Button
    lateinit var goSignUp : Button
    lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_singin)

        auth = FirebaseAuth.getInstance()

        inputEmail = findViewById(R.id.inputEmail)
        inputPwd = findViewById(R.id.inputPwd)
        loginBtn = findViewById(R.id.signInBtn)
        goSignUp = findViewById(R.id.gotoSignUp)

        loginBtn.setOnClickListener {
            var email = inputEmail.text.toString()
            var password = inputPwd.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth?.signInWithEmailAndPassword(email, password)
                    ?.addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(baseContext, "${email}님 환영합니다!", Toast.LENGTH_SHORT).show()
                            login(email, password)
                        }
                        else {
                            Toast.makeText(baseContext, "로그인에 실패 하였습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            else if(email.isEmpty() && password.isNotEmpty()){
                Toast.makeText(this, "이메일을 입력하세요.", Toast.LENGTH_LONG).show();
            }
            else if(email.isNotEmpty() && password.isEmpty()){
                Toast.makeText(this, "비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(baseContext, "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
            }
        }

        goSignUp.setOnClickListener {
            var intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    fun login(email:String, password:String){
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                    result->
                if(result.isSuccessful){
                    var intent = Intent(this, NaviActivity::class.java)
                    startActivity(intent)
                }
            }
    }
}
