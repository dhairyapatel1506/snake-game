import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    final int DELAY = 90;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int bugsEaten;
    int bugX;
    int bugY;
    char direction = 'R';
    boolean running = false;
    boolean changedDirection = false;
    Timer timer;
    Random random;
    
    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
    
    public void startGame() {
        newBug();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
        Toolkit.getDefaultToolkit().sync();
    }

    public void draw(Graphics g) {
        if(running) {
            /*
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }
            */
            g.setColor(Color.red);
            g.fillRect(bugX, bugY, UNIT_SIZE, UNIT_SIZE);

            for(int i = 0; i < bodyParts; i++) {
                if(i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.red);
            g.setFont(new Font("Calibri", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + bugsEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + bugsEaten)) / 2, g.getFont().getSize());
        }
        else {
            gameOver(g);
        }
    }

    public void newBug() {
        bugX = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
        bugY = random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
        for(int i = 0; i < bodyParts; i++) {
            if(bugX == x[i] && bugY == y[i])
                newBug();
        }
    }

    public void move() {
        for(int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch(direction) {
            case 'U': y[0] = y[0] - UNIT_SIZE;
            break;
            case 'D': y[0] = y[0] + UNIT_SIZE;
            break;
            case 'L': x[0] = x[0] - UNIT_SIZE;
            break;
            case 'R': x[0] = x[0] + UNIT_SIZE;
        }
        changedDirection = false;
    }

    public void checkBug() {
        if(x[0] == bugX && y[0] == bugY) {
            bodyParts++;
            bugsEaten++;
            newBug();
        }
    }

    public void checkCollisions() {
        for(int i = bodyParts; i > 0; i--) {
            if(x[0] == x[i] && y[0] == y[i]) {
                running = false;
            }
        }
        if(x[0] < 0) {
            running = false;
        }
        if(x[0] >= SCREEN_WIDTH) {
            running = false;
        }
        if(y[0] < 0) {
            running = false;
        }
        if(y[0] >= SCREEN_HEIGHT) {
            running = false;
        }
        if(!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Calibri", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + bugsEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + bugsEaten)) / 2, g.getFont().getSize());

        g.setColor(Color.red);
        g.setFont(new Font("Calibri", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running) {
            move();
            checkBug();
            checkCollisions();
        }
        repaint();
        Toolkit.getDefaultToolkit().sync();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if(!changedDirection) {
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_LEFT: 
                        if(direction != 'R') {
                            direction = 'L';
                            changedDirection = true;
                        }
                    break;
                    case KeyEvent.VK_RIGHT:
                        if(direction != 'L') {
                            direction = 'R';
                            changedDirection = true;
                        }
                    break;
                    case KeyEvent.VK_UP:
                        if(direction != 'D') {
                            direction = 'U';
                            changedDirection = true;
                        }
                    break;
                    case KeyEvent.VK_DOWN:
                        if(direction != 'U') {
                            direction = 'D';
                            changedDirection = true;
                        }
                    break;
                }
            }
        }
    }
}
