package com.example.digi_tech_healthcare.BaseClasses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB: ViewBinding> : AppCompatActivity() {

    private var _binding: VB? = null
    abstract val bindingInflater: (LayoutInflater) -> VB
    lateinit var layout: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = bindingInflater.invoke(layoutInflater).apply {
            layout = root
            setContentView(root)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}