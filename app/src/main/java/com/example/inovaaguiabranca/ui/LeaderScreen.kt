package com.example.inovaaguiabranca.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inovaaguiabranca.ui.components.AppHeader
import com.example.inovaaguiabranca.ui.components.GiantRoiCard
import com.example.inovaaguiabranca.ui.components.KanbanBoard
import com.example.inovaaguiabranca.ui.components.KanbanItem
import com.example.inovaaguiabranca.ui.theme.BackgroundGray
import com.example.inovaaguiabranca.ui.theme.TextPrimary
import com.example.inovaaguiabranca.ui.theme.TextSecondary
import com.example.inovaaguiabranca.viewmodel.AuthViewModel
import com.example.inovaaguiabranca.viewmodel.InnovationViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun LeaderScreen(
    authViewModel: AuthViewModel, 
    innovationViewModel: InnovationViewModel, 
    onLogout: () -> Unit,
    onNavigateToProject: (String) -> Unit = {}
) {
    val projects by innovationViewModel.projects.collectAsState()
    val ideas by innovationViewModel.ideas.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()

    val pendingIdeas = ideas.filter { it.status == "PENDING" || it.status == "em análise" }
    val activeProjects = projects.filter { it.status == "IN_PROGRESS" }
    val completedProjects = projects.filter { it.status == "COMPLETED" }

    val ptBr = Locale("pt", "BR")
    val formatter = NumberFormat.getCurrencyInstance(ptBr)
    
    // Calcula Economia YTD e ROI real baseado nos projetos
    val totalCostReduction = projects.sumOf { it.costReduction }
    val totalInvestment = projects.sumOf { it.investment }
    
    val roiPercentageText = if (totalInvestment > 0) {
        val roi = ((totalCostReduction - totalInvestment) / totalInvestment) * 100
        String.format("+%.1f%%", roi)
    } else {
        "+0.0%"
    }
    
    val formattedSavings = formatter.format(totalCostReduction).replace("R$", "").trim()
    val goalProgress = (totalCostReduction / 500000.0).toFloat().coerceIn(0f, 1f) // Supondo meta de 500k

    Scaffold(
        containerColor = BackgroundGray,
        topBar = {
            AppHeader(
                userName = currentUser?.name ?: "Líder",
                userRoleInfo = "Diretoria • Grupo Águia Branca",
                onLogout = {
                    authViewModel.logout()
                    onLogout()
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            // Giant ROI Card
            GiantRoiCard(
                totalSavedYtd = formattedSavings,
                roiPercentage = roiPercentageText,
                goalProgress = goalProgress,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Stats Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SmallStatCard("Ativos", activeProjects.size.toString(), Modifier.weight(1f))
                SmallStatCard("Em Avaliação", pendingIdeas.size.toString(), Modifier.weight(1f))
                SmallStatCard("Concluídos", completedProjects.size.toString(), Modifier.weight(1f))
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Pipeline de Iniciativas",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Converter dados para KanbanItems
            val evaluationItems = pendingIdeas.map { idea ->
                KanbanItem(
                    id = idea.id,
                    title = idea.title,
                    valueLabel = "Aguardando",
                    value = "-",
                    tagText = "Ideia",
                    isProject = false
                )
            }
            
            val activeKanbanItems = activeProjects.map { proj ->
                KanbanItem(
                    id = proj.id,
                    title = proj.title,
                    valueLabel = "Economia Esp.",
                    value = formatter.format(proj.costReduction),
                    tagText = "Projeto Ativo",
                    isProject = true
                )
            }
            
            val completedKanbanItems = completedProjects.map { proj ->
                KanbanItem(
                    id = proj.id,
                    title = proj.title,
                    valueLabel = "Economia Real",
                    value = formatter.format(proj.costReduction),
                    tagText = "Concluído",
                    isProject = true
                )
            }
            
            KanbanBoard(
                evaluationItems = evaluationItems,
                activeItems = activeKanbanItems,
                completedItems = completedKanbanItems,
                onItemClick = { item ->
                    if (item.isProject) {
                        onNavigateToProject(item.id)
                    }
                },
                modifier = Modifier.padding(bottom = 32.dp)
            )
        }
    }
}

@Composable
fun SmallStatCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(title, fontSize = 10.sp, color = TextSecondary)
        }
    }
}
