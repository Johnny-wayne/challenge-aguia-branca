package com.example.inovaaguiabranca.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inovaaguiabranca.ui.theme.BackgroundGray
import com.example.inovaaguiabranca.ui.theme.PrimaryBlue
import com.example.inovaaguiabranca.ui.theme.SurfaceWhite
import com.example.inovaaguiabranca.ui.theme.TextPrimary
import com.example.inovaaguiabranca.ui.theme.TextSecondary

data class KanbanItem(
    val id: String,
    val title: String,
    val valueLabel: String,
    val value: String,
    val tagText: String,
    val isProject: Boolean
)

@Composable
fun KanbanBoard(
    evaluationItems: List<KanbanItem>,
    activeItems: List<KanbanItem>,
    completedItems: List<KanbanItem>,
    onItemClick: (KanbanItem) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        item {
            KanbanColumn(
                title = "Em Avaliação",
                items = evaluationItems,
                onItemClick = onItemClick
            )
        }
        item {
            KanbanColumn(
                title = "Projetos Ativos",
                items = activeItems,
                onItemClick = onItemClick
            )
        }
        item {
            KanbanColumn(
                title = "Ganhos Capturados",
                items = completedItems,
                onItemClick = onItemClick
            )
        }
    }
}

@Composable
private fun KanbanColumn(
    title: String,
    items: List<KanbanItem>,
    onItemClick: (KanbanItem) -> Unit
) {
    Column(
        modifier = Modifier
            .width(280.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFEFEFEF)) // Slightly darker gray for column background
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, fontWeight = FontWeight.Bold, color = TextPrimary)
            Box(
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(items.size.toString(), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        items.forEach { item ->
            KanbanCard(item = item, onClick = { onItemClick(item) })
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun KanbanCard(item: KanbanItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .background(PrimaryBlue.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(item.tagText, color = PrimaryBlue, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(item.valueLabel, fontSize = 10.sp, color = TextSecondary)
                Text(item.value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = if (item.isProject) Color(0xFF4CAF50) else Color.Gray)
            }
        }
    }
}
