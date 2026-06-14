
package projectile;

import entity.Entity;
import main.GamePanel;

import main.ImageLoader;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Projectile extends Entity {

    GamePanel gp;

    ImageLoader imgLoader = new ImageLoader();

    public boolean alive = false;
    public int damage;
    public int maxLife;
    public int life;

    public Projectile(GamePanel gp) {
        this.gp = gp;
            solidArea = new Rectangle(0, 0, 0, 0); // Wird in den spezifischen Projektilklassen angepasst
    }

    //Initialisieren der Projektil-Attribute, damit es an der richtigen Stelle erscheint.
    public void set(int x, int y, char direction, boolean alive) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.alive = alive;

        this.life = maxLife;
    }

    public void update() {
        collisionOn = false; //Anfangs keine Kollision

        if(direction == 'L') {
            x -= speed + gp.player.speed - 2; // Projektilgeschwindigkeit plus Spielerbewegung, damit es sich mit dem Spieler bewegt.
            // Der Wert am ende wird abgezogen, damit sich das projektil nicht zu schnell bewegt.
            // Der Wert wird von der x Position abgezogen, damit es sich nach links bewegt.
        }
        if(direction == 'R') {
            x += speed + gp.player.speed - 2; // Projektilgeschwindigkeit plus Spielerbewegung, damit es sich mit dem Spieler bewegt.
            // Der Wert am ende wird abgezogen, damit sich das projektil nicht zu schnell bewegt.
            // Der Wert wird zur x Position addiert, damit es sich nach rechts bewegt.
        }

        gp.collisionsystem.collidesT(this); // Überprüft Kollisionen mit der Tilemap
        gp.collisionsystem.collidesWithObject(this); // Überprüft Kollisionen mit Objekten

        //Wenn es mit etwas kollidiert wird das projektil gelöscht.
        if(collisionOn) {
            alive = false;
            return;
        }

        life--; //Framebasierter Countdown der Lebensdauer des projektils, sonst würde es unendlich weit fliegen.

        if(life <= 0) {
            alive = false; //wenn die lebensdauer aufgebraucht ist, wird das Projektil zerstört.
        }
    }

    public void draw(Graphics2D g2) {

        //Überprüfen der Richtung des Projektils, damit es in die richtige Richtung zeigt. Es wird nur gezeichnet, wenn das Projektil noch lebt.
        if(alive) {
            switch(direction) {
                case 'L':
                    image = img1;
                    break;

                case 'R':
                    image = img2;
                    break;
            }

            // Berechnet die Bildschirmposition des Projektils basierend auf der Kameraposition
            int screenX = x - gp.camera.x;
            int screenY = y - gp.camera.y;

            g2.drawImage(image, screenX, screenY, width, height, null); //Zeichnet das Projektil mit den berechneten Bildschirmkoordinaten
        }
    }
}
