package main;

import javax.swing.*;
import java.awt.*;
import entity.Player;
import system.CollisionSystem;
import main.Camera;
import tile.TileManager;
import object.SuperObject;

public class GamePanel extends JPanel implements Runnable {
    final int originalTileSize = 32; // 16x16 tile
    public static final int scale = 2; //resolution of the tiles will be 3 times bigger than the original tile size

    public final int tileSize = originalTileSize * scale; // 48x48 tile Berechnung der endgültigen Tilegröße
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

    public TileManager tileM = new TileManager(this);
    public KeyHandler keyHandler;

    Thread gameThread; //erstellt den Thread für die Spielschleife zum Bestimmen der FPS
    public CollisionSystem collisionsystem = new CollisionSystem(this);
    public Camera camera;
    public SuperObject obj[] = new SuperObject[10];
    AssetSetter aSetter = new AssetSetter(this);
    public Player player; //erstellt eine neue Instanz des Players, damit wir ihn im Spiel verwenden können

    // Game States
    public UI ui;
    public int gameState;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int titleState = 0;

    public GamePanel() {

        this.setPreferredSize(new Dimension(screenWidth, screenHeight)); // Set the size of the panel to the calculated screen width and height
        this.setBackground(Color.BLACK); //Hintergrundfarbe zu schwarz
        this.setDoubleBuffered(true); //Screen wird zuerst unsichtbar gezeichnet und dann sichtbar gemacht, um Flackern zu vermeiden
        System.out.println("GamePanel created"); //Bestätigung, nur zum Debuggen, remove in Production
        camera = new Camera(screenWidth, screenHeight, worldWidth, worldHeight);
        keyHandler = new KeyHandler(this);
        player = new Player(this, keyHandler);
        aSetter.setObject();
        player.x = tileM.playerSpawnX;
        player.y = tileM.playerSpawnY;
        gameState = titleState;
        ui = new UI(this);
    }

    public void startGameThread() {

        gameThread = new Thread(this); //erstellt einen neuen Thread und übergibt die aktuelle Instanz von GamePanel als Runnable
        gameThread.start(); //startet den Thread, wodurch die run() Methode aufgerufen wird

    }

    @Override //Die run() Methode enthält die Hauptspielschleife, die kontinuierlich ausgeführt wird, solange der gameThread nicht null ist
//    public void run() {
//
//        double Zeitintervall = (double) 1000000000 /fps; //Zeitintervall in Nanosekundnen, welches zwischen den Frames liegt.
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
//            durchlauf = durchlauf + 1;
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
            player.update();
            camera.update(player);
        }
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
            tileM.draw(g2);//zeichnet die Spielkacheln mit der draw() Methode im TileManager
            for (int i = 0; i < obj.length; i++) {
                if (obj[i] != null) {
                    obj[i].draw(g2, this);
                }
            }
            player.draw(g2);
            ui.draw(g2);  // always last so pause screen renders on top
        }
        g2.dispose();

    }
}
