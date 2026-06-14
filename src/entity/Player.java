package entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

import projectile.PT_Fireball;
import projectile.Projectile;
import tile.TileManager;

import main.GamePanel;
import main.KeyHandler;
import main.ImageLoader;
import system.MovementSystem;

public class Player extends Entity {

    private final GamePanel gp;
    private final KeyHandler keyH;
    private TileManager tileManager;
    private int jumpStrength = 34;
    private MovementSystem movementSystem;
    public int hasKey = 0;

    ImageLoader imgLoader = new ImageLoader();

    public int maxLife;
    public int life;
    public boolean invincible = false;
    public int invincibleCounter = 0;
    // Player additions
    public Projectile projectile;

    public int lastgroundposX;
    public int lastgroundposY;

    public int floorY;
    public Player(GamePanel gp, KeyHandler keyH) {
        super(); // Aufruf des Konstruktors der Entity-Klasse
        speed = 6; //Geschwindigkeit des Spielers, wie viele Pixel er sich pro Update bewegen soll
        width = 32 * GamePanel.scale; //Breite des Spielers in Pixeln
        height = 32 * GamePanel.scale; //Höhe des Spielers in Pixeln
        direction = 'D';
        floorY = gp.worldHeight - height; // Setzt die Bodenhöhe basierend auf der Weltgröße und der Spielerhöhe
        this.gp = gp;
        this.keyH = keyH;

        //Maße der Hitbox
        int hitboxWidth = 48;
        int hitboxHeight = 48;
        solidArea = new Rectangle(
                (width - hitboxWidth) / 2,  //inkrement um das die x position verschoben wird, damit die box mittig ist
                (height - hitboxHeight) / 2,  //inkrement um das die y position verschoben wird, damit die box mittig ist
                hitboxWidth,
                hitboxHeight
        );

        projectile = new PT_Fireball(gp);

        // Setzt die Standarposition der Kollisionsbox
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        maxLife = 6; // Maximale Lebenspunkte des Spielers
        life = maxLife; // Aktuelle Lebenspunkte des Spielers
        attackWidth = gp.tileSize;
        attackHeight = gp.tileSize;
        this.movementSystem = new MovementSystem(gp);
        loadPlayerImage();
    }

    // Laden der Spielerbilder aus den Ressourcen
    public void loadPlayerImage() {
            img1 = imgLoader.loadImage("/player/kartoni1.png");
            img2 = imgLoader.loadImage("/player/kartoni2.png");
            img3 = imgLoader.loadImage("/player/kartoni3.png");
            img4 = imgLoader.loadImage("/player/kartoni4.png");
            img5 = imgLoader.loadImage("/player/kartoni5.png");
            img6 = imgLoader.loadImage("/player/kartoni6.png");
    }

    public void update() {
        // Horizontaler Input
        if (keyH.leftPressed) {
            direction = 'L';
            velocityX = -speed;
        } else if (keyH.rightPressed) {
            direction = 'R';
            velocityX = speed;
        } else {
            velocityX = 0;
        }

        // Sprung
        if (keyH.jumpPressed && onGround && y < floorY - 20) {
            velocityY = -jumpStrength;
            onGround = false;
        }

        // Angriff
        if (keyH.enterPressed && !gp.eHandler.isHealingPoolHit()) {
            attack();
            keyH.enterPressed = false;
        }

        //Überprüft ob der Spieler aktuell unverwundbar ist, wenn ja wird der invincibleCounter hochgezählt,
        // wenn der Counter 60 überschreitet, wird die Unverwundbarkeit aufgehoben und der Counter zurückgesetzt.
        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

        // Überprüft, ob der Spieler ein Projektil abfeuern möchte und ob das Projektil nicht bereits aktiv ist.
        // Wenn beide Bedingungen erfüllt sind, wird die Position des Projektils auf die Mitte des Spielers gesetzt und
        // die Richtung entsprechend der aktuellen Blickrichtung des Spielers festgelegt.
        if(keyH.shotKeyPressed && !projectile.alive) {
            int projectileX = x + (width - projectile.width) / 2;
            int projectileY = y + (height - projectile.height) / 2;
            char projectileDirection = direction;

            if (projectileDirection != 'L' && projectileDirection != 'R') {
                projectileDirection = 'R';
            }

            projectile.set(projectileX, projectileY, projectileDirection, true);
            keyH.shotKeyPressed = false;
        }

        //Speichert die letzt bekannte Position des Spielers auf dem Boden, um ihn dorthin zurückzusetzen, falls er aus der Welt fällt.
        if (onGround && y < floorY - 20) {
            lastgroundposX = x;
            lastgroundposY = y;
            System.out.println("On ground. Last ground position updated: (" + lastgroundposX + ", " + lastgroundposY + ")");
        }

        // Überprüft, ob der Spieler unter die Bodenhöhe gefallen ist, und setzt ihn zurück, wenn dies der Fall ist.
        checkOutOfBounds();

        // Überprüft, ob der Spieler mit einem Objekt kollidiert, und führt die entsprechende Interaktion aus.
        int objectIndex = gp.collisionsystem.collisionObject(this, true);
        InteractObject(objectIndex);



        // Delegieren aller Bewegungs- und Kollisionslogik an das MovementSystem, um die Update-Methode des Spielers übersichtlich zu halten.
        movementSystem.updatePlayer(this);
    }

    public void InteractObject(int i) {
        if (i != 999) { // 999 bedeutet, dass keine Kollision mit einem Objekt stattgefunden hat
            String objectName = gp.obj[i].name; // Holt den Namen des Objekts, mit dem der Spieler kollidiert ist
            //Switch-Case-Struktur, um die Interaktion basierend auf dem Namen des Objekts zu bestimmen.
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

                case "Flag": // Das Flag-Objekt ist für das rhöhen der mapIndicator verantwortlich, damit die nächste Karte geladen wird.
                    velocityX = 0;
                    velocityY = 0;
                    gp.mapIndicator++;
                    System.out.println("You win!");
                    break;
                case "heart": // Das Herz-Objekt heilt den Spieler um 2 Lebenspunkte, wenn er es aufnimmt, aber nicht über die maximale Lebenspunkte hinaus.
                    if (life < maxLife) {
                        life++;
                    }
                    if (life < maxLife) {
                            life++;
                        System.out.println("You healed! Current life: " + life);
                    } else {
                        System.out.println("Your life is already full!");
                    }
                    gp.obj[i] = null; // heart disappears
                    break;
            }
        }
    }

    // Berechnet die Position und Größe des Angriffsrechtecks basierend auf der aktuellen Blickrichtung des Spielers
    public void attack() {
        Rectangle attackBox = new Rectangle();

        switch (direction) {
            case 'L':
                attackBox.x = x + solidArea.x - attackWidth;
                attackBox.y = y + solidArea.y;
                break;
            case 'R':
                attackBox.x = x + solidArea.x + solidArea.width;
                attackBox.y = y + solidArea.y;
                break;
        }

        attackBox.width = attackWidth;
        attackBox.height = attackHeight;

        checkMonsterHit(attackBox);
    }

    // Überprüft, ob das Angriffsrechteck des Spielers mit einem Monster kollidiert
    // und ruft die damageMonster-Methode auf, um dem Monster Schaden zuzufügen, wenn eine Kollision festgestellt wird.
    public void checkMonsterHit(Rectangle attackBox) {
        for (Entity monster : gp.monster) {
            if (monster == null || monster.isDead) {
                continue;
            }

            Rectangle monsterBox = new Rectangle(
                    monster.x + monster.solidArea.x,
                    monster.y + monster.solidArea.y,
                    monster.solidArea.width,
                    monster.solidArea.height
            );

            if (attackBox.intersects(monsterBox)) {
                damageMonster(monster);
            }
        }
    }

    // Überprüft, ob das aktive Projektil des Spielers mit einem Monster kollidiert, und reduziert die Lebenspunkte des Monsters entsprechend.
    public void checkProjectileMonsterHit() {
        if (projectile == null || !projectile.alive) {
            return;
        }

        Rectangle projectileBox = new Rectangle(
                projectile.x + projectile.solidArea.x,
                projectile.y + projectile.solidArea.y,
                projectile.solidArea.width,
                projectile.solidArea.height
        );

        for (Entity monster : gp.monster) {
            if (monster == null || monster.isDead) {
                continue;
            }

            Rectangle monsterBox = new Rectangle(
                    monster.x + monster.solidArea.x,
                    monster.y + monster.solidArea.y,
                    monster.solidArea.width,
                    monster.solidArea.height
            );

            if (projectileBox.intersects(monsterBox)) {
                monster.life -= projectile.damage;
                projectile.alive = false;

                if (monster.life <= 0) {
                    monster.isDead = true;
                }

                return;
            }
        }
    }

    // Reduziert die Lebenspunkte eines Monsters um 1, wenn es nicht unverwundbar ist
    // und setzt es für kurze Zeit unverwundbar, um zu verhindern, dass es sofort wieder Schaden nimmt.
    public void damageMonster(Entity monster) {
        if (!monster.invincible) {
            monster.life--;
            monster.invincible = true;

            if (monster.life <= 0) {
                monster.isDead = true;
            }
        }
    }

    // Reduziert die Lebenspunkte des Spielers um 1, wenn er nicht unverwundbar ist
    // und setzt ihn für kurze Zeit unverwundbar, um zu verhindern, dass er sofort wieder Schaden nimmt.
    public void damagePlayer() {

        if (!invincible) {
            life -= 1;
            invincible = true;
        }
    }

    // Überprüft, ob der Spieler unter die Bodenhöhe gefallen ist, und setzt ihn zurück, wenn dies der Fall ist.
    public void checkOutOfBounds() {
        if (y >= floorY) {
            System.out.println("OutofBounds.");
            y = floorY;
            velocityY = 0;
            x = lastgroundposX;
            y = lastgroundposY;
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        if (invincible) {
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
