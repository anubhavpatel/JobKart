package com.fantasy.dreamjobs

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var emailSignIn : EditText
    private lateinit var passwordSignIn : EditText
    private lateinit var  loginBtn : Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        emailSignIn=findViewById(R.id.emailSignIn)
        passwordSignIn=findViewById(R.id.passwordSignIn)
        loginBtn=findViewById(R.id.loginBtn)
        auth = Firebase.auth

        loginBtn.setOnClickListener{
            loginUser()
        }
    }
    private fun loginUser() {
        val email = emailSignIn.text.toString()
        val password = passwordSignIn.text.toString()
        when {
            TextUtils.isEmpty(email) -> Toast.makeText(this, "email is required", Toast.LENGTH_LONG)
                .show()
            TextUtils.isEmpty(password) -> Toast.makeText(
                this,
                "password name is required",
                Toast.LENGTH_LONG
            ).show()
            else -> {
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Sign Up")
                progressDialog.setMessage("Please wait,this may take a while")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(baseContext, " Login successfully", Toast.LENGTH_SHORT)
                                .show()
                            val intent = Intent(this, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                            progressDialog.dismiss()
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(baseContext, "incorrect data", Toast.LENGTH_SHORT).show()
                            progressDialog.dismiss()
                        }
                    }
            }
        }
    }

}