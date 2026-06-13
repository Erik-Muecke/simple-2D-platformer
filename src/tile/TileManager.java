package tile;

import main.GamePanel;
import main.ImageLoader;

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
        tile[0].image = imgLoader.scaleImage("/tiles/transparent.png", gp.tileSize, gp.tileSize); //Funktion aus ImageLoader wird aufgerufen, um das Bild automatisch zu skalieren.
        tile[0].name = "transparent";

        tile[1] = new Tile();
        tile[1].image = imgLoader.scaleImage("/tiles/wall.png", gp.tileSize, gp.tileSize);
        tile[1].collision = true;
        tile[0].name = "wall";

        tile[2] = new Tile();
        tile[2].image = imgLoader.scaleImage("/tiles/earth.png", gp.tileSize, gp.tileSize);
        tile[2].collision = true;
        tile[2].name = "earth";

        tile[3] = new Tile();
        tile[3].image = imgLoader.scaleImage("/tiles/grass.png", gp.tileSize, gp.tileSize);
        tile[3].collision = true;
        tile[3].name = "grass";

        tile[4] = new Tile();
        tile[4].image = imgLoader.scaleImage("/tiles/spike.png", gp.tileSize, gp.tileSize);
        tile[4].name = "spike";

        tile[5] = new Tile();
        tile[5].image = imgLoader.scaleImage("/tiles/transparent.png", gp.tileSize, gp.tileSize);
        tile[5].name = "spike";

        tile[9] = new Tile();
        tile[9].image = imgLoader.scaleImage("/tiles/transparent.png", gp.tileSize, gp.tileSize);
        tile[9].collision = false;
        tile[9].name = "transparent";

        tile[10] = new Tile();
        tile[10].image = imgLoader.scaleImage("/tiles/sand.png", gp.tileSize, gp.tileSize);
        tile[10].name = "sand";



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


            if(col == gp.MaxWorldCol) { //wenn das Ende der Spalten erreicht ist-0

                col = 0; //setzt den Spaltenzähler zurück auf 0

                row++; //erhöht den Zeilenzähler um 1

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
                        gp.player.x = playerSpawnX;
                        gp.player.y = playerSpawnY;
                        break;
                    case 1:
                        gp.player.x = playerSpawnX;
                        gp.player.y = playerSpawnY;
                        break;
                    case 2:
                        gp.player.x = playerSpawnX;
                        gp.player.y = playerSpawnY;
                        break;
                    case 3:
                        gp.player.x = playerSpawnX;
                        gp.player.y = playerSpawnY;
                        break;
                    case 4:
                        gp.player.x = playerSpawnX;
                        gp.player.y = playerSpawnY;
                        break;
                    case 5:
                        gp.player.x = playerSpawnX;
                        gp.player.y = playerSpawnY;
                        break;
                }
                gp.previousmapIndicator = gp.mapIndicator;
                System.out.println(gp.mapIndicator);
                gp.saveHndlr.saveLevel(gp.mapIndicator);
                gp.saveHndlr.savelives(gp.player.life);
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
                String []numbers = line.trim().split("\\s+"); //teilt die zeile in einzelne Strings ein, wenn sie durch Leerzeichen getrennt sind.

                while(col < gp.MaxWorldCol) {

                    int num = Integer.parseInt(numbers[col]); //Gelesene Zahl wird von String zu Integer konvertiert

                    mapTileNum[col][row] = num;

                    if (num == 9) {
                        System.out.println(col + " " + row);
                        playerSpawnX = col * gp.tileSize;
                        playerSpawnY = row * gp.tileSize;
                        System.out.println(playerSpawnX + " " + playerSpawnY + " " + mapTileNum[col][row]);
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
}
