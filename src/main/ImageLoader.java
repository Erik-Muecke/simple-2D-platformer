package main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.io.InputStream;

public class ImageLoader {


    public BufferedImage loadImage(String imagePath) {
        try (InputStream is = getClass().getResourceAsStream(imagePath)) {
            if (is == null) {
                throw new Exception("Ressource nicht gefunden: " + imagePath);
            }
            return ImageIO.read(is);
        } catch (Exception e) {
            System.out.println("Fehler beim Laden der Bildressourcen: " + e.getMessage());
            return loadmissingImage();
        }

    }

    public BufferedImage scaleImage(String imagePath, int width, int height) {
        BufferedImage original = loadImage(imagePath); //lädt das Bild, damit es im RAM gespeichert ist, bevor es skaliert wird
        BufferedImage scaledImage = new BufferedImage(width, height, original.getType()); //erstellt ein neues BufferedImage im Ram
        Graphics2D g2 = scaledImage.createGraphics(); //neues Graphics 2D Objekt, wird zum zeichnen benötigt
        g2.drawImage(original, 0, 0, width, height, null); //Originalbild wird mit neuen Parametern auf das Buffered Image gezeichnet,
        // das BufferedImage ist im RAM gespeichert
        g2.dispose(); //gibt den Speicher des Graphics2D-Objekts frei, da es nicht mehr benötigt wird
        return scaledImage; //return des skalierten Bildes
    }

    public BufferedImage loadmissingImage() {
        try (InputStream no_image = getClass().getResourceAsStream("/missing/image_not_found.png")) {
            if (no_image == null) {
                throw new Exception("Ressource nicht gefunden: /missing/image_not_found.png");
            }
            return ImageIO.read(no_image);

        } catch (Exception f) {
            System.out.println("Fehler beim Laden der Standardfehlermeldung: " + f.getMessage());
            return new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB); //leeres Bild als Notfallrückgabe, damit keine NullPointer exception ershceint
        }
    }
}

