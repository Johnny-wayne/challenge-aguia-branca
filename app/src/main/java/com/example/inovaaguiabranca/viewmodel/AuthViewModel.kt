package com.example.inovaaguiabranca.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inovaaguiabranca.model.User
import com.example.inovaaguiabranca.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        viewModelScope.launch {
            _isLoading.value = true
            val user = repository.getCurrentUser()
            _currentUser.value = user
            _isLoading.value = false
        }
    }

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            if (email.isBlank() || pass.isBlank()) {
                _error.value = "Preencha todos os campos."
                _isLoading.value = false
                return@launch
            }
            try {
                repository.login(email, pass)
                _currentUser.value = repository.getCurrentUser()
            } catch (e: Exception) {
                _error.value = "Falha no login. Verifique suas credenciais."
            }
            _isLoading.value = false
        }
    }

    fun register(name: String, email: String, pass: String, role: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            if (name.isBlank() || email.isBlank() || pass.isBlank()) {
                _error.value = "Preencha todos os campos."
                _isLoading.value = false
                return@launch
            }
            try {
                repository.registerUser(name, email, pass, role)
                _currentUser.value = repository.getCurrentUser()
            } catch (e: com.google.firebase.auth.FirebaseAuthUserCollisionException) {
                _error.value = "Este e-mail já está em uso por outra conta."
            } catch (e: Exception) {
                _error.value = "Falha ao registrar: ${e.message}"
            }
            _isLoading.value = false
        }
    }

    fun logout() {
        repository.logout()
        _currentUser.value = null
    }
}
