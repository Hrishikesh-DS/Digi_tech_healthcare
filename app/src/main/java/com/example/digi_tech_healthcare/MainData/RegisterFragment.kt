package com.example.digi_tech_healthcare.MainData

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.digi_tech_healthcare.BaseClasses.BaseFragment
import com.example.digi_tech_healthcare.R
import com.example.digi_tech_healthcare.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {

    override val bindingInflater: (LayoutInflater) -> FragmentRegisterBinding
        get() = FragmentRegisterBinding::inflate

    val fAuth = FirebaseAuth.getInstance()
    lateinit var fstore: FirebaseFirestore

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.registerB.setOnClickListener {
            onRegisterClicked()
        }
        binding.signT.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun onRegisterClicked(){
        val email = binding.remailE.text.toString().trim()
        val password = binding.rpassE.text.toString().trim()
        val role = binding.rRoleE.text.toString().trim()
        val name = binding.rnameE.text.toString().trim()
        if (email.isEmpty()) {
            Toast.makeText(activity, "enter an email", Toast.LENGTH_SHORT).show()
        }
        if (password.length < 6) {
            Toast.makeText(activity, "password must be at-least 6 characters", Toast.LENGTH_SHORT)
                .show()
        }
        fAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("register", "success")
                    val UID = fAuth.currentUser.uid
                    fstore = FirebaseFirestore.getInstance()
                    val documentReference = fstore
                        .collection("users")
                        .document(UID)

                    val currentUser = HashMap<String, Any>()
                    currentUser["name"] = name
                    currentUser["role"] = role
                    currentUser["email"] = email

                    documentReference.set(currentUser).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Log.d("user", "created : $currentUser")
                        }
                    }


                    Toast.makeText(activity,
                        "User Created", Toast.LENGTH_SHORT).show()

                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                } else {
                    Toast.makeText(activity,
                        "Error! ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                    Log.d("register",
                        "fail :${it.exception?.message} ")
                }
            }

    }


}