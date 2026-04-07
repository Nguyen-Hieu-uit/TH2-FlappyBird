import javax.swing.JFrame;

public class FlappyBirdGame {

    public static final int WIDTH = 360;
    public static final int HEIGHT = 640;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Flappy Bird");
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        GamePanel gamePanel = new GamePanel();
        frame.add(gamePanel);
        frame.pack(); // Adjust the frame size to fit the panel
        frame.setSize(WIDTH, HEIGHT); // Ensure the frame is the correct size after packing

        frame.setVisible(true);
    }
}