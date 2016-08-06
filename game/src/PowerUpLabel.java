import java.awt.*;

public class PowerUpLabel implements Renderable {

    Player player;

    @Override
    public void render(Graphics graphics) {
        PowerUp powerUp = player.getActivePowerUp();
        if (powerUp == null) {
            return;
        }

        String text = new String();

        switch (powerUp.type) {
            case FASTER_BALL:
                text += "Faster ball";
                break;
            case SLOWER_BALL:
                text += "Slower ball";
                break;
            case BIGGER_PADDLE:
                text += "Bigger paddle";
                break;
            case SMALLER_PADDLE:
                text += "Smaller paddle";
                break;
            case FASTER_PADDLE:
                text += "Faster paddle";
                break;
            case SLOWER_PADDLE:
                text += "Slower paddle";
                break;
        }

        text += String.format(" - %1$.1fs", powerUp.effectDuration);

        graphics.setColor(powerUp.effectDuration > 0.2f ? Color.WHITE : Color.RED);
        graphics.drawString(text, 5, 40);
    }
}
