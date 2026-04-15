package main;

import javax.swing.*;
import java.awt.*;
import entity.Player;

public class GamePanel extends JPanel implements Runnable {
    final int originalTileSize = 32; // 16x16 tile
    public static final int scale = 2; //resolution of the tiles will be 3 times bigger than the original tile size

    public final int tileSize = originalTileSize * scale; // 48x48 tile Berechnung der endgültigen Tilegröße
    final static int MaxScreenCol = 16; //Breite des Bildschirms in Tiles
    final static int MaxScreenRow = 12; //Höhe des Bildschirms in Tiles
    final public int screenWidth = tileSize * MaxScreenCol; // 768 pixels
    final public int screenHeight = tileSize * MaxScreenRow; // 576 pixels


    //FPS
    int fps = 60; //Frames per second, die Anzahl der Bilder, die pro Sekunde gezeichnet werden sollen

    static KeyHandler keyHandler = new KeyHandler();

    Thread gameThread; //erstellt den Thread für die Spielschleife zum Bestimmen der FPS

    Player player = new Player(this, keyHandler); //erstellt eine neue Instanz des Players, damit wir ihn im Spiel verwenden können

    public GamePanel() {

        this.setPreferredSize(new Dimension(screenWidth, screenHeight)); // Set the size of the panel to the calculated screen width and height
        this.setBackground(Color.BLACK); //Hintergrundfarbe zu schwarz
        this.setDoubleBuffered(true); //Screen wird zuerst unsichtbar gezeichnet und dann sichtbar gemacht, um Flackern zu vermeiden
        System.out.println("GamePanel created"); //Bestätigung, nur zum Debuggen, remove in Production

    }

    public void startGameThread() {

        gameThread = new Thread(this); //erstellt einen neuen Thread und übergibt die aktuelle Instanz von GamePanel als Runnable
        gameThread.start(); //startet den Thread, wodurch die run() Methode aufgerufen wird

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

        player.update(); //aktualisiert die Informationen des Spielers, indem die update() Methode des Player-Objekts aufgerufen wird

    }


    @Override //Die paintComponent() Methode wird ueberschrieben, um die Grafiken des Spiels zu zeichnen.
    protected void paintComponent(Graphics g) {

        super.paintComponent(g); //Panel-Hintergrund korrekt neu zeichnen

        player.draw(g); //zeichnet den Spieler auf dem Panel
    }
}
