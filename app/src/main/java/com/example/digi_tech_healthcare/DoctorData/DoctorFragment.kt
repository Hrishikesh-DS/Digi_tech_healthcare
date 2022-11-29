package com.example.digi_tech_healthcare.DoctorData

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.digi_tech_healthcare.BaseClasses.BaseFragment
import com.example.digi_tech_healthcare.Dataclasses.PatientResponse
import com.example.digi_tech_healthcare.PatientData.PatientAdapter
import com.example.digi_tech_healthcare.PatientData.PatientInfo
import com.example.digi_tech_healthcare.databinding.FragmentDoctorBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DoctorFragment: BaseFragment<FragmentDoctorBinding>(){

    override val bindingInflater: (LayoutInflater) -> FragmentDoctorBinding
        get() = FragmentDoctorBinding::inflate

    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    var roles = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        val prefs: SharedPreferences? =
            activity?.getSharedPreferences("app-role", Context.MODE_PRIVATE)
        roles = prefs?.getString("role", "")!!
        fetchData()
    }
    private fun fetchData() {
        val docRef = db.collection("patientInfo")
        docRef.get().addOnCompleteListener {
            val patientResponse = PatientResponse()
            if (it.isSuccessful) {
                val result = it.result
                result?.let {
                    patientResponse.patients = result.documents.mapNotNull { snapShot ->
                        snapShot.toObject(PatientInfo::class.java)
                    }
                }
                if (patientResponse.patients != null) {
                    setUpAdapter(patientResponse.patients ?: listOf())
                }
            }
        }
        binding.rvArticles.isVisible = true
    }
    private fun setUpAdapter(patientList: List<PatientInfo>) {
        val adapter = PatientAdapter(patientList as ArrayList<PatientInfo>) {
            findNavController().navigate(
                DoctorFragmentDirections.actionDoctorFragmentToPatientInfoFragment()
                    .apply {
                    name=it.name?:""
                    doc=it.doctorInfo?:""
                    image=it.url?:""
                    age=it.age?:""
                    email=it.email?:""
                    fam=it.familyHistory?:""
                    med=it.medicalHistory?:""
                    role=roles
                }
            )
        }
        binding.rvArticles.adapter = adapter
    }
}