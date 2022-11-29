package com.example.digi_tech_healthcare.PatientData

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.digi_tech_healthcare.BaseClasses.BaseFragment
import com.example.digi_tech_healthcare.databinding.FragmentPatientReportBinding
import com.google.firebase.firestore.*

class PatientReportFragment : BaseFragment<FragmentPatientReportBinding>() {
    

    var imageUrl: Uri = Uri.EMPTY

    private val args: PatientReportFragmentArgs by navArgs()
    private val emails: String by lazy {
        args.email
    }

    override val bindingInflater: (LayoutInflater) -> FragmentPatientReportBinding
        get() = FragmentPatientReportBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getPatientReport()
        binding.backB.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    private fun getPatientReport() {
        val db = FirebaseFirestore.getInstance()
        db.collection("patientReport").addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(
                value: QuerySnapshot?,
                error: FirebaseFirestoreException?
            ) {
                if (error != null) {
                    Log.d("Firebase error", "${error}")
                    return
                }
                for (pi: DocumentChange in value?.documentChanges!!) {
                    if (pi.type == DocumentChange.Type.ADDED) {
                        val patientInfo = pi.document.toObject(PatientInfo::class.java)
                        val patientEmail = patientInfo.email
                        if (patientEmail==emails) {
                            imageUrl = patientInfo.url.toUri()
                            if (imageUrl.toString().isNotEmpty()) {
                                Glide.with(requireContext()).load(imageUrl)
                                    .into(binding.reportIv)
                            }
                            break
                        }
                    }
                }
                db.terminate()
            }
        })
    }

}