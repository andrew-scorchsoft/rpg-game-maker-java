# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run

This project uses **Apache Ant** (with NetBeans project configuration). Java source/target is 1.5.

```bash
ant              # Full build (test, jar, javadoc)
ant compile      # Compile only
ant jar          # Build dist/Game_Maker.jar
ant clean        # Clean build artifacts
ant test         # Run tests (JUnit 4)
ant run          # Run the application
```

The built JAR is output to `dist/Game_Maker.jar`. Libraries are in `lib/` (core) and `lib/Flamingo/` (ribbon UI).

## Architecture

**Desktop Swing application** — an RPG game map editor with an integrated player/test mode. Uses the Flamingo library for a Microsoft Office-style ribbon UI.

### Core Components

- **`Main`** — Entry point. Launches `Ribbon`.
- **`Ribbon`** — Main application window (`JRibbonFrame`). Hosts the ribbon toolbar (Tools, Events, Map Tools, Layers bands) and implements `MapsContainer`.
- **`GameMaps`** — Manages the collection of all `GameMap` instances.
- **`GameMap`** — Central class. Extends `JLayeredPane` with a 10-layer stack and a task-based state machine controlling editor modes (idle, edit walkable, add event, play mode, etc.).
- **`FileHandler`** — Persistence facade. Saves/loads `.game` files (ZIP archives containing XML + images). Uses JDOM for XML.

### Layer System (GameMap)

Maps use 10 ordered layers, each backed by a panel in `gamemaker.Panels`:

1. Bottom → 2. Background (`BackgroundPanel`) → 3. Hide → 4. Events (`EventPanel`) → 5. Sprite (`SpritePanel`) → 6. Foreground (`ForegroundPanel`) → 7. Walkable (`WalkableAreaPanel`) → 8. Perspective (`PerspectivePanel`) → 9. Dialogue (`DialoguePanel`) → 10. Glass (`GlassPanel`)

All panels that need XML persistence implement the `XMLable` interface.

### Event System (`gamemaker.events`)

- **`GameEvent`** — Abstract base. Subclasses: `soutEvent` (messages), `teleportEvent` (map transitions), `VariableChangeEvent` (game state).
- **Triggers:** `TRIGGER_WALKOVER`, `TRIGGER_ACTIONBUTTON`, `TRIGGER_ENTERMAP`.
- **Shapes:** `SQUARE`, `CIRCLE`, `AREA` (polygon).
- Events support conditional execution via `EventCondition` based on sprite variables.

### Sprite System

- **`Sprite`** — 8-directional animated player character with perspective-based scaling. Holds 10 game variables for state tracking.
- **`Player/`** — `PlayFrame` (game window), `MovementThread` (input handling), `RepaintThread` (render loop). Double-buffered rendering via `PaintPanel`.

### Save Format

XML inside a ZIP archive (`.game` extension). Structure:
```xml
<game>
  <sprite><location mapid="" xaxis="" yaxis=""/><appearance ref=""/><variables>...</variables></sprite>
  <gamemap id=""><perspective/><foreground/><background/><walkover/><events/></gamemap>
</game>
```

## Key Packages

| Package | Purpose |
|---------|---------|
| `gamemaker` | Core classes (Main, Ribbon, GameMap, Sprite, FileHandler) |
| `gamemaker.Panels` | Layer panels (Background, Foreground, Event, Walkable, etc.) |
| `gamemaker.events` | Event types, triggers, and conditions |
| `gamemaker.Player` | Play mode (PlayFrame, movement/repaint threads) |
| `gamemaker.RightPanels` | Sidebar UI (map list, hidden panels) |
| `gamemaker.Exceptions` | Custom exceptions |

## Resources

- `files/maps/` — Default map background images
- `files/sprite/` — Character sprite animations (8 directions)
- `files/gamesave/` — Temporary save location
- `files/icons/` and `src/gamemaker/icons/` — Application icons
