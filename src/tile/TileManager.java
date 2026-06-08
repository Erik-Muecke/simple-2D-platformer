package tile;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {

    GamePanel gp;
    public Tile[] tile; //Array um die verschiedenen Kacheln zu speichern
    public int mapTileNum[][]; //2D Array um die Nummer der Kachel zu speichern, die an jeder Position gezeichnet werden soll

    public TileManager(GamePanel gp) {

        this.gp = gp;
        // Tile IDs in the text maps index directly into this array.
        tile = new Tile[30]; //konstruiert 10 Tile Objekte im Tile Array
        mapTileNum = new int[gp.MaxWorldCol][gp.MaxWorldRow];
        getTileImage();

        loadMap();
    }

    public void getTileImage() {

        try {
            tile[0] = new Tile();  //erstellt ein neues Tile Objekt an der Stelle 0 im Array
            tile[0].image = new BufferedImage(gp.tileSize, gp.tileSize, BufferedImage.TYPE_INT_ARGB);
            tile[0].name = "transparent";

            tile[1] = new Tile();
            tile[1].image = loadTileImage("/tiles/wall.png");
            tile[1].collision = true;
            tile[1].name = "wall";

            tile[2] = new Tile();
            tile[2].image = loadTileImage("/tiles/earth.png");
            tile[2].name = "earth";
            tile[2].collision = true;

            tile[3] = new Tile();
            tile[3].image = loadTileImage("/tiles/water.png");
            tile[3].name = "water";

            tile[4] = new Tile();
            tile[4].image = loadTileImage("/tiles/tree.png");
            tile[4].name = "tree";

            tile[5] = new Tile();
            tile[5].image = loadTileImage("/tiles/tree1.png");
            tile[5].name = "tree1";

            tile[6] = new Tile();
            tile[6].image = loadTileImage("/tiles/sand.png");
            tile[6].name = "sand";

            tile[7] = new Tile();
            tile[7].image = loadTileImage("/tiles/spike.png");
            tile[7].name = "spike";

            tile[8] = new Tile();
            tile[8].image = loadTileImage("/tiles/downspike.png");
            tile[8].name = "spike";

            tile[9] = new Tile();
            tile[9].image = loadTileImage("/tiles/coin.png");
            tile[9].name = "coin";

            tile[10] = new Tile();
            tile[10].image = loadTileImage("/tiles/dungeonwall.png");
            tile[10].name = "dungeonwall";

            tile[11] = new Tile();
            tile[11].image = loadTileImage("/tiles/torchwall.png");
            tile[11].name = "torchwall";

            tile[12] = new Tile();
            tile[12].image = loadTileImage("/tiles/earth1.png");
            tile[12].name = "earthwall";

            tile[17] = new Tile();
            tile[17].image = loadTileImage("/tiles/dungeonspike.png");
            tile[17].name = "spike";

            tile[18] = new Tile();
            tile[18].image = loadTileImage("/tiles/dungeondownspike.png");
            tile[18].name = "spike";

            tile[21] = new Tile();
            tile[21].image = loadTileImage("/tiles/wall.png");
            tile[21].name = "fakewall";

            tile[22] = new Tile();
            tile[22].image = loadTileImage("/tiles/earth.png");
            tile[22].name = "fakeearth";

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

            if (isValidTile(tileNum)) {
                g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null); //zeichnet die Kachel an der Position (x, y) mit der Größe von tileSize
            }

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
            // Rebuild map state only when the level number actually changes.
            gp.setMapDimensions(gp.mapIndicator);
            mapTileNum = new int[gp.MaxWorldCol][gp.MaxWorldRow];
            loadMap();
            gp.camera.worldWidth = gp.worldWidth;
            gp.camera.worldHeight = gp.worldHeight;
            gp.player.x = playerSpawnX;
            gp.player.y = playerSpawnY;
            gp.aSetter.updateScene();
            switch (gp.mapIndicator) {
                case 0:
                    //TODO: hier die jeweiligen platzierungen der objekte, Gegner, Backgrounds für die Karte festlegen.
                    //Startpositionen ebenso
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
            }
            gp.previousmapIndicator = gp.mapIndicator;
        }

    }

    public int playerSpawnX = 0;
    public int playerSpawnY = 0;

    public boolean mapExists(int mapIndex) {
        return getMapPath(mapIndex) != null;
    }

    private String getMapPath(int mapIndex) {
        // Numbered maps are preferred; tilemap4.txt remains as a fallback for the old map 2 file.
        String numberedMapPath = "/tiles/tilemap" + mapIndex + ".txt";
        if (getClass().getResource(numberedMapPath) != null) {
            return numberedMapPath;
        }
        return null;
    }

    public void loadMap() {
        //Hier wird die Karte geladen, indem die Nummer der Kachel für jede Position im mapTileNum Array festgelegt wird
        try {
            String mapPath = getMapPath(gp.mapIndicator);
            if (mapPath == null) {
                throw new Exception("Ressource nicht gefunden: /tiles/tilemap" + gp.mapIndicator + ".txt"); //erstellt neue Exception, wenn der wert null ist
            }
            InputStream is = getClass().getResourceAsStream(mapPath); //öffnet die Textdatei

            BufferedReader br = new BufferedReader(new InputStreamReader(is)); //lädt die Textdatei in einen Buffered reader um sie zu benutzen

            int col = 0;
            int row = 0;
            playerSpawnX = 0;
            playerSpawnY = 0;

            while(col < gp.MaxWorldCol && row < gp.MaxWorldRow) {
                String line = br.readLine(); //liest eine Zeile aus der Textdatei
                if(line == null) {
                    throw new IllegalStateException("Tilemap hat weniger als " + gp.MaxWorldRow + " Zeilen."); //erstellt neue Exception, wenn die Zeile null ist, also das Ende der Datei erreicht ist, bevor alle Zeilen gelesen wurden
                }
                String numbers[] = line.trim().split("\\s+"); //teilt die zeile in einzelne Strings ein, wenn sie durch Leerzeichen getrennt sind.

                while(col < gp.MaxWorldCol) {

                    if (col >= numbers.length) {
                        System.out.println("Row " + row + " only has " + numbers.length + " columns, expected " + gp.MaxWorldCol);
                        break;
                    }

                    int num = Integer.parseInt(numbers[col]); //Gelesene Zahl wird von String zu Integer konvertiert

                    // 99 is a marker for the player spawn and is replaced before drawing/collision.
                    if (num != 99 && !isValidTile(num)) {
                        throw new IllegalStateException("Ungueltige Tile-ID " + num + " bei Spalte " + col + ", Zeile " + row);
                    }

                    mapTileNum[col][row] = num;

                    if (num == 99) {
                        playerSpawnX = col * gp.tileSize;
                        playerSpawnY = row * gp.tileSize;
                        // Replace spawn markers with the correct floor tile for the current map.
                        switch (gp.mapIndicator) {
                            case 0:
                                mapTileNum[col][row] = 0; // replace with earth so it doesn't render as spawn tile
                                break;
                            case 1:
                                mapTileNum[col][row] = 0; // replace with earth so it doesn't render as spawn tile
                                break;
                            case 2:
                                mapTileNum[col][row] = 10;
                                break;
                            case 3:
                                mapTileNum[col][row] = 0;
                                break;
                            case 4:
                                mapTileNum[col][row] = 0;
                                break;
                        }
                        gp.previousmapIndicator = gp.mapIndicator;
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

    public java.awt.image.BufferedImage loadTileImage(String imagePath) {
        // Loading through getResourceAsStream keeps images bundled with the compiled resources.
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

    private boolean isValidTile(int tileNum) {
        // Guards drawing/collision against malformed map files.
        return tileNum >= 0
                && tileNum < tile.length
                && tile[tileNum] != null
                && tile[tileNum].image != null;
    }
}
