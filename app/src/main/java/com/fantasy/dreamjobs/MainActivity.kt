package com.fantasy.dreamjobs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.fantasy.dreamjobs.Fragment.AddFragment
import com.fantasy.dreamjobs.Fragment.HomeFragment
import com.fantasy.dreamjobs.Fragment.ProfileFragment
import com.fantasy.dreamjobs.Fragment.chat
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private var onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener{ item->
        when(item.itemId){
            R.id.nav_home->{
                moveToFragment(HomeFragment())
                return@OnNavigationItemSelectedListener  true
            }
            R.id.nav_add->{
                moveToFragment(AddFragment())
                return@OnNavigationItemSelectedListener  true
            }
            R.id.nav_chat->{
                moveToFragment(chat())
                return@OnNavigationItemSelectedListener  true
            }
            R.id.nav_profile->{
                moveToFragment(ProfileFragment())
                return@OnNavigationItemSelectedListener  true
            }}
            false
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView=findViewById(R.id.nav_view)

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        moveToFragment(HomeFragment())
    }
    private fun moveToFragment(fragment: Fragment){
        val fragmentTrans = supportFragmentManager.beginTransaction()
        fragmentTrans.replace(R.id.fragment_container,fragment)
        fragmentTrans.commit()
    }
}