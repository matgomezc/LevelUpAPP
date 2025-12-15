package com.example.levelup.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelup.ui.components.LevelUpLogo
import com.example.levelup.ui.viewmodel.AuthViewModel
import com.example.levelup.ui.theme.GamingBackground
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit = {},
    viewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory)
) {
    val scrollState = rememberScrollState()
    
    // Variables para los campos
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    
    // Variables para los errores
    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    var countryError by remember { mutableStateOf<String?>(null) }
    
    // Lista de países válidos en español (ISO) para validar entrada del usuario
    val validCountries by remember {
        mutableStateOf(
            Locale.getISOCountries()
                .map { code -> Locale("", code).getDisplayCountry(Locale("es", "ES")) }
                .toSet()
        )
    }
    
    // Estado del ViewModel
    val uiState by viewModel.uiState.collectAsState()
    
    // Variable para rastrear si ya se intentó registrar en esta sesión
    var hasAttemptedRegister by remember { mutableStateOf(false) }
    
    // Si el registro fue exitoso, navegar automáticamente al Home
    LaunchedEffect(uiState.isLoggedIn, uiState.isLoading) {
        // Solo navegar si se completó un registro exitoso (no estaba cargando y ahora está logueado)
        if (uiState.isLoggedIn && !uiState.isLoading && hasAttemptedRegister) {
            onRegisterSuccess()
            hasAttemptedRegister = false // Resetear para evitar navegaciones múltiples
        }
    }
    
    Scaffold(
        containerColor = GamingBackground,
        topBar = {
            TopAppBar(
                title = { Text("Registro") },
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
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            
            // Logo LevelUp Gamer
            LevelUpLogo(
                modifier = Modifier.padding(bottom = 8.dp),
                showGamer = true
            )
            
            Text(
                text = "Crear Cuenta",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            
            OutlinedTextField(
                value = name,
                onValueChange = { 
                    name = it
                    nameError = null
                },
                label = { Text("Nombre") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Nombre") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = nameError != null,
                supportingText = nameError?.let { 
                    { Text(it, color = MaterialTheme.colorScheme.error) }
                }
            )
            
            OutlinedTextField(
                value = email,
                onValueChange = { 
                    email = it
                    emailError = null
                },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                isError = emailError != null,
                supportingText = emailError?.let { 
                    { Text(it, color = MaterialTheme.colorScheme.error) }
                }
            )
            
            OutlinedTextField(
                value = country,
                onValueChange = {
                    country = it
                    countryError = null
                },
                label = { Text("País") },
                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = "País") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = countryError != null,
                supportingText = countryError?.let {
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
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                isError = passwordError != null,
                supportingText = passwordError?.let { 
                    { Text(it, color = MaterialTheme.colorScheme.error) }
                }
            )
            
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { 
                    confirmPassword = it
                    confirmPasswordError = null
                },
                label = { Text("Confirmar Contraseña") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Confirmar Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                isError = confirmPasswordError != null,
                supportingText = confirmPasswordError?.let { 
                    { Text(it, color = MaterialTheme.colorScheme.error) }
                }
            )
            
            if (uiState.errorMessage != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = uiState.errorMessage!!,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            
            // Botón para crear cuenta y continuar al menú - AHORA VISIBLE
            Button(
                onClick = {
                    // Validar todos los campos
                    var isValid = true
                    
                    // Validar nombre
                    if (name.isBlank()) {
                        nameError = "El nombre es requerido"
                        isValid = false
                    }
                    
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
                    
                    // Validar país
                    if (country.isBlank()) {
                        countryError = "El país es requerido"
                        isValid = false
                    } else if (validCountries.none { it.equals(country.trim(), ignoreCase = true) }) {
                        countryError = "Ingresa un país válido"
                        isValid = false
                    }
                    
                    // Validar confirmación de contraseña
                    if (confirmPassword.isBlank()) {
                        confirmPasswordError = "Confirma tu contraseña"
                        isValid = false
                    } else if (password != confirmPassword) {
                        confirmPasswordError = "Las contraseñas no coinciden"
                        isValid = false
                    }
                    
                    // Si todo está bien, registrar y continuar al menú
                    if (isValid) {
                        hasAttemptedRegister = true
                        viewModel.register(name, email, password, country)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(top = 8.dp),
                enabled = !uiState.isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = "Crear Cuenta y Continuar",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            TextButton(
                onClick = onNavigateToLogin,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("¿Ya tienes cuenta? Inicia sesión")
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Botón para ir al Home
            OutlinedButton(
                onClick = onNavigateToHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text("Home")
            }
        }
    }
}