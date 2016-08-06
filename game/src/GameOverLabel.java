import java.awt.*;

/**
 * Created by Eryk on 06.08.2016.
 */
public class GameOverLabel implements Renderable {

    public int score;

    public void render(Graphics graphics) {
        graphics.setColor(Color.RED);

        graphics.drawString("Game Over", 800/2-40, 200);
        graphics.drawString("Score: " + score, 800/2-25, 300);

        graphics.setColor(Color.WHITE);
        graphics.drawString("Press ENTER to restart", 800/2-60, 400);
    }

}
