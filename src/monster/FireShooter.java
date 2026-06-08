package monster;

import entity.Entity;
import main.GamePanel;
import object.OBJ_Coin;
import object.OBJ_HealingPotion;
import object.OBJ_Heart;
import object.OBJ_ManaPotion;
import projectile.PT_AimedFireball;
import projectile.Projectile;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;

public class FireShooter extends Entity {

    private final GamePanel gp;
    private final Projectile projectile;
    private int shotCounter = 0;
    private double dirX = 0;
    private double dirY = 0;

    public FireShooter(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = TYPE_MONSTER;
        name = "Fire Shooter";
        speed = 0;
        width = gp.tileSize;
        height = gp.tileSize;
        image = setup("/monsters/fireshooter"); // load sprite without scanning
        int hitboxWidth = 42;
        int hitboxHeight = 42;
        solidArea = new Rectangle(
                6,
                6,
                hitboxWidth,
                hitboxHeight
        );
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        direction = 'L';
        directionBeforeKnockBack = 'L';

        maxLife = 8;
        life = maxLife;
        projectile = new PT_AimedFireball(gp);
    }

    public Projectile getProjectile() {
        return projectile;
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

        // Ignore knockBack — FireShooter does not move
        knockBack = false;

        updateProjectileInteractions();

        if (freezeFrames > 0) {
            freezeFrames--;
            return;
        }

        // Contact damage using the existing CollisionSystem + Player method
        if (gp.collisionsystem.collidesWithPlayer(this)) {
            gp.player.damagePlayer();
        }
        collisionOn = false; // reset the side-effect from collidesWithPlayer

        shotCounter++;
        if (shotCounter > 180 && !projectile.alive) {
            aimAndShoot();
            shotCounter = 0;
        }
    }

    private void aimAndShoot() {
        int monsterCenterX = x + width / 2;
        int monsterCenterY = y + height / 2;
        int playerCenterX = gp.player.x + gp.player.solidArea.x + gp.player.solidArea.width / 2;
        int playerCenterY = gp.player.y + gp.player.solidArea.y + gp.player.solidArea.height / 2;

        double dx = playerCenterX - monsterCenterX;
        double dy = playerCenterY - monsterCenterY;
        double length = Math.sqrt(dx * dx + dy * dy);

        if (length == 0) return;

        dirX = dx / length;
        dirY = dy / length;

        if (projectile instanceof PT_AimedFireball aimed) {
            aimed.setDirection(dirX, dirY);
        }

        int projectileX = x + (width - projectile.width) / 2;
        int projectileY = y + (height - projectile.height) / 2;

        char facing = (dirX >= 0) ? 'R' : 'L';
        direction = facing;
        projectile.set(projectileX, projectileY, facing, true);
    }

    private void updateProjectileInteractions() {
        if (!projectile.alive) return;

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
        if (random < 25) {
            dropItem(new OBJ_Coin());
        } else if (random < 55) {
            dropItem(new OBJ_Heart());
        } else if (random < 80) {
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