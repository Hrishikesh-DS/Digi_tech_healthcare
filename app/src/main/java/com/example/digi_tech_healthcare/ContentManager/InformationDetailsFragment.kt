package com.example.digi_tech_healthcare.ContentManager

import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import androidx.navigation.fragment.findNavController
import com.example.digi_tech_healthcare.BaseClasses.BaseFragment
import com.example.digi_tech_healthcare.databinding.FragmentInformationDetailsBinding

class InformationDetailsFragment : BaseFragment<FragmentInformationDetailsBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentInformationDetailsBinding
        get() = FragmentInformationDetailsBinding::inflate
    private val args: InformationDetailsFragmentArgs by navArgs()
    private val ids: String by lazy {
        args.id
    }
    private val images: String by lazy {
        args.image
    }
    private val titles: String by lazy {
        args.title
    }
    private val descs: String by lazy {
        args.desc
    }
    private val roles: String by lazy {
        args.role
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeUI()
    }
    private fun initializeUI() {
        Glide.with(requireContext()).load(images).into(binding.ivContentIcon)
        binding.tvTitle.text = titles
        binding.tvDesc.text = descs
        if (roles == "Content Manager") {
            binding.btnEdit.visibility = View.VISIBLE
            binding.btnEdit.setOnClickListener {
                findNavController().navigate(
                    InformationDetailsFragmentDirections.actionInformationDetailsFragmentToAddInformationFragment()
                        .apply {
                        id=ids
                        image=images
                        title=titles
                        desc=descs
                        role=roles?:""
                    }
                )
            }
        } else {
            binding.btnEdit.visibility = View.GONE
        }
    }
}