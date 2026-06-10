package tile;

import main.GamePanel;
import main.ImageLoader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class TileManager {

    GamePanel gp;
    public Tile[] tile; //Array um die verschiedenen Kacheln zu speichern
    public int[][] mapTileNum; //2D Array um die Nummer der Kachel zu speichern, die an jeder Position gezeichnet werden soll

    ImageLoader imgLoader = new ImageLoader();

    public TileManager(GamePanel gp) {

        this.gp = gp;

        tile = new Tile[20]; //konstruiert 10 Tile Objekte im Tile Array

        mapTileNum = new int[gp.MaxWorldCol][gp.MaxWorldRow];

        getTileImage();

        loadMap();
    }

    public void getTileImage() {

    try {

        tile[0] = new Tile();  //erstellt ein neues Tile Objekt an der Stelle 0 im Array
        tile[0].image = imgLoader.scaleImage("/tiles/transparent.png", gp.tileSize, gp.tileSize); //Funktion aus ImageLoader wird aufgerufen um das Bild automatisch zu skalieren.
        tile[0].name = "transparent";

        tile[1] = new Tile();
        tile[1].image = imgLoader.scaleImage("/tiles/wall.png", gp.tileSize, gp.tileSize);
        tile[1].collision = true;
        tile[0].name = "wall";

        tile[2] = new Tile();
        tile[2].image = imgLoader.scaleImage("/tiles/earth.png", gp.tileSize, gp.tileSize);
        tile[0].name = "earth";

        tile[3] = new Tile();
        tile[3].image = imgLoader.scaleImage("/tiles/water.png", gp.tileSize, gp.tileSize);
        tile[0].name = "water";

        tile[9] = new Tile();
        tile[9].image = imgLoader.scaleImage("/tiles/transparent.png", gp.tileSize, gp.tileSize);
        tile[9].collision = false;
        tile[0].name = "transparent";

        tile[10] = new Tile();
        tile[10].image = imgLoader.scaleImage("/tiles/sand.png", gp.tileSize, gp.tileSize);
        tile[0].name = "sand";



    }catch(Exception e){
    System.out.println("Fehler beim Laden der Kachelbilder: " + e.getMessage());}
    }

    public void draw(java.awt.Graphics2D g2) {
        //Hier werden die Kacheln gezeichnet
        int col = 0;
        int row = 0;

        while(col < gp.MaxWorldCol && row < gp.MaxWorldRow) {
            int tileNum = mapTileNum[col][row]; //holt die Nummer der Kachel, die an der Position (col, row) gezeichnet werden soll

            int x = col * gp.tileSize;
            int y = row * gp.tileSize;

            int screenX = x - gp.camera.x;
            int screenY = y - gp.camera.y;//getting Screen X and Y

            g2.drawImage(tile[tileNum].image, screenX, screenY, null); //zeichnet die Kachel an der Position (x, y) mit der Größe von tileSize

            col++; //erhöht den Spaltenzähler um 1

            x += gp.tileSize; //erhöht die x-Position um die Größe einer Kachel

            if(col == gp.MaxWorldCol) { //wenn das Ende der Spalten erreicht ist-0

                col = 0; //setzt den Spaltenzähler zurück auf 0
                x = 0; //setzt die x-Position zurück auf 0

                row++; //erhöht den Zeilenzähler um 1

                y += gp.tileSize; //erhöht die y-Position um die Größe einer Kachel
            }
        }
    }

    public void update() {
            if (gp.previousmapIndicator != gp.mapIndicator) {
                gp.player.x = 2 * gp.tileSize;
                gp.player.y = 8 * gp.tileSize;
                loadMap();
                gp.aSetter.updateObject();
                switch (gp.mapIndicator) {
                    case 0:
                        //TODO: hier die jeweiligen platzierungen der objekte, Gegner, Backgrounds für die Karte festlegen.
                        //Startpositionen ebenso
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }
                gp.previousmapIndicator = gp.mapIndicator;
                System.out.println(gp.mapIndicator);
                gp.saveHndlr.saveLevel(gp.mapIndicator);
                System.out.println(gp.saveHndlr.loadLevel());
            }

    }

    public int playerSpawnX = 0;
    public int playerSpawnY = 0;

    public void loadMap() {
        //Hier wird die Karte geladen, indem die Nummer der Kachel für jede Position im mapTileNum Array festgelegt wird
        try {
            InputStream is = getClass().getResourceAsStream("/tiles/tilemap" + gp.mapIndicator + ".txt"); //öffnet die Textdatei

            if (is == null) {
                throw new Exception("Ressource nicht gefunden: /tiles/tilemap" + gp.mapIndicator + ".txt"); //erstellt neue Exception, wenn der wert null ist
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is)); //lädt die Textdatei in einen Buffered reader um sie zu benutzen

            int col = 0;
            int row = 0;

            while(col < gp.MaxWorldCol && row < gp.MaxWorldRow) {
                String line = br.readLine(); //liest eine Zeile aus der Textdatei
                if(line == null) {
                    throw new IllegalStateException("Tilemap hat weniger als " + gp.MaxWorldRow + " Zeilen."); //erstellt neue Exception, wenn die Zeile null ist, also das Ende der Datei erreicht ist, bevor alle Zeilen gelesen wurden
                }
                String numbers[] = line.trim().split("\\s+"); //teilt die zeile in einzelne Strings ein, wenn sie durch Leerzeichen getrennt sind.

                while(col < gp.MaxWorldCol) {

                    int num = Integer.parseInt(numbers[col]); //Gelesene Zahl wird von String zu Integer konvertiert

                    mapTileNum[col][row] = num;

                    if (num == 9) {
                        playerSpawnX = col * gp.tileSize;
                        playerSpawnY = row * gp.tileSize;
                        mapTileNum[col][row] = 0; // replace with earth so it doesn't render as spawn tile
                    }

                    col ++; //nächste Spalte
                }
                if(col == gp.MaxWorldCol) {
                    col = 0; //setzt den Spaltenzähler zurück auf 0
                    row ++; //nächste Zeile
                }
            }
            br.close();

        }catch(Exception e){
            System.out.println("Fehler beim Laden der Karte: " + e.getMessage());
        }
    }

    public BufferedImage loadTileImage(String imagePath) {
        try (InputStream is = getClass().getResourceAsStream(imagePath)) {
            if  (is == null) {
                throw new Exception("Ressource nicht gefunden: " + imagePath);
            }
            return ImageIO.read(is);
        } catch (Exception e) {
            System.out.println("Fehler beim Laden der Kachelbilder: " + e.getMessage());
            try(InputStream no_image = getClass().getResourceAsStream("/missing/image_not_found.png")){
                if (no_image == null) {
                    throw new Exception("Ressource nicht gefunden: /missing/image_not_found.png");
                }
                return ImageIO.read(no_image);

            }catch(Exception f){
                System.out.println("Fehler beim Laden der Standardfehlermeldung: " + f.getMessage());
                return null;
            }
        }
    }
}
