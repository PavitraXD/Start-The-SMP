# Changelog

## [2.0.0] - 2025-12-30

### 🎉 Major Features Added

#### Epic SMP Launch Experience
- **Firework Celebrations**: Random colorful fireworks when SMP starts
- **Rainbow Particle Effects**: Beautiful particles around players during countdown
- **Multi-layered Sound Effects**: Orchestral hits, thunder, and celebration sounds
- **Professional Animations**: Smooth transitions and cinematic timing

#### Two-Phase SMP Management
- **Starting Phase**: Peaceful preparation environment
  - Small configurable world border
  - PvP disabled
  - Block breaking protection
  - Mob spawning control
- **Started Phase**: Full gameplay mode
  - Large world border expansion
  - PvP enabled after optional countdown

#### Advanced PvP Control
- **Configurable PvP Delay**: Set countdown before PvP enables
- **Visual Notifications**: Action bar countdown during PvP delay
- **Phase-Based Management**: Automatic PvP control per phase

#### Enhanced World Border Control
- **Dual Border Sizes**: Separate sizes for starting and started phases
- **Particle Rings**: Red glowing particles around world borders
- **Smooth Expansion**: Configurable expansion duration
- **Easy Centering**: Set border center to player location

#### Protection & Security
- **Building Protection**: Prevent block breaking in starting phase
- **Mob Control**: Disable natural spawning per phase
- **Single-Use Protection**: Prevents multiple SMP starts

### 🔧 New Commands

- `/smpstart stop` - Stop and reset the SMP to initial state
- `/smpstart reset` - Same as stop - reset SMP completely
- `/smpstart reload` - Reload configuration without server restart
- `/smpstart help` - Show all available commands

### ⚙️ Configuration Enhancements

- **Phase-Specific Settings**: Different configs for each phase
- **Hot-Reloadable Config**: Change settings without server restart
- **Custom Messages**: Personalized titles and notifications
- **Audio Customization**: Volume, pitch, and sound controls

### 🛠️ Technical Improvements

- **No GUI Dependency**: Streamlined for server administration
- **Optimized Performance**: Efficient particle and task management
- **Better Error Handling**: Improved logging and error messages
- **Code Refactoring**: Cleaner, more maintainable codebase

### 📦 Build & Deployment

- **Version 2.0.0**: Major version bump for significant feature additions
- **Modrinth Ready**: Professional description and formatting
- **Production Ready**: Tested and optimized for server environments

---

**Previous version: 1.0.0** - Basic SMP countdown and border expansion
