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

public class HeavyFlyer extends Entity {

    private final GamePanel gp;

    public HeavyFlyer(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = TYPE_MONSTER;
        name = "Fire Flyer";
        speed = 2;
        width = gp.tileSize;
        height = gp.tileSize;
        setImageAndSolidAreaFromBlackPixels("/monsters/heavyflyer");
        direction = 'L';
        directionBeforeKnockBack = 'L';

        maxLife = 10;
        life = maxLife;
    }

    @Override
    public void update() {
        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 40) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

        // HeavyFlyer ignores knockback movement, but keeps its pre-hit facing direction.
        if (knockBack) {
            direction = directionBeforeKnockBack;
            knockBackCounter = 0;
            knockBack = false;
        }

        if (freezeFrames > 0) {
            freezeFrames--;
            return;
        }

        // Contact damage using the existing CollisionSystem + Player method
        if (gp.collisionsystem.collidesWithPlayer(this)) {
            gp.player.damagePlayer();
        }
        collisionOn = false; // reset the side-effect from collidesWithPlayer
        gp.movementSystem.updateFlyingMonster(this);
    }

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

        if (x + width < gp.camera.x ||
                x > gp.camera.x + gp.screenWidth ||
                y + height < gp.camera.y ||
                y > gp.camera.y + gp.screenHeight) {
            return;
        }

        if (invincible) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
        }

        g2.drawImage(image, screenX, screenY, width, height, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

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
