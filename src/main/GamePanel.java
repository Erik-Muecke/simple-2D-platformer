package main;

import javax.swing.*;
import java.awt.*;


import entity.Entity;
import entity.Player;
import system.CollisionSystem;
import tile.TileManager;
import object.SuperObject;

public class GamePanel extends JPanel implements Runnable {
    final int originalTileSize = 32; // 32x32 tile
    public static final int scale = 2; //resolution of the tiles will be 3 times bigger than the original tile size

    public final int tileSize = originalTileSize * scale; // 64x64 tile Berechnung der endgültigen Tilegröße
    public final static int MaxScreenCol = 16; //Breite des Bildschirms in Tiles
    public final static int MaxScreenRow = 12; //Höhe des Bildschirms in Tiles
    final public int screenWidth = tileSize * MaxScreenCol; // 768 pixels
    final public int screenHeight = tileSize * MaxScreenRow; // 576 pixels


    public int MaxWorldCol = 38; //Breite der Welt in Tiles
    public int MaxWorldRow = 22; //Höhe der Welts in Tiles
    public int worldWidth = tileSize * MaxWorldCol; //Weite der Welt in Pixeln 2432 pixel
    public int worldHeight = tileSize * MaxWorldRow; //Höhe der Welt in Pixeln 1408 pixel

    //FPS
    int fps = 60; //Frames per second, die Anzahl der Bilder, die pro Sekunde gezeichnet werden sollen

    //Erstellen der verschiedenen Systeme und Manager.
    public SaveHandler saveHndlr = new SaveHandler(this);

    public TileManager tileM = new TileManager(this);
    public KeyHandler keyHandler;

    public BackgroundManager bg = new BackgroundManager(this);

    Thread gameThread; //erstellt den Thread für die Spielschleife zum Bestimmen der FPS
    public CollisionSystem collisionsystem = new CollisionSystem(this);
    public Camera camera;
    public SuperObject[] obj = new SuperObject[10];
    public AssetSetter aSetter = new AssetSetter(this);
    public Entity[] monster = new Entity[20];
    public Player player; //erstellt eine neue Instanz des Players, damit wir ihn im Spiel verwenden können

    // Game States
    public UI ui;
    public int gameState;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int titleState = 0;
    public final int gameOver = 3;
    public final int winState = 4;

    public EventHandler eHandler;

    public boolean showCollisionDebug = false; //nur zum Debuggen, zeigt die Kollisionsboxen

    //Map indicator zeigt an welche Karte gerade gespielt wird.
    public int mapIndicator = 0;
    public int previousmapIndicator = 0;
    public int numberOfMaps = 5; //Anzahl der Karten, die es gibt, damit der winState aktiviert wird, wenn der Spieler alle Karten abgeschlossen hat



    public GamePanel() {

        this.setPreferredSize(new Dimension(screenWidth, screenHeight)); // Set the size of the panel to the calculated screen width and height
        this.setBackground(Color.BLACK); //Hintergrundfarbe zu schwarz
        this.setDoubleBuffered(true); //Screen wird zuerst unsichtbar gezeichnet und dann sichtbar gemacht, um Flackern zu vermeiden
        System.out.println("GamePanel created"); //Bestätigung, nur zum Debuggen, remove in Production
        camera = new Camera(screenWidth, screenHeight, worldWidth, worldHeight); //erstellt eine neue Instanz der Kamera
        keyHandler = new KeyHandler(this); //erstellt eine neue Instanz des KeyHandlers, damit Tastatureingaben verarbeitet werden können
        player = new Player(this, keyHandler); //erstellt eine neue Instanz des Players, damit wir ihn im Spiel verwenden können, übergibt die aktuelle Instanz von GamePanel und den KeyHandler,
        // damit der Spieler auf die Informationen und Funktionen von GamePanel und KeyHandler zugreifen kann

        aSetter.setObjectScene0(); //ruft die Methode setObjectScene0() des AssetSetters auf, um die Objekte für die erste Szene zu platzieren, damit die Karte mit den entsprechenden Objekten gefüllt wird
        aSetter.setMonsterScene0(); //ruft die Methode setMonsterScene0() des AssetSetters auf, um die Monster für die erste Szene zu platzieren, damit die Karte mit den entsprechenden Monstern gefüllt wird

        //setzt die Startposition des Spielers auf die Koordinaten, die im TileManager definiert sind
        player.x = tileM.playerSpawnX;
        player.y = tileM.playerSpawnY;

        eHandler = new EventHandler(this);

        gameState = titleState; //setzt den Spielstatus auf den Titelbildschirm, damit das Spiel mit dem Titelbildschirm beginnt

        ui = new UI(this);



    }

    public void startGameThread() {

        gameThread = new Thread(this); //erstellt einen neuen Thread und übergibt die aktuelle Instanz von GamePanel als Runnable
        gameThread.start(); //startet den Thread, wodurch die run() Methode aufgerufen wird

    }

    @Override //Die run() Methode enthält die Hauptspielschleife, die kontinuierlich ausgeführt wird, solange der gameThread nicht null ist
    public void run() {

        double Zeitintervall = (double) 1000_000_000 /fps;
        double delta = 0;
        long lastTime = System.nanoTime(); //gibt die aktuelle Zeit im Thread in Nanosekunden zurück
        long currentTime;
        long timer = 0;
        int drawCount = 0;


        while(gameThread != null) { //führt die Spielschleife nur aus, solange der Game Thread nicht null ist, also nur wenn das Spiel läuft.

            currentTime = System.nanoTime(); //gibt die aktuelle Zeit im Thread in Nanosekunden zurück, zur Berechnung der Differenz seit dem letztem update

            delta += (currentTime - lastTime) / Zeitintervall; //berechnet die Anzahl der Zeitintervalle,
            // die seit dem letzten Update vergangen sind, indem die Differenz zwischen der aktuellen Zeit und der letzten Zeit durch das Zeitintervall dividiert wird

            timer += (currentTime - lastTime); //berechnet die verstrichene Zeit seit dem letzten Update, indem die Differenz zwischen der aktuellen Zeit und der letzten Zeit zum Timer addiert wird

            lastTime = currentTime; //aktualisiert die letzte Zeit auf die aktuelle Zeit, damit die nächste Berechnung der verstrichenen Zeit korrekt ist
            if (delta >= 1) { //Wenn delta größer oder gleich 1 ist, bedeutet dies, dass genug Zeit vergangen ist, um ein Update durchzuführen
                update(); //Aktualisiert die Spielinformationen

                repaint(); //Zeichnet die Grafiken neu
                delta--; //delta wird um 1 verringert, um die Anzahl der durchgeführten Updates zu verfolgen
                drawCount++; //drawcount um 1 erhöht um die Anzahl der gezeichneten Frames zu verfolgen
            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
                if(timer >= 1000_000_000) { //Wenn der Timer eine Sekunde erreicht oder überschreitet, wird die Anzahl der gezeichneten Frames pro Sekunde ausgegeben und der Timer sowie der drawCount zurückgesetzt
                    System.out.println("FPS: " + drawCount); //gibt die FPS, also Schleifendurchgäng in einer Sekunde an
                    drawCount = 0; //setzt alles zurück, damit die nächste Sekunde neu gezählt werden kann
                    timer = 0; //setzt alles zurück, damit die nächste Sekunde neu gezählt werden kann
                }
            }
        }
    }

    public void update() {
        if (mapIndicator > numberOfMaps) {
            gameState = winState; //Wenn der Spieler mehr Karten abgeschlossen hat als es gibt, wird der winState aktiviert
        }
        if(gameState == playState) { //Wenn der Spielstatus auf PlayState ist, wird das Spiel aktualisiert.
            tileM.update(); //neue Karte wird geladen, wenn der Spieler die Karte wechselt
            player.update(); //Der Spieler wird aktualisiert
            camera.update(player); //Die Kamera wird aktualisiert, damit sie dem Spieler folgt
            eHandler.checkEvent(); //Überprüft ob ein Event ausgelöst wurde
            if(player.projectile.alive) {
                player.projectile.update(); //aktualisiert das Projektil, wenn es aktiv ist
                player.checkProjectileMonsterHit(); //überprüft, ob das Projektil ein Monster getroffen hat, und aktualisiert den Status des Monsters entsprechend
            }

            //aktualisieren jedes einzelnen Monsters im monster Array
            for(int i = 0; i < monster.length; i++) {
                if(monster[i] != null) { //Führt die nächsten schritte nur aus, wenn der Eintrag im Array nicht Null ist, um NullPointerExceptions zu vermeiden
                    if(monster[i].isDead) { //löscht das mosnter wenn es tot ist, damit es nicht mehr gezeichnet wird.
                        monster[i] = null;
                    } else {
                        monster[i].update(); //aktualisiert das Monster, damit es sich bewegt und mit dem Spieler interagiert
                    }
                }
            }
        }

        //Ueberpruefen ob der Spieler tot ist, damit der Game Over Screen angezeigt wird
        if (player.life <= 0) {
            gameState = gameOver;
        }

        if(gameState == pauseState) {
            //Wenn das Spiel pausiert ist wird nichts aktualisiert.
        }
    }


    @Override //Die paintComponent() Methode wird ueberschrieben, um die Grafiken des Spiels zu zeichnen.
    protected void paintComponent(Graphics g) {

        super.paintComponent(g); //Panel-Hintergrund korrekt neu zeichnen
        Graphics2D g2 = (Graphics2D) g; //castet das Graphics object in ein Graphics2D object, um erweiterte Grafikfunktionen nutzen zu können
        if (gameState == titleState) {
            ui.draw(g2); //zeichnet das Title Screen UI
        }
        if (gameState == gameOver) {
                    ui.draw(g2); //zeichnet das Game Over UI
        } else {
            bg.draw(g2); //zeichnet das UI
            tileM.draw(g2);//zeichnet die Spielkacheln mit der draw() Methode im TileManager

            for (SuperObject superObject : obj) {
                if (superObject != null) { //zeichnet nur, wenn das Objekt nicht null ist, um NullPointerExceptions zu vermeiden
                    superObject.draw(g2, this); //zeichnet die Objekte mit der draw() Methode in der SuperObject Klasse für jedes Objekt im obj Array
                }
            }

            for (Entity entity : monster) { //geht das Monster Array durch
                if (entity != null) { //Wenn der Eintrag nicht null ist wird die draw Methode des Monsters aufgerufen
                    entity.draw(g2);
                }
            }

            player.draw(g2); //Zeichnen des Spielers

            ui.draw(g2); //Zeichnet die Herzenanzahl

            if (showCollisionDebug) {
                collisionsystem.drawDebugBoxes(g2, player); //Zeichnet die Kollisionsboxen für den Spieler
            }

            if(player.projectile != null) {
                player.projectile.draw(g2); //Zeichnet das Projektil, wenn es existiert
            }

            ui.draw(g2);  //Zeichnet das UI, damit der Pausenbildschirm über den anderen Elementen liegt
        }
        g2.dispose(); //freigeben des Speichers des Graphics 2D Objektes

    }

    public void resetGame() {
        //Zurücksetzten aller relevanten Variablen und Objekte, damit das Spiel von vorne beginnen kann, wird aufgerufen, wenn der Spieler im Game Over Screen die Option zum Neustart wählt
        keyHandler.commandNum = 0;
        mapIndicator = 0;

        player.life = player.maxLife;
        player.hasKey = 0;
        player.x = tileM.playerSpawnX;
        player.y = tileM.playerSpawnY;
        player.velocityX = 0;
        player.velocityY = 0;
        player.invincible = false;
        player.invincibleCounter = 0;
        player.projectile.alive = false;

        aSetter.setObjectScene0();

        camera.update(player);
        gameState = playState;
    }
}
