package entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

import tile.TileManager;

import main.GamePanel;
import main.KeyHandler;
import system.MovementSystem;

public class Player extends Entity {

    private final GamePanel gp;
    private final KeyHandler keyH;
    private TileManager tileManager;
    private int jumpStrength = 30;
    private int gravity = 2;
    private int maxFallSpeed = 12;
    private MovementSystem movementSystem;
    public int hasKey = 0;

    public int maxLife;
    public int life;
    public boolean invincible = false;
    public int invincibleCounter = 0;


    public Player(GamePanel gp, KeyHandler keyH) {
        super();
        speed = 4; //Geschwindigkeit des Spielers, wie viele Pixel er sich pro Update bewegen soll
        width = 32 * GamePanel.scale; //Breite des Spielers in Pixeln
        height = 32 * GamePanel.scale; //Höhe des Spielers in Pixeln
        direction = 'D';
        this.gp = gp;
        this.keyH = keyH;
        solidArea = new Rectangle(4, 4, 40, 40);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;//declaring the solid parts of the player
        maxLife = 6;
        life = maxLife;
        this.movementSystem = new MovementSystem(gp.worldWidth, gp.worldHeight, gp.tileSize, gp.collisionsystem);
        loadPlayerImage();
    }

    public void loadPlayerImage() {
        try {
            img1 = loadImage("/player/kartoni1.png");
            img2 = loadImage("/player/kartoni2.png");
            img3 = loadImage("/player/kartoni3.png");
            img4 = loadImage("/player/kartoni4.png");
            img5 = loadImage("/player/kartoni5.png");
            img6 = loadImage("/player/kartoni6.png");
        } catch (IOException e) {
            System.err.println("Fehler beim Laden der Player-Sprites: " + e.getMessage());
        }//laden der verschiedenen Player Bilder
    }

    private BufferedImage loadImage(String path) throws IOException {
        InputStream stream = getClass().getResourceAsStream(path);
        if (stream == null) {
            throw new IOException("Ressource nicht gefunden: " + path);
        }
        return ImageIO.read(stream);
    }

    public void update() {
        // Handle horizontal input
        if (keyH.leftPressed) {
            direction = 'L';
            velocityX = -speed;
        } else if (keyH.rightPressed) {
            direction = 'R';
            velocityX = speed;
        } else {
            velocityX = 0;
        }

        // Handle jump
        if (keyH.jumpPressed && onGround) {
            velocityY = -jumpStrength;
            onGround = false;
        }

        if (invincible == true) {
            invincibleCounter++;

            if (invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

        int objectIndex = gp.collisionsystem.collisionObject(this, true);
        pickUpObject(objectIndex);

        // Delegate all physics + collision to MovementSystem
        movementSystem.updatePlayer(this);
    }

    public void pickUpObject(int i) {
        if (i != 999) {
            String objectName = gp.obj[i].name;
            switch (objectName) {
                case "Key":
                    hasKey++;
                    System.out.println("You got a key! Total: " + hasKey);
                    gp.obj[i] = null;
                    break;

                case "Door":
                    if (hasKey > 0) {
                        System.out.println("You opened the door!");
                        hasKey--;
                        gp.obj[i] = null; // door disappears
                    } else {
                        System.out.println("You need a key!");
                    }
                    break;
            }
        }//function, which is enabling the collision and interaction with the different objects
    }

    public void damagePlayer() {

        if (invincible == false) {
            life -= 1;
            invincible = true;
        }
    }

    @Override
    public void draw(Graphics2D g2) {

        if (invincible == true) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
        }

        BufferedImage img = switch (direction) { //Wechselt das Bild des Spielers je nach Richtung, in die er schaut
            case 'U' -> img1;
            case 'D' -> img4;
            case 'L' -> img2;
            case 'R' -> img3;
            default -> img1;
        };
        if (img != null) {
            int screenX = x - gp.camera.x;
            int screenY = y - gp.camera.y;

            g2.drawImage(img, screenX, screenY, width, height, null);
        }//using the camera for the player

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
}
