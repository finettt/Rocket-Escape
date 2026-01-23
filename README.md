<div align="center">
   <img width="256" height="256" alt="Rocket Escape Logo" src="https://github.com/user-attachments/assets/f865895a-5b88-4e1e-b034-2af9cfa4f1da" />
   
   # 🚀 Rocket Escape
   
   **A fast-paced arcade game where you navigate a rocket through obstacles to escape!**

   [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
   [![Java 8+](https://img.shields.io/badge/Java-8+-blue.svg)](https://www.oracle.com/java/)
   [![LibGDX](https://img.shields.io/badge/LibGDX-1.14.0-green.svg)](https://libgdx.com/)
   [![Platforms](https://img.shields.io/badge/Platforms-Desktop%20%7C%20Android%20%7C%20Web-lightgrey.svg)](https://libgdx.com/overview.html)

   [Features](#-features) • [Getting Started](#-getting-started) • [Controls](#-game-controls) • [Contributing](#-contributing)

</div>

---

## 📋 Table of Contents

- [Features](#-features)
- [Technology Stack](#-technology-stack)
- [Getting Started](#-getting-started)
- [Game Controls](#-game-controls)
- [Gameplay Guide](#-gameplay-guide)
- [Game Modes](#-game-modes)
- [Achievements](#-achievements)
- [Project Structure](#-project-structure)
- [Building & Running](#-building--running)
- [Contributing](#-contributing)
- [License](#-license)

---

## ✨ Features

| Feature | Description |
|---------|-------------|
| 🔗 **Combo System** | Chain successful maneuvers for bonus points and score multipliers |
| ❤️ **Lives System** | Multiple lives with invulnerability period after hits |
| 📈 **Progressive Difficulty** | Game intensity increases as your score grows |
| 🎯 **Score Multiplier** | Build longer combos to maximize your score |
| ⚡ **Visual Effects** | Particle trails, screen shake, and flashing indicators |
| 🏆 **High Scores** | Track and compete against your best performance |
| 🎵 **Dynamic Obstacles** | Variable spacing for unpredictable gameplay |
| 🎮 **Game Modes** | Choose from Classic, Hardcore, Zen, or Time Attack modes |
| 🏅 **Achievements** | Unlock achievements for completing various challenges |

---

## 🛠️ Technology Stack

| Technology | Purpose |
|------------|---------|
| **[LibGDX](https://libgdx.com/)** | Cross-platform game development framework |
| **Java 8+** | Primary programming language |
| **Gradle** | Build automation & dependency management |
| **LWJGL3** | Desktop backend renderer |

---

## 🚀 Getting Started

### Prerequisites

- ☕ Java JDK 8 or higher
- 📦 Git

### Quick Installation

```bash
# 1. Clone the repository
git clone https://github.com/finettt/Rocket-Escape.git

# 2. Navigate to project directory
cd Rocket-Escape

# 3. Run the game
./gradlew desktop:run
```

> **Windows Users:** Use `gradlew.bat desktop:run` instead

---

## 🎮 Game Controls

| Platform | Control | Action |
|----------|---------|--------|
| 🖥️ Desktop | Mouse Click | Launch and accelerate rocket |
| 📱 Mobile | Touch/Tap | Control rocket movement |

---

## 📖 Gameplay Guide

```
┌─────────────────────────────────────────────────────┐
│  1. 🚀 Navigate your rocket through obstacles       │
│  2. ⭐ Complete maneuvers to build combos           │
│  3. ⏱️  Watch combo timers - flashing warns expiry  │
│  4. 🎯 Maximize combo multipliers for high scores   │
│  5. ❤️ Use your lives wisely - invulnerability on hit  │
│  6. 📈 Survive longer - difficulty increases!       │
└─────────────────────────────────────────────────────┘
```

### Tips & Tricks

- 💡 **Timing is key** - tap at the right moment to avoid obstacles
- 🔥 **Maintain combos** - don't let the combo timer run out
- 🛡️ **Use invulnerability** - after a hit, you have a brief window of safety

---

## 🎮 Game Modes

| Mode | Description | Starting Difficulty | Lives |
|------|-------------|---------------------|-------|
| 🌟 Classic | Standard gameplay with multiple lives | 1.0x | 3 |
| 💀 Hardcore | One life, faster start, 1.5x score | 1.5x | 1 |
| 🧘 Zen Mode | No death, infinite flight | 1.0x | 999 |
| ⏱️ Time Attack | 60 seconds to score high | 1.3x | 3 |

Each game mode offers a unique challenge and experience. Choose the one that fits your playstyle!

---

## 🏅 Achievements

Unlock achievements by completing various challenges:

| Achievement | Description | Requirement |
|-------------|-------------|-------------|
| 🚀 First Flight | Play your first game | Play 1 game |
| 🔗 Combo Master | Reach 10x combo | 10x combo |
| 🔗 Combo Legend | Reach 25x combo | 25x combo |
| 🏆 Survivor | Score 50 points | 50 points |
| 🏆 Century | Score 100 points | 100 points |
| 🏆 Double Century | Score 200 points | 200 points |
| ✅ Perfect Flight | Score 30 without taking damage | 30 points without damage |
| ⚡ Speed Demon | Survive 2.0x difficulty | 2.0x difficulty |
| 🎁 Powerup Collector | Collect 10 power-ups | 10 power-ups |
| 💪 Hardcore Hero | Score 50 in one life | 50 points in Hardcore mode |

---

## 📁 Project Structure

```
RocketEscape/
├── 📂 core/                    # Shared game logic
│   └── src/main/java/io/finett/rocketescape/
│       ├── Main.java           # Entry point & state management
│       ├── FirstScreen.java    # Main gameplay screen
│       ├── MainMenuScreen.java # Menu system
│       ├── SettingsScreen.java # Settings menu
│       ├── GameModeSelectScreen.java # Game mode selection
│       ├── AchievementsScreen.java # Achievements display
│       ├── PauseMenuScreen.java # Pause menu
│       ├── Achievement.java    # Achievement definitions
│       ├── GameMode.java       # Game mode definitions
│       └── PowerUpType.java    # Power-up types
│
├── 📂 desktop/                 # Desktop implementation
├── 📂 android/                 # Android implementation
├── 📂 lwjgl3/                  # LWJGL3 backend
│
├── 📂 assets/                  # Game resources
│   ├── 🖼️ space-bg.png        # Background image
│   ├── 🚀 rocket.png          # Rocket sprite
│   ├── ⚠️ spike_1-5.png       # Obstacle sprites
│   ├── 🔤 PressStart2P-Regular.ttf
│   └── ✨ particles/           # Particle effects
│
└── 📄 README.md
```

---

## 🔨 Building & Running

### Desktop

```bash
# Run directly
./gradlew desktop:run

# Create distributable JAR
./gradlew desktop:dist
# Output: desktop/build/libs/
```

### Android

```bash
# Build debug APK
./gradlew android:assembleDebug
# Output: android/build/outputs/apk/debug/

# Build release APK
./gradlew android:assembleRelease
```

### Web (HTML5)

```bash
# Build web version
./gradlew html:dist
# Output: html/build/dist/
```

---

## 🤝 Contributing

Contributions are welcome! Here's how you can help:

1. 🍴 **Fork** the repository
2. 🌿 **Create** a feature branch (`git checkout -b feature/AmazingFeature`)
3. 💾 **Commit** your changes (`git commit -m 'Add AmazingFeature'`)
4. 📤 **Push** to the branch (`git push origin feature/AmazingFeature`)
5. 🔃 **Open** a Pull Request

> For major changes, please open an issue first to discuss your ideas.

---

## 📜 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

---

<div align="center">

### 🌟 Star this repo if you enjoyed the game! 🌟

**🚀 Have fun escaping! 🚀**

Made with ❤️ using LibGDX

</div>