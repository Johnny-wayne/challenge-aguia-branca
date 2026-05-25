package com.example.inovaaguiabranca.model

import com.google.firebase.Timestamp

data class Idea(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val authorId: String = "",
    val status: String = "PENDING", // "PENDING", "APPROVED", "REJECTED"
    val createdAt: Timestamp = Timestamp.now()
)
