@echo off
setlocal

REM Change to the directory of the script
@echo sigma
cd /d "%~dp0"

REM Run the command and keep the terminal open
java -jar "C:\Users\mhbau\OneDrive\Dokumente\Java Projects\Projects\psn\PSN\distros\basic\psn.jar" "%~1"

REM Optional: Add a pause to diagnose issues
pause
