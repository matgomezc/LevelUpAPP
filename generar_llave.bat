@echo off
echo Buscando keytool...

set KEYTOOL="C:\Program Files\Android\Android Studio\jbr\bin\keytool.exe"

if not exist %KEYTOOL% (
    set KEYTOOL="C:\Program Files\Android\Android Studio\jre\bin\keytool.exe"
)

if not exist %KEYTOOL% (
    echo ERROR: No se encontro keytool.exe.
    pause
    exit /b
)

echo Borrando llave antigua si existe...
if exist levelup-release.jks del levelup-release.jks

echo Generando NUEVA keystore con la contrasena Juanjo2005...
%KEYTOOL% -genkey -v -keystore levelup-release.jks -keyalg RSA -keysize 2048 -validity 10000 -alias levelup -storepass Juanjo2005 -keypass Juanjo2005 -dname "CN=LevelUp, OU=Dev, O=LevelUp, L=Santiago, S=RM, C=CL"

echo.
if exist levelup-release.jks (
    echo EXITO: Archivo levelup-release.jks creado correctamente.
) else (
    echo ERROR: No se pudo crear el archivo.
)
pause