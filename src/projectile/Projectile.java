package projectile;

import entity.Entity;
import main.GamePanel;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Projectile extends Entity {

    GamePanel gp;

    public boolean alive = false;
    public int damage;
    public int maxLife;
    public int life;
    public int useCost;

    public Projectile(GamePanel gp) {
        this.gp = gp;
        solidArea = new Rectangle(0, 0, 0, 0);
    }

    public void set(int x, int y, char direction, boolean alive) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.alive = alive;

        this.life = maxLife;
    }

    public void update() {
        gp.movementSystem.updateProjectile(this);
    }

    public Rectangle getCollisionBox() {
        return new Rectangle(
                x + solidArea.x,
                y + solidArea.y,
                solidArea.width,
                solidArea.height
        );
    }

    public void draw(Graphics2D g2) {

        if(alive) {
            switch(direction) {
                case 'L':
                    image = img1;
                    break;

                case 'R':
                    image = img2;
                    break;

                case 'D':
                    image = img3;
                    break;
            }

            int screenX = x - gp.camera.x;
            int screenY = y - gp.camera.y;
            g2.drawImage(image, screenX, screenY, width, height, null);
        }
    }
}
