@echo off
setlocal enabledelayedexpansion

REM Set paths
set JAVAFX_PATH=C:\Users\snowl\Downloads\openjfx-21.0.11_windows-x64_bin-sdk\javafx-sdk-21.0.11
set PART1_PATH=..\Part1
set PART2_PATH=.

REM Compile Part1 first
echo Compiling Part1...
pushd !PART1_PATH!
javac -d . *.java

if errorlevel 1 (
    echo Part1 compilation failed!
    pause
    exit /b 1
)

REM Go back to Part2
popd

REM Compile Part2
echo Compiling Part2...
javac -cp "!PART1_PATH!;!JAVAFX_PATH!\lib\javafx.base.jar;!JAVAFX_PATH!\lib\javafx.controls.jar;!JAVAFX_PATH!\lib\javafx.graphics.jar;!JAVAFX_PATH!\lib\javafx.fxml.jar" -d . *.java

if errorlevel 1 (
    echo Part2 compilation failed!
    pause
    exit /b 1
)

REM Run
echo Running TypingRaceApp...
java --module-path "!JAVAFX_PATH!\lib" --add-modules javafx.controls,javafx.graphics,javafx.fxml -cp ".;!PART1_PATH!" Part2.TypingRaceApp

pause
