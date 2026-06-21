package monster;

import entity.Entity;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

// Ein zäher fliegender Feind mit hoher Gesundheit. Fliegt auf den Spieler zu und verursacht Berührungsschaden.
// Ignoriert Rückstoß — er setzt seinen Kurs nach einem Treffer einfach fort.
//Robuster fliegender Gegner mit langsamerer Bewegung und höherer Gesundheit.
public class HeavyFlyer extends Entity {

    private final GamePanel gp;

    public HeavyFlyer(GamePanel gp) {
        super();
        this.gp = gp;

        type = TYPE_MONSTER;
        name = "Heavy Flyer";
        speed = 2;
        width = gp.tileSize;
        height = gp.tileSize;
        direction = 'L';
        directionBeforeKnockBack = 'L';

        // Kollisionsbox setzen
        solidArea = new Rectangle(0, 0, 48, 48);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        maxLife = 10; // deutlich mehr Gesundheit als normale Flyer
        life = maxLife;
        image = imgLoader.scaleImage("/monsters/HeavyFlyer.png", width, height); // Bild auf die Größe der Kacheln skalieren
    }


    @Override
    public void update() {
        // Unverwundbarkeits-Frames nach einem Treffer herunterzählen
        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 40) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

        // HeavyFlyer ignoriert Rückstoßbewegung — Blickrichtung vor dem Treffer einfach wiederherstellen
        if (knockBack) {
            direction = directionBeforeKnockBack;
            knockBackCounter = 0;
            knockBack = false;
        }

        // Freeze-Frames pausieren kurz alle Bewegungen nach einem Treffer
        if (freezeFrames > 0) {
            freezeFrames--;
            return;
        }

        // Berührungsschaden verursachen wenn der Spieler in diesen Gegner läuft
        if (gp.collisionsystem.collidesWithPlayer(this)) {
            gp.player.damagePlayer();
        }
        collisionOn = false; // Flag zurücksetzen das als Nebeneffekt von collidesWithPlayer gesetzt wurde

        gp.movementSystem.updateFlyingMonster(this);
    }


    @Override
    public void draw(Graphics2D g2) {
        int screenX = x - gp.camera.x;
        int screenY = y - gp.camera.y;

        // Zeichnen komplett überspringen wenn außerhalb des Bildschirms — kein Projektil zu zeichnen
        if (x + width < gp.camera.x ||
                x > gp.camera.x + gp.screenWidth ||
                y + height < gp.camera.y ||
                y > gp.camera.y + gp.screenHeight) {
            return;
        }

        // Halbtransparent blinken während der Unverwundbarkeit um Trefferfeedback zu geben
        if (invincible) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
        }

        g2.drawImage(image, screenX, screenY, width, height, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        // Lebensanzeige über dem Gegner sobald er Schaden erlitten hat
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