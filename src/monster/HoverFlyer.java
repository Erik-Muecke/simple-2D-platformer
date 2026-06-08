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

public class HoverFlyer extends Entity {

    private final GamePanel gp;
    private final Random random = new Random();
    private final Projectile projectile;
    private int shotCounter = 0;
    int speedy;

    public HoverFlyer(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = TYPE_MONSTER;
        name = "Fire Flyer";
        speed = 0;
        speedy = 2;
        width = gp.tileSize;
        height = gp.tileSize;
        setImageAndSolidAreaFromBlackPixels("/monsters/hoverflyer");
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
        if(gp.player.x < this.x){
            this.direction = 'L';
        } else if(gp.player.x > this.x){
            this.direction = 'R';
        }
        if(gp.player.y < this.y){
            this.y -= speedy;
        } else if(gp.player.y > this.y){
            this.y += speedy;
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

        // Ignore knockBack — FireShooter does not move
        knockBack = false;

        updateProjectileInteractions();

        shotCounter++;
        if (shotCounter > 180 && !projectile.alive) {
            int centerX = x + (width - projectile.width) / 2;
            int centerY = y + (height - projectile.height) / 2;

            projectile.set(centerX - 18, centerY, direction, true);

            shotCounter = 0;
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

        setAction();
    }

    private void updateProjectileInteractions() {
        handleProjectile(projectile);
    }

    private void handleProjectile(Projectile p) {
        if (!p.alive) return;

        p.update();

        Rectangle projectileBox = p.getCollisionBox();

        if (gp.player.projectile != null
                && gp.player.projectile.alive
                && projectileBox.intersects(gp.player.projectile.getCollisionBox())) {
            p.alive = false;
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
                gp.player.life -= p.damage;
                gp.player.invincible = true;
            }
            p.alive = false;
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
