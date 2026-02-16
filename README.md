# Java RPG Game Maker

<p align="center">
  <img src="assets/screenshots/configured-game-view-with-map-and-events.png" alt="Java RPG Game Maker — Editor view with map, events, and multiple maps in the sidebar" width="700">
</p>

**[Project Page](https://www.scorchsoft.com/rpg-game-maker-java)** | **[User Manual](docs/User%20Manual%20v1.pdf)** | **[Full Documentation](docs/Project%20Documentation%20v1.4.pdf)**

A desktop application (v1.8) for creating simple RPG-style games using your own images, photographs, or drawings as the in-game scenes. Rather than traditional tile-based map editors, this tool lets you upload custom background and foreground images that form the world your character walks around in — making it possible to build interesting and complex games with little or no programming knowledge.

Built with Java Swing, it runs on Windows, Mac, and Linux. A video demo is available on the [project page](https://www.scorchsoft.com/rpg-game-maker-java).

## Project Status

This is a legacy codebase originally developed as a final-year university project by Andrew Lee Ward at the University of Birmingham School of Computer Science. It hasn't been actively maintained in some time, but thanks to Java's strong backwards compatibility, **it still runs** on modern JREs. If you have a reasonably recent version of Java installed, you should be able to build and run it without issues.

## Features

- **Custom image maps** — Upload your own background and foreground images instead of working with tile-based editors. Your images become the world.
- **Event system** — Draw interactive areas onto maps that trigger events: teleportation to another map, message boxes, quiz questions, or game variable changes. Events can be triggered by walking over them, pressing an action button, or entering a map.
- **Conditional logic** — Events support conditions based on sprite variables, allowing branching storylines and puzzle mechanics.
- **Customisable perspective** — Set and preview perspective scaling per map so the player character grows and shrinks naturally as they move closer or further away.
- **Walkable area painting** — Draw directly onto each map to define exactly where the character can and cannot walk.
- **Layer system** — Each map is composed of 10 ordered layers (background, foreground, events, walkable area, perspective, sprite, dialogue, and more), giving you fine control over rendering and interaction.
- **8-directional animated sprite** — The player character supports full 8-directional movement with animation frames.
- **Integrated play mode** — Test your game directly within the editor with a built-in player that features double-buffered rendering.
- **Portable save format** — Games are saved as `.game` files (ZIP archives containing XML and images), making them easy to share with anyone who has the application.
- **Ribbon UI** — A Microsoft Office-style ribbon toolbar (via the Flamingo library) organises the editing tools into logical bands: Tools, Events, Map Tools, and Layers.

## Screenshots

| Map Editor | Event Overlay |
|:---:|:---:|
| ![Map editor view with sprite and ribbon toolbar](assets/screenshots/map-view.png) | ![Events tab showing polygon event areas drawn on the map](assets/screenshots/event-overlay-view.png) |

| Perspective Editor | Play Mode |
|:---:|:---:|
| ![Perspective editor with horizon line and scaled sprite previews](assets/screenshots/perspective-view.png) | ![Integrated play mode running the game](assets/screenshots/play-view.png) |

## Getting Started

### Prerequisites

- **Java JRE/JDK 1.6 or higher** (any modern Java version will work)
- **Apache Ant** (only needed if building from source)

### Running a Pre-built Release

A pre-built v1.8 archive (including all dependencies) is available for download from the [project page](https://www.scorchsoft.com/rpg-game-maker-java). Extract the ZIP and run:

```bash
java -jar Game_Maker.jar
```

If you've cloned this repo and have a built JAR in `dist/`:

```bash
java -jar dist/Game_Maker.jar
```

### Building from Source

This project uses Apache Ant with NetBeans project configuration:

```bash
ant jar          # Build dist/Game_Maker.jar
ant run          # Build and run the application
ant compile      # Compile only
ant clean        # Clean build artifacts
ant test         # Run tests (JUnit 4)
```

The built JAR is output to `dist/Game_Maker.jar`.

## Project Structure

```
├── src/
│   ├── gamemaker/           # Core application classes
│   │   ├── Main.java        # Entry point — launches the Ribbon window
│   │   ├── Ribbon.java      # Main window (JRibbonFrame) with toolbar bands
│   │   ├── GameMap.java     # Central class — layered map editor with state machine
│   │   ├── GameMaps.java    # Manages the collection of all maps
│   │   ├── Sprite.java      # 8-directional animated player character
│   │   ├── FileHandler.java # Save/load .game files (ZIP + XML via JDOM)
│   │   ├── Panels/          # Layer panels (Background, Foreground, Event, Walkable, etc.)
│   │   ├── events/          # Event types, triggers, and conditions
│   │   ├── Player/          # Play mode (PlayFrame, movement/repaint threads)
│   │   ├── RightPanels/     # Sidebar UI (map list, hidden panels)
│   │   ├── Exceptions/      # Custom exceptions
│   │   └── icons/           # Application icons and splash image
│   └── test/                # Test classes and resources
├── files/                   # Runtime assets
│   ├── maps/                # Default map background images
│   ├── sprite/              # Character sprite animations (8 directions)
│   ├── foregrounds/         # Foreground image storage
│   └── gamesave/            # Temporary save location
├── lib/                     # Core dependencies (JAI, JDOM, utils4j)
│   └── Flamingo/            # Ribbon UI library and its dependencies
├── docs/                    # Project documentation (PDFs)
├── reference/               # Reference examples (Flamingo)
├── nbproject/               # NetBeans project configuration
├── build.xml                # Ant build script
└── manifest.mf              # JAR manifest
```

## Dependencies

All dependencies are bundled in the `lib/` directory — no external dependency management is required.

| Library | Purpose |
|---------|---------|
| Flamingo | Microsoft Office-style ribbon toolbar UI |
| JAI (Java Advanced Imaging) | Advanced image processing and format support |
| JDOM | XML parsing and generation for the save file format |
| utils4j | General utility functions |
| Batik | SVG rendering (used by Flamingo) |
| JUnit | Unit testing framework |

## Documentation

The `docs/` folder contains the original project documentation:

- **User Manual v1.pdf** — Getting started guide for using the game maker
- **Project Documentation v1.4.pdf** — Full technical documentation (Andrew Lee Ward's dissertation)
- **Project Proposal v1.1.pdf** — Original project proposal

## Example Games

Some example `.game` files were distributed alongside the project and are available for download from the [project page](https://www.scorchsoft.com/rpg-game-maker-java). Download, unzip, and open them with the game maker to see how games are put together:

- **Final Fantasy 7-style game** — An exploration game with multiple maps and teleportation events
- **Quiz-style game** — Demonstrates the question/answer event system
- **Cave factory game** — A smaller example with walkable area and perspective usage

## Further Information

This project was the final-year university project of Andrew Lee Ward from the University of Birmingham School of Computer Science. For more details, downloads, and a video demo, visit the [Scorchsoft project page](https://www.scorchsoft.com/rpg-game-maker-java).

## License

This project is licensed under the MIT License. See [LICENSE.md](LICENSE.md) for details.
