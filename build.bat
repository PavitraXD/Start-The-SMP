@echo off
echo Building SMPStart Plugin...
echo.

REM Check if Maven is installed
mvn --version >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: Maven is not installed or not in PATH
    echo Please install Maven and try again
    pause
    exit /b 1
)

REM Clean and compile
echo Cleaning previous builds...
mvn clean

echo.
echo Compiling plugin...
mvn package

if %errorlevel% equ 0 (
    echo.
    echo ================================
    echo Build successful!
    echo Plugin JAR location: target\smpstart-1.0.0.jar
    echo ================================
    echo.
    echo To install:
    echo 1. Copy the JAR to your server's plugins folder
    echo 2. Start/restart your server
    echo 3. Configure in plugins/SMPStart/config.yml
    echo.
) else (
    echo.
    echo Build failed! Check the error messages above.
)

pause