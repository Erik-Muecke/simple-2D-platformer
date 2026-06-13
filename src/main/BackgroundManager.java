package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;

public class BackgroundManager {

    GamePanel gp; // access to camera and screen size
    ArrayList<Layer> layers = new ArrayList<>(); // list of background layers (parallax layers)

    public BackgroundManager(GamePanel gp) {
        this.gp = gp;

        // add layered backgrounds with different parallax speeds (far = slower, near = faster)
        addBackgroundLayer("Backgroundlayers/sky_layer_1.png", 0.2);
        addBackgroundLayer("Backgroundlayers/sky_layer_2.png", 0.4);
    }

    public void addBackgroundLayer(String LayerressourcePath, double parralaxe) {

        // loads an image and stores it with a parallax factor
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(LayerressourcePath)) {

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

        // render all background layers with parallax scrolling
        for (Layer layer : layers) {

            BufferedImage img = layer.image;
            if (img == null) continue;

            int imgWidth = img.getWidth(); // width of one tile of background image
            int imgHeight = img.getHeight(); // height of one tile of background image

            double par = layer.parallax; // parallax strength (how fast it moves relative to camera)

            // offset based on camera position (creates parallax illusion)
            double offsetX = (gp.camera.x * par) % imgWidth;
            double offsetY = (gp.camera.y * par) % imgHeight;

            // tile background infinitely across screen
            for (int x = -imgWidth; x < gp.screenWidth + imgWidth; x += imgWidth) {
                for (int y = -imgHeight; y < gp.screenHeight + imgHeight; y += imgHeight) {

                    g2.drawImage(img, (int)(x - offsetX), (int)(y - offsetY), null);
                }
            }
        }
    }

    // small container for image + parallax factor
    private record Layer(BufferedImage image, double parallax) {
    }
}