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

// A ground slime that increases its speed when the player gets close,
// making it unpredictable at short range. Has no ranged attack.
public class SpeedSlime extends Entity {

    private final GamePanel gp;
    private final Random random = new Random();

    public SpeedSlime(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = TYPE_MONSTER;
        name = "Speed Slime";
        speed = 2;
        width = gp.tileSize;
        height = gp.tileSize;
        setImageAndSolidAreaFromBlackPixels("/monsters/speedslime1");
        direction = 'L';
        directionBeforeKnockBack = 'L';

        maxLife = 4;
        life = maxLife;
    }

    // Randomly reverses direction every 120 frames
    public void setAction() {
        actionLockCounter++;

        if (actionLockCounter >= 120) {
            direction = random.nextBoolean() ? 'L' : 'R';
            actionLockCounter = 0;
        }
    }

    // Alternates between two sprite frames to create a walking animation
    public void setWalking() {
        walkingCounter++;

        if (walkingCounter >= 20) {
            image = setup("/monsters/speedslime");
            if (walkingCounter >= 40) {
                image = setup("/monsters/speedslime1");
                walkingCounter = 0;
            }
        }
    }

    // Doubles speed when the player is within ~5 tiles; reverts to normal speed when farther away
    public void setSpeed() {
        if (gp.player.x > this.x + 6 * gp.tileSize || gp.player.x < this.x - 5 * gp.tileSize) {
            speed = 2; // normal patrol speed when player is far
        } else {
            speed = 4; // aggressive speed when player is nearby
        }
    }

    @Override
    public void update() {
        setSpeed(); // update speed every frame based on player proximity

        // Count down invincibility frames after being hit
        if (invincible) {
            invincibleCounter++;

            if (invincibleCounter > 40) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

        // During knockback, move in the hit direction and skip normal AI
        if (knockBack) {
            gp.movementSystem.updateMonsterKnockBack(this);
            return;
        }

        // Freeze frames pause movement briefly after being hit
        if (freezeFrames > 0) {
            freezeFrames--;
            return;
        }

        setAction();
        setWalking();
        gp.movementSystem.updateWalkingMonster(this);
    }

    // Random loot drop weighted toward coins and hearts
    @Override
    public void checkDrop() {
        int random = new Random().nextInt(100);
        if(random < 35) {
            dropItem(new OBJ_Coin());
        }
        else if(random >= 35 && random < 65) {
            dropItem(new OBJ_Heart());
        }
        else if(random >= 65 && random < 85) {
            dropItem(new OBJ_ManaPotion());
        }
        else{
            dropItem(new OBJ_HealingPotion());
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        int screenX = x - gp.camera.x;
        int screenY = y - gp.camera.y;

        // Skip drawing entirely when off-screen
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
