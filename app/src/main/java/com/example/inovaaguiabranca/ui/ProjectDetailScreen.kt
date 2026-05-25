package com.example.inovaaguiabranca.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inovaaguiabranca.ui.components.DynamicNativeBarChart
import com.example.inovaaguiabranca.ui.theme.BackgroundGray
import com.example.inovaaguiabranca.ui.theme.NavyBlue
import com.example.inovaaguiabranca.ui.theme.PrimaryBlue
import com.example.inovaaguiabranca.ui.theme.TextPrimary
import com.example.inovaaguiabranca.ui.theme.TextSecondary
import com.example.inovaaguiabranca.viewmodel.InnovationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailScreen(
    projectId: String,
    innovationViewModel: InnovationViewModel,
    onBack: () -> Unit
) {
    val projects by innovationViewModel.projects.collectAsState()
    val project = projects.find { it.id == projectId }

    Scaffold(
        containerColor = BackgroundGray,
        topBar = {
            TopAppBar(
                title = { Text("Detalhes do Projeto", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NavyBlue)
            )
        }
    ) { padding ->
        if (project == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Projeto não encontrado.", color = TextSecondary)
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Header do Projeto
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .background(PrimaryBlue.copy(alpha=0.1f), RoundedCornerShape(8.dp))
                                .padding(8.dp)
                        ) {
                            Icon(Icons.Default.Lightbulb, contentDescription = null, tint = PrimaryBlue)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Projeto Ativo", fontSize = 12.sp, color = PrimaryBlue, fontWeight = FontWeight.Bold)
                            Text(project.title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    val isNegativeRoi = project.roiPercentage < 0
                    val roiColor = if (isNegativeRoi) Color(0xFFF44336) else Color(0xFF4CAF50)
                    val roiIcon = if (isNegativeRoi) Icons.Default.TrendingDown else Icons.Default.TrendingUp
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(BackgroundGray, RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("ROI Estimado", fontSize = 12.sp, color = TextSecondary)
                            Text("${String.format("%.1f", project.roiPercentage)}%", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = roiColor)
                        }
                        Icon(roiIcon, contentDescription = null, tint = roiColor, modifier = Modifier.size(32.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // O GRÁFICO NATIVO PURO (Feito apenas com Compose)
            DynamicNativeBarChart(
                investment = project.investment,
                costReduction = project.costReduction
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Ações do Líder
            if (project.status != "COMPLETED") {
                Button(
                    onClick = { innovationViewModel.updateProjectStatus(project.id, "COMPLETED") },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NavyBlue)
                ) {
                    Text("Marcar como Concluído (Ganhos Capturados)", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
