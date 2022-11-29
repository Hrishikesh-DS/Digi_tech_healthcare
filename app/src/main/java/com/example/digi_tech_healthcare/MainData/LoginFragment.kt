package com.example.digi_tech_healthcare.MainData

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.digi_tech_healthcare.BaseClasses.BaseFragment
import com.example.digi_tech_healthcare.R
import com.example.digi_tech_healthcare.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    override val bindingInflater: (LayoutInflater) -> FragmentLoginBinding
        get() = FragmentLoginBinding::inflate

    val fAuth = FirebaseAuth.getInstance()
    lateinit var fstore: FirebaseFirestore

    var email:String=""
    var role: String = ""
    var name: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.regT.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.loginB.setOnClickListener {
            onLoginClicked()
        }
    }

    private fun onLoginClicked(){
        if(binding.lnameE.text.toString().isNotEmpty() && binding.lpassE.text.toString().isNotEmpty()){
            fAuth.signInWithEmailAndPassword(binding.lnameE.text.toString(),binding.lpassE.text.toString()).addOnCompleteListener {
                if(it.isSuccessful)
                {
                    fstore = FirebaseFirestore.getInstance()
                    val docRef = fstore.collection("users").document(fAuth.currentUser.uid)
                    docRef.get().addOnCompleteListener {
                        if (it.isSuccessful) {
                            val document = it.result
                            if (document != null) {
                                if (document.exists()) {
                                    role = document.getString("role").toString()
                                    email = document.getString("email").toString()
                                    name = document.getString("name").toString()
                                    val prefs: SharedPreferences? =
                                        getActivity()?.getSharedPreferences("app-role", Context.MODE_PRIVATE)
                                    prefs?.edit()?.putString("role",role)?.apply()
                                    prefs?.edit()?.putString("email",email)?.apply()
                                    prefs?.edit()?.putString("name",name)?.apply()
                                    val intent =Intent(activity, PatientActivity::class.java)
                                    activity?.finish()
                                    startActivity(intent)

                                } else {
                                    Log.d("Role", "The document doesn't exist")
                                }
                            }
                        } else {
                            it.exception?.message?.let { e ->                Log.d("Role", e)
                            }
                        }
                    }
                    Toast.makeText(activity, "Login successful ", Toast.LENGTH_LONG).show()
                    Log.d("register","success")


                }
                else
                {
                    Toast.makeText(activity, "Error! ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                    Log.d("register","fail :: ${it.exception?.message}")
                }
            }
        }

    }




}