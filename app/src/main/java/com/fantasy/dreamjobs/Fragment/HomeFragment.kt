package com.fantasy.dreamjobs.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fantasy.dreamjobs.AdapterPostedJobs
import com.fantasy.dreamjobs.ModelPostedJobs
import com.fantasy.dreamjobs.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomeFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private  lateinit var databaseReference : DatabaseReference
    private lateinit var uid : String
   private lateinit var recyclerView: RecyclerView
   private lateinit var arrayList : ArrayList<ModelPostedJobs>
   private lateinit var adapterJobs : AdapterPostedJobs
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arrayList=ArrayList<ModelPostedJobs>()
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        recyclerView=view.findViewById(R.id.recyclerJobs)
        recyclerView.layoutManager=LinearLayoutManager(context)
        databaseReference = FirebaseDatabase.getInstance().reference.child("Users").child("JobPosted")
        databaseReference.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                arrayList.clear()
                for(i in snapshot.children){
                    val obj=ModelPostedJobs(
                        i.child("jobTitle").value.toString(),
                        i.child("location").value.toString(),
                        i.child("description").value.toString(),
                        i.child("time").value.toString(),
                    )
                    arrayList.add(obj)
                }
                adapterJobs=AdapterPostedJobs(this@HomeFragment,arrayList)
                recyclerView.adapter=adapterJobs
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}