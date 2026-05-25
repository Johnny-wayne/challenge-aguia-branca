package com.example.inovaaguiabranca.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inovaaguiabranca.ui.theme.NavyBlue
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DynamicNativeBarChart(
    investment: Double,
    costReduction: Double,
    modifier: Modifier = Modifier
) {
    val ptBr = Locale("pt", "BR")
    val formatter = NumberFormat.getCurrencyInstance(ptBr)
    
    // Matemática pura para calcular as proporções das barras
    val maxVal = maxOf(investment, costReduction).coerceAtLeast(1.0)
    val investmentFraction = (investment / maxVal).toFloat()
    val costReductionFraction = (costReduction / maxVal).toFloat()

    val investmentColor = Color(0xFFF44336) // Red for investment/cost
    val reductionColor = Color(0xFF4CAF50) // Green for savings

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(280.dp)
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text("Comparativo Financeiro do Projeto", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = NavyBlue)
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            // Barra de Investimento
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.weight(1f).fillMaxHeight()
            ) {
                Text(
                    text = formatter.format(investment), 
                    fontSize = 12.sp, 
                    fontWeight = FontWeight.Bold,
                    color = investmentColor
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .width(70.dp)
                        .fillMaxHeight(investmentFraction) // Altura matemática!
                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp, bottomStart = 0.dp, bottomEnd = 0.dp))
                        .background(investmentColor)
                )
            }
            
            // Barra de Redução de Custos
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.weight(1f).fillMaxHeight()
            ) {
                Text(
                    text = formatter.format(costReduction), 
                    fontSize = 12.sp, 
                    fontWeight = FontWeight.Bold,
                    color = reductionColor
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .width(70.dp)
                        .fillMaxHeight(costReductionFraction) // Altura matemática!
                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp, bottomStart = 0.dp, bottomEnd = 0.dp))
                        .background(reductionColor)
                )
            }
        }
        
        // Eixo X (Divider)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0xFFE0E0E0))
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        // Rótulos na base
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.weight(1f)) {
                Text("Investimento", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color.DarkGray)
            }
            Box(contentAlignment = Alignment.Center, modifier = Modifier.weight(1f)) {
                Text("Economia", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color.DarkGray)
            }
        }
    }
}
