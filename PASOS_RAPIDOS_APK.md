# 🚀 Pasos Rápidos: APK Firmado (Versión Simplificada)

## 📌 Resumen en 3 Pasos

1. **Generar el archivo de firma** (.jks) → Como crear tu "DNI digital" de la app
2. **Guardar las contraseñas** en gradle.properties → Como guardar la clave de tu casa
3. **Generar el APK** → Como "empaquetar" tu app lista para instalar

---

## 🎯 PASO 1: Generar el Keystore (5 minutos)

### ¿Qué es esto?
Es como crear tu "firma digital" para la app. Es un archivo especial que Android necesita para saber que la app es tuya.

### Cómo hacerlo (Método Fácil con Android Studio):

1. **Abre Android Studio**
2. **Click en el menú:** `Build` → `Generate Signed Bundle / APK`
3. **Selecciona "APK"** → Click en `Next`
4. **Click en "Create new..."** (si no tienes keystore)
5. **Llena el formulario:**

   ```
   Key store path: [Click en la carpeta] → Navega a: LevelUp/keystore/levelup-release.jks
   
   Password: [Escribe una contraseña, ejemplo: MiApp2025]
   Confirm: [Escribe la misma contraseña]
   
   Alias: levelup
   Password: [La misma contraseña de arriba]
   Validity: 10000
   
   First name: Tu Nombre
   Last name: Tu Apellido
   Organizational Unit: Universidad (o lo que quieras)
   Organization: LevelUp (o lo que quieras)
   City: Tu Ciudad
   State: Tu Región
   Country Code: CL (Chile) o tu país
   ```

6. **Click en OK**
7. **¡Listo!** Se creó el archivo `levelup-release.jks`

### ⚠️ IMPORTANTE: Anota esto en un papel o archivo seguro:

```
Contraseña del keystore: [La que escribiste]
Alias: levelup
Ubicación: LevelUp/keystore/levelup-release.jks
```

**¡NO LO PIERDAS!** Es como perder las llaves de tu casa, no podrás actualizar tu app.

---

## 🎯 PASO 2: Configurar gradle.properties (2 minutos)

### ¿Qué es esto?
Es un archivo donde guardas las contraseñas para que Gradle pueda firmar el APK automáticamente.

### Cómo hacerlo:

1. **Abre el archivo `gradle.properties`**
   - Está en la raíz del proyecto: `LevelUp/gradle.properties`
   - Puedes abrirlo con cualquier editor de texto (Notepad, Android Studio, etc.)

2. **Ve al final del archivo** y agrega estas 4 líneas:

   ```properties
   # Configuración para APK Firmado
   KEYSTORE_FILE=levelup-release.jks
   KEYSTORE_PASSWORD=MiApp2025
   KEY_ALIAS=levelup
   KEY_PASSWORD=MiApp2025
   ```

3. **Reemplaza `MiApp2025`** con la contraseña REAL que usaste en el Paso 1

4. **Guarda el archivo** (Ctrl + S)

### Ejemplo completo del archivo:

```properties
# ... (todo lo que ya estaba) ...

# Configuración para APK Firmado
KEYSTORE_FILE=levelup-release.jks
KEYSTORE_PASSWORD=TuContraseñaRealAqui
KEY_ALIAS=levelup
KEY_PASSWORD=TuContraseñaRealAqui
```

---

## 🎯 PASO 3: Generar el APK (5-10 minutos)

### Método Fácil (Android Studio):

1. **Abre Android Studio**
2. **Click en:** `Build` → `Generate Signed Bundle / APK`
3. **Selecciona "APK"** → `Next`
4. **Selecciona tu keystore:**
   - **Key store path**: Busca `keystore/levelup-release.jks`
   - **Password**: [Tu contraseña]
   - **Key alias**: `levelup`
   - **Key password**: [Tu contraseña]
5. **Click en `Next`**
6. **Selecciona "release"** → `Finish`
7. **Espera** (verás una barra de progreso)
8. **Cuando termine**, verás: "APK(s) generated successfully"
9. **Click en "locate"** para ver dónde está

### El APK estará en:
```
app/build/outputs/apk/release/app-release.apk
```

---

## ✅ Verificar que Funcionó

### Verificar el APK:

1. **Abre PowerShell** (Windows + R → escribe `powershell`)
2. **Navega al APK:**
   ```powershell
   cd "C:\Users\Central Gamer\Desktop\LevelUp\app\build\outputs\apk\release"
   ```
3. **Verifica:**
   ```powershell
   jarsigner -verify -verbose -certs app-release.apk
   ```
4. **Si ves "jar verified."** → ¡Todo está bien! ✅

---

## 🐛 Problemas Comunes

### "No encuentro el archivo .jks"
- Verifica que esté en: `LevelUp/keystore/levelup-release.jks`
- Si no existe la carpeta `keystore`, créala primero

### "Contraseña incorrecta"
- Verifica que la contraseña en `gradle.properties` sea EXACTAMENTE la misma
- Sin espacios al inicio o final
- Revisa mayúsculas/minúsculas

### "Gradle build failed"
- En Android Studio: `File` → `Sync Project with Gradle Files`
- Luego: `Build` → `Clean Project`
- Luego: `Build` → `Rebuild Project`

### "keytool no se reconoce"
- Necesitas tener Java JDK instalado
- Verifica con: `java -version` en PowerShell
- Si no funciona, instala JDK 11 o superior

---

## 📋 Checklist Final

Antes de entregar, verifica:

- [ ] Archivo `levelup-release.jks` existe en `keystore/`
- [ ] Contraseñas anotadas en lugar seguro
- [ ] `gradle.properties` tiene las 4 líneas con las contraseñas
- [ ] APK generado en `app/build/outputs/apk/release/app-release.apk`
- [ ] APK verificado con `jarsigner` (dice "jar verified")
- [ ] APK probado instalándolo en un celular Android

---

## 💡 Tips

1. **Guarda las contraseñas** en un lugar seguro (archivo de texto encriptado, gestor de contraseñas, etc.)
2. **NO subas el keystore a GitHub** (está en .gitignore normalmente)
3. **NO subas gradle.properties con contraseñas reales** a un repositorio público
4. **Prueba el APK** instalándolo en un celular antes de entregar

---

## 🎉 ¡Listo!

Si seguiste estos pasos, ya tienes tu APK firmado. Ahora solo falta:
- Tomar capturas de pantalla del APK y keystore
- Agregarlas al README
- ¡Entregar! 🚀

