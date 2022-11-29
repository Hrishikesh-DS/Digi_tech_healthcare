package com.example.digi_tech_healthcare.MainData

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.digi_tech_healthcare.BaseClasses.BaseActivity
import com.example.digi_tech_healthcare.R
import com.example.digi_tech_healthcare.databinding.ActivityPatientBinding
import com.google.android.material.navigation.NavigationView

class PatientActivity : BaseActivity<ActivityPatientBinding>() {


    private lateinit var appBarConfiguration: AppBarConfiguration
    override val bindingInflater: (LayoutInflater) -> ActivityPatientBinding
        get() = ActivityPatientBinding::inflate


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs: SharedPreferences? =
            getSharedPreferences("app-role", Context.MODE_PRIVATE)
        val role = prefs?.getString("role","")
        Toast.makeText(this,role,Toast.LENGTH_SHORT).show()
        if(role=="Patient"){
            setupPatientInfo()
        }
        else if(role=="Content Manager"){
            setUpContentManagerInfo()
        }
        else if(role=="Doctor"){
            setUpDoctorInfo()
        }


    }

    private fun setUpDoctorInfo(){
        val drawerLayout: DrawerLayout = findViewById(R.id.patient_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_patient)
        navView.getMenu().clear()
        navView.inflateMenu(R.menu.doctor_menu);

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.doctorFragment,
                R.id.logoutFragment,
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
    private fun setupPatientInfo(){
        val drawerLayout: DrawerLayout = findViewById(R.id.patient_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_patient)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.informationFragment,
                R.id.patientProfileFragment,
                R.id.logoutFragment,
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun setUpContentManagerInfo(){
        val drawerLayout: DrawerLayout = findViewById(R.id.patient_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_patient)
        navView.getMenu().clear();
        navView.inflateMenu(R.menu.content_manager_menu);

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.informationFragment,
                R.id.logoutFragment,
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_patient)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}