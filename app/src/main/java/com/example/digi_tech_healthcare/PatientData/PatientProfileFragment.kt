package com.example.digi_tech_healthcare.PatientData

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.digi_tech_healthcare.BaseClasses.BaseFragment
import com.example.digi_tech_healthcare.R
import com.example.digi_tech_healthcare.databinding.FragmentPatientProfileBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class PatientProfileFragment : BaseFragment<FragmentPatientProfileBinding>() {

    override val bindingInflater: (LayoutInflater) -> FragmentPatientProfileBinding
        get() = FragmentPatientProfileBinding::inflate

    val REQUEST_CODE = 100
    var imageUrl: Uri = Uri.EMPTY
    lateinit var fstorage: FirebaseStorage
    lateinit var fstore: FirebaseFirestore
    var uid: String = ""
    val patientArrayList = ArrayList<PatientInfo>()
    var email = ""
    var name = ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivPatient.setOnClickListener {
            chooseImage()
        }
        binding.enterB.setOnClickListener {
                uploadData()
        }
        getPatientInfo()
        binding.reportTv.setOnClickListener {

            findNavController().navigate(R.id.action_patientProfileFragment_to_reportFragment)
        }

    }

    private fun chooseImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, REQUEST_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE || resultCode == RESULT_OK || data != null || data?.data != null) {
            try {
                imageUrl = data?.data!!
                Glide.with(requireContext()).load(imageUrl).into(binding.ivPatient)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getFileExt(uri: Uri): String? {
        val mimeType = MimeTypeMap.getSingleton()
        return mimeType.getExtensionFromMimeType(requireContext().contentResolver.getType(uri))
    }


    private fun uploadData() {
        val prefs: SharedPreferences? =
            activity?.getSharedPreferences("app-role", Context.MODE_PRIVATE)
        email = prefs?.getString("email", "")!!
        name = prefs?.getString("name","")!!
        fstorage = FirebaseStorage.getInstance()
        fstore = FirebaseFirestore.getInstance()


        val storageReference = fstorage.getReference("image")
        val documentReference = fstore.collection("patientInfo").document()
        val age = binding.ageEt.text.toString()
        val med = binding.medEt.text.toString()
        val doc = binding.docEt.text.toString()
        val fam = binding.famEt.text.toString()

        if (!TextUtils.isEmpty(age) || !TextUtils.isEmpty(med) || !TextUtils.isEmpty(doc) || !TextUtils.isEmpty(
                fam
            ) || imageUrl != null
        ) {
            val reference: StorageReference =
                storageReference.child("${System.currentTimeMillis()}." + getFileExt(imageUrl))
            val uploadTask = reference.putFile(imageUrl)

            val urlTask: Task<Uri> = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    throw task.exception!!
                }
                return@continueWithTask reference.downloadUrl
            }.addOnCompleteListener {
                if (it.isSuccessful) {
                    val downloadUri = it.result
                    val article = hashMapOf(
                        "name" to name,
                        "age" to age,
                        "medicalHistory" to med,
                        "doctorInfo" to doc,
                        "familyHistory" to fam,
                        "email" to email,

                        "url" to downloadUri.toString()
                    )

                    documentReference.set(article).addOnSuccessListener {
                        Toast.makeText(
                            context,
                            "Patient Information Added Successfully",
                            Toast.LENGTH_LONG
                        ).show()
//                        findNavController().navigate(R.id.action_addContentFragment_to_contentFragment)
                    }.addOnFailureListener {
                        Toast.makeText(
                            context,
                            "Patient Information could not be added successfully",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }.addOnFailureListener {
            }
        } else {
            Toast.makeText(context, "All fields are required", Toast.LENGTH_LONG).show()
        }
    }


    private fun getPatientInfo() {
        val prefs: SharedPreferences? =
            activity?.getSharedPreferences("app-role", Context.MODE_PRIVATE)
        email = prefs?.getString("email", "")!!
        val db = FirebaseFirestore.getInstance()
            db.collection("patientInfo").addSnapshotListener(object : EventListener<QuerySnapshot> {
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
                            if ( patientEmail== email) {
                                imageUrl = patientInfo.url.toUri()
                                if (imageUrl.toString().isNotEmpty()) {
                                    Glide.with(requireContext()).load(imageUrl)
                                        .into(binding.ivPatient)
                                }
                                binding.ageEt.setText(patientInfo.age)
                                binding.medEt.setText(patientInfo.medicalHistory)
                                binding.docEt.setText(patientInfo.doctorInfo)
                                binding.famEt.setText(patientInfo.familyHistory)
                                break
                            }
                        }
                    }
                    db.terminate()
                }
            })
        }


}