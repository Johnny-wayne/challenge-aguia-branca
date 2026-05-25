package com.example.inovaaguiabranca

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inovaaguiabranca.ui.AppNavigation
import com.example.inovaaguiabranca.ui.theme.InovaAguiaBrancaTheme
import com.example.inovaaguiabranca.viewmodel.AuthViewModel
import com.example.inovaaguiabranca.viewmodel.InnovationViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InovaAguiaBrancaTheme {
                // Instanciando as ViewModels no escopo da Activity
                val authViewModel: AuthViewModel = viewModel()
                val innovationViewModel: InnovationViewModel = viewModel()
                
                // Chamando a nossa árvore de navegação
                AppNavigation(authViewModel, innovationViewModel)
            }
        }
    }
}