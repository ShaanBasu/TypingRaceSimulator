# 🖮 Typing Race Simulator

A Java-based typing race simulator where typists compete to win by completing a passage of text. The project ships in two distinct parts: a **terminal-based textual simulation** (Part 1) and a **fully graphical JavaFX application** (Part 2).

---

## Table of Contents

1. [Project Overview](#project-overview)
2. [Repository Structure](#repository-structure)
3. [Prerequisites & Dependencies](#prerequisites--dependencies)
4. [Part 1 — Textual Simulation](#part-1--textual-simulation)
   - [How It Works](#how-it-works)
   - [Compiling Part 1](#compiling-part-1)
   - [Running the Race Simulation](#running-the-race-simulation)
   - [Running the Unit Tests](#running-the-unit-tests)
5. [Part 2 — Graphical Application (JavaFX)](#part-2--graphical-application-javafx)
   - [How It Works](#how-it-works-1)
   - [Installing JavaFX](#installing-javafx)
   - [Compiling Part 2](#compiling-part-2)
   - [Running Part 2](#running-part-2)
   - [Using the run.bat Script (Windows)](#using-the-runbat-script-windows)
6. [Gameplay Guide](#gameplay-guide)
   - [Part 1 — Terminal Race](#part-1--terminal-race)
   - [Part 2 — GUI Application](#part-2--gui-application)
7. [Game Mechanics Reference](#game-mechanics-reference)
8. [Class & Architecture Reference](#class--architecture-reference)
9. [Troubleshooting](#troubleshooting)

---

## Project Overview

The Typing Race Simulator models a competitive typing race. Each typist has an **accuracy rating** (0.0–1.0) that governs how likely they are to advance correctly, mistype and slide back, or burn out entirely. The simulation runs turn-by-turn until one typist completes the full passage.

**Part 1** renders the race live in the terminal using Unicode characters.

**Part 2** wraps the same engine in a rich JavaFX GUI with typist customisation, difficulty modifiers, live animated progress bars, a statistics tracker, a leaderboard, and a reward/earnings system.

---

## Repository Structure

```
TypingRaceSimulator/
├── Part1/
│   ├── Typist.java              # Core typist model (fields, movement, burnout)
│   ├── TypingRace.java          # Race engine (turn simulation, terminal display)
│   ├── TypistTest.java          # Unit tests for Typist class
│   └── TypingRaceTest.java      # Integration test that runs a full race
│
├── Part2/
│   ├── TypingRaceApp.java       # JavaFX entry point — main menu & all screens
│   ├── TypingRaceEnhanced.java  # Extended race engine used by the GUI
│   ├── TypistConfiguration.java # Typist customisation (style, keyboard, accessory)
│   ├── TypistWrapper.java       # Wraps Typist with GUI-facing extended state
│   ├── PassageLibrary.java      # Built-in passage texts (Short / Medium / Long)
│   ├── RaceDisplay.java         # Live animated race visualisation panel
│   ├── RaceResult.java          # Result model (WPM, position, burnout count)
│   ├── RewardSystem.java        # Points, earnings, and title calculation
│   ├── LeaderboardEntry.java    # Persistent leaderboard record per typist
│   ├── StatisticsManager.java   # Per-typist history, personal bests, averages
│   ├── Sponsor.java             # Optional sponsor bonus system
│   └── run.bat                  # Windows convenience build-and-run script
│
└── TypingRaceSimulator_Spec.pdf # Original project specification
```

---

## Prerequisites & Dependencies

### Both Parts

| Requirement | Version | Notes |
|---|---|---|
| **Java Development Kit (JDK)** | 11 or later (17+ recommended) | Must include `javac` and `java` on your PATH |

Verify your Java installation:
```bash
java -version
javac -version
```

### Part 2 Only — JavaFX

Part 2 uses JavaFX for its graphical interface. JavaFX is **not** bundled with modern JDKs and must be downloaded separately.

| Requirement | Version | Download |
|---|---|---|
| **JavaFX SDK** | 21 (LTS recommended) | https://gluonhq.com/products/javafx/ |

Choose the SDK (not the jmods) matching your operating system and architecture (Windows x64, macOS aarch64, Linux x64, etc.).

---

## Part 1 — Textual Simulation

### How It Works

Three typists race across a terminal lane. Each turn, every typist either:

- **Advances** one character (normal typing)
- **Slides back** 2–4 characters (mistype — more likely at lower accuracy)
- **Burns out** for 3 turns (overexertion — more likely at very high accuracy)

The race ends the moment any typist reaches or passes the passage length. Winners gain an accuracy boost; ties are handled gracefully.

### Compiling Part 1

Navigate to the `Part1/` directory and compile all `.java` files into a `Part1` package directory:

```bash
cd Part1
javac -d . *.java
```

This creates a `Part1/` subdirectory containing the compiled `.class` files. Your directory will look like:

```
Part1/
├── Part1/
│   ├── Typist.class
│   ├── TypingRace.class
│   ├── TypistTest.class
│   └── TypingRaceTest.class
├── Typist.java
├── TypingRace.java
└── ...
```

### Running the Race Simulation

After compiling, run the integration test which creates three typists and starts a full race:

```bash
# From inside the Part1/ directory
java Part1.TypingRaceTest
```

You will see a live terminal animation like:

```
  TYPING RACE - passage length: 100 chars
=====================================================
|          ①                                       | shaan1 (Accuracy: 0.00)
|                    ②~                            | shaan2 (Accuracy: 0.70) BURNT OUT (2 turns)
|               ③ [<]                              | shaan3 (Accuracy: 0.60) <- just mistyped
=====================================================
  [~] = burnt out    [<] = just mistyped
```

Each frame refreshes automatically with a 200 ms delay so the race is visible in real time.

### Running the Unit Tests

The `TypistTest` class runs five labelled tests covering progress clamping, burnout countdown, state reset, accuracy bounds, and movement blocking:

```bash
# From inside the Part1/ directory
java Part1.TypistTest
```

Expected output covers:

- **TEST 1** — Progress cannot go below zero after `slideBack()`
- **TEST 2** — Burnout counts down turn by turn and clears at zero
- **TEST 3** — `resetToStart()` clears both progress and burnout state
- **TEST 4** — Accuracy is clamped to the `0.0`–`1.0` range
- **TEST 5** — Normal forward movement via `typeCharacter()`
- **BONUS** — `typeCharacter()` is blocked while burnt out

---

## Part 2 — Graphical Application (JavaFX)

### How It Works

Part 2 wraps the Part 1 engine with a full JavaFX GUI. The application opens on a dark-themed main menu and offers:

- **Start New Race** — configure typists, pick a passage, set difficulty modifiers
- **Statistics** — per-typist history including average WPM, accuracy, and first-place count
- **Leaderboard** — cumulative points and earnings across all races

### Installing JavaFX

**Step 1.** Download the JavaFX 21 SDK from https://gluonhq.com/products/javafx/ — pick the SDK zip for your platform.

**Step 2.** Extract the zip to a stable location, for example:

| OS | Suggested path |
|---|---|
| Windows | `C:\javafx-sdk-21` |
| macOS | `/Library/javafx-sdk-21` |
| Linux | `/opt/javafx-sdk-21` |

**Step 3.** Note the path to the `lib/` subdirectory inside the extracted folder — you will need it for every `javac` and `java` command.

```
javafx-sdk-21/
└── lib/
    ├── javafx.base.jar
    ├── javafx.controls.jar
    ├── javafx.fxml.jar
    ├── javafx.graphics.jar
    └── ...
```

### Compiling Part 2

Part 2 depends on the compiled Part 1 classes. You must compile Part 1 first (see above), then compile Part 2 with both Part 1 and the JavaFX jars on the classpath.

> In the commands below, replace `/path/to/javafx-sdk-21` with your actual JavaFX SDK path.

#### macOS / Linux

```bash
# Step 1 — compile Part 1 (skip if already done)
cd Part1
javac -d . *.java
cd ..

# Step 2 — compile Part 2
cd Part2
javac \
  -cp "../Part1:/path/to/javafx-sdk-21/lib/javafx.base.jar:/path/to/javafx-sdk-21/lib/javafx.controls.jar:/path/to/javafx-sdk-21/lib/javafx.graphics.jar:/path/to/javafx-sdk-21/lib/javafx.fxml.jar" \
  -d . \
  *.java
```

#### Windows (Command Prompt)

```cmd
REM Step 1 — compile Part 1
cd Part1
javac -d . *.java
cd ..

REM Step 2 — compile Part 2
cd Part2
javac -cp "..\Part1;C:\javafx-sdk-21\lib\javafx.base.jar;C:\javafx-sdk-21\lib\javafx.controls.jar;C:\javafx-sdk-21\lib\javafx.graphics.jar;C:\javafx-sdk-21\lib\javafx.fxml.jar" -d . *.java
```

### Running Part 2

#### macOS / Linux

```bash
# From inside the Part2/ directory
java \
  --module-path /path/to/javafx-sdk-21/lib \
  --add-modules javafx.controls,javafx.graphics,javafx.fxml \
  -cp ".:/path/to/Part1" \
  Part2.TypingRaceApp
```

> Replace `/path/to/Part1` with the absolute path to your compiled `Part1/` directory.

#### Windows (Command Prompt)

```cmd
REM From inside the Part2/ directory
java --module-path "C:\javafx-sdk-21\lib" --add-modules javafx.controls,javafx.graphics,javafx.fxml -cp ".;..\Part1" Part2.TypingRaceApp
```

### Using the run.bat Script (Windows)

A `run.bat` script is included in `Part2/`. Before using it, **open the file in a text editor** and update the `JAVAFX_PATH` variable to point to your local JavaFX SDK installation:

```bat
set JAVAFX_PATH=C:\path\to\your\javafx-sdk-21
```

Then run from the `Part2/` directory:

```cmd
run.bat
```

The script will compile both parts and launch the application automatically.

---

## Gameplay Guide

### Part 1 — Terminal Race

The terminal race in Part 1 is driven by the test classes — there is no interactive prompt. To customise a race, open `TypingRaceTest.java` and edit the typist setup before recompiling:

```java
// Change passage length (number of characters to type)
TypingRace test1 = new TypingRace(100);

// Create typists: symbol, name, accuracy (0.0 = worst, 1.0 = best)
Typist typist1 = new Typist('①', "TURBOFINGERS", 0.95);
Typist typist2 = new Typist('②', "HUNT_N_PECK",  0.45);
Typist typist3 = new Typist('③', "VOICE_BOT",    0.70);

// Seat them (seats 1, 2, or 3 only)
test1.addTypist(typist1, 1);
test1.addTypist(typist2, 2);
test1.addTypist(typist3, 3);

test1.startRace();
```

Recompile after any changes:

```bash
cd Part1
javac -d . *.java
java Part1.TypingRaceTest
```

### Part 2 — GUI Application

When the application opens you will see the main menu. Follow these steps to run a race:

**1. Start New Race**
Click "Start New Race" on the main menu.

**2. Configure the Race**
- Choose how many typists will compete (2–4).
- Select a passage difficulty: **Short** (~115 chars), **Medium** (~270 chars), or **Long** (~630 chars).
- Toggle optional difficulty modifiers:
  - **Autocorrect** — reduces the penalty for mistypes
  - **Caffeine Mode** — increases typing speed but raises burnout risk
  - **Night Shift** — reduces accuracy slightly (simulating fatigue)

**3. Customise Each Typist**
For each typist you configure:

| Option | Choices | Effect |
|---|---|---|
| Name | Free text | Display name |
| Symbol | Emoji or character | Lane marker |
| Colour | CSS colour string | Progress bar colour |
| Typing Style | Touch Typist, Hunt-and-Peck, Phone Thumbs, Voice-to-Text | Accuracy / speed / burnout modifiers |
| Keyboard Type | Mechanical, Membrane, Touchscreen, Stenography | Accuracy / speed / burnout modifiers |
| Accessory | Wrist Support, Energy Drink, Noise-Cancelling | Stacked bonus modifiers |

**4. Watch the Race**
The live race screen shows animated progress bars. Status indicators appear for burnout and mistypes. The race ends when the first typist completes the passage.

**5. View Results**
The results screen shows final position, WPM, accuracy, burnout count, earnings, and points earned. Results are automatically saved to the Statistics and Leaderboard.

**6. Statistics & Leaderboard**
Return to the main menu to browse:
- **Statistics** — per-typist WPM history, personal bests, and race counts
- **Leaderboard** — ranked cumulative points and total earnings across all sessions

---

## Game Mechanics Reference

### Accuracy Rating

Each typist has an accuracy value between `0.0` (worst) and `1.0` (perfect). This single value determines three outcomes each turn:

| Event | Probability formula | Outcome |
|---|---|---|
| **Mistype** | `(1 − accuracy) × 0.3` | Typist slides back 2–4 characters |
| **Burnout** | `0.05 × accuracy²` | Typist is frozen for 3 turns; accuracy decreases by 0.05 |
| **Normal advance** | Remainder | Typist moves forward 1 character |

High-accuracy typists are less likely to mistype but are more susceptible to burnout. Low-accuracy typists mistype frequently but rarely burn out.

### Typing Styles (Part 2)

| Style | Accuracy Δ | Speed Δ | Burnout Δ |
|---|---|---|---|
| Touch Typist | ±0 | ×1.0 | ×1.0 |
| Hunt and Peck | −15% | ×0.8 | ×0.9 |
| Phone Thumbs | −25% | ×0.6 | ×1.2 |
| Voice to Text | −10% | ×1.3 | ×0.7 |

### Keyboard Types (Part 2)

| Keyboard | Accuracy Δ | Speed Δ | Burnout Δ |
|---|---|---|---|
| Mechanical | +5% | ×1.0 | ×1.0 |
| Membrane | ±0 | ×0.95 | ×1.0 |
| Touchscreen | −10% | ×0.9 | ×1.1 |
| Stenography | +10% | ×1.2 | ×0.8 |

### Accessories (Part 2)

| Accessory | Accuracy Δ | Speed Δ | Burnout Δ |
|---|---|---|---|
| Wrist Support | ±0 | ×1.0 | ×0.7 |
| Energy Drink | +8% | ×1.0 | ×1.0 |
| Noise-Cancelling | +5% | ×1.0 | ×1.0 |

### Reward System (Part 2)

| Placement | Base Points | Base Earnings |
|---|---|---|
| 1st place | 3 pts | £100 |
| 2nd place | 2 pts | £75 |
| 3rd place | 1 pt | £50 |

Bonus: +1 pt and +£0.50 per WPM above 50. Penalty: −1 pt and −£10 per burnout event.

---

## Class & Architecture Reference

### Part 1 Classes

**`Typist`**
Represents a single competitor. Stores name, symbol, accuracy, progress, burnout state, and mistype state. Key methods: `typeCharacter()`, `slideBack(int)`, `burnOut(int)`, `recoverFromBurnout()`, `resetToStart()`.

**`TypingRace`**
The race engine. Holds three `Typist` seats and the passage length. `startRace()` loops until a winner is found, calling `advanceTypist()` and `printRace()` each turn.

### Part 2 Classes

**`TypingRaceApp`**
JavaFX `Application` subclass. Manages scene transitions between the main menu, race config, typist config, live race display, results, statistics, and leaderboard screens.

**`TypingRaceEnhanced`**
Extended race engine that applies `TypistConfiguration` modifiers on top of the base `Typist` logic and drives the animated JavaFX display.

**`TypistConfiguration`**
Encapsulates all customisation choices (typing style, keyboard type, accessory) and exposes `calculateAccuracyModifier()`, `calculateSpeedModifier()`, and `calculateBurnoutModifier()`.

**`TypistWrapper`**
Pairs a `Typist` instance with a `TypistConfiguration` and tracks extended GUI state (burnout events, WPM timing).

**`PassageLibrary`**
Static constants for six built-in passages (two each at Short, Medium, Long difficulty). Access via `PassageLibrary.getPassage("SHORT", 0)`.

**`RaceDisplay`**
JavaFX component rendering live progress bars and status labels. Updates on a `Timeline` animation.

**`RaceResult`**
Immutable result record: typist name, position, WPM, final accuracy, burnout count, and void-race flag.

**`StatisticsManager`**
In-session store of race history per typist. Provides personal-best WPM, average WPM, average accuracy, first-place count, and burnout totals.

**`RewardSystem`**
Calculates and records points, earnings, and titles. Maintains a `Map<String, LeaderboardEntry>` of cumulative performance.

**`LeaderboardEntry`**
Tracks a single typist's cumulative points, earnings, total races, and wins across the session.

**`Sponsor`**
Provides optional bonus multipliers when a typist meets a sponsor's performance threshold.

---

## Troubleshooting

**`javac: command not found`**
Java is not on your PATH. Install a JDK (not just a JRE) from https://adoptium.net/ and ensure `JAVA_HOME` is set and `$JAVA_HOME/bin` is on `PATH`.

**`error: package javafx.application does not exist`**
The JavaFX jars are not on the compile classpath. Double-check the `-cp` path points to the correct `lib/` directory of your extracted JavaFX SDK.

**`Error: JavaFX runtime components are missing`**
You compiled successfully but forgot the `--module-path` and `--add-modules` flags at runtime. JavaFX must be declared as a module at launch — see [Running Part 2](#running-part-2).

**`java.lang.UnsupportedClassVersionError`**
The class files were compiled with a newer JDK than the one running them. Ensure the same JDK version is used for both `javac` and `java`.

**Terminal animation appears garbled (Part 1)**
Part 1 uses `\u000C` (form feed) to clear the terminal. This works best in a standard system terminal (Terminal.app, Windows Terminal, GNOME Terminal). Some IDEs display garbage characters instead — run the program from a native terminal for best results.

**`package Part1 does not exist` when compiling Part 2**
Make sure you compiled Part 1 first with `javac -d . *.java` from inside the `Part1/` directory. The `-d .` flag is required to create the `Part1/` package subdirectory that Part 2 imports.

**Race freezes immediately (Part 1)**
Ensure all three seats are filled before calling `startRace()`. Passing a `null` typist to any seat will cause a `NullPointerException` on the first turn.