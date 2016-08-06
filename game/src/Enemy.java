import java.awt.*;

public class Enemy implements Renderable {

    public final static int ENEMY_WIDTH = 30;
    public final static int ENEMY_HEIGHT = 20;

    int x;
    int y;

    Color color = Color.RED;

    Enemy(int x, int y) {
        this.x = x;
        this.y = y;
    }

    void kill(Game game) {
        game.onEnemyKill(this);
    }

    public Rectangle getBoundingBox() {
        return new Rectangle(x, y, ENEMY_WIDTH, ENEMY_HEIGHT);
    }

    @Override
    public void render(Graphics graphics) {
        graphics.setColor(color);
        graphics.fillRect(x, y, ENEMY_WIDTH, ENEMY_HEIGHT);

        /*graphics.setColor(Color.BLUE);
        graphics.drawRect(x, y, ENEMY_WIDTH, ENEMY_HEIGHT);*/
    }
}
