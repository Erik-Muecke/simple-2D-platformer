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

// A flying enemy that actively tracks the player both horizontally and vertically,
// hovers at the player's height, and fires a fireball in its facing direction every 180 frames.
// Ignores knockback and deals contact damage.
public class HoverFlyer extends Entity {

    private final GamePanel gp;
    private final Random random = new Random();
    private final Projectile projectile; // the fireball this enemy fires
    private int shotCounter = 0;         // counts frames between shots
    int speedy;                          // vertical tracking speed (separate from horizontal speed)

    public HoverFlyer(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = TYPE_MONSTER;
        name = "Hover Flyer";
        speed = 0;   // horizontal movement is handled directly in setAction, not via speed
        speedy = 2;  // vertical pixels moved per frame toward the player's Y
        width = gp.tileSize;
        height = gp.tileSize;
        setImageAndSolidAreaFromBlackPixels("/monsters/hoverflyer");
        direction = 'L';
        directionBeforeKnockBack = 'L';

        maxLife = 5;
        life = maxLife;
        projectile = new PT_Fireball(gp);
    }

    // Exposes the projectile so GamePanel can check for mid-air clashes with the player's fireball
    public Projectile getProjectile() {
        return projectile;
    }

    // Turns to face the player horizontally and moves vertically to match the player's Y position
    public void setAction() {
        if(gp.player.x < this.x){
            this.direction = 'L';
        } else if(gp.player.x > this.x){
            this.direction = 'R';
        }
        // Move up or down by speedy each frame to hover at the player's height
        if(gp.player.y < this.y){
            this.y -= speedy;
        } else if(gp.player.y > this.y){
            this.y += speedy;
        }
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

        // HoverFlyer never responds to knockback — always stays on its path
        knockBack = false;

        // Move the active projectile and check if it hits the player or player's fireball
        updateProjectileInteractions();

        // Fire a projectile in the current facing direction every 180 frames
        shotCounter++;
        if (shotCounter > 180 && !projectile.alive) {
            int centerX = x + (width - projectile.width) / 2;
            int centerY = y + (height - projectile.height) / 2;

            projectile.set(centerX - 18, centerY, direction, true); // slight offset to spawn at the side
            shotCounter = 0;
        }

        // Freeze frames pause movement briefly after being hit
        if (freezeFrames > 0) {
            freezeFrames--;
            return;
        }

        // Deal contact damage if the player touches this enemy
        if (gp.collisionsystem.collidesWithPlayer(this)) {
            gp.player.damagePlayer();
        }
        collisionOn = false; // reset the flag set as a side-effect by collidesWithPlayer

        setAction();
    }

    // Delegates projectile update and hit detection to a shared helper
    private void updateProjectileInteractions() {
        handleProjectile(projectile);
    }

    // Moves the given projectile and checks if it hits the player or the player's fireball
    private void handleProjectile(Projectile p) {
        if (!p.alive) return;

        p.update();

        Rectangle projectileBox = p.getCollisionBox();

        // Cancel both projectiles if they collide mid-air
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

        // Deal damage if the projectile reaches the player
        if (projectileBox.intersects(playerBox)) {
            if (!gp.player.invincible) {
                gp.player.life -= p.damage;
                gp.player.invincible = true;
            }
            p.alive = false;
        }
    }

    // Random loot drop weighted toward coins and hearts
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

        // Still draw the projectile even when the enemy itself is off-screen
        if (x + width < gp.camera.x ||
                x > gp.camera.x + gp.screenWidth ||
                y + height < gp.camera.y ||
                y > gp.camera.y + gp.screenHeight) {
            projectile.draw(g2);
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

        projectile.draw(g2);
    }
}
