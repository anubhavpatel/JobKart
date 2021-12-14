package com.fantasy.dreamjobs

class ModelPostedJobs (
    val jobTitle: String,
    val location:String,
    val description:String,
    val time : String
) {
    constructor() : this("","","","")
}