package com.example.digi_tech_healthcare.MainData

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.digi_tech_healthcare.BaseClasses.BaseFragment
import com.example.digi_tech_healthcare.R
import com.example.digi_tech_healthcare.databinding.FragmentLogoutBinding
import com.google.firebase.auth.FirebaseAuth

class LogoutFragment : BaseFragment<FragmentLogoutBinding>(){
    override val bindingInflater: (LayoutInflater) -> FragmentLogoutBinding
        get() = FragmentLogoutBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var builder = AlertDialog.Builder(activity)
        builder.setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                // on yes selected,delete sharedpref and database
                val fAuth = FirebaseAuth.getInstance()
                fAuth.signOut()

                val prefs: SharedPreferences? =
                    getActivity()?.getSharedPreferences("app-role", Context.MODE_PRIVATE)

                if (prefs != null) {
                    prefs.edit().remove("email").commit()
                    prefs.edit().remove("role").commit()
                    prefs.edit().remove("name").commit()
                }
                navigateToHomeScreen()

            }
            .setNegativeButton("No") { _, _ ->
                findNavController().navigate(R.id.action_logoutFragment_to_informationFragment)
            }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()

        super.onViewCreated(view, savedInstanceState)
    }

    fun navigateToHomeScreen(){
        val intent = Intent(activity, MainActivity::class.java)
        activity?.finish()
        startActivity(intent)
    }
}