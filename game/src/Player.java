import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayDeque;


public class Player implements Renderable {
    public static final int PADDLE_WIDTH = 100;
    public static final int PADDLE_HEIGHT = 20;

    final int MOVE_SPEED = 10;

    private int movement = 0;

    public int x;
    public int y;

    boolean moveLeft = false;
    boolean moveRight = false;

    private ArrayDeque<PowerUp> powerUpStack = new ArrayDeque<>();

    public Player() {
        respawn();
    }

    public void respawn() {
        x = 800 / 2 - PADDLE_WIDTH / 2;
        y = 500;
        powerUpStack.clear();
    }

    public void handleInput(Input input) {
        moveLeft = input.IsKeyPressed(KeyEvent.VK_A) || input.IsKeyPressed(KeyEvent.VK_LEFT);
        moveRight = input.IsKeyPressed(KeyEvent.VK_D) || input.IsKeyPressed(KeyEvent.VK_RIGHT);
    }

    public Rectangle getBoundingBox() {
        int effectiveWidth = PADDLE_WIDTH;

        PowerUp powerUp = getActivePowerUp();
        if (powerUp != null) {
            switch (powerUp.type) {
                case BIGGER_PADDLE:
                    effectiveWidth = (int)((float)effectiveWidth * 1.3f);
                    break;
                case SMALLER_PADDLE:
                    effectiveWidth = (int)((float)effectiveWidth * 0.7f);
                    break;
            }
        }

        return new Rectangle(x, y, effectiveWidth, PADDLE_HEIGHT);
    }

    public void updatePowerUps() {
        PowerUp active = getActivePowerUp();
        if (active == null) {
            return;
        }

        active.effectDuration -= Game.Instance.getDeltaTime();
        if (active.effectDuration <= 0.0f) {
            powerUpStack.removeLast();
        }
    }

    public void tick() {
        movement = 0;
        if (moveLeft) {
            movement = -MOVE_SPEED;
        }
        else if(moveRight) {
            movement = MOVE_SPEED;
        }
        PowerUp powerUp = getActivePowerUp();
        if (powerUp != null) {
            switch (powerUp.type) {
                case FASTER_PADDLE:
                    movement = (int)((float)movement * 1.3f);
                    break;
                case SLOWER_PADDLE:
                    movement = (int)((float)movement * 0.7f);
                    break;
            }
        }

        x += movement;

        if (x < 0) {
            x = 0;
        }

        int paddleWidth = getBoundingBox().width;
        if (x + paddleWidth > 800) {
            x = 800 - paddleWidth;
        }
    }

    public void pickupPowerUp(PowerUp powerUp) {
        powerUpStack.push(powerUp);
    }

    public PowerUp getActivePowerUp() {
        if (powerUpStack.isEmpty()) {
            return null;
        }
        return powerUpStack.peekLast();
    }

    public void render(Graphics graphics) {
        graphics.setColor(Color.GREEN);

        Rectangle boundingBox = getBoundingBox();

        graphics.fillRect(x, y, boundingBox.width, boundingBox.height);

        /*graphics.setColor(Color.BLUE);
        graphics.drawRect(x, y, PADDLE_WIDTH, PADDLE_HEIGHT);*/
    }

}
