package com.example.levelup.ui.screens.profile

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.levelup.ui.theme.GamingBackground
import com.example.levelup.ui.viewmodel.AuthViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit = {},
    onLogout: () -> Unit = {},
    authViewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory)
) {
    val scrollState = rememberScrollState()
    val uiState by authViewModel.uiState.collectAsState()
    val currentUser = uiState.currentUser
    val context = LocalContext.current
    
    // Estado para la URI de la foto
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var imageFile by remember { mutableStateOf<File?>(null) }
    
    // Permisos
    val cameraPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        listOf(Manifest.permission.CAMERA)
    } else {
        listOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
    }
    val permissionsState = rememberMultiplePermissionsState(cameraPermission)
    
    // Launcher para tomar foto
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && photoUri != null) {
            val imagePath = photoUri!!.toString()
            authViewModel.updateProfileImage(imagePath)
        }
    }
    
    // Launcher para seleccionar de galería
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val imagePath = it.toString()
            authViewModel.updateProfileImage(imagePath)
        }
    }
    
    // Función para crear archivo de imagen
    fun createImageFile(context: Context): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_${timeStamp}_"
        val storageDir = context.getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES)
        return File.createTempFile(imageFileName, ".jpg", storageDir).apply {
            imageFile = this
        }
    }
    
    // Función para tomar foto
    fun takePicture() {
        if (permissionsState.allPermissionsGranted) {
            val file = createImageFile(context)
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
            photoUri = uri
            takePictureLauncher.launch(uri)
        } else {
            permissionsState.launchMultiplePermissionRequest()
        }
    }
    
    // Función para seleccionar de galería
    fun pickImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pickImageLauncher.launch("image/*")
        } else {
            if (permissionsState.allPermissionsGranted) {
                pickImageLauncher.launch("image/*")
            } else {
                permissionsState.launchMultiplePermissionRequest()
            }
        }
    }
    
    // Estados para edición
    var isEditing by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf(currentUser?.name ?: "") }
    var email by remember { mutableStateOf(currentUser?.email ?: "") }
    var country by remember { mutableStateOf(currentUser?.country ?: "") }
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    
    // Errores
    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var countryError by remember { mutableStateOf<String?>(null) }
    
    val validCountries by remember {
        mutableStateOf(
            Locale.getISOCountries()
                .map { code -> Locale("", code).getDisplayCountry(Locale("es", "ES")) }
                .toSet()
        )
    }
    
    // Actualizar campos cuando cambie el usuario
    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            name = currentUser.name
            email = currentUser.email
            country = currentUser.country
        }
    }
    
    // Recargar usuario al entrar
    LaunchedEffect(Unit) {
        authViewModel.refreshUser()
    }
    
    Scaffold(
        containerColor = GamingBackground,
        topBar = {
            TopAppBar(
                title = { Text("Perfil") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                actions = {
                    IconButton(onClick = onNavigateToHome) {
                        Icon(Icons.Default.Home, contentDescription = "Ir al Home")
                    }
                    if (currentUser != null && isEditing) {
                        IconButton(onClick = { isEditing = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Cancelar")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        if (currentUser == null || !uiState.isLoggedIn) {
            // Usuario no logueado
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Inicia sesión para ver tu perfil",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onNavigateToLogin,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Iniciar Sesión")
                    }
                    OutlinedButton(
                        onClick = onNavigateToRegister,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Registrarse")
                    }
                }
            }
        } else {
            // Usuario logueado - mostrar perfil
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                
                // Diálogo para elegir fuente de imagen
                var showImageSourceDialog by remember { mutableStateOf(false) }
                
                // Avatar/Imagen de perfil
                Box(
                    modifier = Modifier.size(120.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    // Imagen de perfil o icono por defecto
                    if (currentUser.profileImagePath != null) {
                        val imageUri = try {
                            Uri.parse(currentUser.profileImagePath)
                        } catch (e: Exception) {
                            null
                        }
                        
                        if (imageUri != null) {
                            Image(
                                painter = rememberAsyncImagePainter(imageUri),
                                contentDescription = "Foto de perfil",
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    } else {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    // Botón para cambiar foto
                    FloatingActionButton(
                        onClick = { showImageSourceDialog = true },
                        modifier = Modifier.size(40.dp),
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Cambiar foto",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                
                if (showImageSourceDialog) {
                    AlertDialog(
                        onDismissRequest = { showImageSourceDialog = false },
                        title = { Text("Seleccionar foto") },
                        text = {
                            Column {
                                TextButton(onClick = {
                                    showImageSourceDialog = false
                                    takePicture()
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Tomar foto")
                                }
                                TextButton(onClick = {
                                    showImageSourceDialog = false
                                    pickImage()
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Seleccionar de galería")
                                }
                            }
                        },
                        confirmButton = {
                            TextButton(onClick = { showImageSourceDialog = false }) {
                                Text("Cancelar")
                            }
                        }
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Nombre del usuario
                Text(
                    text = currentUser.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Email del usuario
                Text(
                    text = currentUser.email,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "País: ${currentUser.country.ifBlank { "No establecido" }}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                if (!isEditing) {
                    // Modo visualización
                    Button(
                        onClick = { isEditing = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Editar Perfil")
                    }
                } else {
                    // Modo edición
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "Editar Información",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            
                            // Campo Nombre
                            OutlinedTextField(
                                value = name,
                                onValueChange = {
                                    name = it
                                    nameError = null
                                },
                                label = { Text("Nombre") },
                                leadingIcon = {
                                    Icon(Icons.Default.Person, contentDescription = "Nombre")
                                },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                isError = nameError != null,
                                supportingText = nameError?.let {
                                    { Text(it, color = MaterialTheme.colorScheme.error) }
                                }
                            )
                            
                            // Campo Email
                            OutlinedTextField(
                                value = email,
                                onValueChange = {
                                    email = it
                                    emailError = null
                                },
                                label = { Text("Email") },
                                leadingIcon = {
                                    Icon(Icons.Default.Email, contentDescription = "Email")
                                },
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                singleLine = true,
                                isError = emailError != null,
                                supportingText = emailError?.let {
                                    { Text(it, color = MaterialTheme.colorScheme.error) }
                                }
                            )
                            
                            // Campo País
                            OutlinedTextField(
                                value = country,
                                onValueChange = {
                                    country = it
                                    countryError = null
                                },
                                label = { Text("País") },
                                leadingIcon = {
                                    Icon(Icons.Default.LocationOn, contentDescription = "País")
                                },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                isError = countryError != null,
                                supportingText = countryError?.let {
                                    { Text(it, color = MaterialTheme.colorScheme.error) }
                                }
                            )
                            
                            Divider()
                            
                            Text(
                                text = "Cambiar Contraseña (Opcional)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            
                            // Campo Contraseña Actual
                            OutlinedTextField(
                                value = currentPassword,
                                onValueChange = {
                                    currentPassword = it
                                    passwordError = null
                                },
                                label = { Text("Contraseña Actual") },
                                leadingIcon = {
                                    Icon(Icons.Default.Lock, contentDescription = "Contraseña")
                                },
                                modifier = Modifier.fillMaxWidth(),
                                visualTransformation = PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                singleLine = true
                            )
                            
                            // Campo Nueva Contraseña
                            OutlinedTextField(
                                value = newPassword,
                                onValueChange = {
                                    newPassword = it
                                    passwordError = null
                                },
                                label = { Text("Nueva Contraseña") },
                                leadingIcon = {
                                    Icon(Icons.Default.Lock, contentDescription = "Nueva Contraseña")
                                },
                                modifier = Modifier.fillMaxWidth(),
                                visualTransformation = PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                singleLine = true
                            )
                            
                            // Campo Confirmar Contraseña
                            OutlinedTextField(
                                value = confirmPassword,
                                onValueChange = {
                                    confirmPassword = it
                                    passwordError = null
                                },
                                label = { Text("Confirmar Nueva Contraseña") },
                                leadingIcon = {
                                    Icon(Icons.Default.Lock, contentDescription = "Confirmar")
                                },
                                modifier = Modifier.fillMaxWidth(),
                                visualTransformation = PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                singleLine = true,
                                isError = passwordError != null,
                                supportingText = passwordError?.let {
                                    { Text(it, color = MaterialTheme.colorScheme.error) }
                                }
                            )
                            
                            // Mensaje de error general
                            if (uiState.errorMessage != null) {
                                Text(
                                    text = uiState.errorMessage!!,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            
                            // Botón Guardar
                            Button(
                                onClick = {
                                    // Validar campos
                                    var isValid = true
                                    
                                    if (name.isBlank()) {
                                        nameError = "El nombre es requerido"
                                        isValid = false
                                    }
                                    
                                    if (email.isBlank()) {
                                        emailError = "El email es requerido"
                                        isValid = false
                                    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                        emailError = "Email inválido"
                                        isValid = false
                                    }
                                    
                                    if (country.isBlank()) {
                                        countryError = "El país es requerido"
                                        isValid = false
                                    } else if (validCountries.none { it.equals(country.trim(), ignoreCase = true) }) {
                                        countryError = "Ingresa un país válido"
                                        isValid = false
                                    }
                                    
                                    // Validar contraseña si se quiere cambiar
                                    if (newPassword.isNotBlank() || confirmPassword.isNotBlank()) {
                                        if (currentPassword.isBlank()) {
                                            passwordError = "Debes ingresar tu contraseña actual"
                                            isValid = false
                                        } else if (currentPassword != currentUser.password) {
                                            passwordError = "Contraseña actual incorrecta"
                                            isValid = false
                                        } else if (newPassword.length < 6) {
                                            passwordError = "La nueva contraseña debe tener al menos 6 caracteres"
                                            isValid = false
                                        } else if (newPassword != confirmPassword) {
                                            passwordError = "Las contraseñas no coinciden"
                                            isValid = false
                                        }
                                    }
                                    
                                    if (isValid) {
                                        val passwordToUpdate = if (newPassword.isNotBlank()) newPassword else null
                                        authViewModel.updateProfile(name, email, country, passwordToUpdate)
                                        
                                        // Limpiar campos de contraseña
                                        currentPassword = ""
                                        newPassword = ""
                                        confirmPassword = ""
                                        
                                        // Salir del modo edición después de un momento
                                        if (uiState.errorMessage == null) {
                                            isEditing = false
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = !uiState.isLoading
                            ) {
                                if (uiState.isLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Guardar Cambios")
                                }
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Botón Cerrar Sesión
                OutlinedButton(
                    onClick = {
                        authViewModel.logout()
                        onLogout()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(Icons.Default.ExitToApp, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cerrar Sesión")
                }
                
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}