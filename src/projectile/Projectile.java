package projectile;

import entity.Entity;
import main.GamePanel;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Projectile extends Entity {

    GamePanel gp;//needed to access the game systems

    public boolean alive = false;//checks if the projectile is active
    public int damage;//damage of the projectile
    public int maxLife;//maximum lifetime
    public int life;//current lifetime
    public int useCost;//mana cost

    public Projectile(GamePanel gp) {//constructor
        this.gp = gp;
        solidArea = new Rectangle(0, 0, 0, 0);//hitbox of the projectile
    }

    public void set(int x, int y, char direction, boolean alive) {//sets projectile values
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.alive = alive;

        this.life = maxLife;//reset lifetime
    }

    public void update() {//updates movement
        gp.movementSystem.updateProjectile(this);
    }

    public Rectangle getCollisionBox() {//collision area of the projectile
        return new Rectangle(
                x + solidArea.x,//real x-position of the hitbox
                y + solidArea.y,//real y-position of the hitbox
                solidArea.width,
                solidArea.height
        );
    }

    public void draw(Graphics2D g2) {//draw function

        if(alive) {

            switch(direction) {//different sprite depending on direction
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
            //math:
            //world position - camera position = screen position

            int screenY = y - gp.camera.y;

            g2.drawImage(image, screenX, screenY, width, height, null);
        }
    }
}