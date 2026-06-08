package monster;

import entity.Entity;
import main.GamePanel;
import object.OBJ_Coin;
import object.OBJ_HealingPotion;
import object.OBJ_Heart;
import object.OBJ_ManaPotion;
import projectile.PT_Fireball;
import projectile.Projectile;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;

public class FireSlime extends Entity {

    private final GamePanel gp;
    private final Random random = new Random();
    private final Projectile projectile;
    private int shotCounter = 0;

    public FireSlime(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = TYPE_MONSTER;
        name = "Fire Slime";
        speed = 2;
        width = gp.tileSize;
        height = gp.tileSize;
        setImageAndSolidAreaFromBlackPixels("/monsters/fireslime1");
        direction = 'L';
        directionBeforeKnockBack = 'L';

        maxLife = 5;
        life = maxLife;
        projectile = new PT_Fireball(gp);
    }

    public Projectile getProjectile() {
        return projectile;
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
            image = setup("/monsters/fireslime");
            if (walkingCounter >= 40) {
                image = setup("/monsters/fireslime1");
                walkingCounter = 0;
            }
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

        updateProjectileInteractions();

        if (knockBack) {
            boolean stillKnockedBack = gp.movementSystem.updateMonsterKnockBack(this);
            if (!stillKnockedBack) {
                shotCounter = shotCounter-10;
            }
            return;
        }

        shotCounter++;
        if (shotCounter > 180 && !projectile.alive) {
            int projectileX = x + (width - projectile.width) / 2;
            int projectileY = y + (height - projectile.height) / 2;
            projectile.set(projectileX, projectileY, direction, true);
            shotCounter = 0;
        }

        if (freezeFrames > 0) {
            freezeFrames--;
            return;
        }

        setAction();
        setWalking();
        gp.movementSystem.updateWalkingMonster(this);
    }

    private void updateProjectileInteractions() {
        if (!projectile.alive) {
            return;
        }

        projectile.update();

        Rectangle projectileBox = projectile.getCollisionBox();
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

        if (projectileBox.intersects(playerBox)) {
            if (!gp.player.invincible) {
                gp.player.life -= projectile.damage;
                gp.player.invincible = true;
            }
            projectile.alive = false;
        }
    }

    @Override
    public void checkDrop() {
        int random = new Random().nextInt(100);
        if(random < 25) {
            dropItem(new OBJ_Coin());
        }
        else if(random >= 25 && random < 55) {
            dropItem(new OBJ_Heart());
        }
        else if(random >= 55 && random < 80) {
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
            projectile.draw(g2);
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

        projectile.draw(g2);
    }
}
