package game;

import game.entity.Entity;
import game.system.CollisionSystem;
import game.system.MovementSystem;
import game.system.RenderSystem;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;

public class GamePanel extends JPanel implements Runnable {
    public KeyHandler keyHandler;
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 2; //resolution of the tiles will be 3 times bigger than the original tile size

    public final int tileSize = originalTileSize * scale; // 48x48 tile Berechnung der endgültigen Tilegröße
    final int MaxScreenCol = 16; //Breite des Bildschirms in Tiles
    final int MaxScreenRow = 12; //Höhe des Bildschirms in Tiles
    final int screenWidth = tileSize * MaxScreenCol; // 768 pixels
    final int screenHeight = tileSize * MaxScreenRow; // 576 pixels

    private Entity player;
    private HashSet<Entity> walls;
    private HashSet<Entity> pFfoods;
    private HashSet<Entity> fireballs;
    private HashSet<Entity> opponents;
    private int score = 0;
    private int lives = 3;
    private boolean gameOver = false;
    private MovementSystem movementSystem;
    private CollisionSystem collisionSystem;
    
    private Image wallImage;
    private Image playerImage;
    private Image opponentImage;
    private Image fireballImage;

    //FPS
    int fps = 60; //Frames per second, die Anzahl der Bilder, die pro Sekunde gezeichnet werden sollen

    Thread gameThread; //erstellt den Thread für die Spielschleife zum Bestimmen der FPS

    public GamePanel(KeyHandler keyHandler) {
    this.keyHandler = keyHandler;
        this.setPreferredSize(new Dimension(screenWidth, screenHeight)); // Set the size of the panel to the calculated screen width and height
        this.setBackground(Color.BLACK); //Hintergrundfarbe zu schwarz
        this.setDoubleBuffered(true); //Screen wird zuerst unsichtbar gezeichnet und dann sichtbar gemacht, um Flackern zu vermeiden
        setFocusable(true);
        wallImage = new ImageIcon(getClass().getResource("wall.png")).getImage();
        playerLeftImage = new ImageIcon(getClass().getResource("playerLeft.png")).getImage();
        playerRightImage = new ImageIcon(getClass().getResource("playerRight.png")).getImage();
        opponentImage = new ImageIcon(getClass().getResource("opponent.png")).getImage();
        fireballImage= new ImageIcon(getClass().getResource("fireball.png")).getImage();
        System.out.println("GamePanel created"); //Bestätigung, nur zum Debuggen, remove in Production
        collisionSystem = new CollisionSystem();
        movementSystem = new MovementSystem(screenWidth, screenHeight, tileSize, collisionSystem);
        loadMap();
        for (Entity opponent : opponents) {
            char newDirection = directions[random.nextInt(2)];
            movementSystem.updateOpponentDirection(opponent, newDirection, walls);
        }
    }

    public void startGameThread() {

        gameThread = new Thread(this); //erstellt einen neuen Thread und übergibt die aktuelle Instanz von GamePanel als Runnable
        gameThread.start(); //startet den Thread, wodurch die run() Methode aufgerufen wird

    }
    
    private String[] tileMap1 = {
            "XXXXXXXXXXXXXXXX",
            "X              X",
            "X              X",
            "X              X",
            "X              X",
            "X              X",
            "X        X     X",
            "X       X      X",
            "X     X        X",
            "X   XX         X",
            "X P        o   X",
            "XXXXXXXXXXXXXXXX"};

    
    
    public void loadMap() {
        walls = new HashSet<Entity>();
        pFfoods = new HashSet<Entity>();
        fireballs = new HashSet<Entity>();
        opponents = new HashSet<Entity>();

        for (int r = 0; r < MaxScreenRow; r++) {
            for (int c = 0; c < MaxScreenCol; c++) {
                String row = tileMap1[r];
                char tileMapChar = row.charAt(c);
                int x = c * tileSize;
                int y = r * tileSize;

                if (tileMapChar == 'X') {
                    Entity wall = new Entity(wallImage, x, y, tileSize, tileSize, 0);
                    walls.add(wall);
                } else if (tileMapChar == 'o'){
                    Entity opponent = new Entity(opponentImage, x, y, tileSize, tileSize, 6);
                    opponents.add(opponent);
                } else if (tileMapChar == 'P') {
                    player = new Entity(playerRightImage, x, y, tileSize, tileSize, 8);
                } else if (tileMapChar == 'F') {
                    Entity pFfood = new Entity(null, x + 12, y + 12, 8, 8, 0);
                    pFfoods.add(pFfood);
                }
            }
        }
    }

    @Override //Die run() Methode enthält die Hauptspielschleife, die kontinuierlich ausgeführt wird, solange der gameThread nicht null ist
//    public void run() {
//
//        double Zeitintervall = (double) 1000000000 /fps; //Zeitintervall in Nanosekundnen, welches zwischen den Frames liegt
//        double naechsteUpdateZeit = System.nanoTime() + Zeitintervall; //Zeitpunkt , an dem der GameLoop wweitergeht
//        while(gameThread != null) {
//
//            long currentTime = System.nanoTime(); //gibt die Zeit der aktuellen Ausführung der JVM in Nanosekunden zurück.
//            System.out.println("Current Time:" + currentTime);
//
//            //UPDATE Informationen werden aktualisiert: z.B. Positionen der Spielobjekte, Kollisionen, etc.
//            update();
//            //REPAINT Informationen werden gezeichnet: z.B. die Grafiken der Spielobjekte, Hintergrund, etc.
//            repaint();
//
//
//            try {
//                double uebrigeZeit = naechsteUpdateZeit - System.nanoTime(); //berechnet die verbleibende Zeit bis zum nächsten Update, indem die aktuelle Zeit von der geplanten nächsten Update-Zeit abgezogen wird
//                // Es wird also die Zeit zwischen dem anfang der Schleife und dem Abschluss derSchleife berechnet, damit die übrige Zeit, in der nichts
//                // passieren soll ermittelt werden kann
//                uebrigeZeit = uebrigeZeit / 1000000; //Umwandlung von Nanosekunden in Millisekunden, da die sleep() Methode Millisekunden erwartet
//                Thread.sleep((long) uebrigeZeit);
//                if (uebrigeZeit < 0) {
//                    uebrigeZeit = 0; //Wenn die verbleibende Zeit negativ ist, wird sie auf 0 gesetzt, da der Thread bei negativen werten eine Exception ausgibt.
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            naechsteUpdateZeit += Zeitintervall; //Zeitintervall wird zur Zielzeit addiert, umd die nächste Zeit zum Updaten zu berechnen
//        }
//
//    }
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
            if (delta >= 1) {
                if (!gameOver) {
                    update();   // only run game logic if NOT game over
                }
                repaint();      // always draw (so Game Over screen shows)
                delta--;
                drawCount++;
            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
                if(timer >= 1000000000) { //Wenn der Timer eine Sekunde erreicht oder überschreitet, wird die Anzahl der gezeichneten Frames pro Sekunde ausgegeben und der Timer sowie der drawCount zurückgesetzt
                    System.out.println("FPS: " + drawCount);
                    drawCount = 0;
                    timer = 0;
                }
            }
        }
    }

    public void update() {
        if (player.direction == 'L') player.image = playerLeftImage;
        else if (player.direction == 'R') player.image = playerRightImage;

        if (player.velocityX != 0) {
            movementSystem.updatePlayerVelocity(player);
        }
        if (keyHandler.upPressed && player.onGround == true) {
            player.velocityY = -20;
            player.onGround = false;
        } else if (keyHandler.leftPressed) {
            player.direction = 'L';
            movementSystem.updatePlayerVelocity(player);
        } else if (keyHandler.rightPressed) {
            player.direction = 'R';
            movementSystem.updatePlayerVelocity(player);
        } else {
            player.velocityX = 0;
        }

    movementSystem.updatePlayer(player, walls);
    }
        
    

    @Override //Die paintComponent() Methode wird überschrieben, um die Grafiken des Spiels zu zeichnen. Sie wird automatisch aufgerufen, wenn das Panel neu gezeichnet werden muss.
    public void paintComponent(Graphics g) {
        super.paintComponent(g); //ruft die paintComponent() Methode der übergeordneten Klasse auf, um sicherzustellen, dass das Panel korrekt gezeichnet wird, bevor die benutzerdefinierte Zeichnung erfolgt

        RenderSystem renderSystem = new RenderSystem(tileSize);
        renderSystem.draw(g, player, opponents, walls, pFfoods, fireballs,
              lives, score, gameOver); //zeichnet den Spieler auf dem Panel, indem die draw() Methode des Player-Objekts aufgerufen wird und das Graphics-Objekt übergeben wird
    }
}
