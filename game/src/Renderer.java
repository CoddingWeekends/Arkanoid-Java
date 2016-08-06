import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class Renderer extends Canvas {

    private ArrayList<Renderable> renderables = new ArrayList<>();

    Renderer() {
        // canvas does not need to be focusable, we want our main window handlers to run all the time.
        setFocusable(false);
    }

    public void registerRenderable(Renderable renderable) {
        renderables.add(renderable);
    }

    public void unregisterRenderable(Renderable renderable) {
        renderables.remove(renderable);
    }

    public void render() {
        BufferStrategy bufferStrategy = getBufferStrategy();

        Graphics graphics = bufferStrategy.getDrawGraphics();

        graphics.clearRect(0, 0, 800, 600);

        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, 800, 600);

        for (int i = 0; i < renderables.size(); ++i) {
            renderables.get(i).render(graphics);
        }

        bufferStrategy.show();
    }
}
