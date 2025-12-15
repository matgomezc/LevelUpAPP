package com.example.levelup.ui.screens.products

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.background
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import com.example.levelup.ui.viewmodel.ProductViewModel
import com.example.levelup.ui.viewmodel.AuthViewModel
import com.example.levelup.ui.theme.GamingBackground
import com.example.levelup.ui.theme.GamingBlue
import com.example.levelup.ui.theme.NeonGreen
import com.example.levelup.services.LocationService
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import android.Manifest
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ProductCatalogScreen(
    category: String? = null,
    onNavigateBack: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onLogout: () -> Unit = {},
    productViewModel: ProductViewModel = viewModel(factory = ProductViewModel.Factory),
    authViewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory),
    cartViewModel: com.example.levelup.ui.viewmodel.CartViewModel
) {
    val uiState by productViewModel.uiState.collectAsState()
    
    // Estado para la ubicación
    var currentLocation by remember { mutableStateOf<Pair<Double, Double>?>(null) }
    var showLocationDialog by remember { mutableStateOf(false) }
    var locationError by remember { mutableStateOf<String?>(null) }
    
    // Coroutine scope para operaciones asíncronas
    val scope = rememberCoroutineScope()
    val context = androidx.compose.ui.platform.LocalContext.current
    
    // Permisos de ubicación
    val locationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
    
    // Servicio de ubicación
    val locationService = remember {
        LocationService(context)
    }
    
    // Cargar productos cuando se abre la pantalla
    LaunchedEffect(category) {
        if (category != null) {
            productViewModel.loadProductsByCategory(category)
        } else {
            productViewModel.loadProducts()
        }
    }
    
    // Función para obtener la ubicación
    fun getLocation() {
        if (locationPermissionsState.allPermissionsGranted) {
            showLocationDialog = true
            locationError = null
            currentLocation = null
            scope.launch {
                try {
                    val location = locationService.getCurrentLocation()
                    if (location != null) {
                        currentLocation = Pair(location.latitude, location.longitude)
                    } else {
                        locationError = "No se pudo obtener la ubicación"
                    }
                } catch (e: Exception) {
                    locationError = "Error al obtener ubicación: ${e.message}"
                }
            }
        } else {
            locationPermissionsState.launchMultiplePermissionRequest()
        }
    }
    
    Scaffold(
        containerColor = GamingBackground,
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = category ?: "Catálogo de Productos",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    if (category != null) {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                actions = {
                    IconButton(onClick = onNavigateToHome) {
                        Icon(Icons.Default.Home, contentDescription = "Ir al Home")
                    }
                    // Botón de sincronización con API
                    IconButton(onClick = { productViewModel.syncWithApi() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Sincronizar con API")
                    }
                    // Botón de ubicación (IL2.4 - Recursos nativos)
                    IconButton(onClick = { getLocation() }) {
                        Icon(Icons.Default.LocationOn, contentDescription = "Mi ubicación")
                    }
                    IconButton(onClick = {
                        authViewModel.logout()
                        onLogout()
                    }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar sesión")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                // Lista animada
                AnimatedVisibility(
                    visible = uiState.products.isNotEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.products) { product ->
                            ProductCard(
                                product = product,
                                onAddToCart = { cartViewModel.addToCart(product) }
                            )
                        }
                    }
                }
                
                // Estado vacío animado
                AnimatedVisibility(
                    visible = uiState.products.isEmpty(),
                    enter = fadeIn(animationSpec = tween(250)) + slideInVertically { it / 4 },
                    exit = fadeOut()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(32.dp)
                        ) {
                            Icon(
                                Icons.Default.ShoppingCart,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "No hay productos disponibles",
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Text(
                                text = "Esta categoría está vacía",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
                
                // Mostrar error si hay
                uiState.errorMessage?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(16.dp)
                    )
                }
            }
        }
    }
    
    // Diálogo para mostrar la ubicación (IL2.4)
    if (showLocationDialog) {
        AlertDialog(
            onDismissRequest = { showLocationDialog = false },
            title = { Text("Mi Ubicación") },
            text = {
                if (currentLocation != null) {
                    Column {
                        Text("Latitud: ${currentLocation!!.first}")
                        Text("Longitud: ${currentLocation!!.second}")
                    }
                } else if (locationError != null) {
                    Text(locationError!!, color = MaterialTheme.colorScheme.error)
                } else {
                    CircularProgressIndicator()
                }
            },
            confirmButton = {
                TextButton(onClick = { showLocationDialog = false }) {
                    Text("Cerrar")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCard(
    product: com.example.levelup.data.model.Product,
    onAddToCart: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val formatter = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Imagen del producto
            if (product.imageUrl.isNotBlank()) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(12.dp))
            } else {
                // Si no hay imagen, mostrar un placeholder
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.ShoppingCart,
                        contentDescription = "Sin imagen",
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = product.category,
                        style = MaterialTheme.typography.bodySmall,
                        color = NeonGreen
                    )
                }
                
                // Precio
                Text(
                    text = formatter.format(product.price),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = GamingBlue
                )
            }
            
            if (product.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Stock
                Text(
                    text = "Stock: ${product.stock}",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (product.stock > 0) NeonGreen else MaterialTheme.colorScheme.error
                )
                
                // Botón de comprar
                Button(
                    onClick = onAddToCart,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GamingBlue
                    ),
                    enabled = product.stock > 0
                ) {
                    Text("Agregar al Carrito")
                }
            }
        }
    }
}