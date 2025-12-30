# Start The SMP Plugin

A production-ready Minecraft Paper 1.21.X plugin for officially starting SMP servers with epic effects, phase management, and complete server control.

## Features

### 🎆 Epic Launch Experience
- **Firework celebrations**: Random colorful fireworks when SMP starts
- **Rainbow particle effects**: Beautiful particles around players during countdown
- **Multi-layered sound effects**: Orchestral hits, thunder, and celebration sounds
- **Professional animations**: Smooth transitions and cinematic timing



### 🏁 Two-Phase System
- **Starting Phase**: Peaceful environment for preparation
  - Small world border (configurable)
  - PvP disabled
  - Block breaking protection
  - Mob spawning control
  - Configurable difficulty and gamemode
- **Started Phase**: Full SMP gameplay
  - Large world border (configurable)
  - PvP enabled after optional countdown
  - Normal gameplay unlocked

### ⚔️ Advanced PvP Control
- **Configurable PvP delay**: Optional countdown before PvP enables
- **Visual countdown**: Action bar notifications during PvP countdown
- **Phase-based PvP**: Automatic PvP management per phase

### 🌍 World Border Management
- **Dual border sizes**: Separate sizes for starting and started phases
- **Automatic centering**: Set center to player location or spawn
- **Glowing particles**: Red particle ring around world border in started phase
- **Smooth expansion**: Configurable expansion duration

### 🎵 Immersive Audio Experience
- **Layered sound system**: 4-layer sound experience for SMP start
- **Epic orchestral hits**: Goat horn sounds for dramatic effect
- **Thunder effects**: Lightning and thunder sounds
- **Celebration finale**: Multiple celebration sounds

### 🛡️ Protection Systems
- **Block breaking control**: Prevent building in starting phase
- **Mob spawning control**: Disable natural mob spawning in starting phase
- **Single-use protection**: Prevents multiple SMP starts
- **Permission-based access**: Admin-only commands

### ⚙️ Fully Configurable
- **Hot-reloadable config**: All settings change without restart
- **Phase-specific settings**: Different configs for each phase
- **Color customization**: Full color code support
- **Timing controls**: Precise control over all animations and effects

## Installation

1. Download the plugin JAR file
2. Place it in your server's `plugins/` folder
3. Start/restart your server
4. Configure the plugin in `plugins/SMPStart/config.yml`
5. Reload with `/reload` or restart the server

## Commands

| Command | Permission | Description |
|---------|------------|-------------|
| `/smpstart` | `smpstart.admin` | Start the SMP with epic effects and countdown |
| `/smpstart center` | `smpstart.admin` | Set world border center to your location |
| `/smpstart starting` | `smpstart.admin` | Switch to Starting phase (peaceful) |
| `/smpstart starting border <size>` | `smpstart.admin` | Set border size for Starting phase |
| `/smpstart start border <size>` | `smpstart.admin` | Set border size for Started phase and start SMP |
| `/smpstart stop` | `smpstart.admin` | Stop and reset the SMP to initial state |
| `/smpstart reset` | `smpstart.admin` | Same as stop - reset SMP completely |
| `/smpstart reload` | `smpstart.admin` | Reload configuration without server restart |
| `/smpstart help` | `smpstart.admin` | Show all available commands and usage |

## Permissions

| Permission | Default | Description |
|------------|---------|-------------|
| `smpstart.admin` | op | Allows all SMP start and management commands |

## Configuration

The plugin is highly configurable with hot-reload support. All settings can be changed without restarting the server.

### Phase Settings
```yaml
phases:
  starting:
    border-size: 50.0          # World border size in starting phase
    difficulty: "PEACEFUL"     # Difficulty level (PEACEFUL, EASY, NORMAL, HARD)
    gamemode: "SURVIVAL"       # Gamemode (SURVIVAL, CREATIVE, ADVENTURE, SPECTATOR, NONE)
    allow-block-breaking: false # Allow players to break blocks
    allow-mob-spawning: false   # Allow natural mob spawning
  started:
    border-size: 5000.0        # World border size in started phase
    difficulty: "NORMAL"       # Difficulty level for full gameplay
    gamemode: "SURVIVAL"       # Gamemode for full gameplay
```

### Effects Settings
```yaml
effects:
  fireworks:
    enabled: true
    count: 10                 # Number of fireworks to launch
  rainbow-particles:
    enabled: true             # Rainbow particles around players during countdown
  border-particles:
    enabled: true             # Red particle ring around world border
  multi-layered-sound:
    enabled: true             # 4-layer sound experience
```

### PvP Control
```yaml
pvp:
  countdown-enabled: true     # Enable PvP countdown after SMP start
  countdown-duration: 30      # Seconds to wait before enabling PvP
```

### Countdown & Display
```yaml
countdown:
  duration: 10               # Countdown duration in seconds
  use-actionbar: false       # Use action bar instead of titles for countdown

titles:
  start-title:
    title: "§c§lSMP IS STARTING"
    subtitle: "§7Get ready for the adventure!"
    fade-in: 10
    stay: 40
    fade-out: 10
  countdown-title:
    title: "§e§l{time}"
    subtitle: "§7seconds remaining..."
  started-title:
    title: "§a§lSMP STARTED"
    subtitle: "§7Good luck, have fun!"
```

### World Border Management
```yaml
world-border:
  initial-size: 50.0         # Starting border size (legacy, use phases.starting.border-size)
  final-size: 5000.0         # Final border size (legacy, use phases.started.border-size)
  expansion-duration: 300    # Expansion time in seconds
  world-name: ""             # Target world (empty = main world)
```

### Audio Experience
```yaml
sounds:
  enabled: true
  sound: "ENTITY_WITHER_SPAWN"
  volume: 0.8
  pitch: 0.8
```

### Protection & Security
```yaml
plugin:
  single-use: true           # Prevent multiple SMP starts
  debug-logging: false       # Enable detailed console logging
```

## Usage Example

1. Admin runs `/smpstart`
2. All players see "SMP IS STARTING" title
3. Countdown begins (10 seconds by default)
4. World border is set to small size (50 blocks by default)
5. At countdown end: "SMP STARTED" title appears
6. Optional sound plays globally
7. World border smoothly expands to final size over 5 minutes

## Building from Source

Requirements:
- Java 21+
- Maven 3.6+

```bash
git clone https://github.com/yourusername/start-the-smp.git
cd start-the-smp
mvn clean package
```

The compiled JAR will be in the `target/` folder.

## Contributing

We welcome contributions! Please see [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines.

## Support

For issues, feature requests, or questions:
- Create an issue on [GitHub](https://github.com/yourusername/start-the-smp/issues)
- Join our community discussions

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
