package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;

public class BackgroundManager {
    GamePanel gp;
    ArrayList<Layer> layers = new ArrayList<>();

    public BackgroundManager(GamePanel gp) {
        this.gp = gp;

        addBackgroundLayer("backgrounds/sky_layer_1.png", 0.2);
        addBackgroundLayer("backgrounds/sky_layer_2.png", 0.4);


    }

    public void addBackgroundLayer(String LayerressourcePath, double parralaxe) {
        // Load the background image from the specified resource path
        // and store it along with the parallax factor for rendering
        try(InputStream is = getClass().getClassLoader().getResourceAsStream(LayerressourcePath)) {
            if (is == null) {
                System.err.println("Background image not found: " + LayerressourcePath);
                return;
            }
            BufferedImage image = ImageIO.read(is);
            layers.add(new Layer(image, parralaxe));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        for (Layer layer : layers) {
            BufferedImage img = layer.image;
            if (img == null) continue;

            int imgWidth = img.getWidth(); //erfragen der Bildbreite
            int imgHeight = img.getHeight(); //erfragen der Bildhöhe
            double par = layer.parallax; //parallaxe faktor wird übergeben
            double offsetX = (gp.camera.x * par) % imgWidth;//Berechnung des horizontalen Offsets des Hintergrunds
            // basierend auf der Kameraposition und dem Parallaxefaktor
            //durch das % wird nur der Rest der Division ausgegeben
            double offsetY = (gp.camera.y * par) % imgHeight;

            //Zeichen des Hintergrundes, der Hintergrund wird über dem Bildschirm geloop, daher die for Schleifen.
            //beim Zeichnen wird jeweils der aus der for-Schleife ermittelte Wert von X und Y benutzt und das offset wird abgezogen und in einen Int gecastet
            for (int x = -imgWidth; x < gp.screenWidth + imgWidth; x += imgWidth) {
                for (int y = -imgHeight; y < gp.screenHeight + imgHeight; y += imgHeight) {
                    g2.drawImage(img, (int)(x - offsetX), (int)(y - offsetY), null);
                }
            }
        }
    }

    private record Layer(BufferedImage image, double parallax) {
    }
}
