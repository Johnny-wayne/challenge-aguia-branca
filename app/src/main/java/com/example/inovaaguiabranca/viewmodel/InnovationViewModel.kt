package com.example.inovaaguiabranca.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inovaaguiabranca.model.Idea
import com.example.inovaaguiabranca.model.Project
import com.example.inovaaguiabranca.model.Strategy
import com.example.inovaaguiabranca.repository.InnovationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class InnovationViewModel(
    private val repository: InnovationRepository = InnovationRepository()
) : ViewModel() {

    private val _strategies = MutableStateFlow<List<Strategy>>(emptyList())
    val strategies: StateFlow<List<Strategy>> = _strategies.asStateFlow()

    private val _ideas = MutableStateFlow<List<Idea>>(emptyList())
    val ideas: StateFlow<List<Idea>> = _ideas.asStateFlow()

    private val _projects = MutableStateFlow<List<Project>>(emptyList())
    val projects: StateFlow<List<Project>> = _projects.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getStrategies().collect {
                _strategies.value = it
            }
        }
        viewModelScope.launch {
            repository.getIdeas().collect {
                _ideas.value = it
            }
        }
        viewModelScope.launch {
            repository.getProjects().collect {
                _projects.value = it
            }
        }
    }

    fun addStrategy(title: String, content: String) {
        viewModelScope.launch {
            repository.addStrategy(Strategy(title = title, content = content))
        }
    }

    fun addIdea(title: String, description: String, authorId: String) {
        viewModelScope.launch {
            repository.addIdea(Idea(title = title, description = description, authorId = authorId))
        }
    }

    fun updateIdeaStatus(ideaId: String, newStatus: String) {
        viewModelScope.launch {
            repository.updateIdeaStatus(ideaId, newStatus)
        }
    }

    fun addProject(ideaId: String, title: String, investment: Double, roiPercentage: Double, costReduction: Double) {
        viewModelScope.launch {
            val project = Project(
                ideaId = ideaId,
                title = title,
                investment = investment,
                roiPercentage = roiPercentage,
                costReduction = costReduction
            )
            repository.addProject(project)
        }
    }

    fun updateProjectStatus(projectId: String, newStatus: String) {
        viewModelScope.launch {
            repository.updateProjectStatus(projectId, newStatus)
        }
    }

    fun seedMockData() {
        viewModelScope.launch {
            if (_ideas.value.isEmpty()) {
                addIdea("Redução de papel na frota", "Digitalizar os checklists dos motoristas", "mock-user-1")
                addIdea("Novo sistema de rodízio", "Otimizar o rodízio de pneus para economizar 10%", "mock-user-2")
                addIdea("Painel solar no galpão", "Instalar painéis para reduzir conta de luz", "mock-user-3")
            }
            if (_projects.value.isEmpty()) {
                addProject("mock-idea-1", "Digitalização Total", 50000.0, 15.5, 120000.0)
                addProject("mock-idea-2", "Gestão Inteligente de Pneus", 25000.0, 22.0, 85000.0)
                addProject("mock-idea-3", "Energia Limpa", 150000.0, 18.0, 300000.0)
                updateProjectStatus("mock-idea-1", "COMPLETED")
            }
        }
    }
}
