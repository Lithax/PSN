@echo off
SETLOCAL EnableDelayedExpansion

:: Check if the script is running with administrator privileges
openfiles >nul 2>nul
if %errorlevel% NEQ 0 (
    echo This script requires administrator privileges.
    echo.
    echo Attempting to restart the script with elevated privileges...
    
    :: Restart the batch file with administrator privileges
    powershell -Command "Start-Process cmd -ArgumentList '/c %~s0' -Verb runAs"
    exit /b
)

:: Get the parent directory of the script
set "parentDir=%~dp0.."

:: Remove the last two characters ("..")
set "parentDir=%parentDir:~0,-2%"

:: Remove trailing backslash if exists
if "%parentDir:~-1%"=="\" set "parentDir=%parentDir:~0,-1%"

:: Replace backslashes with regular slashes for safe PATH addition
set "parentDir=%parentDir:\=/%"

:: Add the parent directory to the system PATH (requires admin privileges)
echo Setting system PATH to include the parent directory: %parentDir%
setx PATH "%PATH%;%parentDir%" /M

echo Parent folder added to system PATH.
pause
