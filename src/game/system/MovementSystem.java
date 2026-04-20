package game.system;

import game.entity.Entity;

import java.util.HashSet;

// Handles movement physics and velocity updates for all game entities
public class MovementSystem {
    private int boardWidth;
    private int boardHeight;
    private int tileSize;
    private CollisionSystem collisionSystem;

    public MovementSystem(int boardWidth, int boardHeight, int tileSize, CollisionSystem collisionSystem) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.tileSize = tileSize;
        this.collisionSystem = collisionSystem;
    }

    // Updates player position with gravity, horizontal/vertical movement, and collision handling
    public void updatePlayer(Entity player, HashSet<Entity> walls) {
        int gravity = 2;
        player.velocityY += gravity;

        // Cap maximum falling speed
        if (player.velocityY > 31) {
            player.velocityY = 31;
        }

        // Move horizontally and handle wall collisions
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

        // Move vertically and handle wall/floor collisions
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

        // Handle floor boundary
        int floorY = boardHeight - player.height;
        if (player.y >= floorY) {
            player.y = floorY;
            player.velocityY = 0;
            player.onGround = true;
        }
    }

    // Updates opponent position and handles wall collisions and direction changes
    public void updateOpponent(Entity opponent, HashSet<Entity> walls, char[] directions, java.util.Random random) {
        // Skip update if opponent is frozen
        if (opponent.freezeFrames > 0) {
            opponent.freezeFrames--;
            return;
        }

        opponent.x += opponent.velocityX;
        opponent.y += opponent.velocityY;

        // Check for wall or boundary collisions and change direction if needed
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

    // Updates opponent direction and adjusts position if new direction causes collision
    public void updateOpponentDirection(Entity opponent, char direction, HashSet<Entity> walls) {
        char previousDirection = opponent.direction;
        opponent.direction = direction;
        updateOpponentVelocity(opponent);

        // Revert direction if collision occurs with new direction
        for (Entity wall : walls) {
            if (collisionSystem.collides(opponent, wall)) {
                opponent.y -= opponent.velocityY;
                opponent.direction = previousDirection;
                updateOpponentVelocity(opponent);
            }
        }
    }

    // Sets opponent horizontal velocity based on direction
    public void updateOpponentVelocity(Entity opponent) {
        if (opponent.direction == 'L') {
            opponent.velocityX = -opponent.speed;
        } else if (opponent.direction == 'R') {
            opponent.velocityX = opponent.speed;
        }
    }

    // Updates fireball horizontal position
    public void updateFireball(Entity fireball) {
        fireball.x += fireball.velocityX;
    }

    // Updates player direction and adjusts position if new direction causes collision
    public void updatePlayerDirection(Entity player, char direction, HashSet<Entity> walls) {
        char previousDirection = player.direction;
        player.direction = direction;
        updatePlayerVelocity(player);

        // Revert direction if collision occurs with new direction
        for (Entity wall : walls) {
            if (collisionSystem.collides(player, wall)) {
                player.y -= player.velocityY;
                player.direction = previousDirection;
                updatePlayerVelocity(player);
            }
        }
    }

    // Sets player horizontal velocity based on direction
    public void updatePlayerVelocity(Entity player) {
        if (player.direction == 'L') {
            player.velocityX = -player.speed;
        } else if (player.direction == 'R') {
            player.velocityX = player.speed;
        }
    }
}
