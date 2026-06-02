# Projektdokumentation – simple-2D-platformer

## 1. Einführung

### Projektbeschreibung
Das Projekt **simple-2D-platformer** ist ein 2D-Jump’n’Run in Java mit Swing.  
Der Spieler bewegt sich durch tile-basierte Level, sammelt Schlüssel, öffnet Türen, bekämpft Gegner (Green Slime) und erreicht eine Flagge zum Kartenwechsel.

### Zielsetzung
- Umsetzung eines spielbaren Java-Projekts im Schulkontext (IT-Unterricht, laut `README.md`)
- Praktische Anwendung von:
  - Objektorientierung (Entity-/Object-Hierarchie)
  - Game Loop und Zustandsverwaltung
  - Kollisionserkennung
  - Rendering mit Kamera und Parallax-Hintergrund

### Rahmenbedingungen
- **Kontext:** Schulprojekt im IT-Unterricht (`README.md`)
- **Teamgröße:** Nach Repository-Historie 1 Hauptentwickler (Erik Mücke)
- **Schuljahr / Kurs / Abgabedatum:** Nicht im Repository dokumentiert

### Technologien
- **Sprache:** Java
- **GUI/Rendering:** Swing (`JFrame`, `JPanel`, `Graphics2D`)
- **Assets:** PNG-Ressourcen in `src/tiles`, `resource/*`, `res/*`
- **Build:** Direkte Kompilierung mit `javac` (kein Maven/Gradle im Repository)

---

## 2. Planung

### Spielkonzept
- Start im Titelmenü (New Game, Load Game Platzhalter, Quit)
- Steuerung über Tastatur (WASD, Space, Enter, F, P)
- Kernablauf:
  1. Bewegen und Springen
  2. Objekte einsammeln/interagieren (Schlüssel/Tür/Flagge)
  3. Gegnerkontakt vermeiden bzw. Gegner besiegen
  4. Ziel erreichen (Flagge)

### Anforderungsanalyse
**Funktional**
- Spielfigur bewegen, springen und angreifen
- Projektil-Funktion (Fireball)
- Gegner-KI mit Richtungswechsel bei Kollisionen
- Tilemap-basiertes Level mit Kollisionskacheln
- Kamera-Follow und Pause-/Titelzustände
- Lebenssystem inklusive UI-Herzen

**Nicht-funktional**
- Flüssige Aktualisierung mit Zielwert von 60 FPS (`GamePanel`)
- Klare Trennung in Pakete (`main`, `entity`, `system`, `tile`, `object`, `projectile`, `monster`)
- Robustes Ressourcenladen mit Fallback-Bildern (`Entity.setup`, `TileManager.loadTileImage`)

### Aufgabenverteilung
Nach Commit-Historie wurde das Projekt durch einen Autor umgesetzt:
- **Erik Mücke** – Initiale Implementierung, Asset-/Objektplatzierung, spätere Refaktorierung.

### Zeitplan (aus Commits rekonstruiert)
| Datum | Commit | Inhalt |
|---|---|---|
| 2026-05-20 | `0a8f6d5` | Initialer großer Projektstand mit Spielsystemen, Assets und Leveln |
| 2026-05-23 | `613dd5e` | Refactoring in `GamePanel`, Entfernen ungenutzter Klasse `Block` |

### Klassendiagramm / UML (textuell)
- `Main` → startet `Game`
- `Game` → erstellt `GamePanel` und Fenster
- `GamePanel` (Zentrale Orchestrierung) enthält u. a.:
  - `Player`
  - `TileManager`
  - `CollisionSystem`
  - `BackgroundManager`
  - `UI`
  - `AssetSetter`
  - `EventHandler`
- `Entity` als Basisklasse für `Player`, `GreenSlime`, `Projectile`
- `SuperObject` als Basisklasse für `OBJ_Key`, `OBJ_Door`, `OBJ_Chest`, `OBJ_Flag`

### Mockups / Skizzen
- Keine separaten Mockup-Dateien im Repository.
- Titelbildschirm und HUD wurden direkt im Code umgesetzt (`UI.java`).

---

## 3. Erwartungshorizont

### Minimalziel (Muss)
- Spiel startet zuverlässig im Fenster
- Spielfigur kann laufen/springen
- Kollision mit Boden/Wänden funktioniert
- Mindestens ein spielbares Level mit Zielobjekt (Flagge)

### Erweiterungsziel (Soll)
- Gegner mit Schaden/Invincibility-Frames
- Objektinteraktionen (Schlüssel/Tür/Truhe)
- UI für Lebensanzeige und Pause-/Titelzustände
- Kamera-Scrolling und Parallax-Hintergrund

### Bonusziel (Kann)
- Mehr Karten/Szenen (`tilemap1` bereits vorhanden)
- Speichern/Laden (im Menü vorbereitet, aber noch Platzhalter)
- Weitere Gegnertypen und Soundeffekte

### Bewertungskriterien
- Funktionsfähigkeit des Spiels
- Saubere Klassenstruktur und Lesbarkeit
- Korrekte Kollisions-/Bewegungslogik
- Nachvollziehbare Weiterentwicklung (Commits) und dokumentierter Code

---

## 4. Materialien

### Klassenübersicht (Kurzfassung)
- `main`: Start, Fenster, Game Loop, Kamera, UI, Events, Asset-Platzierung
- `entity`: Basisklasse und Spielerlogik
- `monster`: Gegnerimplementierung (`GreenSlime`)
- `system`: Bewegungs- und Kollisionserkennung
- `tile`: Tiledefinition und Tilemap-Verwaltung
- `object`: Interaktive Weltobjekte
- `projectile`: Projektilbasis und Feuerball

### Quellcode-Struktur
```text
src/
  main/        (Game-Start, Loop, UI, Kamera, Events)
  entity/      (Entity, Player)
  monster/     (GreenSlime)
  system/      (MovementSystem, CollisionSystem)
  tile/        (Tile, TileManager)
  object/      (SuperObject + Objektklassen)
  projectile/  (Projectile, PT_Fireball)
  tiles/       (Tilegrafiken + tilemap*.txt)
```

### Externe Ressourcen
- Genutzte Bildressourcen liegen lokal im Repository (`resource/*`, `res/*`, `src/tiles/*`).
- Explizite Quellen/Lizenzen sind derzeit nicht dokumentiert und sollten für eine finale Abgabe ergänzt werden.

### Hilfsmittel & Quellen
- Im Repository nicht explizit aufgelistet.
- Erkennbar genutzt: Java/Swing-Standardbibliothek und Bildladen per `ImageIO`.

### Entwicklungsumgebung
- Java-Quellcodeprojekt ohne Build-Tooling (kein Maven/Gradle)
- Kompilierung per `javac`
- Versionsverwaltung über Git (2 dokumentierte Commits auf aktuellem Branch)

---

## 5. Umsetzung

### Architektur
Die zentrale Steuerung liegt in `GamePanel`:
- Update-Zyklus (`update`) für Welt, Spieler, Gegner, Kamera und Events
- Render-Zyklus (`paintComponent`) mit Reihenfolge:
  1. Hintergrund
  2. Tiles
  3. Objekte
  4. Gegner
  5. Spieler
  6. UI/Debug

### Kernmechaniken
- **Game Loop:** Zeitbasierte Schleife mit Ziel-FPS (`fps = 60`)
- **Bewegung/Physik:** Schwerkraft, Sprung, Bodenkontakt in `MovementSystem`
- **Kollision:** AABB-basierte Prüfungen für Tiles/Objekte/Spieler in `CollisionSystem`
- **Kampf:** Nahkampfangriff via Attack-Box + Fernkampf mit `PT_Fireball`
- **Levellogik:** Tilemaps (`tilemap0/1`), Spawnpunkt-Marker über Tile `9`

### Benutzeroberfläche
- Titelmenü mit Cursor-Auswahl
- Pause-Anzeige
- Lebensanzeige (Herz-Icons)
- Einfache Zustandssteuerung (`titleState`, `playState`, `pauseState`)

### Herausforderungen & Lösungen (aus Code-Kommentaren erkennbar)
- **Kollision debuggen:** Visualisierung aktueller/projizierter Hitboxen (`drawDebugBoxes`)
- **Ressourcenrobustheit:** Fallback auf Placeholder-Bild bei fehlenden Assets
- **Objektkollision früh erkennen:** Kommentar in `collisionObject` beschreibt Vorwärtsprojektion „one step ahead“
- **Performance/Flüssigkeit:** Double Buffering + FPS-orientierte Schleife

### Abweichungen von der Planung
- Menüpunkt „Load Game“ ist vorhanden, aber noch ohne Ladefunktion.
- Mehrere Szenenmethoden (`setObjectScene1..5`) sind vorbereitet, aber nur teilweise befüllt.
- Kommentare/TODOs zeigen offene Ausbauschritte (z. B. Objekt-/Gegnerplatzierung je Karte).

### Entwicklungsprozess aus Commits und Code-Kommentaren
**Commit-basierte Entwicklung**
- Der erste Commit bildet einen umfangreichen Grundstand mit vollständiger Spielbasis.
- Der zweite Commit fokussiert auf Aufräumen/Refactoring (`GamePanel`) und Entfernen toter Strukturteile (`Block`).

**Kommentarbasierte Entwicklungsspuren**
- Deutsch- und englischsprachige Inline-Kommentare dokumentieren Lern- und Iterationsschritte.
- Mehrere Kommentare erklären Designentscheidungen (z. B. Kollision, Kamera, Zeichnungsreihenfolge).
- TODO-Kommentare markieren bewusst geplante Verbesserungen.

### Screenshots
- Im Repository sind keine fertigen Gameplay-Screenshots abgelegt.
- Für die finale Abgabe sollten Screenshots von Titelbildschirm, Gameplay (inkl. Gegner), sowie Pause-/UI-Ansicht ergänzt werden.

---

## 6. Rekapitulation

### Selbstbewertung
**Gut gelungen**
- Solide Grundarchitektur mit klarer Pakettrennung
- Spielbare Kernmechanik (Bewegung, Kollision, Interaktion, Gegner)
- Sichtbare Iteration und Refactoring durch Commits

**Noch ausbaufähig**
- Dokumentation externer Asset-Lizenzen
- Vollständige Nutzung aller geplanten Szenen
- Persistenz (Load/Save)

### Zielerreichung
- **Minimalziel:** erreicht
- **Erweiterungsziel:** weitgehend erreicht (Kampf, UI, Gegner, Kamera)
- **Bonusziel:** teilweise vorbereitet, aber nicht vollständig umgesetzt

### Lessons Learned
- Trennung von Bewegung und Kollision in eigene Systeme verbessert Wartbarkeit.
- Frühe Kollisionsprojektion reduziert Interaktionsfehler an Türen/Objekten.
- Iteratives Refactoring nach erstem funktionalen Stand ist sinnvoll und sichtbar im Verlauf.

### Ausblick
- Erweiterung auf mehrere vollständige Level mit eigener Objekt-/Gegnerlogik
- Implementierung echter Save/Load-Funktion
- Audio, zusätzliche Gegner und Polishing der UI

### Fazit
Das Projekt liefert einen funktionsfähigen 2D-Platformer-Grundkern mit klarer technischer Struktur und nachvollziehbarer Entwicklung über Commits und Kommentare. Für eine finale Schulabgabe sind vor allem Feinschliff, Lizenzdokumentation und zusätzliche Inhalte der nächste sinnvolle Schritt.
