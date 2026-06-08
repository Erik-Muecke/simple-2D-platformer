# Simple 2D Platformer - Project Documentation

## 1. Introduction

### Project Description

`Simple 2D Platformer` is a Java-based 2D platform game created as a school project. The game belongs to the platformer genre. The player controls a character in a side-view world, moves through tile-based levels, jumps across platforms, collects keys, opens doors, fights enemies, shoots projectiles, and reaches flags to progress to the next map.

The game is built without a professional game engine. Instead, the project uses Java Swing and Java AWT to create the game window, draw graphics, read keyboard input, and update the game manually through a custom game loop. This makes the project useful for learning how games work internally.

### Objectives

The main objective of the project is to create a playable 2D platformer while learning important programming and software design concepts.

Technical learning goals:

- Create a graphical Java application with Swing.
- Build and manage a custom game loop.
- Render game objects with `Graphics2D`.
- Load images and text files as resources.
- Implement keyboard input.
- Build a tile-based map system.
- Implement gravity, jumping, movement, and collision detection.
- Use object-oriented programming with packages, classes, inheritance, and composition.
- Structure a larger Java project into separate responsibilities.
- Work with reusable systems such as movement, collision, UI, camera, and map loading.

Gameplay goals:

- The player should be able to move, jump, attack, and shoot.
- The world should scroll with a camera.
- The player should interact with keys, doors, flags, and enemies.
- The game should include win and lose conditions.
- The game should include a title screen, pause screen, and game-over screen.

### Framework Conditions

| Item | Description |
| --- | --- |
| School year | `TODO: Insert school year` |
| Course | `TODO: Insert course name` |
| Teacher | `TODO: Insert teacher name if required` |
| Submission date | `TODO: Insert submission date` |
| Team size | `TODO: Insert team size` |
| Team members | `TODO: Insert names and roles` |
| Project type | Java Swing 2D platformer |

### Technologies

| Use in the project | Technology |
| --- | --- |
| Main programming language | Java |
| Window, panel, keyboard listener, and GUI base | Java Swing |
| Drawing, images, colors, fonts, rectangles, input events | Java AWT |
| Rendering tiles, player, enemies, UI, objects, and backgrounds | `Graphics2D` |
| Loading PNG image resources | `ImageIO` |
| Storing tile maps | Text files |
| IDE used for development | IntelliJ IDEA |
| Version control, if used through the `.git` repository | Git |

Java version: `TODO: Insert Java version used in your IDE, for example Java 17 or Java 21`.

No external game library is used. The game logic is implemented directly in Java.

## 2. Planning

### Game Concept

The game is a side-scrolling platformer. The player starts on a map at a spawn point. The player can move left and right, jump, collect keys, open doors, attack enemies, shoot fireballs, and reach the flag at the end of the level.

Basic gameplay flow:

1. The game starts on the title screen.
2. The player selects `NEW GAME`.
3. The first map is loaded.
4. The player moves through the level.
5. The player collects keys and uses them to open doors.
6. The player avoids or fights enemies.
7. The player reaches the flag.
8. If another map exists, the next map is loaded.
9. If no next map exists, the game displays a win message.
10. If the player's life reaches 0, the game-over screen appears.

Game rules:

- The player can move with `A` and `D`.
- The player can jump with `Space`.
- The player can attack with `Enter`.
- The player can shoot with `F`.
- The player can pause with `P`.
- Solid tiles and solid objects block movement.
- Keys can be collected.
- Doors require one key.
- Monsters damage the player on contact.
- Fire slimes can shoot projectiles.
- The player loses when life reaches 0.
- The player progresses by touching flags.

Win condition:

- The player reaches a flag and there is no further map to load, or the final map is completed.

Lose condition:

- The player's life reaches 0.

### Requirements Analysis

#### Functional Requirements

| ID | Requirement | Implemented |
| --- | --- | --- |
| F1 | The game must open in a window. | Yes |
| F2 | The game must have a title screen. | Yes |
| F3 | The player must be controllable with the keyboard. | Yes |
| F4 | The player must be able to move left and right. | Yes |
| F5 | The player must be able to jump. | Yes |
| F6 | The world must be built from tiles. | Yes |
| F7 | Maps must be loaded from text files. | Yes |
| F8 | Some tiles must block movement. | Yes |
| F9 | The player must collide with tiles and objects. | Yes |
| F10 | The camera must follow the player. | Yes |
| F11 | The game must include collectible keys. | Yes |
| F12 | The game must include doors that require keys. | Yes |
| F13 | The game must include enemies. | Yes |
| F14 | The player must be able to damage enemies. | Yes |
| F15 | The player must be able to shoot a projectile. | Yes |
| F16 | The game must include a pause function. | Yes |
| F17 | The game must include a game-over screen. | Yes |
| F18 | The game should support multiple maps. | Partly/Yes |
| F19 | The game should display player health. | Yes |
| F20 | The game should display short messages. | Yes |

#### Non-Functional Requirements

| ID | Requirement | Explanation |
| --- | --- | --- |
| NF1 | Performance | The game should run at about 60 FPS. |
| NF2 | Usability | Controls should be simple and easy to understand. |
| NF3 | Maintainability | Code should be split into clear classes and packages. |
| NF4 | Extensibility | New maps, objects, enemies, and tiles should be addable. |
| NF5 | Reliability | Missing resources should not immediately crash the entire game where fallback behavior exists. |
| NF6 | Readability | Class names and package structure should make responsibilities understandable. |

### Task Distribution

`TODO: Fill in if this was group work.`

Example:

| Team member | Responsibility |
| --- | --- |
| Member 1 | Game loop, player movement, collision |
| Member 2 | Maps, tiles, level design |
| Member 3 | UI, assets, documentation |
| Member 4 | Enemies, projectiles, testing |

If this was an individual project:

| Team member | Responsibility |
| --- | --- |
| `TODO: Your name` | Complete implementation, testing, assets, documentation |

### Schedule

`TODO: Adjust dates to match the real project timeline.`

| Milestone | Planned date | Status |
| --- | --- | --- |
| Project idea and concept | `TODO` | Done |
| Basic Java Swing window | `TODO` | Done |
| Game loop | `TODO` | Done |
| Player movement | `TODO` | Done |
| Tile map loading | `TODO` | Done |
| Collision detection | `TODO` | Done |
| Objects and interaction | `TODO` | Done |
| Enemies | `TODO` | Done |
| Projectiles | `TODO` | Done |
| UI screens | `TODO` | Done |
| Testing and bug fixing | `TODO` | In progress / Done |
| Documentation | `TODO` | Done |
| Final submission | `TODO` | TODO |

### Class Diagram / UML

The following diagram shows the main class relationships in simplified form.

```text
Main
 └── Game
      └── GamePanel
           ├── KeyHandler
           ├── TileManager
           │    └── Tile[]
           ├── CollisionSystem
           ├── Camera
           ├── BackgroundManager
           ├── AssetSetter
           ├── EventHandler
           ├── UI
           ├── Player
           │    ├── Entity
           │    └── PT_Fireball
           │         └── Projectile
           │              └── Entity
           ├── SuperObject[]
           │    ├── OBJ_Key
           │    ├── OBJ_Door
           │    ├── OBJ_Chest
           │    └── OBJ_Flag
           └── Entity[] monsters
                ├── GreenSlime
                └── FireSlime
```

Inheritance overview:

```text
Entity
├── Player
├── Projectile
│   └── PT_Fireball
├── GreenSlime
└── FireSlime

SuperObject
├── OBJ_Key
├── OBJ_Door
├── OBJ_Chest
└── OBJ_Flag
```

### Mockups / Sketches

The final game uses a simple Swing-rendered interface.

Title screen mockup:

```text
+------------------------------------------------+
|                                                |
|              ADVENTURE AWAITS                  |
|                                                |
|                   [Player]                     |
|                                                |
|                 > NEW GAME                     |
|                   LOAD GAME                    |
|                   QUIT                         |
|                                                |
+------------------------------------------------+
```

Gameplay screen mockup:

```text
+------------------------------------------------+
| Hearts                                         |
|                                                |
|        Background / sky layers                 |
|                                                |
|    Player      Slime        Key      Door      |
|  _________   _________              ______     |
| | tiles   | | tiles   |            |tiles |    |
+------------------------------------------------+
```

Game-over mockup:

```text
+------------------------------------------------+
|                                                |
|                  GAME OVER                     |
|                                                |
|                   > RETRY                      |
|                     QUIT                       |
|                                                |
+------------------------------------------------+
```

## 3. Expected Outcomes / Assessment Criteria

### Minimum Goal (Must-Have)

The minimum goal is a playable basic platformer.

Must-have features:

- A working game window.
- A controllable player.
- Basic left/right movement.
- Jumping and gravity.
- A tile-based level.
- Collision with solid tiles.
- A visible player sprite.
- A simple goal or endpoint.
- Basic game documentation.

### Extended Goal (Should-Have)

Should-have features:

- Multiple maps.
- Camera that follows the player.
- Collectible keys.
- Doors that require keys.
- Enemies.
- Player health.
- Game-over screen.
- Pause screen.
- Simple UI messages.
- Projectiles.
- Better object-oriented structure.

### Bonus Goal (Optional)

Optional features if enough time is available:

- Sound effects and music.
- High score saving.
- Save/load system.
- More enemy types.
- Animated sprites.
- More detailed level design.
- Better menu design.
- Boss enemy.
- More advanced AI behavior.
- More maps.
- Checkpoints.

### Assessment Criteria

Possible grading criteria:

| Criterion | Description |
| --- | --- |
| Functionality | Does the game run and are the planned features implemented? |
| Code structure | Are classes and packages organized clearly? |
| Object-oriented design | Are inheritance, composition, and responsibilities used sensibly? |
| Gameplay | Is the game understandable and playable? |
| Technical difficulty | Does the project show meaningful programming work? |
| Error handling | Does the game handle missing resources or invalid states reasonably? |
| Documentation | Is the project explained clearly and completely? |
| Presentation | Can the project be demonstrated and explained? |
| Teamwork | If group work, were tasks distributed and completed fairly? |

## 4. Materials

### Class Overview

| Class | Package | Responsibility |
| --- | --- | --- |
| `Main` | `main` | Program entry point. Starts the game. |
| `Game` | `main` | Creates the Swing window and starts the game panel. |
| `GamePanel` | `main` | Main game canvas, game loop, update logic, rendering, global game state. |
| `KeyHandler` | `main` | Handles keyboard input and menu navigation. |
| `AssetSetter` | `main` | Places objects and monsters into maps. |
| `BackgroundManager` | `main` | Loads and draws parallax background layers. |
| `Camera` | `main` | Follows the player and clamps view to world bounds. |
| `EventHandler` | `main` | Handles tile-based events such as healing, damage, and teleport. |
| `UI` | `main` | Draws title screen, pause screen, game-over screen, hearts, and messages. |
| `Entity` | `entity` | Base class for player, monsters, and projectiles. |
| `Player` | `entity` | Player movement, attacks, object interaction, drawing, and damage handling. |
| `Block` | `entity` | Older/simple block class, currently mostly replaced by tile logic. |
| `CollisionSystem` | `system` | AABB collision with tiles, objects, player, and entities. |
| `MovementSystem` | `system` | Gravity, player movement, and movement helper methods. |
| `Tile` | `tile` | Stores tile image and collision property. |
| `TileManager` | `tile` | Loads tile images, reads map files, draws the map, handles map changes. |
| `SuperObject` | `object` | Base class for objects such as keys, doors, chests, and flags. |
| `OBJ_Key` | `object` | Collectible key. |
| `OBJ_Door` | `object` | Solid door that can be opened with a key. |
| `OBJ_Chest` | `object` | Solid chest object. |
| `OBJ_Flag` | `object` | Map transition / level goal object. |
| `GreenSlime` | `monster` | Basic walking enemy. |
| `FireSlime` | `monster` | Enemy that moves and shoots fireballs. |
| `Projectile` | `projectile` | Base projectile logic. |
| `PT_Fireball` | `projectile` | Fireball projectile implementation. |

### Source Code Structure

```text
src/
├── entity/
│   ├── Block.java
│   ├── Entity.java
│   └── Player.java
├── main/
│   ├── AssetSetter.java
│   ├── BackgroundManager.java
│   ├── Camera.java
│   ├── EventHandler.java
│   ├── Game.java
│   ├── GamePanel.java
│   ├── KeyHandler.java
│   ├── Main.java
│   └── UI.java
├── monster/
│   ├── FireSlime.java
│   └── GreenSlime.java
├── object/
│   ├── OBJ_Chest.java
│   ├── OBJ_Door.java
│   ├── OBJ_Flag.java
│   ├── OBJ_Key.java
│   └── SuperObject.java
├── projectile/
│   ├── Projectile.java
│   └── PT_Fireball.java
├── system/
│   ├── CollisionSystem.java
│   └── MovementSystem.java
└── tile/
    ├── Tile.java
    └── TileManager.java
```

Resource structure:

```text
resource/
├── Backgroundlayers/
├── objects/
├── player/
└── projectile/

src/
├── res/missing/
└── tiles/
```

### External Resources

The project uses image files for the player, objects, backgrounds, hearts, tiles, and projectiles.

Current resource categories:

- Player sprites: `resource/player/`
- Object images: `resource/objects/`
- Projectile images: `resource/projectile/`
- Background layers: `resource/Backgroundlayers/`
- Tile images: `src/tiles/`
- Missing-image fallback: `src/res/missing/`

Licenses and sources:

| Resource | Source / License |
| --- | --- |
| Player images | `TODO: Insert source or "self-made"` |
| Object images | `TODO: Insert source or "self-made"` |
| Tile images | `TODO: Insert source or "self-made"` |
| Background images | `TODO: Insert source or "self-made"` |
| Projectile images | `TODO: Insert source or "self-made"` |
| Heart images | `TODO: Insert source or "self-made"` |

If assets were downloaded, their original links and licenses should be added here.

### Tools & Sources

Tools used:

- IntelliJ IDEA
- Java JDK
- Git, if used
- Image editor, if used: `TODO`

Sources and references:

- Java documentation: `TODO: Insert exact links if required`
- Swing tutorials: `TODO: Insert exact links if used`
- YouTube/tutorial references: `TODO: Insert tutorial names if used`
- Stack Overflow posts: `TODO: Insert links if used`
- Class notes: `TODO: Insert if relevant`

### Development Environment

| Item | Description |
| --- | --- |
| Operating system | `TODO: Insert OS, for example Windows` |
| IDE | IntelliJ IDEA |
| Java version | `TODO: Insert Java version` |
| Build system | Plain Java project, no Maven/Gradle |
| Version control | Git repository exists in the project folder |

## 5. Implementation

### Architecture

The final architecture is split into packages by responsibility.

The `main` package controls the application lifecycle. `Main` starts the program, `Game` creates the window, and `GamePanel` owns the main loop and central game objects.

The `entity` package contains the base entity model and the player. The player extends `Entity`, which provides common fields such as position, size, direction, velocity, collision area, life, and image data.

The `system` package contains logic that can be reused by different game objects. `CollisionSystem` handles collision checks and `MovementSystem` handles player movement and physics.

The `tile` package contains the tile map logic. `TileManager` loads tile images, reads map files, stores tile IDs in a 2D array, and draws the map using the camera.

The `object` package contains collectible or interactable world objects. These objects extend `SuperObject`.

The `monster` package contains enemies. Enemies extend `Entity` and implement their own update and draw behavior.

The `projectile` package contains projectile logic. `PT_Fireball` extends `Projectile`, and `Projectile` extends `Entity`.

### Important Classes

`GamePanel` is the central class. It stores the player, tile manager, collision system, camera, background manager, UI, objects, monsters, and the current game state. It also runs the update and render loop.

`Player` is responsible for reading input, moving, jumping, attacking, shooting, picking up objects, opening doors, taking damage, and drawing the player sprite.

`TileManager` is responsible for reading text maps and converting numbers into visual tiles. It also detects the player spawn tile.

`CollisionSystem` is responsible for checking whether entities overlap with solid tiles, solid objects, the player, or other entities.

`UI` is responsible for drawing menus, hearts, messages, pause overlay, and game-over overlay.

### Core Mechanics

#### Game Loop

The game loop is implemented in `GamePanel.run()`. It aims to update and repaint the game 60 times per second.

The loop uses `System.nanoTime()` to measure time. When enough time has passed, it calls:

```java
update();
repaint();
```

`update()` changes the game state, such as player position, enemy movement, projectile movement, camera position, and event handling.

`repaint()` tells Swing to call `paintComponent`, where the game is drawn.

#### Tile Map Loading

Maps are text files with numbers. Every number is a tile ID. `TileManager.loadMap()` reads each line, splits it by spaces, converts the numbers to integers, and stores them in `mapTileNum`.

The world currently expects 32 columns and 16 rows.

Tile ID `9` is used as a player spawn marker. When it is found, the player spawn coordinates are saved and the tile is replaced with tile `0`.

#### Collision Detection

Collision uses axis-aligned bounding boxes.

Every entity has a `solidArea`. The real collision rectangle is calculated by adding the entity's world position to the `solidArea` offset.

The collision system checks overlap with this logic:

```java
aLeft < bRight && aRight > bLeft &&
aTop < bBottom && aBottom > bTop
```

This is used for:

- player and tile collision
- player and object collision
- enemy and tile collision
- enemy and player collision
- projectile collision

#### Movement and Gravity

The player has horizontal and vertical velocity.

Horizontal movement:

- `A` sets velocity left.
- `D` sets velocity right.
- No key sets horizontal velocity to 0.

Jumping:

- If `Space` is pressed and the player is on the ground, vertical velocity is set to a negative jump value.

Gravity:

- Gravity increases vertical velocity every update.
- Falling speed is capped.
- If the player collides with the floor, vertical movement is undone and `onGround` is set to true.

#### Camera

The camera follows the player by centering itself on the player's position. It is clamped so it cannot move outside the world.

Drawing uses:

```java
screenX = worldX - camera.x;
screenY = worldY - camera.y;
```

This makes all objects appear relative to the camera.

#### Object Interaction

The player checks for object collision and interaction in `Player.pickUpObject`.

Current interactions:

- `Key`: increases `hasKey` and removes the key.
- `Door`: opens only if the player has at least one key.
- `Flag`: loads the next map if it exists, otherwise displays a win message.
- `Chest`: exists as a solid object, but currently has no special behavior.

#### Enemy Logic

`GreenSlime` and `FireSlime` move horizontally and randomly change direction. They are affected by gravity and collide with tiles and objects.

If an enemy touches the player, the player takes damage.

`FireSlime` also shoots fireballs after a timer reaches 180 frames.

#### Combat

The player has two attack types:

- Melee attack with `Enter`.
- Fireball projectile with `F`.

The melee attack creates an attack rectangle in front of the player. If the attack rectangle intersects a monster hitbox, the monster takes damage.

The fireball moves left or right and damages a monster when its hitbox intersects the monster hitbox.

### User Interface

The UI is drawn with Java `Graphics2D`.

UI parts:

- Title screen
- Menu cursor
- Pause screen
- Player hearts
- Game-over screen
- Temporary messages

Controls:

| Key | Action |
| --- | --- |
| `W` | Move menu cursor up |
| `S` | Move menu cursor down |
| `Enter` | Confirm menu option / melee attack / healing pool interaction |
| `A` | Move left |
| `D` | Move right |
| `Space` | Jump |
| `F` | Shoot fireball |
| `P` | Pause or unpause |

### Challenges & Solutions

| Challenge | Solution |
| --- | --- |
| Creating a stable game loop | Used a separate thread and `System.nanoTime()` delta timing. |
| Drawing a scrolling world | Added a camera and subtracted camera coordinates during rendering. |
| Preventing the player from walking through walls | Implemented AABB tile collision and undo movement when blocked. |
| Loading levels without hardcoding every tile | Used text files where each number represents a tile. |
| Managing different game screens | Added `titleState`, `playState`, `pauseState`, and `gameOver`. |
| Making objects reusable | Created `SuperObject` and subclasses for keys, doors, chests, and flags. |
| Making enemies separate from the player | Created enemy classes that extend `Entity`. |
| Avoiding repeated event triggers | Added an `eventReady` flag in `EventHandler`. |

### Deviations from the Plan

Possible deviations:

- JavaFX was considered in the original short description, but the current project uses Swing.
- A save/load system is visible as a menu option but is not implemented.
- Some scene methods exist for more maps, but not every scene is fully filled with unique content.
- Some assets are stored in `resource`, while some tile resources are stored in `src`.
- Some code comments suggest older or planned features that are not part of the final implementation.

### Screenshots

`TODO: Add screenshots from the finished game.`

Suggested screenshots:

- Title screen
- Gameplay with player and tiles
- Enemy encounter
- Key and door interaction
- Game-over screen
- Pause screen

Example placeholder:

```text
![Title Screen](docs/screenshots/title-screen.png)
![Gameplay](docs/screenshots/gameplay.png)
![Game Over](docs/screenshots/game-over.png)
```

## 6. Recap / Reflection

### Self-Evaluation

What worked well:

- The project successfully creates a playable Java Swing game.
- The game is divided into packages and classes with different responsibilities.
- The tilemap system makes it possible to design levels with text files.
- The camera system allows the world to be larger than the screen.
- Basic enemy, combat, object, and UI systems are implemented.

What did not work perfectly:

- Resource organization could be cleaner.
- The save/load option is not implemented.
- Some code is duplicated, especially enemy movement.
- Some comments and names could be cleaned up.
- There are no automated tests.
- Some planned systems are only partially implemented.

### Goal Achievement

| Goal type | Result |
| --- | --- |
| Minimum goal | Achieved: the game window, movement, jumping, tile map, collision, and basic gameplay work. |
| Extended goal | Mostly achieved: objects, enemies, projectiles, UI, health, camera, and multiple maps are implemented. |
| Bonus goal | Partly achieved: projectiles and multiple maps exist, but save/load, sound, high scores, and advanced AI are not implemented. |

### Lessons Learned

Technical lessons:

- How to create a Swing window and draw with `Graphics2D`.
- How a game loop works.
- How delta timing can be used for repeated updates.
- How to load resources from the classpath.
- How to use tile maps for level design.
- How to use rectangles for collision detection.
- How to separate game logic into classes and packages.
- How to use inheritance for entities, objects, monsters, and projectiles.
- How camera movement changes rendering.

Organizational lessons:

- Planning class responsibilities early makes implementation easier.
- Resource paths should be organized consistently.
- Regular testing is important because small changes in collision or map loading can affect gameplay.
- Documentation is easier when the code structure is clear.

### Outlook

Possible future improvements:

- Add sound effects and background music.
- Add animated player and enemy sprites.
- Add a real save/load system.
- Add a high score or completion time system.
- Add more maps.
- Add checkpoints.
- Add more enemy types.
- Add a boss fight.
- Add a proper settings menu.
- Add automated tests for collision and map loading.
- Clean up resource folders.
- Add Maven or Gradle for easier building.
- Improve enemy AI.
- Add a level editor or easier map creation workflow.

### Conclusion

The project demonstrates how a small 2D platformer can be created in Java using Swing and AWT. It includes the most important systems of a basic game: a game loop, rendering, input, movement, collision, maps, camera, objects, enemies, projectiles, events, UI screens, and game states.

The final result is a playable school project that shows both technical understanding and object-oriented design. Although some features could still be improved, the project provides a strong foundation for extending the game further.
