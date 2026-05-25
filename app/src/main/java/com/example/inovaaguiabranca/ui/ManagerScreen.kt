package com.example.inovaaguiabranca.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inovaaguiabranca.ui.components.*
import com.example.inovaaguiabranca.ui.theme.*
import com.example.inovaaguiabranca.viewmodel.AuthViewModel
import com.example.inovaaguiabranca.viewmodel.InnovationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagerScreen(authViewModel: AuthViewModel, innovationViewModel: InnovationViewModel, onLogout: () -> Unit) {
    val ideas by innovationViewModel.ideas.collectAsState()
    
    var showProjectDialogForIdea by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text("Painel de Inovação", fontSize = 14.sp, color = TextSecondary)
                        Text("Gestor", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    }
                },
                actions = {
                    IconButton(onClick = { authViewModel.logout(); onLogout() }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Sair", tint = StatusErrorText)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundGray)
            )
        },
        containerColor = BackgroundGray
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).padding(horizontal = 16.dp).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            item {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    ChartCard(
                        title = "ROI Esperado (%)",
                        data = listOf(12f, 15f, 22f, 18f, 25f),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Text(
                    text = "Aprovação e Prioritização",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
            }

            val pendingIdeas = ideas.filter { it.status == "PENDING" || it.status == "em análise" }
            
            if (pendingIdeas.isEmpty()) {
                item {
                    Text("Nenhuma ideia pendente de avaliação no momento.", color = TextSecondary)
                }
            }

            items(pendingIdeas) { idea ->
                IdeaListItem(
                    title = idea.title,
                    category = "Problema/Melhoria",
                    date = "Recente",
                    status = "Em análise",
                    showActions = true,
                    onApproveClick = { 
                        innovationViewModel.updateIdeaStatus(idea.id, "Aprovado")
                        showProjectDialogForIdea = idea.id 
                    },
                    onPrioritizeClick = {
                        // Just an example action
                    }
                )
            }
            
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }

        if (showProjectDialogForIdea != null) {
            var investment by remember { mutableStateOf("") }
            var roi by remember { mutableStateOf("") }
            var costReduction by remember { mutableStateOf("") }
            val relatedIdea = ideas.find { it.id == showProjectDialogForIdea }

            AlertDialog(
                onDismissRequest = { showProjectDialogForIdea = null },
                containerColor = SurfaceWhite,
                title = { Text("Transformar em Projeto", fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Ideia: ${relatedIdea?.title}", color = TextSecondary, fontSize = 14.sp)
                        OutlinedTextField(
                            value = investment, 
                            onValueChange = { investment = it }, 
                            label = { Text("Investimento (R$)") },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = roi, 
                            onValueChange = { roi = it }, 
                            label = { Text("ROI (%)") },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = costReduction, 
                            onValueChange = { costReduction = it }, 
                            label = { Text("Redução de Custo (R$)") },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            innovationViewModel.addProject(
                                ideaId = showProjectDialogForIdea!!,
                                title = "Projeto: ${relatedIdea?.title}",
                                investment = investment.toDoubleOrNull() ?: 0.0,
                                roiPercentage = roi.toDoubleOrNull() ?: 0.0,
                                costReduction = costReduction.toDoubleOrNull() ?: 0.0
                            )
                            showProjectDialogForIdea = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                    ) { Text("Criar Projeto") }
                },
                dismissButton = {
                    TextButton(onClick = { showProjectDialogForIdea = null }) { Text("Cancelar", color = TextSecondary) }
                }
            )
        }
    }
}
