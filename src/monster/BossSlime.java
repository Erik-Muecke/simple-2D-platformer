package monster;

import entity.Entity;
import main.GamePanel;
import object.OBJ_HealingPotion;
import object.OBJ_ManaPotion;
import projectile.PT_Fireball;
import projectile.Projectile;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;

public class BossSlime extends Entity {

    private final GamePanel gp;
    private final Projectile projectile;
    private int shotCounter = 0;
    private final int jumpStrength = 25;
    private int jumpPower;

    public BossSlime(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = TYPE_MONSTER;
        name = "Boss Slime";
        speed = 2;
        width = (int)(gp.tileSize * 1.5);
        height = (int)(gp.tileSize * 1.5);
        setImageAndSolidAreaFromBlackPixels("/monsters/bossslime1");
        direction = 'L';
        directionBeforeKnockBack = 'L';

        maxLife = 30;
        life = maxLife;
        projectile = new PT_Fireball(gp);
    }

    public Projectile getProjectile() {
        return projectile;
    }

    public void setAction() {
        if(gp.player.x < this.x){
            this.direction = 'L';
        } else if(gp.player.x > this.x){
            this.direction = 'R';
        }
    }

    public void setWalking() {
        walkingCounter++;

        if (walkingCounter >= 20) {
            image = setup("/monsters/bossslime");
            if (walkingCounter >= 40) {
                image = setup("/monsters/bossslime1");
                walkingCounter = 0;
            }
        }
    }

    @Override
    public void update() {
        if (gp.player.boss1) {
            jumpPower++;
            if (invincible) {
                invincibleCounter++;
                if (invincibleCounter > 20) {
                    invincible = false;
                    invincibleCounter = 0;
                }
            }

            if (jumpPower >= 300 && onGround) {
                gp.movementSystem.startJump(this, jumpStrength);
                jumpPower = 0;
            }

            updateProjectileInteractions();

            if (knockBack) {
                boolean stillKnockedBack = gp.movementSystem.updateMonsterKnockBack(this);
                if (!stillKnockedBack) {
                    shotCounter = shotCounter - 5;
                }
                return;
            }

            shotCounter++;
            if (shotCounter > 150 && !projectile.alive) {
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
    public void checkDrop(){
        dropItem(new OBJ_HealingPotion());
        dropItem(new OBJ_ManaPotion());
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
