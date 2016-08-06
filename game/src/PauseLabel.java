import java.awt.*;


public class PauseLabel implements Renderable {
    @Override
    public void render(Graphics graphics) {
        graphics.setColor(Color.WHITE);
        graphics.drawString("PAUSE", 800/2-20, 250);
        graphics.drawString("Press ESC to unpause", 800/2-60, 300);
    }
}
