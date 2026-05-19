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

    @Override
    public void update() {
        if (invincible) {
            invincibleCounter++;

            if (invincibleCounter > 40) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

        if (freezeFrames > 0) {
            freezeFrames--;
            return;
        }

        setAction();

        char horizontalDirection = direction;
        if (horizontalDirection == 'L') {
            velocityX = -speed;
        } else {
            velocityX = speed;
        }

        x += velocityX;
        direction = horizontalDirection;
        collisionOn = false;
        gp.collisionsystem.collidesT(this);
        gp.collisionsystem.collidesWithObject(this);

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

        velocityY += 2;
        if (velocityY > 31) {
            velocityY = 31;
        }

        y += velocityY;
        char savedDirection = direction;
        direction = 'D';
        collisionOn = false;
        gp.collisionsystem.collidesT(this);
        gp.collisionsystem.collidesWithObject(this);
        direction = savedDirection;

        if (collisionOn) {
            y -= velocityY;
            velocityY = 0;
            onGround = true;
        } else {
            onGround = false;
        }

        if (gp.collisionsystem.collidesWithPlayer(this)) {
            gp.player.damagePlayer();
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

        g2.setColor(new Color(58, 176, 84));
        g2.fillOval(screenX + 6, screenY + 18, width - 12, height - 22);

        g2.setColor(new Color(25, 92, 45));
        g2.fillOval(screenX + 20, screenY + 30, 7, 7);
        g2.fillOval(screenX + width - 27, screenY + 30, 7, 7);
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
