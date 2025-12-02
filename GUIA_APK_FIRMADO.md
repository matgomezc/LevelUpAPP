# 🔐 Guía Completa: Generar APK Firmado para LevelUp

## 📋 Índice
1. [Generar Keystore (.jks)](#paso-1-generar-keystore-jks)
2. [Configurar gradle.properties](#paso-2-configurar-gradleproperties)
3. [Generar APK Firmado](#paso-3-generar-apk-firmado)
4. [Verificar el APK](#paso-4-verificar-el-apk)

---

## Paso 1: Generar Keystore (.jks)

### ¿Qué es un keystore?
Es un archivo que contiene tu "firma digital" para la aplicación. Es como tu DNI digital para la app. **¡GUÁRDALO BIEN!** Si lo pierdes, no podrás actualizar tu app en el futuro.

### Método 1: Desde PowerShell o CMD (Recomendado)

1. **Abre PowerShell o CMD** en Windows
   - Presiona `Windows + R`
   - Escribe `powershell` o `cmd`
   - Presiona Enter

2. **Navega a la carpeta del proyecto**
   ```powershell
   cd "C:\Users\Central Gamer\Desktop\LevelUp"
   ```

3. **Crea una carpeta para el keystore** (opcional, pero recomendado)
   ```powershell
   mkdir keystore
   cd keystore
   ```

4. **Ejecuta el comando para generar el keystore**
   ```powershell
   keytool -genkey -v -keystore levelup-release.jks -keyalg RSA -keysize 2048 -validity 10000 -alias levelup
   ```

5. **Te pedirá información. Responde así:**

   ```
   Ingresa la contraseña del almacén de claves: 
   > [Escribe una contraseña SEGURA, ejemplo: LevelUp2025!Secure]
   > [Presiona Enter - NO verás lo que escribes, es normal]
   
   Vuelve a ingresar la contraseña del almacén de claves:
   > [Escribe la MISMA contraseña otra vez]
   > [Presiona Enter]
   
   ¿Cuál es su nombre y apellido?
   > [Tu nombre, ejemplo: Matias Gomez]
   > [Presiona Enter]
   
   ¿Cuál es el nombre de la unidad organizativa?
   > [Tu universidad o empresa, ejemplo: Universidad]
   > [Presiona Enter]
   
   ¿Cuál es el nombre de su organización?
   > [Tu organización, ejemplo: LevelUp Team]
   > [Presiona Enter]
   
   ¿Cuál es el nombre de su ciudad o localidad?
   > [Tu ciudad, ejemplo: Santiago]
   > [Presiona Enter]
   
   ¿Cuál es el nombre de su estado o provincia?
   > [Tu región, ejemplo: Region Metropolitana]
   > [Presiona Enter]
   
   ¿Cuál es el código de país de dos letras para esta unidad?
   > [Tu país, ejemplo: CL]
   > [Presiona Enter]
   
   ¿Es correcto?
     [no]:  yes
   > [Escribe: yes]
   > [Presiona Enter]
   
   Ingrese la contraseña de la clave para <levelup>
     (RETURN si es la misma que la contraseña del almacén):
   > [Presiona Enter directamente - usa la misma contraseña]
   ```

6. **¡Listo!** Se creará el archivo `levelup-release.jks` en la carpeta `keystore`

### Método 2: Desde Android Studio (Más fácil)

1. **Abre Android Studio**
2. **Ve a: Build → Generate Signed Bundle / APK**
3. **Selecciona "APK"** y presiona Next
4. **Si no tienes keystore, haz clic en "Create new..."**
5. **Completa el formulario:**
   - **Key store path**: `C:\Users\Central Gamer\Desktop\LevelUp\keystore\levelup-release.jks`
   - **Password**: [Tu contraseña segura]
   - **Key alias**: `levelup`
   - **Key password**: [La misma contraseña o diferente]
   - **Validity**: `10000` (días)
   - **First and Last Name**: Tu nombre
   - **Organizational Unit**: Tu unidad
   - **Organization**: Tu organización
   - **City**: Tu ciudad
   - **State**: Tu región
   - **Country Code**: CL (o tu país)
6. **Presiona OK**
7. **Se creará el keystore automáticamente**

### ⚠️ IMPORTANTE: Guarda esta información

Crea un archivo de texto (NO lo subas a GitHub) con:
```
Keystore: levelup-release.jks
Contraseña del keystore: [TU_CONTRASEÑA]
Alias: levelup
Contraseña del alias: [TU_CONTRASEÑA]
Ubicación: C:\Users\Central Gamer\Desktop\LevelUp\keystore\levelup-release.jks
```

**¡GUÁRDALO EN UN LUGAR SEGURO!** Si lo pierdes, no podrás actualizar tu app.

---

## Paso 2: Configurar gradle.properties

### ¿Qué es gradle.properties?
Es un archivo de configuración donde guardas información sensible (como contraseñas) que Gradle necesita para firmar el APK.

### Pasos:

1. **Abre el archivo `gradle.properties`** que está en la raíz del proyecto
   - Ruta: `C:\Users\Central Gamer\Desktop\LevelUp\gradle.properties`

2. **Agrega estas líneas al FINAL del archivo:**

   ```properties
   # Configuración para APK Firmado
   KEYSTORE_FILE=levelup-release.jks
   KEYSTORE_PASSWORD=TU_CONTRASEÑA_AQUI
   KEY_ALIAS=levelup
   KEY_PASSWORD=TU_CONTRASEÑA_AQUI
   ```

3. **Reemplaza `TU_CONTRASEÑA_AQUI`** con la contraseña que usaste al crear el keystore
   - Si usaste la misma contraseña para todo, ponla en ambos lugares
   - Ejemplo:
     ```properties
     KEYSTORE_PASSWORD=LevelUp2025!Secure
     KEY_PASSWORD=LevelUp2025!Secure
     ```

4. **Guarda el archivo** (Ctrl + S)

### ⚠️ IMPORTANTE:
- **NO subas `gradle.properties` a GitHub** si contiene contraseñas reales
- O usa contraseñas de ejemplo para el repositorio público
- Para producción, usa variables de entorno o archivos locales

---

## Paso 3: Generar APK Firmado

### Opción A: Desde la Terminal (PowerShell/CMD)

1. **Abre PowerShell o CMD**

2. **Navega a la carpeta del proyecto:**
   ```powershell
   cd "C:\Users\Central Gamer\Desktop\LevelUp"
   ```

3. **Ejecuta el comando:**
   ```powershell
   .\gradlew.bat assembleRelease
   ```
   
   O si estás en Git Bash:
   ```bash
   ./gradlew assembleRelease
   ```

4. **Espera a que termine** (puede tardar varios minutos la primera vez)

5. **Si todo sale bien**, verás algo como:
   ```
   BUILD SUCCESSFUL in 2m 30s
   ```

6. **El APK estará en:**
   ```
   app\build\outputs\apk\release\app-release.apk
   ```

### Opción B: Desde Android Studio

1. **Abre Android Studio**

2. **Ve a: Build → Generate Signed Bundle / APK**

3. **Selecciona "APK"** y presiona Next

4. **Selecciona tu keystore:**
   - **Key store path**: `C:\Users\Central Gamer\Desktop\LevelUp\keystore\levelup-release.jks`
   - **Key store password**: [Tu contraseña]
   - **Key alias**: `levelup`
   - **Key password**: [Tu contraseña]

5. **Presiona Next**

6. **Selecciona "release"** como build variant

7. **Presiona Finish**

8. **Espera a que termine** (verás una barra de progreso)

9. **Cuando termine**, verás un mensaje: "APK(s) generated successfully"

10. **Haz clic en "locate"** para ver dónde está el APK

---

## Paso 4: Verificar el APK

### Verificar que el APK está firmado:

1. **Abre PowerShell o CMD**

2. **Navega a donde está el APK:**
   ```powershell
   cd "C:\Users\Central Gamer\Desktop\LevelUp\app\build\outputs\apk\release"
   ```

3. **Ejecuta el comando de verificación:**
   ```powershell
   jarsigner -verify -verbose -certs app-release.apk
   ```

4. **Si está bien firmado**, verás:
   ```
   jar verified.
   ```

### Ver información del APK:

Puedes usar herramientas como:
- **APK Analyzer** en Android Studio: Build → Analyze APK
- O simplemente ver las propiedades del archivo en Windows

---

## 🐛 Solución de Problemas Comunes

### Error: "keytool no se reconoce como comando"
**Solución:** Necesitas tener Java JDK instalado. El `keytool` viene con Java.
- Verifica que tengas Java instalado: `java -version`
- Si no lo tienes, instala JDK 11 o superior

### Error: "Keystore file does not exist"
**Solución:** 
- Verifica que el archivo `levelup-release.jks` exista
- Verifica la ruta en `build.gradle.kts`
- Asegúrate de que el archivo esté en la carpeta correcta

### Error: "Password was incorrect"
**Solución:**
- Verifica que la contraseña en `gradle.properties` sea exactamente la misma que usaste al crear el keystore
- No debe tener espacios extra al inicio o final

### Error: "Gradle build failed"
**Solución:**
- Verifica que todas las dependencias estén sincronizadas
- En Android Studio: File → Sync Project with Gradle Files
- Revisa los errores en la pestaña "Build"

### El APK no se genera
**Solución:**
- Verifica que `build.gradle.kts` tenga la configuración de signing (yo la agregaré)
- Verifica que `gradle.properties` tenga las credenciales correctas
- Limpia el proyecto: Build → Clean Project
- Reconstruye: Build → Rebuild Project

---

## 📝 Checklist Final

Antes de considerar que terminaste, verifica:

- [ ] Keystore generado (`levelup-release.jks` existe)
- [ ] Contraseñas guardadas en lugar seguro
- [ ] `gradle.properties` configurado con las contraseñas
- [ ] `build.gradle.kts` configurado para signing (yo lo haré)
- [ ] APK generado exitosamente (`app-release.apk` existe)
- [ ] APK verificado con `jarsigner`
- [ ] APK probado en un dispositivo Android

---

## 📸 Para la Documentación

Necesitarás capturas de:

1. **Propiedades del archivo APK:**
   - Click derecho en `app-release.apk` → Propiedades
   - Captura la pestaña "Detalles" o "General"

2. **Información del keystore:**
   - Puedes usar: `keytool -list -v -keystore levelup-release.jks`
   - Captura la salida (sin mostrar contraseñas)

3. **Resultado de la verificación:**
   - Captura la salida de `jarsigner -verify`

---

## 🎯 Resumen Rápido

```powershell
# 1. Generar keystore
keytool -genkey -v -keystore keystore\levelup-release.jks -keyalg RSA -keysize 2048 -validity 10000 -alias levelup

# 2. Configurar gradle.properties (editar manualmente)

# 3. Generar APK
.\gradlew.bat assembleRelease

# 4. Verificar
jarsigner -verify -verbose -certs app\build\outputs\apk\release\app-release.apk
```

---

**¡Éxito!** 🎉 Si sigues estos pasos, tendrás tu APK firmado listo para entregar.

