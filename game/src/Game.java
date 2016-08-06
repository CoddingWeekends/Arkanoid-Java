import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Game implements Runnable {

    final int START_ENEMIES_COUNT = 72;

    private JFrame window = null;
    private Renderer renderer = null;
    private Input input = null;

    private boolean running = true;

    public Player player;
    private Ball ball;
    public ArrayList<Enemy> enemies = new ArrayList<>();
    private ArrayList<Enemy> enemiesToKill = new ArrayList<>();
    private ScoreLabel scoreLabel;

    private ArrayList<PowerUp> fallingPowerUps = new ArrayList<>();
    private ArrayList<PowerUp> pickedUpPowerUps = new ArrayList<>();

    private GameOverLabel gameOverLabel = null;

    public static Game Instance = null;

    private long lastTime = 0;
    private double accumulator = 0.0f;

    private int enemiesCount = START_ENEMIES_COUNT;

    private float gameTime = 0.0f;
    private float deltaTime = 0.0f;

    private int pause = 0;

    Game() {
        Instance = this;

        lastTime = System.nanoTime();

        window = new JFrame("Arkanoid");
        window.setSize(800, 600);
        window.setVisible(true);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        input = new Input(window);

        renderer = new Renderer();
        renderer.setBounds(0, 0, 800, 600);

        window.add(renderer);

        renderer.createBufferStrategy(3);

        player = new Player();
        renderer.registerRenderable(player);

        ball = new Ball();
        renderer.registerRenderable(ball);

        generateEnemies();

        scoreLabel = new ScoreLabel();
        renderer.registerRenderable(scoreLabel);

        PowerUpLabel powerUpLabel = new PowerUpLabel();
        powerUpLabel.player = player;
        renderer.registerRenderable(powerUpLabel);
    }

    void pause() {
        pause++;

        deltaTime = 0.0f;
    }

    boolean gameOver = false;

    void onEnemyKill(Enemy enemy) {
        enemiesToKill.add(enemy);

        if (Math.random() < 0.3) {
            PowerUp powerUp = new PowerUp(enemy.x, enemy.y);
            renderer.registerRenderable(powerUp);
            fallingPowerUps.add(powerUp);
        }
    }

    void handleEnemiesKill() {
        while (enemiesToKill.size() > 0) {
            Enemy toKill = enemiesToKill.get(0);
            enemies.remove(toKill);
            enemiesToKill.remove(toKill);
            renderer.unregisterRenderable(toKill);

            scoreLabel.score += 1;
        }

        if (enemies.isEmpty()) {
            ball.respawn();

            enemiesCount *= 2;
            generateEnemies();
        }
    }

    void onPowerUpPickup(PowerUp powerUp) {
        pickedUpPowerUps.add(powerUp);
        renderer.unregisterRenderable(powerUp);
    }


    void handlePowerupsPickup() {
        while (pickedUpPowerUps.size() > 0) {
            PowerUp powerUp = pickedUpPowerUps.get(0);
            fallingPowerUps.remove(powerUp);
            pickedUpPowerUps.remove(powerUp);
        }
    }

    void restartGame() {
        // remove enemies
        enemiesToKill.clear();
        while (enemies.size() > 0) {
            Enemy enemy = enemies.get(0);
            renderer.unregisterRenderable(enemy);
            enemies.remove(enemy);
        }

        // remove all powerups
        pickedUpPowerUps.clear();
        while (fallingPowerUps.size() > 0) {
            PowerUp powerUp = fallingPowerUps.get(0);
            renderer.unregisterRenderable(powerUp);
            fallingPowerUps.remove(powerUp);
        }

        renderer.unregisterRenderable(gameOverLabel);
        gameOverLabel = null;

        scoreLabel.score = 0;
        enemiesCount = START_ENEMIES_COUNT;
        gameOver = false;

        ball.respawn();
        player.respawn();

        generateEnemies();

        unPause();
    }

    void unPause() {
        assert(pause > 0);
        if (--pause == 0) {
            lastTime = System.nanoTime();
        }
    }

    void generateEnemies() {
        int currentY = 50;
        int currentX = 10;
        boolean offsetSwitch = true;

        int row = 0;
        for (int i = 0; i < enemiesCount; ++i) {
            Enemy enemy = new Enemy(currentX, currentY);
            enemy.color = (row%2) == 0 ? Color.RED : Color.ORANGE;
            renderer.registerRenderable(enemy);
            enemies.add(enemy);

            int newX = currentX + Enemy.ENEMY_WIDTH + 2;
            if ((newX + Enemy.ENEMY_WIDTH) >= (800 - 10)) {
                ++row;
                currentY += Enemy.ENEMY_HEIGHT + 2;
                newX = offsetSwitch ? 15 : 10;
                offsetSwitch = !offsetSwitch;
            }
            currentX = newX;
        }
    }

    final double fixedDelta = 1.0f / 60.0f;

    void updateFixedTick() {
        while (accumulator > fixedDelta) {
            player.tick();
            ball.tick(this);

            for (int i = 0; i < fallingPowerUps.size(); ++i) {
                fallingPowerUps.get(i).tick(this, player);
            }

            accumulator -= fixedDelta;
        }

        handleEnemiesKill();
        handlePowerupsPickup();
    }

    boolean requestedPause = false;
    boolean wasEscapePressed = false;

    PauseLabel pauseLabel = new PauseLabel();

    void tick() {
        player.handleInput(input);

        if (pause == 0) {
            player.updatePowerUps();
        }

        if (input.IsKeyPressed(KeyEvent.VK_ENTER) && gameOver) {
            restartGame();
        }

        final boolean escapePressed = input.IsKeyPressed(KeyEvent.VK_ESCAPE);
        if (escapePressed && !wasEscapePressed) {
            if (requestedPause) {
                unPause();
                renderer.unregisterRenderable(pauseLabel);
                requestedPause = false;
            }
            else {
                pause();
                renderer.registerRenderable(pauseLabel);
                requestedPause = true;
            }
        }
        wasEscapePressed = escapePressed;

        long now = System.nanoTime();
        long delta = now - lastTime;
        lastTime = now;

        final long nanosecondsInSecond = 1000000000;

        if (delta < 0) delta = 0;
        if (delta > nanosecondsInSecond) delta = nanosecondsInSecond;

        if (pause == 0) {
            double deltaSeconds = (double)delta / (double)nanosecondsInSecond;
            deltaTime = (float)deltaSeconds;
            gameTime += (float)deltaSeconds;
            accumulator += deltaSeconds;

            updateFixedTick();
        }
    }

    public float getDeltaTime() {
        return deltaTime;
    }

    /** Get gameplay time in seconds */
    public float getTime() {
        return gameTime;
    }

    public void gameOver() {
        pause();
        gameOver = true;

        gameOverLabel = new GameOverLabel();
        gameOverLabel.score = scoreLabel.score;
        renderer.registerRenderable(gameOverLabel);
    }

    public void run() {
        window.requestFocus();

        while (running) {
            tick();
            renderer.render();
        }
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.run();
    }
}
