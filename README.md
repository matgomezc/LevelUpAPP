# LevelUp - Aplicación Móvil de E-commerce Gaming

## Información del Proyecto

**Nombre del Proyecto**: LevelUp  
**Asignatura**: DSY1105 - Desarrollo de Aplicaciones Móviles  
**Evaluación**: Parcial 4 - Encargo Estudiante

## Integrantes

- Matias Benjamin Gomez Cespedes
- Juan Jose Escobar

---

## Funcionalidades

### Autenticación de Usuarios
- Registro de nuevos usuarios
- Inicio de sesión
- Persistencia de sesión
- Gestión de perfil de usuario
- Cierre de sesión

### Catálogo de Productos
- Visualización de todos los productos
- Filtrado por categorías:
  - Consolas
  - Accesorios
  - PC Gamers
  - Juegos de mesa
  - Mouses
  - Mousepad
  - Sillas gamers
  - Poleras personalizadas
- Búsqueda de productos
- Detalle de producto
- **Integración con API Externa (FakeStoreAPI)** para productos adicionales

### Carrito de Compras
- Agregar productos al carrito
- Visualizar items en el carrito
- Modificar cantidades
- Eliminar productos del carrito
- Calcular total

### Perfil de Usuario
- Visualización de perfil
- Edición de información personal
- Cambio de contraseña
- Actualización de foto de perfil (cámara o galería)

### Servicios de Ubicación
- Obtención de ubicación actual
- Integración con Google Play Services

---

## Endpoints Utilizados

### Microservicio Spring Boot (Backend Propio)

#### Autenticación
- `POST /api/auth/login` - Iniciar sesión
- `POST /api/auth/register` - Registrar nuevo usuario
- `POST /api/auth/refresh` - Refrescar token de autenticación

#### Productos
- `GET /api/products` - Obtener todos los productos
- `GET /api/products/{id}` - Obtener producto por ID
- `GET /api/products/category/{category}` - Obtener productos por categoría
- `POST /api/products` - Crear nuevo producto (admin)
- `PUT /api/products/{id}` - Actualizar producto (admin)
- `DELETE /api/products/{id}` - Eliminar producto (admin)

#### Usuarios
- `GET /api/users/me` - Obtener información del usuario actual
- `PUT /api/users/me` - Actualizar información del usuario actual

#### Carrito
- `GET /api/cart` - Obtener carrito del usuario
- `POST /api/cart/add` - Agregar producto al carrito
- `POST /api/cart/remove` - Eliminar producto del carrito
- `POST /api/cart/clear` - Vaciar carrito

**URL Base del Microservicio**: `http://[IP_SERVIDOR]:8080/`

### API Externa

**Nombre de la API**: FakeStoreAPI
**URL Base**: `https://fakestoreapi.com/`
**Propósito**: Obtener datos de productos simulados para probar la integración con servicios externos.

#### Endpoints de la API Externa
- `GET /products` - Obtiene la lista de todos los productos.
- `GET /products/{id}` - Obtiene un producto específico por su ID.
- `GET /products/categories` - Obtiene las categorías de productos.
- `GET /products/category/{category}` - Obtiene productos de una categoría específica.

---

## Tecnologías Utilizadas

- **Kotlin 2.1.21**: Lenguaje de programación
- **Jetpack Compose**: Framework de UI declarativa
- **Room Database 2.7.2**: Base de datos local
- **Retrofit 2.9.0**: Cliente HTTP para APIs
- **Coroutines**: Programación asíncrona
- **Material 3**: Sistema de diseño
- **Navigation Compose**: Navegación entre pantallas
- **MockK & Turbine**: Pruebas unitarias

---

## Pasos para Ejecutar

### Requisitos Previos

- Android Studio Hedgehog (2023.1.1) o superior
- JDK 11 o superior
- Android SDK 34
- Gradle 8.13
- Dispositivo Android o emulador configurado

### Instalación

1. **Clonar el repositorio**:
   ```bash
   git clone [URL_DEL_REPOSITORIO]
   cd LevelUp
   ```

2. **Abrir en Android Studio**:
   - Abrir Android Studio
   - Seleccionar "Open an Existing Project"
   - Navegar a la carpeta del proyecto

3. **Sincronizar Gradle**:
   - Android Studio sincronizará automáticamente
   - O manualmente: `File > Sync Project with Gradle Files`

4. **Configurar Backend**:
   - Asegurarse de que los microservicios Spring Boot estén ejecutándose
   - Configurar la URL base del backend en el código (si es necesario)
   - La URL por defecto está configurada en `RetrofitClient.kt`

5. **Ejecutar la aplicación**:
   - Conectar un dispositivo Android o iniciar un emulador
   - Presionar el botón "Run" o `Shift + F10`

---

## Generación de APK Firmado

### Requisitos

- Archivo `keystore.properties` en el directorio raíz de `app/`.
- Archivo Keystore (`.jks` o `.keystore`).

### Pasos para Generar APK Firmado

1. **Crear archivo `keystore.properties`**:
   Crea un archivo llamado `keystore.properties` dentro de la carpeta `app/` con el siguiente contenido:
   ```properties
   storeFile=ruta/a/tu/keystore.jks
   storePassword=tu_password_store
   keyAlias=tu_alias
   keyPassword=tu_password_key
   ```
   *Nota: `storeFile` puede ser una ruta relativa o absoluta.*

2. **Generar APK Release**:
   Ejecuta el comando de Gradle:
   ```bash
   ./gradlew assembleRelease
   ```
   O usa el menú de Android Studio: `Build > Generate Signed Bundle / APK`.

3. **Ubicación del APK**:
   El APK firmado se generará en: `app/build/outputs/apk/release/app-release.apk`

### Verificación de la Firma

```bash
jarsigner -verify -verbose -certs app-release.apk
```

---

## Pruebas Unitarias

El proyecto incluye pruebas unitarias para validar la lógica de negocio.

### Ejecutar Pruebas

```bash
./gradlew testDebugUnitTest
```

### Descripción de Tests
- **AuthViewModelTest**: Verifica el flujo de inicio de sesión y registro.
  - `login success`: Valida que el estado cambie a `isLoggedIn = true` tras un login exitoso.
  - `login failure`: Valida que se muestre un mensaje de error tras un login fallido.
  - `register success`: Valida que el usuario se registre y loguee correctamente.

### Tecnologías de Testing
- **JUnit 4**: Framework de pruebas.
- **MockK**: Para crear mocks de dependencias (`UserRepository`, `DataStoreManager`).
- **Turbine**: Para probar Kotlin Flows.
- **Coroutines Test**: Para controlar la ejecución de corrutinas en tests.

---

## Estructura del Proyecto

```
app/src/main/java/com/example/levelup/
│
├── data/
│   ├── dao/                    # Data Access Objects (Room)
│   ├── database/               # Configuración de Room
│   ├── local/                  # DataStore Manager
│   ├── model/                  # Modelos de datos
│   ├── network/                # Cliente Retrofit y servicios API
│   │   ├── model/              # DTOs y Mappers
│   │   ├── ApiService.kt       # Interfaz Retrofit
│   │   └── RetrofitClient.kt   # Cliente Singleton
│   ├── repository/             # Repositorios
│   └── seed/                   # Datos iniciales
│
├── services/                   # Servicios del sistema
│
├── ui/
│   ├── components/             # Componentes reutilizables
│   ├── navigation/            # Navegación
│   ├── screens/               # Pantallas
│   ├── theme/                 # Tema y estilos
│   └── viewmodel/             # ViewModels
│
└── MainActivity.kt            # Punto de entrada
```

---

**Última actualización**: Noviembre 2025
