package com.example.boogilog

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    lateinit var addEmail: EditText
    lateinit var addPassword: EditText
    lateinit var signUpBtn : Button

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()

        addEmail = findViewById(R.id.addEmail)
        addPassword = findViewById(R.id.addPwd)
        signUpBtn = findViewById(R.id.signUpBtn)

        signUpBtn.setOnClickListener {
            var email = addEmail.text.toString()
            var password = addPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { result ->

                        if (result.isSuccessful) {
                            Toast.makeText(this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                            var intent = Intent(this, MakeProfileActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();
            }
        }

    }

}
