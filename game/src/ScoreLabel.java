import java.awt.*;

public class ScoreLabel implements Renderable {
    public int score = 0;

    @Override
    public void render(Graphics graphics) {
        graphics.setColor(Color.WHITE);
        graphics.drawString("Score: " + score, 5, 15);
    }
}
