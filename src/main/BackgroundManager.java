package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;

public class BackgroundManager {
    GamePanel gp;
    ArrayList<Layer> layers = new ArrayList<>(); // Liste der Hintergrundebenen, jede Ebene enthält ein Bild und einen Parallaxefaktor

    public BackgroundManager(GamePanel gp) {
        this.gp = gp;

        addBackgroundLayer("backgrounds/sky_layer_1.png", 0.2);
        addBackgroundLayer("backgrounds/sky_layer_2.png", 0.4);


    }

    public void addBackgroundLayer(String LayerressourcePath, double parralaxe) {
        // Laden des Hintergrundbildes aus den Ressourcen
        // Speichern des Bildes und des Parallaxefaktors in der Layer-Klasse und Hinzufügen zur Liste der Layer
        try(InputStream is = getClass().getClassLoader().getResourceAsStream(LayerressourcePath)) {
            if (is == null) {
                System.err.println("Background image not found: " + LayerressourcePath); // Fehlermeldung, wenn die Ressource nicht gefunden wird
                return;
            }
            BufferedImage image = ImageIO.read(is); // Laden des Bildes aus dem InputStream als BufferedImage im RAM
            layers.add(new Layer(image, parralaxe)); // Erstellen eines neuen Layer-Objekts mit dem geladenen Bild und dem Parallaxefaktor und dem Hinzufügen zur Array Liste der Layer
        } catch (Exception e) {
            e.printStackTrace(); // Fehlerbehandlung, falls das Bild nicht geladen werden kann oder ein anderes Problem auftritt
        }
    }

    public void draw(Graphics2D g2) {
        for (Layer layer : layers) { // Wiederholung für alle Layer in der Liste
            BufferedImage img = layer.image; //das Bild der aktuellen Ebene wird als BufferedImage im RAM gespeichert
            if (img == null) continue; // Überprüfen, ob das Bild erfolgreich geladen wurde, wenn nicht wird das nächste Element der Liste aufgerufen

            int imgWidth = img.getWidth(); //erfragen der Bildbreite
            int imgHeight = img.getHeight(); //erfragen der Bildhöhe
            double par = layer.parallax; //parallaxe faktor wird übergeben
            double offsetX = (gp.camera.x * par) % imgWidth;//Berechnung des horizontalen Offsets des Hintergrunds
            // basierend auf der Kameraposition und dem Parallaxefaktor
            // durch das % wird nur der Rest der Division ausgegeben
            // Dadurch wird der Hintergrund nahtlos geloopt, da der Offset immer zwischen 0 und der Bildbreite liegt
            double offsetY = (gp.camera.y * par) % imgHeight;

            //Zeichen des Hintergrundes, der Hintergrund wird über dem Bildschirm geloop, daher die for Schleifen.
            //beim Zeichnen wird jeweils der aus der for-Schleife ermittelte Wert von X und Y benutzt, das offset wird abgezogen und in einen Int gecastet
            for (int x = -imgWidth; x < gp.screenWidth + imgWidth; x += imgWidth) {
                for (int y = -imgHeight; y < gp.screenHeight + imgHeight; y += imgHeight) {
                    g2.drawImage(img, (int)(x - offsetX), (int)(y - offsetY), null);
                }
            }
        }
    }

    // Die Layer-Klasse speichert das Hintergrundbild und den Parallaxefaktor für jede Ebene des Hintergrunds. Es handelt sich um eine Record Klasse, daher die andere Schreibweise
    private record Layer(BufferedImage image, double parallax) {
    }
}
