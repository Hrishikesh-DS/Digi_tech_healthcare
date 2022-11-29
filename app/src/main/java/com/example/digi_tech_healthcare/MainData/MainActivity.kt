package com.example.digi_tech_healthcare.MainData

import android.os.Bundle
import android.view.LayoutInflater
import com.example.digi_tech_healthcare.BaseClasses.BaseActivity
import com.example.digi_tech_healthcare.databinding.ActivityMainBinding


class MainActivity : BaseActivity<ActivityMainBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}