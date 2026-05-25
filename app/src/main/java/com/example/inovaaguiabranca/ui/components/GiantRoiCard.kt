package com.example.inovaaguiabranca.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.example.inovaaguiabranca.ui.theme.NavyBlue

@Composable
fun GiantRoiCard(
    totalSavedYtd: String,
    roiPercentage: String,
    goalProgress: Float,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = NavyBlue),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = "ECONOMIA GERADA (YTD)",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "R$ ",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = totalSavedYtd,
                    color = Color.White,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Black
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ROI Projetos",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                    Text(
                        text = roiPercentage,
                        color = Color(0xFF4CAF50), // Green for positive ROI
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Meta Anual",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.White.copy(alpha = 0.2f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(goalProgress)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFFFFC107)) // Yellow progress
                        )
                    }
                    Text(
                        text = "${(goalProgress * 100).toInt()}% atingida",
                        color = Color.White,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}
