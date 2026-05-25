package com.example.inovaaguiabranca.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
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
import com.example.inovaaguiabranca.ui.theme.PrimaryBlue

@Composable
fun AppHeader(
    userName: String,
    userRoleInfo: String,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            .background(NavyBlue)
            .padding(top = 48.dp, bottom = 24.dp, start = 16.dp, end = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(PrimaryBlue),
                    contentAlignment = Alignment.Center
                ) {
                    val initials = userName.split(" ").take(2).joinToString("") { it.take(1) }.uppercase()
                    Text(text = if (initials.isEmpty()) "U" else initials, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column {
                    Text(
                        text = "Olá, $userName",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = userRoleInfo,
                        color = Color.LightGray,
                        fontSize = 12.sp
                    )
                }
            }
            
            Row {
                IconButton(onClick = { /* TODO: Search */ }) {
                    Icon(Icons.Default.Search, contentDescription = "Buscar", tint = Color.LightGray)
                }
                IconButton(onClick = onLogout) {
                    Icon(Icons.Default.ExitToApp, contentDescription = "Sair", tint = Color.LightGray)
                }
            }
        }
    }
}
