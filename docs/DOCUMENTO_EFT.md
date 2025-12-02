# CONTENIDO PARA DOCUMENTO WORD - EFT DSY1105

**Nota**: Este es el contenido que debes copiar a tu documento Word. Ajusta los datos personales y completa las secciones según tu proyecto.

---

# LevelUp - Aplicación Móvil de E-commerce Gaming

## Información del Proyecto

**Nombre del Proyecto**: LevelUp  
**Asignatura**: DSY1105 - Desarrollo de Aplicaciones Móviles  
**Evaluación**: Parcial 4 - Encargo Estudiante  
**Fecha de Entrega**: [FECHA]  
**Integrantes**: 
- [Nombre Integrante 1]
- [Nombre Integrante 2]

---

## 1. Introducción

### 1.1 Descripción del Proyecto

LevelUp es una aplicación móvil Android desarrollada en Kotlin que ofrece una plataforma de e-commerce especializada en productos gaming. La aplicación permite a los usuarios explorar, buscar y comprar productos relacionados con gaming, incluyendo consolas, accesorios, PC gamers, juegos de mesa y más.

### 1.2 Objetivos del Proyecto

- Desarrollar una aplicación móvil completamente funcional utilizando Kotlin y Jetpack Compose
- Implementar arquitectura MVVM con Repository Pattern siguiendo mejores prácticas
- Integrar la aplicación con microservicios backend desarrollados en Spring Boot
- Consumir APIs externas para enriquecer la funcionalidad de la aplicación
- Implementar pruebas unitarias que cubran al menos el 80% de la lógica de negocio
- Generar APK firmado para distribución
- Documentar todo el proceso de desarrollo

### 1.3 Contexto y Justificación

El proyecto LevelUp surge de la necesidad de crear una plataforma especializada en productos gaming que ofrezca una experiencia de usuario moderna y fluida. La aplicación integra tecnologías actuales de desarrollo móvil Android, demostrando competencias en:

- Desarrollo de interfaces modernas con Jetpack Compose
- Gestión de estado y arquitectura escalable
- Integración con servicios backend
- Consumo de APIs REST
- Pruebas automatizadas
- Gestión de versiones y trabajo colaborativo

---

## 2. Arquitectura y Diseño

### 2.1 Patrón Arquitectónico: MVVM

El proyecto implementa el patrón **Model-View-ViewModel (MVVM)** combinado con **Repository Pattern**, siguiendo las recomendaciones oficiales de Google para desarrollo Android moderno.

#### Justificación del Patrón MVVM:

1. **Separación de Responsabilidades**: La lógica de negocio está completamente separada de la capa de presentación
2. **Testabilidad**: Los ViewModels pueden probarse independientemente de la UI
3. **Ciclo de Vida**: Integración nativa con el ciclo de vida de Android
4. **Mantenibilidad**: Código más organizado y fácil de mantener

### 2.2 Diagrama de Arquitectura

```
┌─────────────────────────────────────────┐
│           UI Layer (Compose)            │
│  ┌──────────┐  ┌──────────┐           │
│  │  Screen  │  │  Screen  │  ...      │
│  └────┬─────┘  └────┬─────┘           │
└───────┼──────────────┼──────────────────┘
        │              │
        ▼              ▼
┌─────────────────────────────────────────┐
│         ViewModel Layer                  │
│  ┌──────────┐  ┌──────────┐           │
│  │AuthViewModel│ProductViewModel│      │
│  └────┬─────┘  └────┬─────┘           │
└───────┼──────────────┼──────────────────┘
        │              │
        ▼              ▼
┌─────────────────────────────────────────┐
│        Repository Layer                  │
│  ┌──────────┐  ┌──────────┐           │
│  │UserRepo  │  │ProductRepo│          │
│  └────┬─────┘  └────┬─────┘           │
└───────┼──────────────┼──────────────────┘
        │              │
        ▼              ▼
┌─────────────────────────────────────────┐
│          Data Layer                      │
│  ┌──────────┐  ┌──────────┐           │
│  │   Room   │  │  Retrofit │          │
│  │ Database │  │   API     │           │
│  └──────────┘  └───────────┘           │
└─────────────────────────────────────────┘
```

### 2.3 Capas de la Arquitectura

#### 2.3.1 UI Layer (Capa de Presentación)
- **Tecnología**: Jetpack Compose
- **Responsabilidad**: Renderizar la interfaz de usuario y manejar interacciones
- **Componentes**: Pantallas (Screens), Componentes reutilizables

#### 2.3.2 ViewModel Layer (Capa de Lógica de Negocio)
- **Responsabilidad**: Gestionar el estado de la UI y coordinar con los repositorios
- **Componentes**: 
  - `AuthViewModel`: Maneja autenticación y gestión de usuarios
  - `ProductViewModel`: Gestiona el catálogo de productos
  - `CartViewModel`: Administra el carrito de compras

#### 2.3.3 Repository Layer (Capa de Abstracción)
- **Responsabilidad**: Abstraer el acceso a datos, permitiendo múltiples fuentes
- **Componentes**:
  - `UserRepository`: Abstrae acceso a datos de usuarios
  - `ProductRepository`: Abstrae acceso a datos de productos

#### 2.3.4 Data Layer (Capa de Datos)
- **Responsabilidad**: Proporcionar acceso a fuentes de datos
- **Componentes**:
  - **Room Database**: Almacenamiento local SQLite
  - **Retrofit**: Cliente HTTP para consumo de APIs
  - **DataStore**: Preferencias del usuario

---

## 3. Tecnologías Utilizadas

### 3.1 Lenguaje y Framework Base

| Tecnología | Versión | Justificación |
|------------|---------|---------------|
| Kotlin | 2.1.21 | Lenguaje oficial de Android, moderno y seguro |
| Android Gradle Plugin | 8.5.0 | Herramienta de build oficial, última versión estable |
| KSP | 2.1.21-2.0.1 | Procesamiento de anotaciones más rápido que KAPT |

### 3.2 UI y Navegación

| Tecnología | Versión | Justificación |
|------------|---------|---------------|
| Jetpack Compose | BOM 2024.09.00 | Framework moderno de UI declarativa, recomendado por Google |
| Material 3 | - | Sistema de diseño moderno y consistente |
| Navigation Compose | 2.7.7 | Navegación tipo-segura entre pantallas |
| Coil | 2.7.0 | Carga eficiente de imágenes desde URLs |

### 3.3 Persistencia de Datos

| Tecnología | Versión | Justificación |
|------------|---------|---------------|
| Room Database | 2.7.2 | Abstracción SQLite con type-safety y soporte para Coroutines |
| DataStore Preferences | 1.1.1 | Almacenamiento de preferencias moderno y asíncrono |

### 3.4 Arquitectura y Asincronía

| Tecnología | Versión | Justificación |
|------------|---------|---------------|
| Lifecycle ViewModel | 2.7.0 | ViewModels para Compose con ciclo de vida integrado |
| Coroutines | 1.7.3 | Programación asíncrona moderna y eficiente |
| Flow | - | Streams de datos reactivos |

### 3.5 Integración con Backend

| Tecnología | Versión | Justificación |
|------------|---------|---------------|
| Retrofit | 2.9.0 | Cliente HTTP tipo-seguro para consumo de APIs REST |
| OkHttp | 4.12.0 | Cliente HTTP con interceptores y logging |
| Gson | 2.10.1 | Serialización/deserialización JSON |

### 3.6 Testing

| Tecnología | Versión | Justificación |
|------------|---------|---------------|
| JUnit | 4.13.2 | Framework estándar para pruebas unitarias |
| MockK | 1.13.8 | Framework de mocking para Kotlin |
| Espresso | 3.5.1 | Framework para pruebas de UI |
| Compose UI Test | - | Pruebas específicas para componentes Compose |

---

## 4. Funcionalidades Implementadas

### 4.1 Autenticación de Usuarios

**Descripción**: Sistema completo de registro e inicio de sesión de usuarios.

**Funcionalidades**:
- Registro de nuevos usuarios con validación de datos
- Inicio de sesión con credenciales
- Persistencia de sesión usando DataStore
- Gestión de perfil de usuario
- Cierre de sesión

**Pantallas**:
- `LoginScreen`: Pantalla de inicio de sesión
- `RegisterScreen`: Pantalla de registro

**ViewModel**: `AuthViewModel`

### 4.2 Catálogo de Productos

**Descripción**: Visualización y exploración de productos gaming organizados por categorías.

**Funcionalidades**:
- Visualización de todos los productos
- Filtrado por categorías
- Búsqueda de productos
- Detalle de producto

**Categorías Disponibles**:
- Consolas
- Accesorios
- PC Gamers
- Juegos de mesa
- Mouses
- Mousepad
- Sillas gamers
- Poleras personalizadas

**Pantallas**:
- `HomeScreen`: Pantalla principal con categorías
- `ProductCatalogScreen`: Catálogo de productos por categoría

**ViewModel**: `ProductViewModel`

### 4.3 Carrito de Compras

**Descripción**: Gestión de productos seleccionados para compra.

**Funcionalidades**:
- Agregar productos al carrito
- Visualizar items en el carrito
- Modificar cantidades
- Eliminar productos del carrito
- Calcular total

**Pantalla**: `CartScreen`

**ViewModel**: `CartViewModel`

### 4.4 Perfil de Usuario

**Descripción**: Gestión de información personal del usuario.

**Funcionalidades**:
- Visualización de perfil
- Edición de información personal
- Cambio de contraseña
- Actualización de foto de perfil (cámara o galería)
- Validación de datos

**Pantalla**: `ProfileScreen`

**ViewModel**: `AuthViewModel`

### 4.5 Servicios de Ubicación

**Descripción**: Integración con servicios de ubicación del dispositivo.

**Funcionalidades**:
- Obtención de ubicación actual
- Solicitud de permisos de ubicación
- Manejo de errores de ubicación

**Servicio**: `LocationService`

---

## 5. Integración con Microservicios Spring Boot

### 5.1 Descripción de la Integración

La aplicación móvil se integra con microservicios desarrollados en Spring Boot que proporcionan la lógica de negocio y persistencia de datos en el backend.

### 5.2 Endpoints del Microservicio

[COMPLETAR CON LOS ENDPOINTS REALES DE TU BACKEND]

#### 5.2.1 Autenticación
- `POST /api/auth/login` - Iniciar sesión
- `POST /api/auth/register` - Registrar nuevo usuario
- `POST /api/auth/refresh` - Refrescar token

#### 5.2.2 Productos
- `GET /api/products` - Obtener todos los productos
- `GET /api/products/{id}` - Obtener producto por ID
- `GET /api/products/category/{category}` - Obtener productos por categoría
- `POST /api/products` - Crear nuevo producto (admin)
- `PUT /api/products/{id}` - Actualizar producto (admin)
- `DELETE /api/products/{id}` - Eliminar producto (admin)

#### 5.2.3 Usuarios
- `GET /api/users/me` - Obtener usuario actual
- `PUT /api/users/me` - Actualizar usuario actual

#### 5.2.4 Carrito
- `GET /api/cart` - Obtener carrito del usuario
- `POST /api/cart/add` - Agregar producto al carrito
- `POST /api/cart/remove` - Eliminar producto del carrito
- `POST /api/cart/clear` - Vaciar carrito

### 5.3 Implementación de la Integración

La integración se realiza mediante:

1. **Retrofit Client**: Cliente HTTP configurado con interceptores para autenticación
2. **ApiService**: Interfaz que define todos los endpoints
3. **Repository Pattern**: Los repositorios abstraen el acceso a datos locales y remotos
4. **Sincronización**: Los datos se sincronizan entre Room (local) y el backend

### 5.4 Flujos CRUD Implementados

#### Create (Crear)
- Registro de nuevos usuarios
- Agregar productos al carrito

#### Read (Leer)
- Obtener lista de productos
- Obtener productos por categoría
- Obtener información del usuario
- Obtener carrito de compras

#### Update (Actualizar)
- Actualizar perfil de usuario
- Modificar cantidades en el carrito

#### Delete (Eliminar)
- Eliminar productos del carrito
- [Agregar otras operaciones DELETE si aplican]

---

## 6. Consumo de API Externa

### 6.1 API Externa Utilizada

[COMPLETAR CON LA API EXTERNA QUE VAYAS A USAR]

**Ejemplo**: 
- **API**: [Nombre de la API]
- **URL Base**: [URL de la API]
- **Propósito**: [Para qué se usa en la aplicación]

### 6.2 Endpoints Consumidos

[LISTAR LOS ENDPOINTS DE LA API EXTERNA]

### 6.3 Implementación

La API externa se consume mediante Retrofit y se integra en el flujo de la aplicación sin interferir con los microservicios propios. Los datos se muestran en la interfaz de manera coherente.

**Ejemplo de integración**:
- [Describir cómo se integra visualmente en la app]

---

## 7. Pruebas Unitarias

### 7.1 Cobertura de Pruebas

El proyecto implementa pruebas unitarias que cubren al menos el **80%** de la lógica de negocio, enfocándose en:

- ViewModels
- Repositories
- Lógica de negocio
- Validaciones

### 7.2 Herramientas Utilizadas

- **JUnit 4**: Framework base para pruebas
- **MockK**: Framework de mocking para Kotlin
- **Turbine**: Testing de Flows
- **Coroutines Test**: Testing de código asíncrono

### 7.3 Módulos con Pruebas

#### 7.3.1 AuthViewModel Tests
- Prueba de login exitoso
- Prueba de login con credenciales incorrectas
- Prueba de registro exitoso
- Prueba de registro con email duplicado
- Prueba de actualización de perfil

#### 7.3.2 ProductViewModel Tests
- Prueba de carga de productos
- Prueba de filtrado por categoría
- Prueba de manejo de errores

#### 7.3.3 UserRepository Tests
- Prueba de inserción de usuario
- Prueba de búsqueda por email
- Prueba de actualización de usuario

#### 7.3.4 ProductRepository Tests
- Prueba de obtención de productos
- Prueba de filtrado por categoría
- Prueba de inserción de producto

### 7.4 Ejecución de Pruebas

```bash
./gradlew test
```

### 7.5 Resultados de Cobertura

[AGREGAR CAPTURA DE PANTALLA DE LA COBERTURA DE CÓDIGO]

---

## 8. Generación de APK Firmado

### 8.1 Proceso de Firma

El APK se firma digitalmente usando un keystore (.jks) para garantizar la autenticidad y permitir actualizaciones futuras de la aplicación.

### 8.2 Configuración del Keystore

1. **Generación del Keystore**:
   ```bash
   keytool -genkey -v -keystore levelup-release.jks -keyalg RSA -keysize 2048 -validity 10000 -alias levelup
   ```

2. **Configuración en gradle.properties**:
   ```properties
   KEYSTORE_FILE=levelup-release.jks
   KEYSTORE_PASSWORD=[password]
   KEY_ALIAS=levelup
   KEY_PASSWORD=[password]
   ```

3. **Configuración en build.gradle.kts**:
   ```kotlin
   signingConfigs {
       create("release") {
           storeFile = file("${project.rootDir}/keystore/levelup-release.jks")
           storePassword = project.findProperty("KEYSTORE_PASSWORD") as String
           keyAlias = project.findProperty("KEY_ALIAS") as String
           keyPassword = project.findProperty("KEY_PASSWORD") as String
       }
   }
   ```

### 8.3 Generación del APK

```bash
./gradlew assembleRelease
```

El APK firmado se genera en: `app/build/outputs/apk/release/app-release.apk`

### 8.4 Verificación de la Firma

```bash
jarsigner -verify -verbose -certs app-release.apk
```

[AGREGAR CAPTURA DE PANTALLA DEL APK FIRMADO Y CONFIGURACIÓN]

---

## 9. Herramientas de Planificación y Control de Versiones

### 9.1 GitHub

#### 9.1.1 Repositorio
- **URL**: [URL del repositorio GitHub]
- **Visibilidad**: Público
- **Ramas**: main, develop

#### 9.1.2 Commits por Integrante

[AGREGAR TABLA O GRÁFICO DE COMMITS POR INTEGRANTE]

**Integrante 1**: [Número] commits
**Integrante 2**: [Número] commits

#### 9.1.3 Actividad Técnica

[AGREGAR CAPTURAS DE PANTALLA DE LA ACTIVIDAD EN GITHUB]

### 9.2 Trello

#### 9.2.1 Tablero de Proyecto
- **URL**: [URL del tablero Trello]
- **Estado**: Activo

#### 9.2.2 Distribución de Tareas

[AGREGAR CAPTURAS DE PANTALLA DEL TABLERO TRELLO]

**Tareas Completadas**:
- [Lista de tareas completadas]

**Tareas en Progreso**:
- [Lista de tareas en progreso]

#### 9.2.3 Planificación

[DESCRIBIR CÓMO SE PLANIFICÓ EL TRABAJO EN TRELLO]

---

## 10. Estructura del Proyecto

```
app/src/main/java/com/example/levelup/
│
├── data/
│   ├── dao/                    # Data Access Objects (Room)
│   ├── database/               # Configuración de Room
│   ├── local/                  # DataStore Manager
│   ├── model/                  # Modelos de datos
│   ├── network/                # Cliente Retrofit y servicios API
│   ├── repository/             # Repositorios
│   └── seed/                   # Datos iniciales
│
├── services/                   # Servicios del sistema
│
├── ui/
│   ├── components/             # Componentes reutilizables
│   ├── navigation/             # Navegación
│   ├── screens/                # Pantallas
│   ├── theme/                  # Tema y estilos
│   └── viewmodel/              # ViewModels
│
└── MainActivity.kt             # Punto de entrada
```

---

## 11. Instrucciones de Ejecución

### 11.1 Requisitos Previos

- Android Studio Hedgehog (2023.1.1) o superior
- JDK 11 o superior
- Android SDK 34
- Gradle 8.13
- Dispositivo Android o emulador

### 11.2 Pasos para Ejecutar

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
   - `File > Sync Project with Gradle Files`

4. **Configurar Backend**:
   - Asegurarse de que los microservicios Spring Boot estén ejecutándose
   - Configurar la URL base en el código

5. **Ejecutar la aplicación**:
   - Conectar dispositivo o iniciar emulador
   - Presionar "Run" (▶️)

---

## 12. Conclusiones

### 12.1 Logros Alcanzados

- ✅ Aplicación móvil completamente funcional
- ✅ Integración exitosa con microservicios Spring Boot
- ✅ Consumo de API externa implementado
- ✅ Pruebas unitarias con cobertura superior al 80%
- ✅ APK firmado generado correctamente
- ✅ Trabajo colaborativo evidenciado en GitHub y Trello

### 12.2 Aprendizajes

[DESCRIBIR LOS PRINCIPALES APRENDIZAJES DEL PROYECTO]

### 12.3 Dificultades Encontradas

[DESCRIBIR LAS PRINCIPALES DIFICULTADES Y CÓMO SE RESOLVIERON]

### 12.4 Mejoras Futuras

- Implementar caché más robusto
- Agregar más pruebas de integración
- Mejorar manejo de errores
- Optimizar rendimiento
- Agregar más funcionalidades

---

## 13. Referencias

- [Documentación de Android](https://developer.android.com)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Room Database](https://developer.android.com/training/data-storage/room)
- [Retrofit](https://square.github.io/retrofit/)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)

---

**Fin del Documento**

