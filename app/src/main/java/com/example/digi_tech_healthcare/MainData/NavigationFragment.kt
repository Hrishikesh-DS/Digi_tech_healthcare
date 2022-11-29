package com.example.digi_tech_healthcare.MainData

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.digi_tech_healthcare.BaseClasses.BaseFragment
import com.example.digi_tech_healthcare.R
import com.example.digi_tech_healthcare.databinding.FragmentNavigationBinding


class NavigationFragment : BaseFragment<FragmentNavigationBinding>() {

    override val bindingInflater: (LayoutInflater) -> FragmentNavigationBinding
        get() = FragmentNavigationBinding::inflate
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigate()
    }

    private fun navigate(){
        val prefs: SharedPreferences? =
            activity?.getSharedPreferences("app-role", Context.MODE_PRIVATE)
        val roles = prefs?.getString("role", "")!!
        if(roles=="Doctor"){
            findNavController().navigate(R.id.action_navigationFragment_to_doctorFragment)
        }else{
            findNavController().navigate(R.id.action_navigationFragment_to_informationFragment)
        }

    }
}