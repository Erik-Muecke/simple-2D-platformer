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

// Ein fliegender Feind der den Spieler aktiv horizontal und vertikal verfolgt,
// auf Spielerhöhe schwebt und alle 180 Frames einen Feuerball in Blickrichtung abfeuert.
// Ignoriert Rückstoß und verursacht Berührungsschaden.
/**
 * Fliegender Gegner der schwebt während er den Spieler verfolgt oder angreift.
 */
public class HoverFlyer extends Entity {

    private final GamePanel gp;
    private final Random random = new Random();
    private final Projectile projectile; // der Feuerball den dieser Gegner abfeuert
    private int shotCounter = 0;         // zählt Frames zwischen den Schüssen
    int speedy;                          // vertikale Verfolgungsgeschwindigkeit, getrennt von der horizontalen

    public HoverFlyer(GamePanel gp) {
        super();
        this.gp = gp;

        type = TYPE_MONSTER;
        name = "Hover Flyer";
        speed = 0;   // horizontale Bewegung wird direkt in setAction gesteuert, nicht über speed
        speedy = 2;  // vertikale Pixel pro Frame in Richtung der Y-Position des Spielers
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

        projectile = new PT_Fireball(gp);

        image = imgLoader.scaleImage("/monsters/HoverFlyer.png", width, height);

    }


    // Gibt das Projektil zurück damit GamePanel Kollisionen mit dem Spieler-Feuerball prüfen kann
    public Projectile getProjectile() {
        return projectile;
    }

    // Dreht den Gegner horizontal zum Spieler und bewegt ihn vertikal auf die Y-Position des Spielers
    public void setAction() {

        // Horizontal verfolgen
        if (gp.player.x < x) {
            direction = 'L';
        } else if (gp.player.x > x) {
            direction = 'R';
        }

        // Vertikal bewegen mit Kollision
        if (gp.player.y < y) {

            y -= speedy;
            collisionOn = false;
            gp.collisionsystem.collidesT(this);

            if (collisionOn) {
                y += speedy; // Bewegung rückgängig machen
            }

        } else if (gp.player.y > y) {

            y += speedy;
            collisionOn = false;
            gp.collisionsystem.collidesT(this);

            if (collisionOn) {
                y -= speedy; // Bewegung rückgängig machen
            }
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

        // HoverFlyer reagiert nie auf Rückstoß — er bleibt immer auf seinem Kurs
        knockBack = false;

        // Projektilbewegung und Kollision mit Spieler / Spieler-Feuerball verarbeiten
        updateProjectileInteractions();

        // Alle 180 Frames ein Projektil in aktueller Blickrichtung abfeuern
        shotCounter++;
        if (shotCounter > 180 && !projectile.alive) {
            int centerX = x + (width - projectile.width) / 2;
            int centerY = y + (height - projectile.height) / 2;

            projectile.set(centerX - 18, centerY, direction, true); // leichter Versatz zum Spawnen an der Seite
            shotCounter = 0;
        }

        // Freeze-Frames pausieren kurz alle Bewegungen nach einem Treffer
        if (freezeFrames > 0) {
            freezeFrames--;
            return;
        }

        // Berührungsschaden verursachen wenn der Spieler diesen Gegner berührt
        if (gp.collisionsystem.collidesWithPlayer(this)) {
            gp.player.damagePlayer();
        }
        collisionOn = false; // Flag zurücksetzen das als Nebeneffekt von collidesWithPlayer gesetzt wurde

        setAction();
    }

    // Delegiert die Projektilaktualisierung an eine gemeinsame Hilfsmethode
    private void updateProjectileInteractions() {
        handleProjectile(projectile);
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

        // Projektil auch zeichnen wenn der Gegner selbst außerhalb des Bildschirms ist
        if (x + width < gp.camera.x ||
                x > gp.camera.x + gp.screenWidth ||
                y + height < gp.camera.y ||
                y > gp.camera.y + gp.screenHeight) {
            projectile.draw(g2);
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
    }
}