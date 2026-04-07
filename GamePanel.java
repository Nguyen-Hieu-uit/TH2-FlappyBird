import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.util.ArrayList;
import java.awt.Rectangle;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    private Image backgroundImage;
    private Image birdImage;
    private Image topPipeImage;
    private Image bottomPipeImage;

    private int birdX = FlappyBirdGame.WIDTH / 2 - 20; // Center the bird horizontally
    private int birdY = FlappyBirdGame.HEIGHT / 2; // Center the bird vertically
    private final int BIRD_WIDTH = 34; // Width of the bird image
    private final int BIRD_HEIGHT = 24; // Height of the bird image

    private int velocity = 0;
    private final int GRAVITY = 1;
    private Timer gaTimer;
    private Timer pipeTimer;

    private boolean gameOver = false;
    private double score = 0;

    private final int PIPE_WIDTH = 64;
    private final int PIPE_HEIGHT = 400;
    private final int PIPE_GAP = 150;

    private ArrayList<Pipe> pipes;

    class Pipe {
        int x;
        int y;
        int height;
        int width;
        Image img;

        boolean passed = false; // To track if the bird has passed the pipe for scoring

        Pipe(Image img) {
            this.img = img;
            this.x = FlappyBirdGame.WIDTH; // Start at the right edge of the screen
            this.height = PIPE_HEIGHT;
            this.y = 0;
            this.width = PIPE_WIDTH;
        }
    }

    public GamePanel() {
        try {
            backgroundImage = ImageIO.read(new File("imgs/flappybirdbg.png"));
            System.out.println("Background image loaded successfully.");
            birdImage = ImageIO.read(new File("imgs/flappybird.png"));
            System.out.println("Bird image loaded successfully.");

            topPipeImage = ImageIO.read(new File("imgs/toppipe.png"));
            System.out.println("Top pipe image loaded successfully.");
            bottomPipeImage = ImageIO.read(new File("imgs/bottompipe.png"));
            System.out.println("Bottom pipe image loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error loading images: " + e.getMessage());
            e.printStackTrace();
        }

        pipes = new ArrayList<>();
        
        gaTimer = new Timer(20, this);
        gaTimer.start();

        setFocusable(true);
        
        addKeyListener(this);

        pipeTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });



        pipeTimer.start();
    }

    public void placePipes() {
        int randomPipeY = (int) (0 - PIPE_HEIGHT/4 - Math.random() * (PIPE_HEIGHT/2));
        int openingSpace = FlappyBirdGame.HEIGHT/4;

        Pipe topPipe = new Pipe(topPipeImage);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);
        
        Pipe bottomPipe = new Pipe(bottomPipeImage);
        bottomPipe.y = randomPipeY + PIPE_HEIGHT + openingSpace;
        pipes.add(bottomPipe);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, FlappyBirdGame.WIDTH, FlappyBirdGame.HEIGHT, null);
        }
        if (birdImage != null) {
            g.drawImage(birdImage, birdX, birdY, BIRD_WIDTH, BIRD_HEIGHT, null);
        }
        for (Pipe pipe : pipes) {
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }
        g.drawString("Score: " + (int)score, 10, 20);
        if (gameOver) {
            g.drawString("Game Over!", FlappyBirdGame.WIDTH / 2 - 30, FlappyBirdGame.HEIGHT / 2);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
        velocity += GRAVITY;
        birdY += velocity;

        Rectangle birdRect = new Rectangle(birdX, birdY, BIRD_WIDTH, BIRD_HEIGHT);
        if (birdY + BIRD_HEIGHT > FlappyBirdGame.HEIGHT) {
            birdY = FlappyBirdGame.HEIGHT - BIRD_HEIGHT;
            gameOver = true; // Game over if the bird hits the ground
            
        }

        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.x -= 5;

            Rectangle pipeRect = new Rectangle(pipe.x, pipe.y, pipe.width, pipe.height);
            if (birdRect.intersects(pipeRect)) {
                gameOver = true; // Game over if the bird hits a pipe
            }

            if (!pipe.passed && birdX > pipe.x + pipe.width) {
                score += 0.5;
                pipe.passed = true;
            }
        }
    }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER) {
            velocity = -15; // Make the bird jump
            if(gameOver) {
                // Reset the game
                birdY = FlappyBirdGame.HEIGHT / 2;
                velocity = 0;
                score = 0;
                pipes.clear();
                gameOver = false;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}