@echo off
setlocal enabledelayedexpansion

REM Set paths
set JAVAFX_PATH=C:\Users\snowl\Downloads\openjfx-21.0.11_windows-x64_bin-sdk (1)\javafx-sdk-21.0.11
set PART1_PATH=..\Part1
set CURRENT_DIR=.

REM Compile Part1 first
echo Compiling Part1...
cd !PART1_PATH!
javac -d . *.java

if errorlevel 1 (
    echo Part1 compilation failed!
    pause
    exit /b 1
)

REM Go back to Part2
cd !CURRENT_DIR!

REM Compile Part2
echo Compiling Part2...
javac -cp "!PART1_PATH!;!JAVAFX_PATH!\lib\*" -d . *.java

if errorlevel 1 (
    echo Part2 compilation failed!
    pause
    exit /b 1
)

REM Run
echo Running TypingRaceApp...
java -cp ".;!PART1_PATH!;!JAVAFX_PATH!\lib\javafx.base.jar;!JAVAFX_PATH!\lib\javafx.controls.jar;!JAVAFX_PATH!\lib\javafx.graphics.jar;!JAVAFX_PATH!\lib\javafx.fxml.jar" Part2.TypingRaceApp

pause
