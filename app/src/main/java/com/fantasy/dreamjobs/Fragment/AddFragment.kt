package com.fantasy.dreamjobs.Fragment


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.Navigation
import com.fantasy.dreamjobs.MainActivity
import com.fantasy.dreamjobs.R
import com.fantasy.dreamjobs.databinding.FragmentAddBinding
import com.fantasy.dreamjobs.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddFragment : Fragment() {
    private  lateinit var jobPosted: Button
    private lateinit var jobTitle : EditText
    private lateinit var binding : FragmentAddBinding
    private lateinit var location : EditText
    private lateinit var closeAdd : ImageView
    private lateinit var reset :  Button
    private lateinit var description : EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var uid : String
    private  lateinit var databaseReference : DatabaseReference
    private  lateinit var databaseReference1 : DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentAddBinding.inflate(layoutInflater)
        jobPosted=view.findViewById(R.id.jobPosted)
        jobTitle=view.findViewById(R.id.jobTitle)
        reset=view.findViewById(R.id.reset)
        closeAdd=view.findViewById(R.id.closeAdd)
        location=view.findViewById(R.id.location)
        description=view.findViewById(R.id.description)
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        reset.setOnClickListener {
            jobTitle.setText("Set a Job Title")
            location.setText("")
            description.setText("")
        }
        closeAdd.setOnClickListener {
            val intent = Intent(context,MainActivity::class.java)
            startActivity(intent)
        }
        jobPosted.setOnClickListener {
            val currentTimestamp = System.currentTimeMillis()
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("jobPost").child(currentTimestamp.toString())
            val map= HashMap<String,Any>()
            map["jobTitle"]=jobTitle.text.toString()
            map["location"]=location.text.toString()
            map["description"]=description.text.toString()
            map["time"] = currentTimestamp.toString()
            databaseReference.updateChildren(map).addOnCompleteListener{ it ->
                if (it.isSuccessful){
                    databaseReference1 = FirebaseDatabase.getInstance().getReference("Users").child("JobPosted").child(currentTimestamp.toString())
                    databaseReference1.updateChildren(map).addOnCompleteListener {
                        if (it.isSuccessful){
                            Toast.makeText(context,"Successfully job posted",Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(context,"something went wrong with main job post",Toast.LENGTH_SHORT).show()
                        }
                    }


                }
                else{
                    Toast.makeText(context,"something went wrong",Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}