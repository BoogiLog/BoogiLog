package com.example.boogilog

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    lateinit var addEmail: EditText
    lateinit var addPassword: EditText
    lateinit var signUpBtn : Button

    lateinit var auth: FirebaseAuth
    var firebaseAuth:FirebaseAuth? = null

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

            firebaseAuth = FirebaseAuth.getInstance()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { result ->
                        if (result.isSuccessful) {
                            Toast.makeText(this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                            var intent = Intent(this, MakeProfileActivity::class.java)
                            startActivity(intent)
                        } else {
                            try{
                                result.result;
                            } catch (e: Exception) {
                                e.printStackTrace();
                                if(password.length>=6)
                                    Toast.makeText(this, "입력하신 이메일을 다시 확인해주세요", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
            }

            else if(email.isEmpty() && password.isNotEmpty()){
                Toast.makeText(this, "이메일을 입력하세요.", Toast.LENGTH_LONG).show();
            }
            else if(email.isNotEmpty() && password.isEmpty()){
                Toast.makeText(this, "비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();
            }
            else if (email.isEmpty() && password.isEmpty()) {
                Toast.makeText(this, "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();
            }
            else if(password.length < 6) {
                Toast.makeText(this, "비밀번호는 6자리 이상이어야 합니다.", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this, "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show()
            }

        }

    }

}
