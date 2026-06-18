package monster;

import entity.Entity;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.awt.Rectangle;

/**
 * Schneller Schleimgegner, der aggressiver als normale Schleime vorgeht.
 */
public class SpeedSlime extends Entity {

    private final GamePanel gp;
    private final Random random = new Random();

    // Animationsframes als Felder
    private BufferedImage frame1;
    private BufferedImage frame2;

    public SpeedSlime(GamePanel gp) {
        super();
        this.gp = gp;

        type = TYPE_MONSTER;
        name = "Speed Slime";
        speed = 2;
        width = gp.tileSize;
        height = gp.tileSize;
        direction = 'L';
        directionBeforeKnockBack = 'L';

        // Kollisionsbox setzen
        solidArea = new Rectangle(0, 0, 48, 48);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        maxLife = 4;
        life = maxLife;

        // Bilder beim Erstellen laden, nicht jeden Frame neu
        frame1 = loadImage("/monsters/speedslime.png");
        frame2 = loadImage("/monsters/speedslime1.png");
        image = frame1; // Startbild setzen
    }

    /**
     * Lädt ein Bild aus dem Classpath.
     * Gibt ein magenta Platzhalterbild zurück, falls die Datei fehlt.
     */
    private BufferedImage loadImage(String path) {
        try (InputStream stream = getClass().getResourceAsStream(path)) {
            if (stream != null) return ImageIO.read(stream); // Bild laden, falls gefunden
        } catch (IOException e) {
            System.err.println("Fehler beim Laden: " + path);
        }

        // Fallback: image_not_found.png laden
        try (InputStream stream = getClass().getResourceAsStream("/missing/image_not_found.png")) {
            if (stream != null) return ImageIO.read(stream);
        } catch (IOException e) {
            System.err.println("Fallback Fehler: " + e.getMessage());
        }

        return null;
    }

    // Wechselt alle 120 Frames zufällig die Richtung
    public void setAction() {
        actionLockCounter++;
        if (actionLockCounter >= 120) {
            direction = random.nextBoolean() ? 'L' : 'R';
            actionLockCounter = 0;
        }
    }

    // Wechselt zwischen zwei Frames für eine einfache Laufanimation
    public void setWalking() {
        walkingCounter++;
        if (walkingCounter >= 20) {
            image = frame1;
        }
        if (walkingCounter >= 40) {
            image = frame2;
            walkingCounter = 0;
        }
    }

    // Verdoppelt die Geschwindigkeit, wenn der Spieler innerhalb von ~5 Tiles ist; kehrt zur normalen Geschwindigkeit zurück, wenn er weiter entfernt ist
    public void setSpeed() {
        if (gp.player.x > this.x + 6 * gp.tileSize || gp.player.x < this.x - 5 * gp.tileSize) {
            speed = 2; // normale Patrouilliengeschwindigkeit wenn Spieler weit entfernt
        } else {
            speed = 4; // aggressive Geschwindigkeit wenn Spieler in der Nähe
        }
    }

    @Override
    public void update() {
        setSpeed(); // Geschwindigkeit jeden Frame basierend auf Spielernähe aktualisieren

        // Unverwundbarkeits-Frames nach einem Treffer herunterzählen
        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 40) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

        // Während des Rückstoßes in Trefferrichtung bewegen und normale KI überspringen
        if (knockBack) {
            gp.movementSystem.updateMonsterKnockBack(this);
            return;
        }

        // Freeze-Frames pausieren kurz die Bewegung nach einem Treffer
        if (freezeFrames > 0) {
            freezeFrames--;
            return;
        }

        setAction();
        setWalking();
        gp.movementSystem.updateWalkingMonster(this);
    }

    @Override
    public void draw(Graphics2D g2) {
        int screenX = x - gp.camera.x;
        int screenY = y - gp.camera.y;

        // Außerhalb des Bildschirms: nicht zeichnen
        if (x + width < gp.camera.x ||
                x > gp.camera.x + gp.screenWidth ||
                y + height < gp.camera.y ||
                y > gp.camera.y + gp.screenHeight) {
            return;
        }

        // Halbtransparent blinken während der Unverwundbarkeit
        if (invincible) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
        }

        // Bild zeichnen oder rotes Rechteck als Fallback, falls kein Bild vorhanden
        if (image != null) {
            g2.drawImage(image, screenX, screenY, width, height, null);
        } else {
            g2.setColor(Color.RED);
            g2.fillRect(screenX, screenY, width, height);
        }

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        // Lebensanzeige sobald Schaden erlitten wurde
        if (life < maxLife) {
            int barWidth = width - 12;
            int currentLifeWidth = barWidth * life / maxLife;

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85f));
            g2.setColor(Color.DARK_GRAY);
            g2.fillRect(screenX + 6, screenY + 6, barWidth, 6);
            g2.setColor(Color.RED);
            g2.fillRect(screenX + 6, screenY + 6, currentLifeWidth, 6);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }
    }
}