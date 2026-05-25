package com.example.inovaaguiabranca.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inovaaguiabranca.ui.components.AppHeader
import com.example.inovaaguiabranca.ui.components.TShirtSizingChips
import com.example.inovaaguiabranca.ui.theme.*
import com.example.inovaaguiabranca.viewmodel.AuthViewModel
import com.example.inovaaguiabranca.viewmodel.InnovationViewModel

@Composable
fun ManagerScreen(authViewModel: AuthViewModel, innovationViewModel: InnovationViewModel, onLogout: () -> Unit) {
    val ideas by innovationViewModel.ideas.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()

    val pendingIdeas = ideas.filter { it.status == "PENDING" || it.status == "em análise" }
    val approvedIdeas = ideas.filter { it.status == "Aprovado" || it.status == "aprovada" }
    val executionIdeas = ideas.filter { it.status == "em execução" || it.status == "execução" }

    var ideaToApprove by remember { mutableStateOf<com.example.inovaaguiabranca.model.Idea?>(null) }

    Scaffold(
        containerColor = BackgroundGray,
        topBar = {
            AppHeader(
                userName = currentUser?.name ?: "Gestor",
                userRoleInfo = "Logística ES • Grupo Águia Branca",
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
            // Cards Superiores
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MetricCard("Pendentes", pendingIdeas.size.toString(), Color(0xFFFFF3E0), Color(0xFFFF9800), Modifier.weight(1f))
                    MetricCard("Aprovadas", approvedIdeas.size.toString(), Color(0xFFE8F5E9), Color(0xFF4CAF50), Modifier.weight(1f))
                    MetricCard("Em execução", executionIdeas.size.toString(), Color(0xFFE3F2FD), PrimaryBlue, Modifier.weight(1f))
                }
            }

            item {
                Text(
                    text = "Aguardando sua análise",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            if (pendingIdeas.isEmpty()) {
                item {
                    Text("Você não tem nenhuma ideia aguardando análise no momento.", color = TextSecondary)
                }
            }

            items(pendingIdeas) { idea ->
                ManagerIdeaCard(
                    title = idea.title,
                    description = idea.description,
                    authorInitial = "OP",
                    authorName = "Operador",
                    timeAgo = "Recente",
                    onApprove = { _, _ ->
                        ideaToApprove = idea
                    }
                )
            }
        }

        if (ideaToApprove != null) {
            var investment by remember { mutableStateOf("") }
            var costReduction by remember { mutableStateOf("") }

            AlertDialog(
                onDismissRequest = { ideaToApprove = null },
                containerColor = SurfaceWhite,
                title = { Text("Valores do Projeto", fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Preencha as estimativas reais para a ideia: ${ideaToApprove?.title}", color = TextSecondary, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = investment,
                            onValueChange = { investment = it },
                            label = { Text("Investimento Estimado (R$)") },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = costReduction,
                            onValueChange = { costReduction = it },
                            label = { Text("Economia Estimada (R$)") },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val inv = investment.toDoubleOrNull() ?: 0.0
                            val costRed = costReduction.toDoubleOrNull() ?: 0.0
                            val roi = if (inv > 0) ((costRed - inv) / inv) * 100.0 else 0.0

                            ideaToApprove?.let { idea ->
                                innovationViewModel.updateIdeaStatus(idea.id, "Aprovada")
                                innovationViewModel.addProject(
                                    ideaId = idea.id,
                                    title = idea.title,
                                    investment = inv,
                                    roiPercentage = roi,
                                    costReduction = costRed
                                )
                            }
                            ideaToApprove = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        enabled = investment.isNotBlank() && costReduction.isNotBlank()
                    ) { Text("Aprovar e Criar Projeto") }
                },
                dismissButton = {
                    TextButton(onClick = { ideaToApprove = null }) { Text("Cancelar", color = TextSecondary) }
                }
            )
        }
    }
}

@Composable
fun MetricCard(title: String, value: String, bgColor: Color, valueColor: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .background(bgColor, RoundedCornerShape(8.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(title, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = valueColor)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        }
    }
}

@Composable
fun ManagerIdeaCard(
    title: String,
    description: String,
    authorInitial: String,
    authorName: String,
    timeAgo: String,
    onApprove: (effort: String, impact: String) -> Unit
) {
    var effort by remember { mutableStateOf<String?>(null) }
    var impact by remember { mutableStateOf<String?>(null) }
    
    val tagText = when {
        effort == "Baixo" && impact == "Alto" -> "Quick Win"
        effort == "Alto" && impact == "Baixo" -> "Descarte"
        effort == "Alto" && impact == "Alto" -> "Projeto Estratégico"
        effort != null && impact != null -> "Inovação Incremental"
        else -> "Aguardando Análise"
    }
    
    val tagColor = when (tagText) {
        "Quick Win" -> Color(0xFF4CAF50)
        "Projeto Estratégico" -> PrimaryBlue
        "Descarte" -> Color.Red
        "Aguardando Análise" -> Color.Gray
        else -> Color(0xFF2196F3)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Author & Time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE3F2FD)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(authorInitial, color = PrimaryBlue, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(authorName, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextSecondary)
                }
                Text(timeAgo, fontSize = 12.sp, color = Color.Gray)
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(description, fontSize = 13.sp, color = TextSecondary, lineHeight = 18.sp)
            
            Divider(modifier = Modifier.padding(vertical = 16.dp), color = Color(0xFFEEEEEE))
            
            // T-Shirt Sizing
            Text("Esforço de Implementação", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(8.dp))
            TShirtSizingChips(selectedValue = effort, onValueChange = { effort = it })
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text("Impacto no Negócio", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(8.dp))
            TShirtSizingChips(selectedValue = impact, onValueChange = { impact = it })
            
            // Tag Automática
            if (effort != null && impact != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("TAG AUTOMÁTICA", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = tagColor, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(tagText, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = tagColor)
                            }
                        }
                        Box(
                            modifier = Modifier
                                .background(tagColor.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text("${effort}/${impact}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = tagColor)
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = { if (effort != null && impact != null) onApprove(effort!!, impact!!) },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = effort != null && impact != null,
                colors = ButtonDefaults.buttonColors(containerColor = NavyBlue)
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Aprovar Projeto", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

