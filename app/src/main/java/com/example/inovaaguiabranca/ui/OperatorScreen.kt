package com.example.inovaaguiabranca.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inovaaguiabranca.ui.components.AppHeader
import com.example.inovaaguiabranca.ui.components.IdeaTimelineTracker
import com.example.inovaaguiabranca.ui.theme.*
import com.example.inovaaguiabranca.viewmodel.AuthViewModel
import com.example.inovaaguiabranca.viewmodel.InnovationViewModel

@Composable
fun OperatorScreen(authViewModel: AuthViewModel, innovationViewModel: InnovationViewModel, onLogout: () -> Unit) {
    val ideas by innovationViewModel.ideas.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()
    val myIdeas = ideas.filter { it.authorId == currentUser?.id }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var showForm by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Scaffold(
        containerColor = BackgroundGray,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            AppHeader(
                userName = currentUser?.name ?: "Operador",
                userRoleInfo = "Operações • Grupo Águia Branca",
                onLogout = {
                    authViewModel.logout()
                    onLogout()
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            
            // Diretriz Estratégica Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = PrimaryBlue),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("DIRETRIZ ESTRATÉGICA DO TRIMESTRE", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Reduzir em 15% o custo operacional da frota até dez/2026",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocalShipping, contentDescription = null, tint = Color.White.copy(alpha=0.7f), modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Águia Branca Logística", color = Color.White.copy(alpha=0.7f), fontSize = 12.sp)
                        }
                    }
                }
            }

            // Botão Principal
            item {
                Button(
                    onClick = { showForm = !showForm },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NavyBlue)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Adicionar", tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Registrar Nova Ideia / Problema", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            // Formulário Oculto
            if (showForm) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            OutlinedTextField(
                                value = title,
                                onValueChange = { title = it },
                                label = { Text("Resumo da Ideia") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = description,
                                onValueChange = { description = it },
                                label = { Text("Detalhes de como implementar") },
                                modifier = Modifier.fillMaxWidth().height(100.dp),
                                maxLines = 4
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    if (title.isNotBlank() && description.isNotBlank()) {
                                        innovationViewModel.addIdea(title, description, currentUser?.id ?: "unknown")
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Ideia enviada com sucesso!")
                                        }
                                        title = ""
                                        description = ""
                                        showForm = false
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                            ) {
                                Text("Enviar Ideia")
                            }
                        }
                    }
                }
            }

            // Minhas Ideias Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Minhas Ideias", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text("${myIdeas.size} ativas", fontSize = 12.sp, color = TextSecondary)
                }
            }

            // Lista de Ideias
            items(myIdeas.sortedByDescending { it.createdAt }) { idea ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = idea.title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .background(StatusAnalysisBg, RoundedCornerShape(12.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text("Operações", color = StatusAnalysisText, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Recente", // Em produção poderia usar DateUtils para calcular tempo real
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // O NOVO TRACKER DA LINHA DO TEMPO!
                        IdeaTimelineTracker(currentStatus = idea.status)
                    }
                }
            }

            if (myIdeas.isEmpty() && !showForm) {
                item {
                    Text(
                        text = "Você ainda não registrou nenhuma ideia. Clique no botão acima para começar!",
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        }
    }
}
