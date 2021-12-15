package com.fantasy.dreamjobs

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {
    private lateinit var backSignUp : ImageView
    private lateinit var registerbtn : Button
    private lateinit var fullNameSignUp : EditText
    private lateinit var mobileNumberSignUp : EditText
    private lateinit var emailSignUp : EditText
    private lateinit var passwordSignUp : EditText
    private lateinit var datbaseref:DatabaseReference
    private  lateinit var backToLogin: TextView
    private var bool:Boolean=false
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        backSignUp=findViewById(R.id.backSignUp)
        registerbtn=findViewById(R.id.registerBtn)
        fullNameSignUp=findViewById(R.id.fullNameSignUp)
        mobileNumberSignUp=findViewById(R.id.mobileNumberSignUp)
        emailSignUp=findViewById(R.id.emailSignUp)
        passwordSignUp=findViewById(R.id.passwordSignUp)
        backToLogin=findViewById(R.id.backToLogin)
        auth = Firebase.auth
        backToLogin.setOnClickListener {
            val intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
        backSignUp.setOnClickListener{
            val intent = Intent(this,StartActivity::class.java)
            startActivity(intent)
            finish();
        }
        registerbtn.setOnClickListener{
            createAccount()
        }
    }

    private fun createAccount() {

        val fullName = fullNameSignUp.text.toString()
        val  mobileNumber= mobileNumberSignUp.text.toString()
        val  email = emailSignUp.text.toString()
        val  password = passwordSignUp.text.toString()
        datbaseref=FirebaseDatabase.getInstance().reference.child("Users")
        datbaseref.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                bool=false
                for(i in snapshot.children) {
                    if(i.child("email").value.toString()==email){
                        bool=true
                    }
                }
                when{
                    TextUtils.isEmpty(fullName)-> Toast.makeText(this@RegisterActivity,"full name is required", Toast.LENGTH_LONG).show()
                    TextUtils.isEmpty(mobileNumber)  -> Toast.makeText(this@RegisterActivity,"mobile number is less than 10 can't proceed", Toast.LENGTH_LONG).show()
                    TextUtils.isEmpty(email) || bool -> Toast.makeText(this@RegisterActivity,"email already exits or required", Toast.LENGTH_LONG).show()
                    TextUtils.isEmpty(password)||(password.length<6 )-> Toast.makeText(this@RegisterActivity,"password is required", Toast.LENGTH_LONG).show()
                    else -> {
                        if (mobileNumber.length==10){
                            val progressDialog = ProgressDialog(this@RegisterActivity)
                            progressDialog.setTitle("Sign Up")
                            progressDialog.setMessage("Please wait,this may take a while")
                            progressDialog.setCanceledOnTouchOutside(false)
                            progressDialog.show()
                            auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(this@RegisterActivity) { task ->
                                    if (task.isSuccessful) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Toast.makeText(baseContext, "Authentication success.",
                                            Toast.LENGTH_SHORT).show()
                                        val user = auth.currentUser
                                        saveInfo(fullName, mobileNumber, email, password)
                                        progressDialog.dismiss()

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(baseContext, " failed.",
                                            Toast.LENGTH_SHORT).show()
                                        progressDialog.dismiss()
                                    }
                                }}else{
                            Toast.makeText(this@RegisterActivity,"mobile number is less than 10 can't proceed", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }
    private fun saveInfo(fullName: String, mobileNumber: String, email: String, password: String) {
        val currentUseID= FirebaseAuth.getInstance().currentUser!!.uid
        val userRef : DatabaseReference=FirebaseDatabase.getInstance().reference.child("Users")
        val userMap = HashMap<String,Any>()
        userMap["uid"] = currentUseID
        userMap["fullName"] = fullName
        userMap["mobileNumber"] = mobileNumber
        userMap["email"] = email
        userMap["password"] = password
        userRef.child(currentUseID).updateChildren(userMap)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    Toast.makeText(baseContext, "Account created successfully",Toast.LENGTH_LONG).show()
                    val intent = Intent(this,MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }
                else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
    }
}