package com.example.inovaaguiabranca.repository

import com.example.inovaaguiabranca.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    suspend fun getCurrentUser(): User? {
        val uid = auth.currentUser?.uid ?: return null
        val snapshot = db.collection("users").document(uid).get().await()
        return snapshot.toObject(User::class.java)
    }

    suspend fun registerUser(name: String, email: String, pass: String, role: String) {
        val result = auth.createUserWithEmailAndPassword(email, pass).await()
        val uid = result.user?.uid ?: throw Exception("Falha ao obter ID do usuário")
        val user = User(id = uid, name = name, role = role)
        db.collection("users").document(uid).set(user).await()
    }
    
    suspend fun login(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email, pass).await()
    }
    
    fun logout() {
        auth.signOut()
    }
}
