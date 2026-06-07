// OBJ_Fireball.java

package projectile;

import main.GamePanel;


public class PT_Fireball extends Projectile {



    public PT_Fireball(GamePanel gp) {

        super(gp);

        speed = 5;
        maxLife = 80;
        damage = 2;

        width = gp.tileSize * 2 / 3;
        height = gp.tileSize * 2 / 3;
        int hitboxWidth = width / 2;
        int hitboxHeight = height / 2;
        solidArea.setBounds(
                (width - hitboxWidth) / 2,
                (height - hitboxHeight) / 2,
                hitboxWidth,
                hitboxHeight
        );
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    public void getImage() {
        img1 = imgLoader.scaleImage("/projectile/fireball_right.png", gp.tileSize, gp.tileSize);
        img2 = imgLoader.scaleImage("/projectile/fireball_left.png", gp.tileSize, gp.tileSize);
    }
}
