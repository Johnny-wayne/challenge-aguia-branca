package com.example.inovaaguiabranca.model

import com.google.firebase.Timestamp

data class Strategy(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val createdAt: Timestamp = Timestamp.now()
)
