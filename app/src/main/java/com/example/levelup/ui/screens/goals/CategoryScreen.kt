package com.example.levelup.ui.screens.goals

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.levelup.ui.theme.GamingBlue
import com.example.levelup.ui.theme.GamingBackground
import com.example.levelup.ui.theme.NeonGreen

// Datos de las categorías gaming
data class GamingCategory(
    val name: String,
    val icon: ImageVector,
    val color: Color = GamingBlue
)

// Lista de categorías gaming
val gamingCategories = listOf(
    GamingCategory("Juegos de mesa", Icons.Default.Star, GamingBlue),
    GamingCategory("Accesorios", Icons.Default.Star, GamingBlue),
    GamingCategory("Consolas", Icons.Default.Star, GamingBlue),
    GamingCategory("PC Gamers", Icons.Default.Star, GamingBlue),
    GamingCategory("Sillas gamers", Icons.Default.Star, GamingBlue),
    GamingCategory("Mouses", Icons.Default.Star, GamingBlue),
    GamingCategory("Mousepad", Icons.Default.Star, NeonGreen),
    GamingCategory("Poleras personalizadas", Icons.Default.Star, GamingBlue)
)

@Composable
fun CategoryCard(
    category: GamingCategory,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp),
        colors = CardDefaults.cardColors(
            containerColor = category.color
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    imageVector = category.icon,
                    contentDescription = category.name,
                    modifier = Modifier.size(48.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = category.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    maxLines = 2
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryGridScreen(
    onCategoryClick: (String) -> Unit,
    onBackClick: () -> Unit,
    onNavigateToCatalog: (String?) -> Unit = {}
) {
    Scaffold(
        containerColor = GamingBackground,
        topBar = {
            TopAppBar(
                title = { Text("Categorías Gaming") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        // Usar LazyColumn con grid manual ya que LazyVerticalGrid puede no estar disponible
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Dividir en filas de 2 columnas
            items(gamingCategories.chunked(2)) { rowCategories ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowCategories.forEach { category ->
                        CategoryCard(
                            category = category,
                            onClick = { 
                                onNavigateToCatalog(category.name)
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    // Si hay una sola categoría en la fila, agregar un espacio
                    if (rowCategories.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

