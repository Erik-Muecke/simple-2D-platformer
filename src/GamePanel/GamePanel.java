package GamePanel;

import javax.swing.*;
import java.awt.*;
import Entity.Player;

public class GamePanel extends JPanel implements Runnable {
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3; //resolution of the tiles will be 3 times bigger than the original tile size

    public final int tileSize = originalTileSize * scale; // 48x48 tile Berechnung der endgültigen Tilegröße
    final int MaxScreenCol = 16; //Breite des Bildschirms in Tiles
    final int MaxScreenRow = 12; //Höhe des Bildschirms in Tiles
    final int screenWidth = tileSize * MaxScreenCol; // 768 pixels
    final int screenHeight = tileSize * MaxScreenRow; // 576 pixels

    public Player player;


    Thread gameThread; //erstellt den Thread für die Spielschleife zum Bestimmen der FPS

    public GamePanel() {

        this.setPreferredSize(new Dimension(screenWidth, screenHeight)); // Set the size of the panel to the calculated screen width and height
        this.setBackground(Color.BLACK); //Hintergrundfarbe zu schwarz
        this.setDoubleBuffered(true); //Screen wird zuerst unsichtbar gezeichnet und dann sichtbar gemacht, um Flackern zu vermeiden
        System.out.println("GamePanel created"); //Bestätigung, nur zum Debuggen, remove in Production
        player = new Player(null, 100, 100, tileSize, tileSize, 1); //erstellt eine neue Instanz des Players, damit wir ihn im Spiel verwenden können
    }

    public void startGameThread() {

        gameThread = new Thread(this); //erstellt einen neuen Thread und übergibt die aktuelle Instanz von GamePanel als Runnable
        gameThread.start(); //startet den Thread, wodurch die run() Methode aufgerufen wird

    }

    @Override //Die run() Methode enthält die Hauptspielschleife, die kontinuierlich ausgeführt wird, solange der gameThread nicht null ist
    public void run() {

        while(gameThread != null) {

            //System.out.println("Game loop running"); //Bestätigung, nur zum Debuggen, remove in Production

            //UPDATE Informationen werden aktualisiert: z.B. Positionen der Spielobjekte, Kollisionen, etc.
            update();
            //REPAINT Informationen werden gezeichnet: z.B. die Grafiken der Spielobjekte, Hintergrund, etc.
            repaint();
        }
    }
    public void update() {
        //Hier werden die Spielobjekte aktualisiert, z.B. Positionen, Kollisionen, etc.
        if(Main.Game.keyHandler.upPressed){ //true kann in der Syntax auch weggelassen werden. überprüft, ob die taste gedrückt wurde.
            System.out.println("Up key is pressed"); //Bestätigung, nur zum Debuggen und da ich für heute schluss mache. remove in Production
        }
    }

    @Override //Die paintComponent() Methode wird überschrieben, um die Grafiken des Spiels zu zeichnen. Sie wird automatisch aufgerufen, wenn das Panel neu gezeichnet werden muss.
    public void paint(Graphics g) {

        super.paintComponent(g); //ruft die paintComponent() Methode der übergeordneten Klasse auf, um sicherzustellen, dass das Panel korrekt gezeichnet wird, bevor die benutzerdefinierte Zeichnung erfolgt

        player.draw(g); //zeichnet den Spieler auf dem Panel, indem die draw() Methode des Player-Objekts aufgerufen wird und das Graphics-Objekt übergeben wird
    }
}
