package com.fantasy.dreamjobs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class EditProfileActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private  lateinit var databaseReference : DatabaseReference
    private lateinit var uid : String
    private lateinit var fullNameEditProfile : TextView
    private lateinit var mobileNoEditProfile : TextView
    private lateinit var emailEditProfile : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        fullNameEditProfile=findViewById(R.id.fullNameEditProfile)
        mobileNoEditProfile=findViewById(R.id.mobileNoEditProfile)
        emailEditProfile=findViewById(R.id.emailEditProfile)
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        if (uid.isNotEmpty()) {
            getUserData()
        }
    }

    private fun getUserData() {
        databaseReference.child(uid).addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {

                fullNameEditProfile.text=snapshot.child("fullName").value.toString()
                emailEditProfile.text=snapshot.child("email").value.toString()
                mobileNoEditProfile.text=snapshot.child("mobileNumber").value.toString()

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    }