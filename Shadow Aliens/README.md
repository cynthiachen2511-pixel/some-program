# Shadow Aliens
A 2D vertical-scrolling shooter built in Java using the BAGEL game engine, inspired by classic arcade shmups like Space Invaders and Galaxian.

# About
Shadow Aliens is a wave-based space shooter where the player pilots a ship at the bottom of the screen, dodging and destroying waves of enemies while collecting powerups. The project was originally developed as a university assignment and demonstrates object-oriented design principles including inheritance, polymorphism, and encapsulation.

## Tech Stack
- Language: Java 17
- Game Engine: [BAGEL](https://github.com/eleanor-em/bagel) (2D graphics library)
- Build Tool: Maven
- IDE: IntelliJ IDEA

## Features
- Multiple enemy types — regular, strafing (bouncing movement), and shooting enemies
- Powerup system — shield, life, cooldown reduction, and speed boost powerups
- Wave-based progression — enemies and powerups are configured per-wave via a data file
- Full screen flow*— start screen, battle screen, pause screen, and win/lose end screens
- Dynamic data loading — all game parameters (positions, speeds, scores, text) are loaded from an external `.properties` file at runtime, with no hardcoded values
- Dev-mode controls — speed up/down, invincibility toggle, restart, and skip-wave for testing

## Running Instructions

**Requirements:** Java 17 must be installed and configured as the Project SDK.

1. Clone the repository
2. Open the project in **IntelliJ IDEA** as a Maven project
3. Go to **File → Project Structure → Project** and set the **SDK** and **Language Level** to **Java 17**
4. Ensure a `gameData.properties` file exists in the project root (or set the `-DgameData` JVM argument to point to one)
5. Run `ShadowAliens.java` using the pre-configured run configuration

Alternatively, from the command line (with Java 17 on the system path):
```bash
java -DgameData=gameData.properties -cp "target/classes;bagel-swen20003-2.0.0.jar" game.ShadowAliens
```

## Controls

| Key | Action |
|---|---|
| A / D | Move left / right |
| Space | Shoot |
| Esc | Pause / Unpause |
| G / F | Increase / decrease game speed |
| I | Toggle invincibility (dev mode) |
| R | Restart game |
| N | Skip to next wave (dev mode) |

---
*This project was built as a university assignment for an Object-Oriented Software Engineering course. Game assets and the BAGEL engine are used under course-provided licensing.*
