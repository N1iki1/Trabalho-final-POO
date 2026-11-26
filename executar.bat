@echo off
cd /d "%~dp0"
chcp 65001 >nul
echo Compilando...
dir /s /b src\*.java > fontes.txt
javac -encoding UTF-8 -d out -cp "lib/*" @fontes.txt
if errorlevel 1 (
  echo Erro na compilacao.
  pause
  exit /b
)
del fontes.txt
echo Abrindo o sistema...
java -cp "out;lib/*" br.com.neuraxi.Main
pause
