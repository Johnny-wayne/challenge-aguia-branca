package com.example.inovaaguiabranca.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.TrendingUp
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
fun LeaderScreen(authViewModel: AuthViewModel, innovationViewModel: InnovationViewModel, onLogout: () -> Unit) {
    val projects by innovationViewModel.projects.collectAsState()
    
    val totalInvestment = projects.sumOf { it.investment }
    val totalCostReduction = projects.sumOf { it.costReduction }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text("Painel de Inovação", fontSize = 14.sp, color = TextSecondary)
                        Text("Dashboard Executivo", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
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
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    StatCard(
                        title = "ROI Projetos (%)",
                        value = "15.4",
                        icon = Icons.Default.TrendingUp,
                        iconBgColor = PrimaryBlue.copy(alpha = 0.1f),
                        iconColor = PrimaryBlue,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Redução (R$)",
                        value = "1.2M",
                        icon = Icons.Default.AttachMoney,
                        iconBgColor = StatusApprovedBg,
                        iconColor = StatusApprovedText,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                ChartCard(
                    title = "Evolução do ROI (Últimos meses)",
                    data = listOf(10f, 12f, 15f, 14f, 18f, 15f, 22f),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Text(
                    text = "Projetos em Andamento",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
            }
            
            if (projects.isEmpty()) {
                item {
                    Text("Nenhum projeto em andamento.", color = TextSecondary)
                }
            }

            items(projects) { project ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(project.title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("ROI Esperado: ${project.roiPercentage}%", color = PrimaryBlue)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text("Investimento: R$ ${project.investment}", fontSize = 12.sp, color = TextSecondary)
                            Text("Redução: R$ ${project.costReduction}", fontSize = 12.sp, color = StatusApprovedText)
                        }
                    }
                }
            }
            
            item { 
                Button(
                    onClick = { innovationViewModel.seedMockData() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    Text("Gerar Dados de Teste (Mock)")
                }
                Spacer(modifier = Modifier.height(32.dp)) 
            }
        }
    }
}
