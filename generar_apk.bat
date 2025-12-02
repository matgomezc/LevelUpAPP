@echo off
echo Configurando entorno Java...

if exist "C:\Program Files\Android\Android Studio\jbr" (
    set JAVA_HOME=C:\Program Files\Android\Android Studio\jbr
) else (
    set JAVA_HOME=C:\Program Files\Android\Android Studio\jre
)

echo Usando Java en: %JAVA_HOME%
echo Generando APK Firmado...

call gradlew.bat assembleRelease

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================================
    echo EXITO: El APK se genero correctamente.
    echo Ubicacion: app\build\outputs\apk\release\app-release.apk
    echo ========================================================
) else (
    echo.
    echo ERROR: Fallo la generacion del APK.
)
pause