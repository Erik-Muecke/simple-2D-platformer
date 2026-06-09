package main;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import entity.Entity;
import entity.Player;
import monster.FireSlime;
import monster.FireShooter;
import projectile.Projectile;
import system.CollisionSystem;
import system.MovementSystem;
import main.Camera;
import tile.TileManager;
import object.SuperObject;

public class GamePanel extends JPanel implements Runnable {
    final int originalTileSize = 32; // 32x32 tile
    public static final int scale = 2; //resolution of the tiles will be 3 times bigger than the original tile size

    public final int tileSize = originalTileSize * scale; // 64x64 tile Berechnung der endgültigen Tilegröße
    public int MaxScreenCol = 20; //Breite des Bildschirms in Tiles
    public int MaxScreenRow = 11; //Höhe des Bildschirms in Tiles
    final public int screenWidth = tileSize * MaxScreenCol; // 768 pixels
    final public int screenHeight = tileSize * MaxScreenRow; // 576 pixels

    public int MaxWorldCol; //Breite der Welt in Tiles
    public int MaxWorldRow; //Höhe der Welts in Tiles
    public int worldWidth;
    public int worldHeight;

    //FPS
    int fps = 60; //Frames per second, die Anzahl der Bilder, die pro Sekunde gezeichnet werden sollen

    public TileManager tileM;
    public KeyHandler keyHandler;
    public AssetSetter aSetter;

    Thread gameThread; //erstellt den Thread für die Spielschleife zum Bestimmen der FPS
    public CollisionSystem collisionsystem = new CollisionSystem(this);
    public MovementSystem movementSystem = new MovementSystem(this);
    public Camera camera;
    public BackgroundManager backgroundManager;
    public SuperObject obj[] = new SuperObject[30];
    public Entity monster[] = new Entity[20];
    public Entity[] npc = new Entity[20];
    public Player player; //erstellt eine neue Instanz des Players, damit wir ihn im Spiel verwenden können

    // Game States
    public UI ui;
    public int gameState;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int inventoryState = 3;
    public final int optionsState = 4;
    public final int titleState = 0;
    public final int dialogueState = 5;
    public final int transitionState = 6;
    public final int tradeState = 7;
    public boolean gameOver = false;

    public EventHandler eHandler;

    public boolean showCollisionDebug = true;

    //Map indicator which map to load
    public int mapIndicator = 0;
    public int previousmapIndicator = -1;
    public boolean fullScreenOn = false;
    BufferedImage tempScreen;
    Graphics2D g2;

    public GamePanel() {
        // World dimensions must be known before camera, tile manager, and entities are created.
        this.setMapDimensions(mapIndicator);
        this.setPreferredSize(new Dimension(screenWidth, screenHeight)); // Set the size of the panel to the calculated screen width and height
        this.setBackground(Color.BLACK); //Hintergrundfarbe zu schwarz
        this.setDoubleBuffered(true); //Screen wird zuerst unsichtbar gezeichnet und dann sichtbar gemacht, um Flackern zu vermeiden
        System.out.println("GamePanel created"); //Bestätigung, nur zum Debuggen, remove in Production
        camera = new Camera(screenWidth, screenHeight, worldWidth, worldHeight);
        backgroundManager = new BackgroundManager(this);
        keyHandler = new KeyHandler(this);
        player = new Player(this, keyHandler);
        aSetter = new AssetSetter(this);
        aSetter.updateScene();
        tileM = new TileManager(this);
        player.x = tileM.playerSpawnX;
        player.y = tileM.playerSpawnY;
        eHandler = new EventHandler(this);
        gameState = titleState;
        ui = new UI(this);
        tempScreen = new BufferedImage(
                screenWidth,
                screenHeight,
                BufferedImage.TYPE_INT_ARGB
        );
        g2 = (Graphics2D)tempScreen.getGraphics();
    }

    public void startGameThread() {
        gameThread = new Thread(this); //erstellt einen neuen Thread und übergibt die aktuelle Instanz von GamePanel als Runnable
        gameThread.start(); //startet den Thread, wodurch die run() Methode aufgerufen wird

    }

    public void setMapDimensions(int mapIndicator) {
        // Each map can have a different tile grid size, so world bounds are recalculated here.
        switch (mapIndicator) {
            case 0:
                MaxWorldCol = 32;
                MaxWorldRow = 16;
                break;
            case 1:
                MaxWorldCol = 32;
                MaxWorldRow = 16;
                break;
            case 2:
                MaxWorldCol = 48;
                MaxWorldRow = 32;
                break;
            case 3:
                MaxWorldCol = 64;
                MaxWorldRow = 16;
                break;
            case 4:
                MaxWorldCol = 48;
                MaxWorldRow = 22;
                break;
            default:
                MaxWorldCol = 32;
                MaxWorldRow = 16;
                break;
        }
        worldWidth  = tileSize * MaxWorldCol;
        worldHeight = tileSize * MaxWorldRow;
    }


    @Override //Die run() Methode enthält die Hauptspielschleife, die kontinuierlich ausgeführt wird, solange der gameThread nicht null ist
    public void run() {

        // Fixed timestep loop: update at the target FPS even if repaint timing varies.
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
            if(player.life <= 0) {
                player.life = 0;
                gameOver = true;
            }
            if(gameOver == false) {
                // Update order matters: player movement may trigger map transitions before monsters update.
                player.update();
                tileM.update();
                for(int i = 0; i < monster.length; i++) {
                    if(monster[i] != null) {
                        if(monster[i].isDead) {
                            monster[i] = null;
                        } else {
                            monster[i].update();
                        }
                    }
                }
                camera.update(player);

                // Event checks are map-specific because event tile coordinates differ per level.
                switch(mapIndicator){
                    case 0:
                        eHandler.checkEvent0();
                        break;
                    case 1:
                        eHandler.checkEvent1();
                        break;
                    case 2:
                        eHandler.checkEvent2();
                        break;
                    case 3:
                        eHandler.checkEvent3();
                        break;
                    case 4:
//                        eHandler.checkEvent4();
                        break;
                }
                if(player.projectile.alive) {
                    player.projectile.update();
                    player.checkProjectileMonsterHit();
                    checkProjectileClash();
                }
            }
        } //aktualisiert die Informationen des Spielers, indem die update() Methode des Player-Objekts aufgerufen wird
        if(gameState == pauseState) {
            // do nothing (game is frozen)
        }
    }

    public JFrame gameWindow;

    public void toggleFullScreen() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();

        if (!fullScreenOn) {
            gameWindow.dispose();
            gameWindow.setUndecorated(true);
            gameWindow.pack();
            gd.setFullScreenWindow(gameWindow);
            fullScreenOn = true;
        } else {
            gd.setFullScreenWindow(null);
            gameWindow.dispose();
            gameWindow.setUndecorated(false);
            gameWindow.pack();
            gameWindow.setLocationRelativeTo(null);
            gameWindow.setVisible(true);
            fullScreenOn = false;
        }
        gameWindow.requestFocus();
        requestFocusInWindow();
    }

    private void checkProjectileClash() {
        Projectile playerProjectile = player.projectile;

        if (playerProjectile == null || !playerProjectile.alive) {
            return;
        }

        for (Entity entity : monster) {
            Projectile monsterProjectile = null;

            if (entity instanceof FireSlime fireSlime) {
                monsterProjectile = fireSlime.getProjectile();
            } else if (entity instanceof FireShooter fireShooter) {
                monsterProjectile = fireShooter.getProjectile();
            }

            if (monsterProjectile == null || !monsterProjectile.alive) {
                continue;
            }

            if (playerProjectile.getCollisionBox().intersects(monsterProjectile.getCollisionBox())) {
                playerProjectile.alive = false;
                monsterProjectile.alive = false;
                return;
            }
        }
    }

    public void resetGame() {
        // Reset player state and rebuild scene arrays so collected objects/dead monsters return.
        gameOver = false;
        keyHandler.commandNum = 0;
        mapIndicator = 0;

        player.life = player.maxLife;
        player.hasKey = 0;
        player.hasSpKey = 0;
        player.hasCoin = 0;
        player.inventory.clear();
        player.boss1 = false;
        player.x = tileM.playerSpawnX;
        player.y = tileM.playerSpawnY;
        player.velocityX = 0;
        player.velocityY = 0;
        player.invincible = false;
        player.invincibleCounter = 0;
        player.projectile.alive = false;
        player.mana = 5;
        player.resetBoosts();

        obj = new SuperObject[30];
        monster = new Entity[20];
        aSetter.updateScene();
        camera.update(player);
        ui.clearMessages();
        gameState = playState;
    }

    @Override //Die paintComponent() Methode wird ueberschrieben, um die Grafiken des Spiels zu zeichnen.
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); //Panel-Hintergrund korrekt neu zeichnen

        // tempScreen wird geleert, damit alte Frames nicht durchscheinen
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, screenWidth, screenHeight);

        if (gameState == titleState) {
            ui.draw(g2); //draw the title screen
        } else {
            // Draw world first, then entities, then UI overlays last.
            backgroundManager.draw(g2);
            tileM.draw(g2); //zeichnet die Spielkacheln mit der draw() Methode im TileManager
            for (int i = 0; i < obj.length; i++) {
                if (obj[i] != null) {
                    obj[i].draw(g2, this);
                }
            }
            for (int i = 0; i < monster.length; i++) {
                if (monster[i] != null) {
                    monster[i].draw(g2);
                }
            }
            for(int i = 0; i < npc.length; i++) {
                if(npc[i] != null) {
                    npc[i].draw(g2);
                }
            }
            player.draw(g2);

            if (showCollisionDebug) {
                collisionsystem.drawDebugBoxes(g2, player);
            }

            if (player.projectile != null) {
                player.projectile.draw(g2);
            }
            ui.draw(g2); // always last so pause screen renders on top
        }

        // tempScreen wird auf den echten Bildschirm gestreckt - immer zuletzt
        Graphics2D g2Screen = (Graphics2D) g;
        g2Screen.drawImage(tempScreen, 0, 0, getWidth(), getHeight(), null);
        // g2 wird NICHT disposed, da es zu tempScreen gehoert und frameübergreifend wiederverwendet wird
    }
}
