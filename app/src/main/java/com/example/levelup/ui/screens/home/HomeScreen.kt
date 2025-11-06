package com.example.levelup.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.levelup.ui.components.LevelUpLogoSmall
import com.example.levelup.ui.theme.GamingBackground
import com.example.levelup.ui.theme.GamingBlue
import com.example.levelup.ui.theme.GamingBlueDark
import com.example.levelup.ui.theme.NeonGreen
import com.example.levelup.ui.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToFilters: () -> Unit,
    onNavigateToCategory: (String) -> Unit,
    onNavigateToProducts: () -> Unit,
    cartViewModel: CartViewModel
) {
    // Estado del carrito
    val cartState by cartViewModel.uiState.collectAsState()
    val cartItemCount = cartState.totalItems
    
    // Producto destacado
    val featuredProduct = remember {
        Product(
            name = "PlayStation 5",
            price = 499990.0,
            category = "Consolas",
            description = "Consola PlayStation 5 con control DualSense",
            imageUrl = "https://www.facilitea.com/on/demandware.static/-/Sites-promocaixa-m-catalog/default/dw33be0a78/electronica/Gaming/Consolas/121-4007379/121-4007379_1_600x600.png"
        )
    }
    
    // Categorías
    val categories = remember {
        listOf(
            Category("Juegos de mesa", "https://images.unsplash.com/photo-1529699211952-734e80c4d42b?w=400&h=300&fit=crop"),
            Category("Accesorios", "https://images.unsplash.com/photo-1541140532154-b024d705b90a?w=400&h=300&fit=crop"),
            Category("Consolas", "https://images.unsplash.com/photo-1606813907291-d86efa9b94db?w=400&h=300&fit=crop"),
            Category("PC gamers", "https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=400&h=300&fit=crop")
        )
    }
    
    Scaffold(
        containerColor = GamingBackground,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Logo a la izquierda
                        LevelUpLogoSmall()
                        
                        // Botones de carrito y perfil a la derecha
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Botón de carrito
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(GamingBlueDark)
                                    .clickable(onClick = onNavigateToCart)
                                    .padding(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        Icons.Default.ShoppingCart,
                                        contentDescription = "Carrito",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text("Carrito", color = Color.White, fontSize = 10.sp)
                                    if (cartItemCount > 0) {
                                        Badge(
                                            modifier = Modifier.size(16.dp),
                                            containerColor = Color.Red
                                        ) {
                                            Text(
                                                cartItemCount.toString(),
                                                color = Color.White,
                                                fontSize = 8.sp
                                            )
                                        }
                                    }
                                }
                            }
                            
                            // Botón de perfil
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(GamingBlueDark)
                                    .clickable(onClick = onNavigateToProfile)
                                    .padding(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Person,
                                        contentDescription = "Perfil",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text("Perfil", color = Color.White, fontSize = 10.sp)
                                }
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = GamingBackground
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            // Sección de Iniciar Sesión / Registrarse
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = GamingBlueDark
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "¡Bienvenido a LEVEL-UP GAMER!",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Inicia sesión o regístrate para acceder a todas las funciones",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Botón Iniciar Sesión
                        Button(
                            onClick = onNavigateToLogin,
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GamingBlue
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                "Iniciar Sesión",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        
                        // Botón Registrarse
                        OutlinedButton(
                            onClick = onNavigateToRegister,
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.White
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                2.dp,
                                GamingBlue
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                "Registrarse",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
            
            // Producto destacado en el centro
            
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1A1A1A)
                )
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Imagen del producto
                    AsyncImage(
                        model = featuredProduct.imageUrl,
                        contentDescription = featuredProduct.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    
                    // Gradiente oscuro en la parte inferior para el texto
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .align(Alignment.BottomCenter)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.8f)
                                    )
                                )
                            )
                    )
                    
                    // Información del producto
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = featuredProduct.name,
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$${String.format("%.0f", featuredProduct.price)}",
                            color = NeonGreen,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    // Botón para ver productos
                    Button(
                        onClick = { onNavigateToCategory("Consolas") },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GamingBlue
                        )
                    ) {
                        Text("Ver Productos", color = Color.White)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Título de categorías
            Text(
                text = "Categorías",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Categorías en scroll horizontal
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories) { category ->
                    CategoryCard(
                        category = category,
                        onClick = { onNavigateToCategory(category.name) }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun CategoryCard(
    category: Category,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .width(200.dp)
            .height(150.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = GamingBlue
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Imagen de fondo
            AsyncImage(
                model = category.imageUrl,
                contentDescription = category.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            
            // Overlay oscuro
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
            )
            
            // Título de la categoría
            Text(
                text = category.name,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
            )
        }
    }
}

// Data classes
data class Product(
    val name: String,
    val price: Double,
    val category: String,
    val description: String,
    val imageUrl: String
)

data class Category(
    val name: String,
    val imageUrl: String
)

