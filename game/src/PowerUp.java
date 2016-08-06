import java.awt.*;

public class PowerUp implements Renderable {
    final int POWER_UP_SIZE = 10;

    enum Type
    {
        SLOWER_BALL,
        FASTER_BALL,
        BIGGER_PADDLE,
        SMALLER_PADDLE,
        FASTER_PADDLE,
        SLOWER_PADDLE
    };

    Type type = Type.FASTER_BALL;
    boolean pickedUp = false;

    int x;
    int y;

    int vy;

    float effectDuration;

    public PowerUp(int x, int y) {
        this.x = x;
        this.y = y;

        // calculate effect duration between range 5s - 20s.
        this.effectDuration = (float)(5 + Math.random() * 15);

        // calculate random falling speed.
        this.vy = (int)(1 + Math.random() * 5);

        this.type = getRandomType();
    }

    private Color getColor() {
        switch (this.type) {
            case FASTER_BALL:
                return Color.BLUE;
            case SLOWER_BALL:
                return Color.GREEN;
            case BIGGER_PADDLE:
                return Color.WHITE;
            case SMALLER_PADDLE:
                return Color.CYAN;
            case FASTER_PADDLE:
                return Color.ORANGE;
            case SLOWER_PADDLE:
                return Color.RED;
        }
        return Color.WHITE;
    }

    private static Type getRandomType() {
        double rand = Math.random();

        if (rand > 0.84f) {
            return Type.FASTER_BALL;
        }
        else if (rand > 0.68f) {
            return Type.SLOWER_BALL;
        }
        else if (rand > 0.52f) {
            return Type.BIGGER_PADDLE;
        }
        else if (rand > 0.36f) {
            return Type.SMALLER_PADDLE;
        }
        else if (rand > 0.20f) {
            return Type.FASTER_PADDLE;
        }
        else {
            return Type.SLOWER_PADDLE;
        }
    }

    private Rectangle getBoundingBox() {
        return new Rectangle(x, y, POWER_UP_SIZE, POWER_UP_SIZE);
    }

    public void tick(Game game, Player player) {
        assert (pickedUp == false);
        y += vy;

        if (y > 550) {
            game.onPowerUpPickup(this);
        }

        if (player.getBoundingBox().intersects(getBoundingBox())) {
            pickedUp = true;

            player.pickupPowerUp(this);

            game.onPowerUpPickup(this);
        }
    }

    @Override
    public void render(Graphics graphics) {
        graphics.setColor(getColor());
        graphics.fillRect(x - POWER_UP_SIZE/2, y - POWER_UP_SIZE/2, POWER_UP_SIZE, POWER_UP_SIZE);

        /*graphics.setColor(Color.BLUE);
        graphics.drawRect(x - POWER_UP_SIZE/2, y - POWER_UP_SIZE/2, POWER_UP_SIZE, POWER_UP_SIZE);*/
    }
}
