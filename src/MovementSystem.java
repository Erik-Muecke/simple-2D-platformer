package src;

import java.util.HashSet;

public class MovementSystem {
    private int boardWidth;
    private int boardHeight;
    private int tileSize;
    private CollisionSystem collisionSystem;

    MovementSystem(int boardWidth, int boardHeight, int tileSize, CollisionSystem collisionSystem) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.tileSize = tileSize;
        this.collisionSystem = collisionSystem;
    }

    public void updatePlayer(Entity player, HashSet<Entity> walls) {
        int gravity = 2;
        player.velocityY += gravity;

        if (player.velocityY > 31) {
            player.velocityY = 31;
        }

        // Move horizontally
        player.x += player.velocityX;
        for (Entity wall : walls) {
            if (collisionSystem.collides(player, wall)) {
                if (player.velocityX > 0) {
                    player.x = wall.x - player.width;
                } else if (player.velocityX < 0) {
                    player.x = wall.x + wall.width;
                }
                player.velocityX = 0;
            }
        }

        // Move vertically
        player.y += player.velocityY;
        player.onGround = false;
        for (Entity wall : walls) {
            if (collisionSystem.collides(player, wall)) {
                if (player.velocityY > 0) {
                    player.y = wall.y - player.height;
                    player.velocityY = 0;
                    player.onGround = true;
                } else if (player.velocityY < 0) {
                    player.y = wall.y + wall.height;
                    player.velocityY = 0;
                }
            }
        }

        // Floor
        int floorY = boardHeight - player.height;
        if (player.y >= floorY) {
            player.y = floorY;
            player.velocityY = 0;
            player.onGround = true;
        }
    }

    public void updateOpponent(Entity opponent, HashSet<Entity> walls, char[] directions, java.util.Random random) {
        if (opponent.freezeFrames > 0) {
            opponent.freezeFrames--;
            return;
        }

        opponent.x += opponent.velocityX;
        opponent.y += opponent.velocityY;

        for (Entity wall : walls) {
            if (collisionSystem.collides(opponent, wall) ||
                opponent.x <= 0 ||
                opponent.x + opponent.width >= boardWidth) {

                opponent.x -= opponent.velocityX;
                opponent.y -= opponent.velocityY;

                char newDirection = directions[random.nextInt(2)];
                updateOpponentDirection(opponent, newDirection, walls);
            }
        }
    }

    public void updateOpponentDirection(Entity opponent, char direction, HashSet<Entity> walls) {
        char previousDirection = opponent.direction;
        opponent.direction = direction;
        updateOpponentVelocity(opponent);

        for (Entity wall : walls) {
            if (collisionSystem.collides(opponent, wall)) {
                opponent.y -= opponent.velocityY;
                opponent.direction = previousDirection;
                updateOpponentVelocity(opponent);
            }
        }
    }

    public void updateOpponentVelocity(Entity opponent) {
        if (opponent.direction == 'L') {
            opponent.velocityX = -opponent.speed;
        } else if (opponent.direction == 'R') {
            opponent.velocityX = opponent.speed;
        }
    }

    public void updateFireball(Entity fireball) {
        fireball.x += fireball.velocityX;
    }

    public void updatePlayerDirection(Entity player, char direction, HashSet<Entity> walls) {
        char previousDirection = player.direction;
        player.direction = direction;
        updatePlayerVelocity(player);

        for (Entity wall : walls) {
            if (collisionSystem.collides(player, wall)) {
                player.y -= player.velocityY;
                player.direction = previousDirection;
                updatePlayerVelocity(player);
            }
        }
    }

    public void updatePlayerVelocity(Entity player) {
        if (player.direction == 'L') {
            player.velocityX = -player.speed;
        } else if (player.direction == 'R') {
            player.velocityX = player.speed;
        }
    }
}
