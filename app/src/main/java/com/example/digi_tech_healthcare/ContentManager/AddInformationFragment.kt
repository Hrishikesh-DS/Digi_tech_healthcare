package com.example.digi_tech_healthcare.ContentManager

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.digi_tech_healthcare.databinding.FragmentAddInformationBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import androidx.navigation.fragment.findNavController
import com.example.digi_tech_healthcare.BaseClasses.BaseFragment
import com.example.digi_tech_healthcare.R
import com.google.firebase.firestore.SetOptions
import java.util.*

class AddInformationFragment : BaseFragment<FragmentAddInformationBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentAddInformationBinding
        get() = FragmentAddInformationBinding::inflate
    private lateinit var db: FirebaseFirestore
    private lateinit var imageUrl: Uri
    private val PICK_IMAGE = 1
    private lateinit var uploadTask: UploadTask
    private lateinit var fs: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private lateinit var documentReference: DocumentReference
    private var bool = false
    var uniqueID = UUID.randomUUID().toString()
    private val args: AddInformationFragmentArgs by navArgs()
    private val id: String by lazy {
        args.id
    }
    private val image: String by lazy {
        args.image
    }
    private val title: String by lazy {
        args.title
    }
    private val desc: String by lazy {
        args.desc
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = FirebaseFirestore.getInstance()
        fs = FirebaseStorage.getInstance()
        documentReference = db.collection("articles").document()
        storageReference = fs.getReference("article_images")
        initializeUI()
    }
    private fun initializeUI() {
//        val toolbarTitle = if (!id.isNullOrBlank()) {
//            "Edit Content"
//        } else {
//            "Add Content"
//        }
        if (!image.isNullOrBlank()) {
            Glide.with(requireContext()).load(image).into(binding.ivArticle)
        }
        if (!title.isNullOrBlank()) {
            binding.titleEt.setText(title)
        }
        if (!desc.isNullOrBlank()) {
            binding.descEt.setText(desc)
        }
        binding.ivArticle.setOnClickListener {
            bool = true
            chooseImage()
        }
        binding.saveBtn.setOnClickListener {
            if (!id.isNullOrBlank()) {
                updateData()
            } else {
                uploadData()
            }
        }
    }
    private fun chooseImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE || resultCode == RESULT_OK || data != null || data?.data != null) {
            try {
                imageUrl = data?.data!!
                Glide.with(requireContext()).load(imageUrl).into(binding.ivArticle)
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
        val title = binding.titleEt.text.toString()
        val desc = binding.descEt.text.toString()
        try {
            if (!TextUtils.isEmpty(title) || !TextUtils.isEmpty(desc) || imageUrl != null) {
                val reference: StorageReference =
                    storageReference.child("${System.currentTimeMillis()}." + getFileExt(imageUrl))
                uploadTask = reference.putFile(imageUrl)
                val urlTask: Task<Uri> = uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        throw task.exception!!
                    }
                    return@continueWithTask reference.downloadUrl
                }.addOnCompleteListener {
                    if (it.isSuccessful) {
                        val downloadUri = it.result
                        val article = hashMapOf(
                            "title" to title,
                            "description" to desc,
                            "url" to downloadUri.toString(),
                            "id" to uniqueID
                        )
                        documentReference.set(article).addOnSuccessListener {
                            Toast.makeText(context, "Article Added Successfully", Toast.LENGTH_LONG)
                                .show()
                            findNavController().popBackStack()
                        }.addOnFailureListener {
                            Toast.makeText(
                                context,
                                "Article could not be added successfully",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }.addOnFailureListener {
                }
            } else {
                Toast.makeText(context, "All fields are required", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "All fields are required", Toast.LENGTH_LONG).show()
        }
    }
    private fun updateData() {
        val title = binding.titleEt.text.toString()
        val desc = binding.descEt.text.toString()
        if (!TextUtils.isEmpty(title) || !TextUtils.isEmpty(desc) || imageUrl != null) {
            if (bool) {
                val reference: StorageReference =
                    storageReference.child("${System.currentTimeMillis()}." + getFileExt(imageUrl))
                uploadTask = reference.putFile(imageUrl)
                val urlTask: Task<Uri> = uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        throw task.exception!!
                    }
                    return@continueWithTask reference.downloadUrl
                }.addOnCompleteListener {
                    if (it.isSuccessful) {
                        val downloadUri = it.result
                        val article = hashMapOf(
                            "title" to title,
                            "description" to desc,
                            "url" to downloadUri.toString()
                        )
                        val query = db.collection("articles").whereEqualTo("id", id).get()
                        query.addOnSuccessListener {
                            for (doc in it) {
                                db.collection("articles").document(doc.id)
                                    .set(article, SetOptions.merge())
                            }
                            Toast.makeText(
                                context,
                                "Information Updated successfully",
                                Toast.LENGTH_LONG
                            ).show()
                            findNavController().popBackStack(R.id.informationFragment, false)
                        }
                    } else {
                        Toast.makeText(context, "Please add all values", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                val article = hashMapOf(
                    "title" to title,
                    "description" to desc
                )
                val query = db.collection("articles").whereEqualTo("id", id).get()
                query.addOnSuccessListener {
                    for (doc in it) {
                        db.collection("articles").document(doc.id)
                            .set(article, SetOptions.merge())
                    }
                    Toast.makeText(
                        context,
                        "Information Updated successfully",
                        Toast.LENGTH_LONG
                    ).show()
                    findNavController().popBackStack(R.id.informationFragment, false)
                }.addOnFailureListener {
                    Toast.makeText(context, "Please add all values", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}