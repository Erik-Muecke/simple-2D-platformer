package game.system;

import game.entity.Entity;

import java.awt.*;
import java.util.HashSet;

public class RenderSystem {
    private int tileSize;//tile size reference

    public RenderSystem(int tileSize) {
        this.tileSize = tileSize;//store tile size
    }

    public void draw(Graphics g, Entity player, HashSet<Entity> opponents,
                     HashSet<Entity> walls, HashSet<Entity> pFfoods, HashSet<Entity> fireballs,
                     int lives, int score, boolean gameOver) {//render all objects
        // draw player
        g.setColor(Color.RED);
        g.drawImage(player.image, player.x, player.y,
                    player.width, player.height, null);

        // Draw opponents
        for (Entity opponent : opponents) {
            if (!opponent.isDead) {
                g.drawImage(opponent.image, opponent.x, opponent.y,
                            opponent.width, opponent.height, null);
            }
        }

        // Draw walls
        for (Entity wall : walls) {
            g.drawImage(wall.image, wall.x, wall.y,
                        wall.width, wall.height, null);
        }
        
        
        // Draw fire pellets
        g.setColor(Color.MAGENTA);
        for (Entity pFfood : pFfoods) {
            g.fillRect(pFfood.x, pFfood.y,
                       pFfood.width, pFfood.height);
        }

        // Draw fireballs
        for (Entity fireball : fireballs) {
            g.drawImage(fireball.image, fireball.x, fireball.y,
                        fireball.width, fireball.height, null);
        }

        // Draw score and lives
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        if (gameOver) {
            g.drawString("Game Over: " + score, tileSize / 2, tileSize / 2);//game over text
        } else {
            g.drawString("x" + lives + " Score" + score, tileSize / 2, tileSize / 2);//typical text
        }
    }
}
