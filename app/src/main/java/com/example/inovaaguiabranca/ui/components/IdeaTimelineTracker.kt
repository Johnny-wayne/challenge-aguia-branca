package com.example.inovaaguiabranca.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inovaaguiabranca.ui.theme.PrimaryBlue

@Composable
fun IdeaTimelineTracker(
    currentStatus: String,
    modifier: Modifier = Modifier
) {
    val stages = listOf(
        "Enviada",
        "Em Avaliação",
        "Aprovada",
        "Execução",
        "Concluída"
    )

    // Map the string status to an index (0 to 4)
    val currentIndex = when (currentStatus.lowercase()) {
        "concluída", "concluído", "completed" -> 4
        "em execução", "execução" -> 3
        "aprovada", "aprovado", "approved" -> 2
        "em análise", "em avaliação" -> 1
        "pendente", "enviada", "pending" -> 0
        "rejeitado", "rejeitada", "rejected" -> -1 // Special case
        else -> 0
    }

    if (currentIndex == -1) {
        Text("Ideia Rejeitada", color = Color.Red, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        return
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        stages.forEachIndexed { index, stageName ->
            val isCompleted = index < currentIndex
            val isCurrent = index == currentIndex
            val isFuture = index > currentIndex

            // Draw connecting line before node (except for first node)
            if (index > 0) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(3.dp)
                        .background(
                            if (isCompleted || isCurrent) Color(0xFF4CAF50) else Color(0xFFE0E0E0)
                        )
                )
            }

            // Node
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(50.dp) // Fixed width to ensure text fits
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isCompleted -> Color(0xFF4CAF50) // Green
                                isCurrent -> Color(0xFFFFC107) // Yellow
                                else -> Color(0xFFE0E0E0) // Gray
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        isCompleted -> Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        isCurrent -> Icon(Icons.Default.Schedule, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        else -> Text("${index + 1}", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stageName,
                    fontSize = 9.sp,
                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                    color = if (isCurrent) Color.Black else Color.Gray,
                    textAlign = TextAlign.Center,
                    lineHeight = 10.sp
                )
            }
        }
    }
}
