package com.fantasy.dreamjobs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fantasy.dreamjobs.Fragment.HomeFragment

class AdapterPostedJobs(val context: HomeFragment, val jobPost: ArrayList<ModelPostedJobs>):
    RecyclerView.Adapter<AdapterPostedJobs.HolderJob>() {
    class HolderJob(view: View) :RecyclerView.ViewHolder(view) {
        val jobTitle :TextView=view.findViewById(R.id.titleJob)
        val location :TextView=view.findViewById(R.id.locationJob)
        val description:TextView=view.findViewById(R.id.descriptionJob)
        val time :TextView=view.findViewById(R.id.timeJob)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderJob {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.single_job_posted,parent,false)
        return HolderJob(view)
    }

    override fun onBindViewHolder(holder: HolderJob, position: Int) {
        val jobs=jobPost[position]
        holder.jobTitle.text=jobs.jobTitle
        holder.location.text=jobs.location
        holder.description.text=jobs.description
    }

    override fun getItemCount(): Int {
        return jobPost.size
    }
}