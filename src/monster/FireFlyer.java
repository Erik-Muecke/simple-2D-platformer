package monster;

import entity.Entity;
import main.GamePanel;
import projectile.PT_Fireball;
import projectile.Projectile;

import javax.imageio.ImageIO;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

// Ein fliegender Feind der in der Luft schwebt und zwei gleichzeitige Feuerbälle nach unten abfeuert.
// Verfolgt den Spieler horizontal und schießt in Salven.
/**
 * Fliegender Feuergegner der patrouilliert und aus der Luft angreift.
 */
public class FireFlyer extends Entity {

    private final GamePanel gp;
    private final Random random = new Random();
    private final Projectile projectile;  // linker Feuerball
    private final Projectile projectile2; // rechter Feuerball, wird gleichzeitig mit dem ersten abgefeuert
    private int shotCounter = 0;          // zählt Frames zwischen den Schüssen

    public FireFlyer(GamePanel gp) {
        super();
        this.gp = gp;

        type = TYPE_MONSTER;
        name = "Fire Flyer";
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

        projectile  = new PT_Fireball(gp);
        projectile2 = new PT_Fireball(gp);
        image = loadImage("/monsters/FireFlyer.png");
    }

//  Lädt ein Bild aus dem Classpath.
//  Gibt image_not_found.png zurück, falls die Datei fehlt.
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

    // Gibt das primäre Projektil zurück damit GamePanel Kollisionen in der Luft prüfen kann
    public Projectile getProjectile() {
        return projectile;
    }

    // Dreht den Gegner jeden Frame zum Spieler hin
    public void setAction() {
        if(gp.player.x < this.x){
            this.direction = 'L';
        } else if(gp.player.x > this.x){
            this.direction = 'R';
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

        // Projektilbewegung und Kollision mit Spieler / Spieler-Feuerball verarbeiten
        updateProjectileInteractions();

        // Während des Rückstoßes in Trefferrichtung bewegen und normale KI überspringen
        if (knockBack) {
            boolean stillKnockedBack = gp.movementSystem.updateMonsterKnockBack(this);
            if (!stillKnockedBack) {
                // Schusszähler reduzieren damit der Gegner nach der Erholung nicht sofort schießt
                shotCounter = shotCounter - 10;
            }
            return;
        }

        // Alle 180 Frames zwei Projektile nebeneinander abfeuern wenn beide inaktiv sind
        shotCounter++;
        if (shotCounter > 180 && !projectile.alive && !projectile2.alive) {
            int centerX = x + (width - projectile.width) / 2;
            int centerY = y + (height - projectile.height) / 2;

            projectile.set(centerX - 18, centerY, 'D', true);  // linker Feuerball mit Versatz
            projectile2.set(centerX + 18, centerY, 'D', true); // rechter Feuerball mit Versatz
            shotCounter = 0;
        }

        // Freeze-Frames pausieren kurz alle Bewegungen nach einem Treffer
        if (freezeFrames > 0) {
            freezeFrames--;
            return;
        }

        setAction();
        gp.movementSystem.updateFlyingMonster(this);
    }

    // Delegiert die Projektilaktualisierung an eine gemeinsame Hilfsmethode
    private void updateProjectileInteractions() {
        handleProjectile(projectile);
        handleProjectile(projectile2);
    }

    // Bewegt das gegebene Projektil und prüft ob es den Spieler oder den Spieler-Feuerball trifft
    private void handleProjectile(Projectile p) {
        if (!p.alive) return;

        p.update();

        Rectangle projectileBox = p.getCollisionBox();

        // Beide Projektile aufheben wenn sie sich in der Luft treffen
        if (gp.player.projectile != null
                && gp.player.projectile.alive
                && projectileBox.intersects(gp.player.projectile.getCollisionBox())) {
            p.alive = false;
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
                gp.player.life -= p.damage;
                gp.player.invincible = true;
            }
            p.alive = false;
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        int screenX = x - gp.camera.x;
        int screenY = y - gp.camera.y;

        // Projektile auch zeichnen wenn der Gegner außerhalb des Bildschirms ist
        if (x + width < gp.camera.x ||
                x > gp.camera.x + gp.screenWidth ||
                y + height < gp.camera.y ||
                y > gp.camera.y + gp.screenHeight) {
            projectile.draw(g2);
            projectile2.draw(g2);
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

        projectile.draw(g2);
        projectile2.draw(g2);
    }
}