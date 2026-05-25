package com.example.inovaaguiabranca.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inovaaguiabranca.ui.components.*
import com.example.inovaaguiabranca.ui.theme.*
import com.example.inovaaguiabranca.viewmodel.AuthViewModel
import com.example.inovaaguiabranca.viewmodel.InnovationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OperatorScreen(authViewModel: AuthViewModel, innovationViewModel: InnovationViewModel, onLogout: () -> Unit) {
    val ideas by innovationViewModel.ideas.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()
    val myIdeas = ideas.filter { it.authorId == currentUser?.id }

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Ideia de Melhoria") }
    val categories = listOf("Ideia de Melhoria", "Problema")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text("Painel de Inovação", fontSize = 14.sp, color = TextSecondary)
                        Text("Operador", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
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

            // Stats Row
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    StatCard(
                        title = "Ideias",
                        value = "125",
                        icon = Icons.Default.Lightbulb,
                        iconBgColor = PrimaryBlue.copy(alpha = 0.1f),
                        iconColor = PrimaryBlue,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Problemas",
                        value = "32",
                        icon = Icons.Default.Warning,
                        iconBgColor = StatusErrorBg,
                        iconColor = StatusErrorText,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    StatCard(
                        title = "Projetos",
                        value = "10",
                        icon = Icons.Default.Build,
                        iconBgColor = StatusApprovedBg,
                        iconColor = StatusApprovedText,
                        modifier = Modifier.weight(1f)
                    )
                    // Just a filler
                    Box(modifier = Modifier.weight(1f))
                }
            }

            // Progress Goal
            item {
                ProgressGoalCard(
                    title = "Redução de Custos",
                    currentValue = 72,
                    targetValue = 100,
                    unit = "%",
                    color = PrimaryBlue
                )
            }

            // Register Form
            item {
                Text(
                    text = "Registrar Ideias & Problemas",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Título da Ideia ou Problema") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            categories.forEach { category ->
                                val isSelected = selectedCategory == category
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { selectedCategory = category },
                                    label = { Text(category) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = PrimaryBlue.copy(alpha = 0.1f),
                                        selectedLabelColor = PrimaryBlue
                                    )
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Descrição detalhada") },
                            modifier = Modifier.fillMaxWidth().height(120.dp),
                            shape = RoundedCornerShape(12.dp),
                            maxLines = 5
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                if (title.isNotBlank() && description.isNotBlank()) {
                                    innovationViewModel.addIdea(title, description, currentUser?.id ?: "")
                                    title = ""
                                    description = ""
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                        ) {
                            Text("Enviar para Análise", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // My Ideas List
            item {
                Text(
                    text = "Minhas ideias recentes",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
            }
            
            items(myIdeas) { idea ->
                IdeaListItem(
                    title = idea.title,
                    category = "Melhoria", // Assuming from model
                    date = "Hoje", // Assuming placeholder for now, would format timestamp
                    status = idea.status,
                    showActions = false
                )
            }
            
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}
