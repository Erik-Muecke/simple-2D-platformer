package tile;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {

    GamePanel gp;
    Tile[] tile; //Array um die verschiedenen Kacheln zu speichern
    int mapTileNum[][]; //2D Array um die Nummer der Kachel zu speichern, die an jeder Position gezeichnet werden soll

    public TileManager(GamePanel gp) {
        this.gp = gp;

        tile = new Tile[10]; //konstruiert 10 Tile Objekte im Tile Array

        mapTileNum = new int[GamePanel.MaxScreenCol][GamePanel.MaxScreenRow];

        getTileImage();

        loadMap();
    }


    public void getTileImage() {

    try {

        tile[0] = new Tile();  //erstellt ein neues Tile Objekt an der Stelle 0 im Array
        tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/grass.png"));  //laedt das Bild aus dem res Ordner

        tile[1] = new Tile();
        tile[1].image = ImageIO.read(getClass().getResourceAsStream("/tiles/wall.png"));

        tile[2] = new Tile();
        tile[2].image = ImageIO.read(getClass().getResourceAsStream("/tiles/earth.png"));

    }catch(Exception e){
    System.out.println("Fehler beim Laden der Kachelbilder: " + e.getMessage());}
    }

    public void draw(java.awt.Graphics2D g2) {
        //Hier werden die Kacheln gezeichnet
        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;

        while(col < GamePanel.MaxScreenCol && row < GamePanel.MaxScreenRow) {
            int tileNum = mapTileNum[col][row]; //holt die Nummer der Kachel, die an der Position (col, row) gezeichnet werden soll

            g2.drawImage(tile[tileNum].image, x, y, gp.tileSize, gp.tileSize, null); //zeichnet die Kachel an der Position (x, y) mit der Größe von tileSize

            col++; //erhöht den Spaltenzähler um 1

            x += gp.tileSize; //erhöht die x-Position um die Größe einer Kachel

            if(col == GamePanel.MaxScreenCol) { //wenn das Ende der Spalten erreicht ist

                col = 0; //setzt den Spaltenzähler zurück auf 0
                x = 0; //setzt die x-Position zurück auf 0

                row++; //erhöht den Zeilenzähler um 1

                y += gp.tileSize; //erhöht die y-Position um die Größe einer Kachel
            }

        }

    }
    public void loadMap() {
        //Hier wird die Karte geladen, indem die Nummer der Kachel für jede Position im mapTileNum Array festgelegt wird
        try {
            InputStream is = getClass().getResourceAsStream("tilemap.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while(col < GamePanel.MaxScreenCol && row < GamePanel.MaxScreenRow) {
                String line = br.readLine(); //liest eine Zeile aus der Textdatei

                while(col < GamePanel.MaxScreenCol) {

                    String numbers[] = line.split("  ");

                    int num = Integer.parseInt(numbers[col]); //Gelesene Zahl wird von String zu Integer konvertiert

                    mapTileNum[col][row] = num;
                    col ++; //nächste Spalte
                }
                if(col == GamePanel.MaxScreenCol) {
                    col = 0; //setzt den Spaltenzähler zurück auf 0
                    row ++; //nächste Zeile
                }


                row++; //erhöht den Zeilenzähler um 1
            }
            br.close();


        }catch(Exception e){
            System.out.println("Fehler beim Laden der Karte: " + e.getMessage());
        }
    }
}
