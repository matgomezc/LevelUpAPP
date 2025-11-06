package com.example.levelup.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.levelup.ui.theme.GamingBlue
import com.example.levelup.ui.theme.GamingWhite
import com.example.levelup.ui.theme.NeonGreen

// Componente del logo LevelUp Gamer
@Composable
fun LevelUpLogo(
    modifier: Modifier = Modifier,
    showGamer: Boolean = true
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Texto "LEVEL-UP"
        Text(
            text = "LEVEL-UP",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = GamingWhite,
            style = androidx.compose.ui.text.TextStyle(
                shadow = androidx.compose.ui.graphics.Shadow(
                    color = GamingBlue,
                    blurRadius = 8f
                )
            )
        )
        
        // Texto "GAMER" si se muestra
        if (showGamer) {
            Text(
                text = "GAMER",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = NeonGreen,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        
        // Flecha hacia abajo
        Text(
            text = "▼",
            fontSize = 16.sp,
            color = GamingWhite,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

// Logo pequeño para usar en TopBar
@Composable
fun LevelUpLogoSmall(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "LEVEL-UP",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = GamingWhite,
            style = androidx.compose.ui.text.TextStyle(
                shadow = androidx.compose.ui.graphics.Shadow(
                    color = GamingBlue,
                    blurRadius = 4f
                )
            )
        )
        Text(
            text = " GAMER",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = NeonGreen
        )
    }
}

