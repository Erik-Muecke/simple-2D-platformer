package monster;

import entity.Entity;
import main.GamePanel;
import projectile.PT_Fireball;
import projectile.Projectile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

// Bodenschleim der zufällig links/rechts läuft und alle 180 Frames einen horizontalen
// Feuerball in seine aktuelle Blickrichtung schießt.
/**
 * Feuervariante des Schleimgegners mit stärkeren Kampfwerten.
 */
public class FireSlime extends Entity {

    private final GamePanel gp;
    private final Random random = new Random();
    private final Projectile projectile; // der Feuerball den dieser Schleim schießt
    private int shotCounter = 0;         // zählt Frames zwischen den Schüssen

    // Animationsframes als Felder
    private BufferedImage frame1;
    private BufferedImage frame2;

    public FireSlime(GamePanel gp) {
        super();
        this.gp = gp;

        type = TYPE_MONSTER;
        name = "Fire Slime";
        speed = 2;
        width = gp.tileSize;
        height = gp.tileSize;
        direction = 'L';
        directionBeforeKnockBack = 'L';

        maxLife = 5;
        life = maxLife;

        // Kollisionsbox setzen
        solidArea = new Rectangle(0, 0, 48, 48);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        // Bilder beim Erstellen laden, nicht jeden Frame neu
        frame1 = loadImage("/monsters/fireslime.png");
        frame2 = loadImage("/monsters/fireslime1.png");
        image = frame1; // Startbild setzen

        projectile = new PT_Fireball(gp);
    }

    /**
     * Lädt ein Bild aus dem Classpath.
     * Gibt image_not_found.png zurück, falls die Datei fehlt.
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

    // Gibt das Projektil zurück, damit GamePanel Kollisionen mit dem Spieler-Feuerball prüfen kann
    public Projectile getProjectile() {
        return projectile;
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

        // Aktives Projektil bewegen und auf Treffer mit Spieler oder Spieler-Feuerball prüfen
        updateProjectileInteractions();

        // Während des Rückstoßes in Trefferrichtung bewegen und normale KI überspringen
        if (knockBack) {
            boolean stillKnockedBack = gp.movementSystem.updateMonsterKnockBack(this);
            if (!stillKnockedBack) {
                // Schusszähler zurückziehen damit Erholung nicht sofort einen neuen Schuss auslöst
                shotCounter = shotCounter - 10;
            }
            return;
        }

        // Alle 180 Frames ein Projektil in die aktuelle Blickrichtung abfeuern
        shotCounter++;
        if (shotCounter > 180 && !projectile.alive) {
            int projectileX = x + (width - projectile.width) / 2;
            int projectileY = y + (height - projectile.height) / 2;
            projectile.set(projectileX, projectileY, direction, true);
            shotCounter = 0;
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

    // Bewegt das aktive Projektil und prüft ob es den Spieler oder seinen Feuerball trifft
    private void updateProjectileInteractions() {
        if (!projectile.alive) {
            return;
        }

        projectile.update();

        Rectangle projectileBox = projectile.getCollisionBox();

        // Beide Projektile aufheben wenn sie sich in der Luft treffen
        if (gp.player.projectile != null
                && gp.player.projectile.alive
                && projectileBox.intersects(gp.player.projectile.getCollisionBox())) {
            projectile.alive = false;
            gp.player.projectile.alive = false;
            return;
        }

        Rectangle playerBox = new Rectangle(
                gp.player.x + gp.player.solidArea.x,
                gp.player.y + gp.player.solidArea.y,
                gp.player.solidArea.width,
                gp.player.solidArea.height
        );

        // Schaden verursachen wenn das Projektil den Spieler erreicht
        if (projectileBox.intersects(playerBox)) {
            if (!gp.player.invincible) {
                gp.player.life -= projectile.damage;
                gp.player.invincible = true;
            }
            projectile.alive = false;
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        int screenX = x - gp.camera.x;
        int screenY = y - gp.camera.y;

        // Projektil auch zeichnen wenn der Schleim selbst außerhalb des Bildschirms ist
        if (x + width < gp.camera.x ||
                x > gp.camera.x + gp.screenWidth ||
                y + height < gp.camera.y ||
                y > gp.camera.y + gp.screenHeight) {
            projectile.draw(g2);
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

        projectile.draw(g2);
    }
}