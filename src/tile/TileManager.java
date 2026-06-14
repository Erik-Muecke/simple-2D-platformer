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
        tile[0].name = "transparent"; //name der Tile wird festgelegt, für Kollisionsberechnung relevant

        tile[1] = new Tile();
        tile[1].image = imgLoader.scaleImage("/tiles/wall1.png", gp.tileSize, gp.tileSize);
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
    System.out.println("Fehler beim Laden der Kachelbilder: " + e.getMessage());} //Error Handling, falls die Bilder nicht gefunden werden oder ein anderes Problem auftritt
    }

    public void draw(java.awt.Graphics2D g2) {
        //Hier werden die Kacheln gezeichnet

        int col = 0;
        int row = 0;

        while(col < gp.MaxWorldCol && row < gp.MaxWorldRow) {
            int tileNum = mapTileNum[col][row]; //holt die Nummer der Kachel, die an der Position (col, row) gezeichnet werden soll

            int x = col * gp.tileSize; //bestimmt die y position der Kachel, indem die Spaltennummer mit der Größe der Kachel multipliziert wird
            int y = row * gp.tileSize; //bestimmt die x position der Kachel, indem die Zeilennummer mit der Größe der Kachel multipliziert wird

            // Berechnet die Bildschirmposition der Kachel, indem die Weltkoordinaten (x, y) um die Kameraposition (gp.camera.x, gp.camera.y) verschoben werden
            int screenX = x - gp.camera.x;
            int screenY = y - gp.camera.y;

            g2.drawImage(tile[tileNum].image, screenX, screenY, null); //zeichnet die Kachel an der Position (x, y) mit der Größe von tileSize

            col++; //erhöht den Spaltenzähler um 1


            if(col == gp.MaxWorldCol) { //wenn das Ende der Spalten erreicht ist-0

                col = 0; //setzt den Spaltenzähler zurück auf 0

                row++; //erhöht den Zeilenzähler um 1

            }
        }
    }

    //Diese Funktion sorgt für das Wechseln der Karte. Sie wird in der update Funktion des GamePanels aufgerufen,
    // damit die Karte immer aktualisiert wird, wenn sich der mapIndicator ändert.
    public void update() {
            if (gp.previousmapIndicator != gp.mapIndicator) { //überprüft, ob der mapIndicator sich geändert hat, um unnötiges Neuladen der Karte zu vermeiden
                loadMap(); //lädt die neue Karte basierend auf dem aktuellen mapIndicator
                gp.aSetter.updateObject(); //aktualisiert die Positionen der Objekte auf der Karte, damit sie sich an die neue Karte anpassen

                //neusetzen der Startposition des Spielers
                gp.player.x = playerSpawnX;
                gp.player.y = playerSpawnY;

                gp.previousmapIndicator = gp.mapIndicator; //aktualisiert den previousmapIndicator, damit die Karte nicht erneut geladen wird, bis sich der mapIndicator wieder ändert

                gp.saveHndlr.saveLevel(gp.mapIndicator); //speichern des mapindicators, damit die Karte beim nächsten Start des Spiels wieder geladen werden kann
                gp.saveHndlr.savelives(gp.player.life); //speichern der Leben des Spielers, damit sie beim nächsten Start des Spiels wiederhergestellt werden können
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

            BufferedReader br = new BufferedReader(new InputStreamReader(is)); //lädt die Textdatei in einen Buffered reader um sie einzulesen

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

                    //Setzen der Spawnposition des Spielers, wenn die Kachelnummer 9 ist. gezeichnet wird die Kachel als transparente Kachel
                    if (num == 9) {
                        System.out.println(col + " " + row);

                        //ermittelen des x und y Werts der Spawnposition
                        playerSpawnX = col * gp.tileSize;
                        playerSpawnY = row * gp.tileSize;
                    }

                    col ++; //nächste Spalte
                }
                if(col == gp.MaxWorldCol) {
                    col = 0; //setzt den Spaltenzähler zurück auf 0
                    row ++; //nächste Zeile
                }
            }
            br.close(); //schließt den BufferedReader, um Ressourcen freizugeben

        }catch(Exception e){
            System.out.println("Fehler beim Laden der Karte: " + e.getMessage()); //Error Handling, falls die Karte nicht gefunden wird oder ein anderes Problem auftritt
        }
    }
}
