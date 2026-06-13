package monster;

import entity.Entity;
import main.GamePanel;
import object.OBJ_Coin;
import object.OBJ_HealingPotion;
import object.OBJ_Heart;
import object.OBJ_ManaPotion;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

// A tanky flying enemy with high health. Flies toward the player and deals contact damage.
// Ignores knockback — it simply resumes its path after being hit.
public class HeavyFlyer extends Entity {

    private final GamePanel gp;

    public HeavyFlyer(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = TYPE_MONSTER;
        name = "Heavy Flyer";
        speed = 2;
        width = gp.tileSize;
        height = gp.tileSize;
        setImageAndSolidAreaFromBlackPixels("/monsters/heavyflyer");
        direction = 'L';
        directionBeforeKnockBack = 'L';

        maxLife = 15; // significantly more health than standard flyers
        life = maxLife;
    }

    @Override
    public void update() {
        // Count down invincibility frames after being hit
        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 40) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

        // HeavyFlyer ignores knockback movement; just restore the pre-hit facing direction
        if (knockBack) {
            direction = directionBeforeKnockBack;
            knockBackCounter = 0;
            knockBack = false;
        }

        // Freeze frames pause movement briefly after being hit
        if (freezeFrames > 0) {
            freezeFrames--;
            return;
        }

        // Deal contact damage if the player walks into this enemy
        if (gp.collisionsystem.collidesWithPlayer(this)) {
            gp.player.damagePlayer();
        }
        collisionOn = false; // reset the flag set as a side-effect by collidesWithPlayer

        gp.movementSystem.updateFlyingMonster(this);
    }

    // Random loot drop weighted toward coins and hearts
    @Override
    public void checkDrop() {
        int random = new Random().nextInt(100);
        if (random < 25) {
            dropItem(new OBJ_Coin());
        } else if (random >= 25 && random < 55) {
            dropItem(new OBJ_Heart());
        } else if (random >= 55 && random < 80) {
            dropItem(new OBJ_ManaPotion());
        } else {
            dropItem(new OBJ_HealingPotion());
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        int screenX = x - gp.camera.x;
        int screenY = y - gp.camera.y;

        // Skip drawing entirely when off-screen — no projectile to draw for this enemy
        if (x + width < gp.camera.x ||
                x > gp.camera.x + gp.screenWidth ||
                y + height < gp.camera.y ||
                y > gp.camera.y + gp.screenHeight) {
            return;
        }

        // Flash semi-transparent while invincible
        if (invincible) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
        }

        g2.drawImage(image, screenX, screenY, width, height, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        // Health bar shown once the enemy has taken damage
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
