package com.example.levelup.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelup.ui.components.LevelUpLogo
import com.example.levelup.ui.viewmodel.AuthViewModel
import com.example.levelup.ui.theme.GamingBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToHome: () -> Unit = {},
    viewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory)
) {
    // Variables para los campos del formulario
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    
    // Obtener el estado del ViewModel
    val uiState by viewModel.uiState.collectAsState()
    
    // Variable para rastrear si ya se intentó hacer login en esta sesión
    var hasAttemptedLogin by remember { mutableStateOf(false) }
    
    // Si el login fue exitoso, navegar automáticamente al Home
    LaunchedEffect(uiState.isLoggedIn, uiState.isLoading) {
        // Solo navegar si se completó un login exitoso (no estaba cargando y ahora está logueado)
        if (uiState.isLoggedIn && !uiState.isLoading && hasAttemptedLogin) {
            onLoginSuccess()
            hasAttemptedLogin = false // Resetear para evitar navegaciones múltiples
        }
    }
    
    Scaffold(
        containerColor = GamingBackground,
        topBar = {
            TopAppBar(
                title = { Text("Iniciar Sesión") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                actions = {
                    IconButton(onClick = onNavigateToHome) {
                        Icon(Icons.Default.Home, contentDescription = "Ir al Home")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo LevelUp Gamer
            LevelUpLogo(
                modifier = Modifier.padding(bottom = 48.dp)
            )
            
            OutlinedTextField(
                value = email,
                onValueChange = { 
                    email = it
                    emailError = null
                },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                isError = emailError != null,
                supportingText = emailError?.let { 
                    { Text(it, color = MaterialTheme.colorScheme.error) }
                }
            )
            
            OutlinedTextField(
                value = password,
                onValueChange = { 
                    password = it
                    passwordError = null
                },
                label = { Text("Contraseña") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Contraseña") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                isError = passwordError != null,
                supportingText = passwordError?.let { 
                    { Text(it, color = MaterialTheme.colorScheme.error) }
                }
            )
            
            if (uiState.errorMessage != null) {
                Text(
                    text = uiState.errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 16.dp),
                    textAlign = TextAlign.Center
                )
            }
            
            Button(
                onClick = {
                    // Validar los campos antes de hacer login
                    var isValid = true
                    
                    // Validar email
                    if (email.isBlank()) {
                        emailError = "El email es requerido"
                        isValid = false
                    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        emailError = "Email inválido"
                        isValid = false
                    }
                    
                    // Validar contraseña
                    if (password.isBlank()) {
                        passwordError = "La contraseña es requerida"
                        isValid = false
                    } else if (password.length < 6) {
                        passwordError = "La contraseña debe tener al menos 6 caracteres"
                        isValid = false
                    }
                    
                    // Si todo está bien, hacer login
                    if (isValid) {
                        hasAttemptedLogin = true
                        viewModel.login(email, password)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Iniciar Sesión")
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            TextButton(
                onClick = onNavigateToRegister,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("¿No tienes cuenta? Regístrate")
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Botón para ir al Home
            OutlinedButton(
                onClick = onNavigateToHome,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Home")
            }
        }
    }
}