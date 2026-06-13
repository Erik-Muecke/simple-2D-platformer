package tile; // Verwaltet alle Kacheln (Tiles) und das Laden der Map

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {

    GamePanel gp;
    public Tile[] tile; // Speichert alle Tile-Arten (z.B. Erde, Wasser, Wand)
    public int mapTileNum[][]; // Speichert für jede Position die Tile-ID der Map

    public TileManager(GamePanel gp) {
        this.gp = gp;

        tile = new Tile[30]; // Array für alle verfügbaren Tiles
        mapTileNum = new int[gp.MaxWorldCol][gp.MaxWorldRow]; // 2D Map-Gitter (Spalten x Reihen)

        getTileImage(); // lädt alle Tile-Bilder und Eigenschaften
        loadMap(); // lädt die aktuelle Map aus einer Textdatei
    }

    public void getTileImage() {
        try {
            tile[0] = new Tile(); tile[0].image = new BufferedImage(gp.tileSize, gp.tileSize, BufferedImage.TYPE_INT_ARGB); tile[0].name = "transparent";

            tile[1] = new Tile(); tile[1].image = loadTileImage("/tiles/wall.png"); tile[1].collision = true; tile[1].name = "wall";
            tile[2] = new Tile(); tile[2].image = loadTileImage("/tiles/earth.png"); tile[2].name = "earth"; tile[2].collision = true;

            tile[3] = new Tile(); tile[3].image = loadTileImage("/tiles/water.png"); tile[3].name = "water";

            tile[4] = new Tile(); tile[4].image = loadTileImage("/tiles/tree.png"); tile[4].name = "tree";
            tile[5] = new Tile(); tile[5].image = loadTileImage("/tiles/tree1.png"); tile[5].name = "tree1";

            tile[6] = new Tile(); tile[6].image = loadTileImage("/tiles/sand.png"); tile[6].name = "sand";

            tile[7] = new Tile(); tile[7].image = loadTileImage("/tiles/spike.png"); tile[7].name = "spike";
            tile[8] = new Tile(); tile[8].image = loadTileImage("/tiles/downspike.png"); tile[8].name = "spike";

            tile[9] = new Tile(); tile[9].image = loadTileImage("/tiles/coin.png"); tile[9].name = "coin";

            tile[10] = new Tile(); tile[10].image = loadTileImage("/tiles/dungeonwall.png"); tile[10].name = "dungeonwall";

            tile[11] = new Tile(); tile[11].image = loadTileImage("/tiles/torchwall.png"); tile[11].name = "torchwall";

            tile[12] = new Tile(); tile[12].image = loadTileImage("/tiles/earth1.png"); tile[12].name = "earthwall";

            tile[13] = new Tile(); tile[13].image = loadTileImage("/tiles/earthedge.png"); tile[13].name = "earth"; tile[13].collision = true;

            tile[17] = new Tile(); tile[17].image = loadTileImage("/tiles/dungeonspike.png"); tile[17].name = "spike";
            tile[18] = new Tile(); tile[18].image = loadTileImage("/tiles/dungeondownspike.png"); tile[18].name = "spike";

            tile[21] = new Tile(); tile[21].image = loadTileImage("/tiles/wall.png"); tile[21].name = "fakewall";
            tile[22] = new Tile(); tile[22].image = loadTileImage("/tiles/earth.png"); tile[22].name = "fakeearth";

        } catch(Exception e) {
            System.out.println("Fehler beim Laden der Kachelbilder: " + e.getMessage());
        }
    }

    public void draw(java.awt.Graphics2D g2) {
        int col = 0;
        int row = 0;

        while(col < gp.MaxWorldCol && row < gp.MaxWorldRow) {

            int tileNum = mapTileNum[col][row]; // Tile-ID an dieser Position

            int x = col * gp.tileSize; // Weltposition X
            int y = row * gp.tileSize; // Weltposition Y

            int screenX = x - gp.camera.x; // Kamera-Verschiebung X
            int screenY = y - gp.camera.y; // Kamera-Verschiebung Y

            if (isValidTile(tileNum)) {
                g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }

            col++;

            x += gp.tileSize;

            if(col == gp.MaxWorldCol) {
                col = 0;
                x = 0;
                row++;
                y += gp.tileSize;
            }
        }
    }

    public void update() {
        if (gp.previousmapIndicator != gp.mapIndicator) {
            int previousMap = gp.previousmapIndicator;

            gp.setMapDimensions(gp.mapIndicator); // passt Weltgröße an Map an

            mapTileNum = new int[gp.MaxWorldCol][gp.MaxWorldRow]; // neue leere Map

            loadMap(); // Map neu laden

            gp.camera.worldWidth = gp.worldWidth;
            gp.camera.worldHeight = gp.worldHeight;

            gp.player.x = playerSpawnX; // Spieler-Spawn X
            gp.player.y = playerSpawnY; // Spieler-Spawn Y
            gp.player.setPreviousSafePosition();

            gp.aSetter.updateScene(); // Objekte & Gegner neu setzen

            switch (gp.mapIndicator) {
                case 0: break;
                case 1: break;
                case 2: break;
                case 3: break;
                case 4: break;
                case 5: break;
            }

            gp.previousmapIndicator = gp.mapIndicator;
            if (previousMap != -1) {
                gp.saveLoad.save();
            }
        }
    }

    public int playerSpawnX = 0;
    public int playerSpawnY = 0;

    public boolean mapExists(int mapIndex) {
        return getMapPath(mapIndex) != null;
    }

    private String getMapPath(int mapIndex) {
        String numberedMapPath = "/tiles/tilemap" + mapIndex + ".txt";
        if (getClass().getResource(numberedMapPath) != null) {
            return numberedMapPath;
        }
        return null;
    }

    public void loadMap() {
        try {
            String mapPath = getMapPath(gp.mapIndicator);
            if (mapPath == null) throw new Exception("Map nicht gefunden");

            InputStream is = getClass().getResourceAsStream(mapPath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;
            playerSpawnX = 0;
            playerSpawnY = 0;

            while(col < gp.MaxWorldCol && row < gp.MaxWorldRow) {

                String line = br.readLine();
                if(line == null) throw new IllegalStateException("Map zu kurz");

                String numbers[] = line.trim().split("\\s+");

                while(col < gp.MaxWorldCol) {

                    int num = Integer.parseInt(numbers[col]);

                    if (num != 99 && !isValidTile(num)) {
                        throw new IllegalStateException("Ungültige Tile-ID");
                    }

                    mapTileNum[col][row] = num;

                    if (num == 99) { // 99 = Spieler-Spawnpunkt
                        playerSpawnX = col * gp.tileSize;
                        playerSpawnY = row * gp.tileSize;

                        switch (gp.mapIndicator) {
                            case 0: mapTileNum[col][row] = 0; break;
                            case 1: mapTileNum[col][row] = 0; break;
                            case 2: mapTileNum[col][row] = 10; break;
                            case 3: mapTileNum[col][row] = 0; break;
                            case 4: mapTileNum[col][row] = 0; break;
                            case 5: mapTileNum[col][row] = 0; break;
                        }
                    }

                    col++;
                }

                if(col == gp.MaxWorldCol) {
                    col = 0;
                    row++;
                }
            }

            br.close();

        } catch(Exception e) {
            System.out.println("Fehler beim Laden der Karte: " + e.getMessage());
        }
    }

    public java.awt.image.BufferedImage loadTileImage(String imagePath) {
        try (InputStream is = getClass().getResourceAsStream(imagePath)) {
            if (is == null) throw new Exception("Bild nicht gefunden");
            return ImageIO.read(is);

        } catch (Exception e) {
            try(InputStream no_image = getClass().getResourceAsStream("/missing/image_not_found.png")){
                return ImageIO.read(no_image);
            } catch(Exception f){
                return null;
            }
        }
    }

    private boolean isValidTile(int tileNum) {
        return tileNum >= 0
                && tileNum < tile.length
                && tile[tileNum] != null
                && tile[tileNum].image != null;
    }
}
