package com.example.digi_tech_healthcare.MainData

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.digi_tech_healthcare.BaseClasses.BaseFragment
import com.example.digi_tech_healthcare.R
import com.example.digi_tech_healthcare.Dataclasses.User
import com.example.digi_tech_healthcare.databinding.FragmentSplashScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject


class SplashScreenFragment : BaseFragment<FragmentSplashScreenBinding>() {
    lateinit var fstore: FirebaseFirestore

    var email:String=""
    var role: String = ""
    var name: String = ""

    override val bindingInflater: (LayoutInflater) -> FragmentSplashScreenBinding
        get() = FragmentSplashScreenBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CheckUserLoggedIn()
    }

    fun CheckUserLoggedIn() {
        val fAuth = FirebaseAuth.getInstance()
        if (fAuth.currentUser != null) {


            getUserFromId(fAuth.currentUser.uid) {
                Log.d("obtainedusernamelogin", it.email)
            }

            Log.d("sessiontokenlambda", fAuth.currentUser.uid.toString())
            Log.d("sessiontokenlambda", "tokensession")
            val x = fAuth.currentUser.getIdToken(true)
            Log.d("sessiontokenlambda", x.toString())

            val intent = Intent(activity, PatientActivity::class.java)
            activity?.finish()
            startActivity(intent)

        }
        else {
            findNavController().navigate(R.id.action_splashScreenFragment_to_loginFragment)
        }


    }
    fun getUserFromId(UID: String, callback: (User) -> (Unit)) {
        val fstore = FirebaseFirestore.getInstance()
        val fAuth = FirebaseAuth.getInstance()
        val documentReference = fstore
            .collection("users")
            .document(UID)
        documentReference.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("getuser", "DocumentSnapshot data: ${document.data}")
                    val user = document.toObject<User>()
                    Log.d("getusersnapshot", "DocumentSnapshot data: ${user.toString()}")
                    if (user != null) {
                        callback(user)
                    }
                } else {
                    Log.d("getuser", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("getuser", "get failed with ", exception)
            }
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
                    } else {
                        Log.d("Role", "The document doesn't exist")
                    }
                }
            }
            else {
                it.exception?.message?.let { e ->                Log.d("Role", e)
                }
            }
        }
        fstore.terminate()

    }


}