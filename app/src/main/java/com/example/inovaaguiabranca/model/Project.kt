package com.example.inovaaguiabranca.model

import com.google.firebase.Timestamp

data class Project(
    val id: String = "",
    val ideaId: String = "",
    val title: String = "",
    val status: String = "IN_PROGRESS", // "IN_PROGRESS", "COMPLETED"
    val investment: Double = 0.0,
    val roiPercentage: Double = 0.0,
    val costReduction: Double = 0.0,
    val launchDate: Timestamp = Timestamp.now()
)
