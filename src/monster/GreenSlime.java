package monster;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import entity.Entity;
import main.GamePanel;

public class GreenSlime extends Entity {

    private final GamePanel gp;
    private final Random random = new Random();

    public GreenSlime(GamePanel gp) {
        super();
        this.gp = gp;

        type = TYPE_MONSTER;
        name = "Green Slime";
        speed = 2;
        width = gp.tileSize;
        height = gp.tileSize;
        direction = 'L';

        solidArea = new java.awt.Rectangle(8, 16, gp.tileSize - 16, gp.tileSize - 16);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        maxLife = 3;
        life = maxLife;
    }

    // Zufääiges änder der Bewegungsrichtung alle 2 Sekunden
    public void setAction() {
        actionLockCounter++;

        if (actionLockCounter >= 120) {
            if (random.nextBoolean()) {
                direction = 'L';
            } else {
                direction = 'R';
            }
            actionLockCounter = 0;
        }
    }

    // update-Methode, überschreibt die update Methode der Entity-Klasse
    @Override
    public void update() {
        // Wenn der Slime unverwundbar ist, erhöht sich der invincibleCounter. Nach 40 Frames wird die Unverwundbarkeit aufgehoben.
        if (invincible) {
            invincibleCounter++;

            if (invincibleCounter > 40) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

        // Wenn der Slime gerade eine Kollision hatte, wird er für 15 Frames bewegungsunfähig (freezeFrames). Während dieser Zeit wird die setAction-Methode nicht aufgerufen,
        // damit der Slime nicht sofort die Richtung ändert.
        if (freezeFrames > 0) {
            freezeFrames--;
            return;
        }

        //aufruf der setAction Methode, um die Bewegungsrichtung zu bestimmen
        setAction();

        // Berechnung der horizontalen Bewegung basierend auf der aktuellen Richtung
        char horizontalDirection = direction;
        if (horizontalDirection == 'L') {
            velocityX = -speed;
        } else {
            velocityX = speed;
        }

        // Aktualisierung der x-Position basierend auf der horizontalen Geschwindigkeit
        x += velocityX;
        direction = horizontalDirection;
        collisionOn = false;
        gp.collisionsystem.collidesT(this);
        gp.collisionsystem.collidesWithObject(this);

        // Wenn eine Kollision erkannt wird oder der Slime den Rand der Welt erreicht, wird die Bewegung rückgängig gemacht und die Richtung geändert.
        if (collisionOn || x <= 0 || x + width >= gp.worldWidth) {
            x -= velocityX;
            if (horizontalDirection == 'L') {
                direction = 'R';
            } else {
                direction = 'L';
            }
            velocityX = 0;
            freezeFrames = 15;
        }

        // Berechnung der vertikalen Bewegung (Schwerkraft)
        velocityY += 2;
        if (velocityY > 31) {
            velocityY = 31;
        }

        // Aktualisierung der y-Position basierend auf der vertikalen Geschwindigkeit
        y += velocityY;
        char savedDirection = direction;
        direction = 'D';
        collisionOn = false;
        gp.collisionsystem.collidesT(this);
        gp.collisionsystem.collidesWithObject(this);
        direction = savedDirection;

        // Wenn eine Kollision erkannt wird, wird die vertikale Bewegung rückgängig gemacht
        if (collisionOn) {
            y -= velocityY;
            velocityY = 0;
            onGround = true;
        } else {
            onGround = false;
        }

        // Überprüfen, ob der Slime mit dem Spieler kollidiert. Wenn ja, wird die damagePlayer-Methode des Spielers aufgerufen, um Schaden zu verursachen.
        if (gp.collisionsystem.collidesWithPlayer(this)) {
            gp.player.damagePlayer();
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        // Berechnung der Bildschirmposition basierend auf der Kameraposition
        int screenX = x - gp.camera.x;
        int screenY = y - gp.camera.y;

        // Überprüfen, ob der Slime innerhalb des sichtbaren Bereichs der Kamera liegt. Wenn nicht, wird die Methode verlassen, um unnötiges Zeichnen zu vermeiden.
        if (x + width < gp.camera.x ||
                x > gp.camera.x + gp.screenWidth ||
                y + height < gp.camera.y ||
                y > gp.camera.y + gp.screenHeight) {
            return;
        }

        // Wenn der Slime unverwundbar ist, wird die Transparenz auf 40% gesetzt, um dies visuell darzustellen.
        if (invincible) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
        }

        // Zeichnen des Slimes
        g2.setColor(new Color(58, 176, 84));
        g2.fillOval(screenX + 6, screenY + 18, width - 12, height - 22);

        g2.setColor(new Color(25, 92, 45));
        g2.fillOval(screenX + 20, screenY + 30, 7, 7);
        g2.fillOval(screenX + width - 27, screenY + 30, 7, 7);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        // Zeichnen der Lebensleiste über dem Slime, wenn er nicht volle Lebenspunkte hat
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
