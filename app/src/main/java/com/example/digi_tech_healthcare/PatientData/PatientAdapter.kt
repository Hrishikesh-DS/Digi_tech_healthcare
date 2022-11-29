package com.example.digi_tech_healthcare.PatientData

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.digi_tech_healthcare.databinding.ItemContentBinding

class PatientAdapter(private val patientList: ArrayList<PatientInfo>, private val listener: (PatientInfo) -> Unit): RecyclerView.Adapter<PatientAdapter.PatientViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PatientViewHolder {
        val binding = ItemContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PatientViewHolder(binding)
    }
    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        val item = patientList[position]
        holder.title.text = item.doctorInfo
        holder.itemView.setOnClickListener {
            listener(item)
        }
    }
    override fun getItemCount(): Int {
        return patientList.size
    }
    class PatientViewHolder(binding: ItemContentBinding): RecyclerView.ViewHolder(binding.root){
        val title = binding.tvContentTitle
        val desc = binding.tvContentDesc
    }



}