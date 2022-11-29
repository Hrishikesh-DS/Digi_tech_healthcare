package com.example.digi_tech_healthcare.PatientData

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.digi_tech_healthcare.BaseClasses.BaseFragment
import com.example.digi_tech_healthcare.databinding.FragmentPatientInfoBinding


class PatientInfoFragment : BaseFragment<FragmentPatientInfoBinding>() {

    override val bindingInflater: (LayoutInflater) -> FragmentPatientInfoBinding
        get() = FragmentPatientInfoBinding::inflate

    private val args: PatientInfoFragmentArgs by navArgs()
    private val name: String by lazy {
        args.name
    }
    private val images: String by lazy {
        args.image
    }
    private val doc: String by lazy {
        args.doc
    }
    private val age: String by lazy {
        args.age
    }
    private val emails: String by lazy {
        args.email
    }
    private val fam: String by lazy {
        args.fam
    }
    private val med: String by lazy {
        args.med
    }
    private val role: String by lazy {
        args.role
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeUI()
        binding.reportTv.setOnClickListener {
            findNavController().navigate(
                PatientInfoFragmentDirections.actionPatientInfoFragmentToPatientReportFragment()
                    .apply {
                    email=emails
                }
            )
        }
    }

    private fun initializeUI() {
        Glide.with(requireContext()).load(images).into(binding.ivContentIcon)
        binding.nameTv.text = name
        binding.ageTv.text = age
        binding.medTv.text = med
        binding.docTv.text = doc
        binding.famTv.text = fam
        binding.mailTv.text = emails
    }



}