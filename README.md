# Rocket Escape

A fast-paced arcade game where you navigate a rocket through obstacles to escape!

## Features

- **Combo System**: Chain together successful maneuvers to earn bonus points
- **Score Multiplier**: Build combos to increase your score multiplier
- **Combo Expiration**: Watch out for combo timers with visual flash warnings
- **Obstacle Course**: Navigate through challenging obstacles
- **Lives System**: 3 lives with invulnerability after hits
- **Progressive Difficulty**: Game gets harder as you score more points
- **Visual Effects**: Particle trails, screen shake, and flashing indicators

## Technology Stack

- **LibGDX** - Cross-platform game development framework
- **Java** - Primary programming language
- **Gradle** - Build automation tool

## Installation

### Prerequisites

- Java JDK 8 or higher
- Git

### Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/finettt/Rocket-Escape.git
   ```

2. Navigate to the project directory:
   ```bash
   cd Rocket-Escape
   ```

3. Run the game using Gradle:
   ```bash
   ./gradlew desktop:run
   ```
   (On Windows, use `gradlew.bat desktop:run`)

## Running the Game

### Desktop Version

To run the game on desktop:
```bash
./gradlew desktop:run
```

To build the desktop version:
```bash
./gradlew desktop:dist
```

### Android Version

To build the Android APK:
```bash
./gradlew android:assembleDebug
```

The APK will be generated in `android/build/outputs/apk/debug/`

### Web Version

To build the HTML5 version:
```bash
./gradlew html:dist
```

## Controls

- **Touch/Click**: Launch and accelerate the rocket
- **On-screen tap**: Tap anywhere on the screen to control the rocket

## Gameplay

- Navigate your rocket through the obstacle course
- Complete maneuvers to build combos
- Watch for combo expiration warnings (flashing effect)
- Aim for the highest score with maximum combo multipliers!
- Use your 3 lives wisely - you get brief invulnerability after hits
- The game gets progressively harder as you score more points

## Code Structure

- `core/` - Main game logic (shared across all platforms)
  - `src/main/java/io/finett/rocketescape/` - Game source code
    - `Main.java` - Main game entry point
    - `FirstScreen.java` - Main game screen with all gameplay logic

- `desktop/` - Desktop platform implementation
  - `src/main/java/io/finett/rocketescape/desktop/` - Desktop launcher

- `android/` - Android platform implementation
  - `src/main/java/io/finett/rocketescape/android/` - Android launcher

- `lwjgl3/` - LWJGL3 desktop backend
  - `src/main/java/io/finett/rocketescape/lwjgl3/` - LWJGL3 launcher

- `assets/` - Game resources
  - `space-bg.png` - Background image
  - `rocket.png` - Rocket sprite
  - `spike_1.png` - `spike_5.png` - Obstacle sprites
  - `PressStart2P-Regular.ttf` - Pixel font
  - `particles/` - Particle effect configurations

## Recent Updates

- Added lives system with 3 lives and invulnerability after hits
- Implemented progressive difficulty - game gets harder as you score more
- Added variable obstacle spacing for more dynamic gameplay
- Improved score timing and combo calculations
- Enhanced visual feedback with screen shake and particle effects

## License

MIT License - see LICENSE file for details.
