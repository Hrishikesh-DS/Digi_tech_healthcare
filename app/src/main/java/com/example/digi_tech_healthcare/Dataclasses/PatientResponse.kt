package com.example.digi_tech_healthcare.Dataclasses

import com.example.digi_tech_healthcare.PatientData.PatientInfo

data class PatientResponse (
    var patients: List<PatientInfo>? = null,
    var exception: Exception? = null
)