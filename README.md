# 🚀 Rocket Escape

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java 8+](https://img.shields.io/badge/Java-8+-blue.svg)](https://www.oracle.com/java/)
[![LibGDX](https://img.shields.io/badge/LibGDX-1.14.0-green.svg)](https://libgdx.com/)
[![Platforms](https://img.shields.io/badge/Platforms-Desktop%20%7C%20Android%20%7C%20Web-lightgrey.svg)](https://libgdx.com/overview.html)

A fast-paced arcade game where you navigate a rocket through obstacles to escape!

---

## 🎮 Features

✨ **Combo System** - Chain successful maneuvers for bonus points and score multipliers
🔥 **Lives System** - 3 lives with invulnerability after hits
📈 **Progressive Difficulty** - Game gets harder as you score more points
🎯 **Score Multiplier** - Build longer combos to increase your multiplier
⚡ **Visual Effects** - Particle trails, screen shake, and flashing indicators
🏆 **High Scores** - Compete against your best performance

---

## 🛠️ Technology Stack

- **[LibGDX](https://libgdx.com/)** - Cross-platform game development framework
- **Java 8+** - Primary programming language
- **Gradle** - Build automation
- **LWJGL3** - Desktop backend
- **Particle Effects** - Smooth visual feedback

---

## 🚀 Getting Started

### Prerequisites

- Java JDK 8 or higher
- Git

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/finettt/Rocket-Escape.git
   ```

2. **Navigate to the project directory:**
   ```bash
   cd Rocket-Escape
   ```

3. **Run the game:**
   ```bash
   ./gradlew desktop:run
   ```
   *(On Windows, use `gradlew.bat desktop:run`)*

---

## 🎯 Game Controls

🖱️ **Touch/Click** - Launch and accelerate the rocket
📱 **On-screen tap** - Tap anywhere to control the rocket

---

## 📖 Gameplay Guide

1. **Navigate your rocket** through the obstacle course
2. **Complete maneuvers** to build combos
3. **Watch for combo timers** - flashing effect warns when combo is expiring
4. **Aim for high scores** with maximum combo multipliers
5. **Use your 3 lives wisely** - you get brief invulnerability after hits
6. **Survive longer** - the game gets progressively harder as you score more

---

## 📁 Project Structure

```
RocketEscape/
├── core/              # Main game logic (shared across platforms)
│   └── src/main/java/io/finett/rocketescape/
│       ├── Main.java          # Game entry point & state management
│       ├── FirstScreen.java   # Main gameplay screen
│       ├── MainMenuScreen.java # Menu system
│       └── SettingsScreen.java # Settings menu
├── desktop/           # Desktop platform implementation
├── android/           # Android platform implementation
├── lwjgl3/            # LWJGL3 desktop backend
├── assets/            # Game resources
│   ├── space-bg.png     # Background
│   ├── rocket.png       # Rocket sprite
│   ├── spike_1-5.png    # Obstacle sprites
│   ├── PressStart2P-Regular.ttf # Pixel font
│   └── particles/       # Particle effects
└── README.md
```

---

## 🎮 Running the Game

### Desktop Version

```bash
# Run the game
./gradlew desktop:run

# Build distributable
./gradlew desktop:dist
```

### Android Version

```bash
# Build debug APK
./gradlew android:assembleDebug

# APK location: android/build/outputs/apk/debug/
```

### Web Version

```bash
# Build HTML5 version
./gradlew html:dist
```

---

## 💡 Recent Updates

🆕 **New Features:**
- Lives system with 3 lives and invulnerability after hits
- Progressive difficulty - game gets harder as you score more
- Variable obstacle spacing for dynamic gameplay
- Enhanced visual feedback with screen shake and particle effects

---

## 📜 License

MIT License - see [LICENSE](LICENSE) file for details.

---

## 🔧 Contributing

Pull requests are welcome! For major changes, please open an issue first to discuss what you would like to change.

---

**🚀 Have fun escaping! 🚀**