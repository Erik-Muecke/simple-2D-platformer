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

public class JumpSlime extends Entity {

    private final GamePanel gp;
    private final Random random = new Random();
    private final int jumpStrength = 20;
    private int jumpPower;

    public JumpSlime(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = TYPE_MONSTER;
        name = "Jump Slime";
        speed = 2;
        width = gp.tileSize;
        height = gp.tileSize;
        setImageAndSolidAreaFromBlackPixels("/monsters/jumpslime1");
        direction = 'L';
        directionBeforeKnockBack = 'L';

        maxLife = 4;
        life = maxLife;
    }

    public void setAction() {
        actionLockCounter++;

        if (actionLockCounter >= 120) {
            direction = random.nextBoolean() ? 'L' : 'R';
            actionLockCounter = 0;
        }
    }

    public void setWalking() {
        walkingCounter++;

        if (walkingCounter >= 20) {
            image = setup("/monsters/jumpslime");
            if (walkingCounter >= 40) {
                image = setup("/monsters/jumpslime1");
                walkingCounter = 0;
            }
        }
    }

    @Override
    public void update() {
        jumpPower++;

        if (invincible) {
            invincibleCounter++;

            if (invincibleCounter > 40) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

        if ((gp.keyHandler.jumpPressed && onGround && jumpPower >= 60) || (jumpPower >= 300 && onGround)) {
            gp.movementSystem.startJump(this, jumpStrength);
            jumpPower = 0;
        }

        if (knockBack) {
            gp.movementSystem.updateMonsterKnockBack(this);
            return;
        }

        if (freezeFrames > 0) {
            freezeFrames--;
            return;
        }

        setAction();
        setWalking();
        gp.movementSystem.updateWalkingMonster(this);
    }

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
