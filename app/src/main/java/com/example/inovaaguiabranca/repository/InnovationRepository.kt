package com.example.inovaaguiabranca.repository

import com.example.inovaaguiabranca.model.Idea
import com.example.inovaaguiabranca.model.Project
import com.example.inovaaguiabranca.model.Strategy
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class InnovationRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    
    // --- STRATEGIES ---
    fun getStrategies(): Flow<List<Strategy>> = callbackFlow {
        val listener = db.collection("strategies")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val strategies = snapshot.documents.mapNotNull { it.toObject(Strategy::class.java) }
                    trySend(strategies)
                }
            }
        awaitClose { listener.remove() }
    }

    suspend fun addStrategy(strategy: Strategy) {
        val docRef = db.collection("strategies").document()
        docRef.set(strategy.copy(id = docRef.id)).await()
    }

    // --- IDEAS ---
    fun getIdeas(): Flow<List<Idea>> = callbackFlow {
        val listener = db.collection("ideas")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val ideas = snapshot.documents.mapNotNull { it.toObject(Idea::class.java) }
                    trySend(ideas)
                }
            }
        awaitClose { listener.remove() }
    }

    suspend fun addIdea(idea: Idea) {
        val docRef = db.collection("ideas").document()
        docRef.set(idea.copy(id = docRef.id)).await()
    }

    suspend fun updateIdeaStatus(ideaId: String, newStatus: String) {
        db.collection("ideas").document(ideaId).update("status", newStatus).await()
    }

    // --- PROJECTS ---
    fun getProjects(): Flow<List<Project>> = callbackFlow {
        val listener = db.collection("projects")
            .orderBy("launchDate", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val projects = snapshot.documents.mapNotNull { it.toObject(Project::class.java) }
                    trySend(projects)
                }
            }
        awaitClose { listener.remove() }
    }

    suspend fun addProject(project: Project) {
        val docRef = db.collection("projects").document()
        docRef.set(project.copy(id = docRef.id)).await()
    }
    
    suspend fun updateProjectStatus(projectId: String, newStatus: String) {
        db.collection("projects").document(projectId).update("status", newStatus).await()
    }
}
