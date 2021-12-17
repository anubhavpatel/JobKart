package com.fantasy.dreamjobs.Fragment

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.fantasy.dreamjobs.EditProfileActivity
import com.fantasy.dreamjobs.LoginActivity
import com.fantasy.dreamjobs.R
import com.fantasy.dreamjobs.databinding.FragmentProfileBinding

import com.firebase.ui.auth.data.model.User
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class ProfileFragment : Fragment() {
    private lateinit var binding : FragmentProfileBinding
    private lateinit var auth: FirebaseAuth
    private  lateinit var databaseReference : DatabaseReference
    private lateinit var storageReference: DatabaseReference
    private lateinit var uid : String
    private lateinit var user : User
    private  lateinit var dialog :Dialog
    private lateinit var profile_name : TextView
    private  lateinit var setImage : ImageView
    private lateinit var imgProfile : CircleImageView
    private lateinit var logOutAcc : TextView
    private lateinit var editProfile : TextView
    private var filepath: Uri?=null
    private  var uri:String?=null
    private lateinit var ImageUri : Uri
    private  var ImgUrl : String=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return  inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        profile_name= view.findViewById<TextView>(R.id.profile_name)!!
        binding = FragmentProfileBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        if (checkNetwork()) {
        }
        else if (!checkNetwork()) {
            val builder = AlertDialog.Builder(context)
            builder.setMessage("Ohh fuck!")
            // Set Alert Title
            builder.setTitle("No internet connection")
            builder.setPositiveButton("OK",null)
            val alertDialog = builder.create()
            alertDialog.show()

        }
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        if (uid.isNotEmpty()) {
            getUserData()
        }

        setImage = view.findViewById(R.id.setImage)
        imgProfile = view.findViewById(R.id.imgProfile)
        setImage.setOnClickListener {
           startFileChooser()
        }
        editProfile=view.findViewById(R.id.editProfile)
        editProfile.setOnClickListener {
            val intent = Intent(activity,EditProfileActivity::class.java)
            startActivity(intent)
        }
        logOutAcc=view.findViewById(R.id.logOutAcc)
        logOutAcc.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(activity,LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
        private fun checkNetwork() : Boolean{
            return try {
                val command = "ping -c 1 google.com"
                Runtime.getRuntime().exec(command).waitFor() == 0
            } catch (e: Exception) {
                false
            }
        }
    private fun startFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Choose Image"), 111)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 111 && resultCode == Activity.RESULT_OK && data != null) {
            filepath = data.data!!

            val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, filepath)
            imgProfile.setImageBitmap(bitmap)
        }
        if(filepath!=null) {
            uploadImageToFirebaseStorage()
        }
    }
    private fun uploadImageToFirebaseStorage() {
        if(filepath!= null){
            val progressDialog = ProgressDialog(context)
            progressDialog.setTitle("Uploading...")
            progressDialog.show()
            var imageRef =FirebaseStorage.getInstance().reference.child("image/pic.jpg")
            val uploadTask:UploadTask=imageRef.putFile(filepath!!)
            uploadTask.addOnSuccessListener { it ->
                val imageUri=it.storage.downloadUrl
                imageUri.addOnSuccessListener {
                    ImgUrl=it.toString()
                    Toast.makeText(context,"Uploaded",Toast.LENGTH_SHORT).show()
                    if(ImgUrl!=""){
                        databaseReference.child(uid).child("imgUrl").setValue(ImgUrl)
                    }
                    progressDialog.dismiss()
                }
            }





    }
    }
    
    private fun getUserData() {

        databaseReference.child(uid).addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {

                 profile_name.text=snapshot.child("fullName").value.toString()

//                Glide.with(activity!!).load(uri).into(imgProfile)
                if(snapshot.child("imgUrl").exists()){
                context?.let {
                    Glide.with(it)
                        .load(snapshot.child("imgUrl").value.toString())
                        .into(imgProfile)
                }
            }else{
//                    context?.let {
//                        Glide.with(it)
//                            .load()
//                            .into(imgProfile)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

}