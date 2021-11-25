package com.fantasy.dreamjobs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class StartActivity : AppCompatActivity() {
    private  lateinit var register : Button
    private lateinit var  login : Button
    private  var backPressedTime : Long =0
    private   var backToast : Toast?= null
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        register=findViewById(R.id.register)
        login=findViewById(R.id.login)
        auth = Firebase.auth
       register.setOnClickListener{
           val intent = Intent(this,RegisterActivity::class.java)
           startActivity(intent)
       }
        login.setOnClickListener{
            val intent = Intent(this,LoginActivity ::class.java)
            startActivity(intent)
        }
    }
    override fun onBackPressed() {
        if(backPressedTime + 2000 > System.currentTimeMillis()){
            backToast?.cancel()
            super.onBackPressed()
            return
        }
        else{
            backToast =Toast.makeText(baseContext,"Press again to exit app",Toast.LENGTH_SHORT)
            backToast?.show()
        }
        backPressedTime =System.currentTimeMillis()
    }
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            val intent = Intent(this,MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }
}