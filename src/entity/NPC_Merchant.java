package entity;

import main.GamePanel;
import object.OBJ_HealingPotion;
import object.OBJ_ManaPotion;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class NPC_Merchant extends Entity {

    GamePanel gp;

    public NPC_Merchant(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = TYPE_NPC; // make sure Entity has this constant defined (see Entity notes)
        direction = 'D';
        speed = 0;
        width = 32 * GamePanel.scale;
        height = 32 * GamePanel.scale;

        getNPCImage();
        setDialogue();
        setItems();

        // Solid area so the player's collision check can detect contact
        solidArea = new java.awt.Rectangle(8, 8, 48, 48);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    public void getNPCImage() {
        try {
            // 'image' is the field used by Entity.draw() — not img1
            image = ImageIO.read(
                    getClass().getResourceAsStream("/npc/merchant.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setDialogue() {
        dialogues[0] = "Hello, traveler! I have fine wares for sale. Please have a look at them.";
        dialogues[1] = "If you want to sell something, just tell it me.";
        dialogues[2] = "Press ENTER to open my shop.";
    }

    public void setItems() {
        shopInventory[0] = new OBJ_HealingPotion();
        shopInventory[1] = new OBJ_ManaPotion();
    }

    @Override
    public void draw(Graphics2D g2) {
        if (image != null) {
            int screenX = x - gp.camera.x;
            int screenY = y - gp.camera.y;
            g2.drawImage(image, screenX, screenY, width, height, null);
        }
    }

    @Override
    public void update() {
        // Merchant stands still — nothing to update
    }
}