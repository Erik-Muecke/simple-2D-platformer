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

// Der finale Boss — ein großer Schleim der den Spieler verfolgt, periodisch springt
// und Feuerbälle schießt.
//Bossgegner mit hoher Gesundheit, Berührungsschaden und Fernkampfdruck.
public class BossSlime extends Entity {

    private final GamePanel gp;
    private final Projectile projectile; // der Feuerball den der Boss auf den Spieler schießt
    private int shotCounter = 0;         // zählt Frames zwischen den Schüssen
    private final int jumpStrength = 25; // aufwärts Geschwindigkeit die beim Sprung angewendet wird
    private int jumpPower;               // sammelt sich jeden Frame an; löst bei 300 einen Sprung aus

    // Animationsframes als Felder
    private BufferedImage frame1;
    private BufferedImage frame2;

    public BossSlime(GamePanel gp) {
        super();
        this.gp = gp;

        type = TYPE_MONSTER;
        name = "Boss Slime";
        speed = 2;
        width = (int)(gp.tileSize * 1.5);  // etwas größer als ein normales Tile
        height = (int)(gp.tileSize * 1.5);
        direction = 'L';
        directionBeforeKnockBack = 'L';
        maxLife = 30;
        life = maxLife;

        // Kollisionsbox setzen
        solidArea = new Rectangle(0, 0, 72, 72);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        // Bilder beim Erstellen laden, nicht jeden Frame neu
        frame1 = loadImage("/monsters/bossslime.png");
        frame2 = loadImage("/monsters/bossslime1.png");
        image = frame1; // Startbild setzen

        projectile = new PT_Fireball(gp);
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

    // Gibt das Projektil zurück, damit GamePanel Kollisionen mit dem Spieler-Feuerball prüfen kann
    public Projectile getProjectile() {
        return projectile;
    }

    // Dreht den Boss jeden Frame zum Spieler hin
    public void setAction() {
        if (gp.player.x < this.x) {
            this.direction = 'L';
        } else if (gp.player.x > this.x) {
            this.direction = 'R';
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
        // Boss-Logik ist komplett deaktiviert bis der Spieler den Bosskampf auslöst
//        if (gp.player.boss1) {

        // Sprungladung jeden Frame aufbauen
        jumpPower++;

        // Unverwundbarkeits-Frames nach einem Treffer herunterzählen
        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 20) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

        // Sprung auslösen wenn genug Ladung aufgebaut ist und der Boss auf dem Boden ist
        if (jumpPower >= 300 && onGround) {
            gp.movementSystem.startJump(this, jumpStrength);
            jumpPower = 0;
        }

        // Projektilbewegung und Kollision mit Spieler / Spieler-Feuerball verarbeiten
        updateProjectileInteractions();

        // Während des Rückstoßes in Trefferrichtung bewegen und normale KI überspringen
        if (knockBack) {
            boolean stillKnockedBack = gp.movementSystem.updateMonsterKnockBack(this);
            if (!stillKnockedBack) {
                // Schusszähler reduzieren damit der Boss nach der Erholung nicht sofort schießt
                shotCounter = shotCounter - 5;
            }
            return;
        }

        // Alle 150 Frames ein Projektil in Richtung Spieler abfeuern
        shotCounter++;
        if (shotCounter > 150 && !projectile.alive) {
            int projectileX = x + (width - projectile.width) / 2;
            int projectileY = y + (height - projectile.height) / 2;
            projectile.set(projectileX, projectileY, direction, true);
            shotCounter = 0;
        }

        // Freeze-Frames pausieren kurz alle Bewegungen nach einem Treffer
        if (freezeFrames > 0) {
            freezeFrames--;
            return;
        }

        setAction();
        setWalking();
        gp.movementSystem.updateWalkingMonster(this);
    }
//    }

    // Bewegt das aktive Projektil und prüft ob es den Spieler trifft oder vom Spieler-Feuerball abgebrochen wird
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

        // Projektil auch zeichnen wenn der Boss selbst außerhalb des Bildschirms ist
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

        // Bild zeichnen oder rotes Rechteck als Fallback, falls kein Bild vorhanden
        if (image != null) {
            g2.drawImage(image, screenX, screenY, width, height, null);
        } else {
            g2.setColor(Color.RED);
            g2.fillRect(screenX, screenY, width, height);
        }

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        // Lebensanzeige über dem Boss sobald er Schaden erlitten hat
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