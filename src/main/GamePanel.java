package main;

import javax.swing.*;
import java.awt.*;
import java.io.File;


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


    public int MaxWorldCol = 32; //Breite der Welt in Tiles
    public int MaxWorldRow = 16; //Höhe der Welts in Tiles
    public int worldWidth = tileSize * MaxWorldCol;
    public int worldHeight = tileSize * MaxWorldRow;

    //FPS
    int fps = 60; //Frames per second, die Anzahl der Bilder, die pro Sekunde gezeichnet werden sollen

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

    public EventHandler eHandler;

    public boolean showCollisionDebug = true;

    //Map indicator which map to load
    public int mapIndicator = 0;
    public int previousmapIndicator = 0;



    public GamePanel() {

        this.setPreferredSize(new Dimension(screenWidth, screenHeight)); // Set the size of the panel to the calculated screen width and height
        this.setBackground(Color.BLACK); //Hintergrundfarbe zu schwarz
        this.setDoubleBuffered(true); //Screen wird zuerst unsichtbar gezeichnet und dann sichtbar gemacht, um Flackern zu vermeiden
        System.out.println("GamePanel created"); //Bestätigung, nur zum Debuggen, remove in Production
        camera = new Camera(screenWidth, screenHeight, worldWidth, worldHeight);
        keyHandler = new KeyHandler(this);
        player = new Player(this, keyHandler);
        aSetter.setObjectScene0();
        aSetter.setMonster();
        player.x = tileM.playerSpawnX;
        player.y = tileM.playerSpawnY;
        eHandler = new EventHandler(this);
        gameState = titleState;
        ui = new UI(this);



    }

    public void startGameThread() {

        gameThread = new Thread(this); //erstellt einen neuen Thread und übergibt die aktuelle Instanz von GamePanel als Runnable
        gameThread.start(); //startet den Thread, wodurch die run() Methode aufgerufen wird

    }

    @Override //Die run() Methode enthält die Hauptspielschleife, die kontinuierlich ausgeführt wird, solange der gameThread nicht null ist
    public void run() {

        double Zeitintervall = (double) 1000000000 /fps;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;


        while(gameThread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / Zeitintervall; //berechnet die Anzahl der Zeitintervalle,
            // die seit dem letzten Update vergangen sind, indem die Differenz zwischen der aktuellen Zeit und der letzten Zeit durch das Zeitintervall dividiert wird

            timer += (currentTime - lastTime); //berechnet die verstrichene Zeit seit dem letzten Update, indem die Differenz zwischen der aktuellen Zeit und der letzten Zeit zum Timer addiert wird

            lastTime = currentTime; //aktualisiert die letzte Zeit auf die aktuelle Zeit, damit die nächste Berechnung der verstrichenen Zeit korrekt ist
            if (delta >= 1) { //Wenn delta größer oder gleich 1 ist, bedeutet dies, dass genug Zeit vergangen ist, um ein Update durchzuführen
                update(); //Aktualisiert die Spielinformationen

                repaint(); //Zeichnet die Grafiken neu
                delta--; //delta wird um 1 verringert, um die Anzahl der durchgeführten Updates zu verfolgen
                drawCount++;
            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
                if(timer >= 1000000000) { //Wenn der Timer eine Sekunde erreicht oder überschreitet, wird die Anzahl der gezeichneten Frames pro Sekunde ausgegeben und der Timer sowie der drawCount zurückgesetzt
                    System.out.println("FPS: " + drawCount); //gibt die FPS, also Schleifendurchgäng in einer Sekunde an
                    drawCount = 0; //setzt alles zurück, damit die nächste Sekunde neu gezählt werden kann
                    timer = 0; //setzt alles zurück, damit die nächste Sekunde neu gezählt werden kann
                }
            }
        }
    }

    public void update() {
        if(gameState == playState) {
            tileM.update();
            player.update();
            camera.update(player);
            eHandler.checkEvent();
            if(player.projectile.alive) {
                player.projectile.update();
                player.checkProjectileMonsterHit();
            }
            for(int i = 0; i < monster.length; i++) {
                if(monster[i] != null) {
                    if(monster[i].isDead) {
                        monster[i] = null;
                    } else {
                        monster[i].update();
                    }
                }
            }
        } //aktualisiert die Informationen des Spielers, indem die update() Methode des Player-Objekts aufgerufen wird
        if(gameState == pauseState) {
            // do nothing (game is frozen)
        } //aktualisiert die Informationen des Spielers, indem die update() Methode des Player-Objekts aufgerufen wird
    }


    @Override //Die paintComponent() Methode wird ueberschrieben, um die Grafiken des Spiels zu zeichnen.
    protected void paintComponent(Graphics g) {

        super.paintComponent(g); //Panel-Hintergrund korrekt neu zeichnen
        Graphics2D g2 = (Graphics2D) g;
        if (gameState == titleState) {
            ui.draw(g2);
        } else {
            bg.draw(g2);
            tileM.draw(g2);//zeichnet die Spielkacheln mit der draw() Methode im TileManager
            for (SuperObject superObject : obj) {
                if (superObject != null) {
                    superObject.draw(g2, this);
                }
            }
            for (Entity entity : monster) {
                if (entity != null) {
                    entity.draw(g2);
                }
            }
            player.draw(g2);
            ui.draw(g2);

            if (showCollisionDebug) {
                collisionsystem.drawDebugBoxes(g2, player);
            }

            if(player.projectile != null) {
                player.projectile.draw(g2);
            }
            ui.draw(g2);  // always last so pause screen renders on top
        }
        g2.dispose();

    }
}
