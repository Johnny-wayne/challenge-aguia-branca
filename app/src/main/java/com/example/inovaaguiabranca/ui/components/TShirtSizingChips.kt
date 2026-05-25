package com.example.inovaaguiabranca.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inovaaguiabranca.ui.theme.PrimaryBlue

@Composable
fun TShirtSizingChips(
    options: List<String> = listOf("Baixo", "Médio", "Alto"),
    selectedValue: String?,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { option ->
            val isSelected = selectedValue == option
            
            Button(
                onClick = { onValueChange(option) },
                modifier = Modifier.weight(1f).height(40.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) PrimaryBlue else Color.White,
                    contentColor = if (isSelected) Color.White else Color.DarkGray
                ),
                border = if (!isSelected) BorderStroke(1.dp, Color.LightGray) else null,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = option,
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}
