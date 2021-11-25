package com.fantasy.dreamjobs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class RegisterActivity : AppCompatActivity() {
    private lateinit var backSignUp : ImageView
    private lateinit var registerbtn : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        backSignUp=findViewById(R.id.backSignUp)
        registerbtn=findViewById(R.id.registerBtn)
        backSignUp.setOnClickListener{
            val intent = Intent(this,StartActivity::class.java)
            startActivity(intent)
            finish();
        }
        registerbtn.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish();
        }
    }
}