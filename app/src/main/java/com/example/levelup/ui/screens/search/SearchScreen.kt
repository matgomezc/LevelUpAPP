package com.example.levelup.ui.screens.search

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.levelup.ui.theme.GamingBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onNavigateBack: () -> Unit,
    onNavigateToProduct: (String?) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    
    Scaffold(
        containerColor = GamingBackground,
        topBar = {
            TopAppBar(
                title = {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Buscar productos...") },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = "Buscar")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (searchQuery.isBlank()) {
                Text(
                    text = "Busca productos en el catálogo",
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                Text(
                    text = "Resultados para: $searchQuery",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

