package game.entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;
import game.system.MovementSystem;

public class Player extends Entity {

    private final GamePanel gp;
    private final KeyHandler keyH;
    private int jumpStrength = 30;
    private int gravity = 2;
    private int maxFallSpeed = 12;
    private MovementSystem movementSystem;


    public Player(GamePanel gp, KeyHandler keyH) {
        super();
        speed = 4;
        width = 32 * GamePanel.scale;
        height = 32 * GamePanel.scale;
        direction = 'D';
        x = 100;
        y = gp.screenHeight - height;
        this.gp = gp;
        this.keyH = keyH;
        solidArea = new Rectangle(0, 0, 48, 40);
        this.movementSystem = new MovementSystem(gp.screenWidth, gp.screenHeight, gp.tileSize, gp.collisionsystem);
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
        }
    }

    private BufferedImage loadImage(String path) throws IOException {
        InputStream stream = getClass().getResourceAsStream(path);
        if (stream == null) {
            throw new IOException("Ressource nicht gefunden: " + path);
        }
        return ImageIO.read(stream);
    }

    public void update() {
        if (keyH.leftPressed) {
            direction = 'L';
            velocityX = -speed;
        } else if (keyH.rightPressed) {
            direction = 'R';
            velocityX = speed;
        } else {
            velocityX = 0;
        }

        if (keyH.jumpPressed && onGround) {
            velocityY = -20;
            onGround = false;
        }

        movementSystem.updatePlayer(this);
    }

    @Override
    public void draw(Graphics2D g2) {
        BufferedImage img = switch (direction) {
            case 'U' -> img1;
            case 'D' -> img1;
            case 'L' -> img2;
            case 'R' -> img3;
            default -> img1;
        };
        if (img != null) {
            g2.drawImage(img, x, y, width, height, null);
        }
    }
}