package com.example.inovaaguiabranca.model

data class User(
    val id: String = "",
    val name: String = "",
    val role: String = "OPERATOR" // "OPERATOR", "MANAGER", "LEADER"
)
