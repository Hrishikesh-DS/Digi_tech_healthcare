package com.example.digi_tech_healthcare.ContentManager

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import com.example.digi_tech_healthcare.databinding.FragmentInformationBinding
import com.google.firebase.auth.FirebaseAuth
import androidx.navigation.fragment.findNavController
import com.example.digi_tech_healthcare.BaseClasses.BaseFragment
import com.example.digi_tech_healthcare.Dataclasses.ArticleResponse
import com.example.digi_tech_healthcare.Dataclasses.Articles
import com.example.digi_tech_healthcare.R
import com.google.firebase.firestore.FirebaseFirestore

class InformationFragment : BaseFragment<FragmentInformationBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentInformationBinding
        get() = FragmentInformationBinding::inflate
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    var roles = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        initializeUi()
        val prefs: SharedPreferences? =
            activity?.getSharedPreferences("app-role", Context.MODE_PRIVATE)
        roles = prefs?.getString("role", "")!!
        if(roles=="Patient"){
            binding.fab.visibility=View.GONE
        }
        if(roles=="Doctor"){
            findNavController().navigate(R.id.action_informationFragment_to_doctorFragment)
        }
        fetchData()
    }
    private fun initializeUi() {
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_informationFragment_to_addInformationFragment)
        }
    }
    private fun fetchData() {
        val docRef = db.collection("articles")
        docRef.get().addOnCompleteListener {
            val articleResponse = ArticleResponse()
            if (it.isSuccessful) {
                val result = it.result
                result?.let {
                    articleResponse.articles = result.documents.mapNotNull { snapShot ->
                        snapShot.toObject(Articles::class.java)
                    }
                }
                if (articleResponse.articles != null) {
                    setUpAdapter(articleResponse.articles ?: listOf())
                }
            }
        }
        binding.rvArticles.isVisible = true
    }
    private fun setUpAdapter(articleList: List<Articles>) {
        val adapter = ContentAdapter(articleList as ArrayList<Articles>) {
            findNavController().navigate(
                InformationFragmentDirections.actionInformationFragmentToInformationDetailsFragment()
                    .apply {
                    id=it.id?:""
                    image=it.url?:""
                    title=it.title?:""
                    desc=it.description?:""
                    role=roles
                }
            )
        }
        binding.rvArticles.adapter = adapter
    }
}