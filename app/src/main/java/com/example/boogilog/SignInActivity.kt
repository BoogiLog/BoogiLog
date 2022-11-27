package com.example.boogilog

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class SignInActivity : AppCompatActivity() {
    lateinit var inputEmail: EditText
    lateinit var inputPwd: EditText
    lateinit var loginBtn: Button
    lateinit var goSignUp : Button
    var firebaseAuth:FirebaseAuth? = null
    lateinit var firestore :FirebaseFirestore

    val db: FirebaseFirestore = Firebase.firestore
    var auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_singin)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val database = Firebase.database

        inputEmail = findViewById(R.id.inputEmail)
        inputPwd = findViewById(R.id.inputPwd)
        loginBtn = findViewById(R.id.signInBtn)
        goSignUp = findViewById(R.id.gotoSignUp)

        val path = auth?.currentUser?.email

        db.collection("users")
            .document(path.toString()).get()
            .addOnSuccessListener {
                System.out.println("success")
            }


        loginBtn.setOnClickListener {

            var email = inputEmail.text.toString()
            var password = inputPwd.text.toString()
            firebaseAuth = FirebaseAuth.getInstance()


            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(baseContext, "${email}님 환영합니다!", Toast.LENGTH_SHORT).show()
                            login(email, password)
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
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
