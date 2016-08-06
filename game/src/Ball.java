import java.awt.*;

public class Ball implements Renderable {
    final int BALL_SIZE = 20;

    Point position = new Point();
    Point velocity = new Point();

    int speed = 4;

    Ball() {
        respawn();
    }

    public void respawn() {
        velocity.x = 0;
        velocity.y = -1;

        position.x  = 800 / 2 - BALL_SIZE / 2;
        position.y = 450;
    }

    public void render(Graphics graphics) {
        graphics.setColor(Color.WHITE);
        graphics.fillArc(position.x, position.y, BALL_SIZE, BALL_SIZE, 0, 360);

        /*graphics.setColor(Color.BLUE);
        graphics.drawRect(position.x, position.y, BALL_SIZE, BALL_SIZE);*/
    }


    private Rectangle getBoundingBox() {
        return new Rectangle(position.x, position.y, BALL_SIZE, BALL_SIZE);
    }

    private boolean handleCollisions(Game game) {
        Rectangle boundingBox = getBoundingBox();

        if ((position.x < 0) || (position.x+BALL_SIZE > 800)) {
            velocity.x = -velocity.x;
            return true;
        }

        if (position.y < 0) {
            velocity.y = -velocity.y;
            return true;
        }

        Player player = game.player;
        Rectangle playerBoundingBox = player.getBoundingBox();
        if (playerBoundingBox.intersects(boundingBox)) {

            final int paddleCenter = (player.x + playerBoundingBox.width/2);

            Point ballCenter = new Point(position.x + BALL_SIZE/2, position.y + BALL_SIZE/2);
            velocity.x = ballCenter.x > paddleCenter ? 1 : -1;
            velocity.y = -1;
            return true;
        }

        // ugly way and slow way however as we have not so many enemies it works..
        for (int i = 0; i < game.enemies.size(); ++i) {
            Enemy enemy = game.enemies.get(i);

            if (enemy.getBoundingBox().intersects(boundingBox)) {
                velocity.y = -velocity.y;

                enemy.kill(game);
                return true;
            }
        }
        return false;
    }


    public void tick(Game game) {
        Point oldPosition = position;
        int effectiveSpeed = speed;

        PowerUp powerUp = game.player.getActivePowerUp();
        if (powerUp != null) {
            switch (powerUp.type) {
                case FASTER_BALL:
                    effectiveSpeed = (int)((float)effectiveSpeed * 1.3f);
                    break;
                case SLOWER_BALL:
                    effectiveSpeed = (int)((float)effectiveSpeed * 0.8f);
                    break;
            }
        }

        position.x += velocity.x * effectiveSpeed;
        position.y += velocity.y * effectiveSpeed;

        if (handleCollisions(game)) {
            position = oldPosition;
            return;
        }

        if (position.y > 530) {
            game.gameOver();
        }
    }
}
