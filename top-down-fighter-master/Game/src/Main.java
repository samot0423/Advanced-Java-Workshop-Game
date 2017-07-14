import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class Main extends JFrame implements KeyListener {
    int w = 40;
    int h = 40;
    //create array list
    ArrayList<Integer> keys = new ArrayList<>();

    private void handleKeys() {
        for (int i = keys.size() - 1; i >= 0; i--) {
            switch (keys.get(i)) {
                //handle movement
                case KeyEvent.VK_W:
                    y -= vy * dt;
                    break;
                case KeyEvent.VK_A:
                    x -= vx * dt;
                    break;
                case KeyEvent.VK_S:
                    y += vy * dt;
                    break;
                case KeyEvent.VK_D:
                    x += vx * dt;
                    break;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if (!keys.contains(keyEvent.getKeyCode()))
            keys.add(keyEvent.getKeyCode());


    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        for (int i = keys.size() - 1; i >= 0; i--) {
            if (keys.get(i) == keyEvent.getKeyCode())
                keys.remove(i);
        }
    }


    //window vars
    private final int MAX_FPS;
    private final int WIDTH;
    private final int HEIGHT;

    //double buffer
    private BufferStrategy strategy;

    //loop variables
    private boolean isRunning = true;
    private long rest = 0;

    //timing variables
    private float dt;
    private long lastFrame;
    private long startFrame;
    private int fps;

    //player variables
    private float x = 100.0f;
    private float y = 50.0f;

    //gray sprite variables
    private float x2 = 50.0f;
    private float v2 = 100.0f;

    //player velocity variables
    private float vx = 105.0f;
    private float vy = 105.0f;

    public Main(int width, int height, int fps) {
        super("Top Down Fighter");
        this.MAX_FPS = fps;
        this.WIDTH = width;
        this.HEIGHT = height;
    }

    void init() {
        //initialize JFrame
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setBounds(0, 0, WIDTH, HEIGHT);

        setIgnoreRepaint(true);

        setResizable(false);
        setVisible(true);

        //create double buffer strategy
        createBufferStrategy(2);
        strategy = getBufferStrategy();

        lastFrame = System.currentTimeMillis();
        addKeyListener(this);
        setFocusable(true);
    }

    private void update() {
        //update current fps
        fps = (int) (1f / dt);

        handleKeys();

        //update sprite
        x2 += v2 * dt;
        if (x2 < 1 || x2 > (574)) v2 *= -1.0f;

        //set player to teleport to other side when hits one side
        if (x <= 0) {
            x = WIDTH - w - 1;
        } else if (x + w >= WIDTH) {
            x = 1;
        }
        if (y <= 25) {
            y = HEIGHT - h - 5;
        } else if (y + h >= HEIGHT) {
            y = 25;
        }
    }

    private void draw() {

        Color myColor = new Color(117, 68, 24);
        Color MyColor = new Color(52, 157, 39);
        //get canvas
        Graphics2D g = (Graphics2D) strategy.getDrawGraphics();

        //clear screen
        g.setColor(MyColor);
        g.fillRect(0, 0, WIDTH, HEIGHT);


        //put image on screen
        g.drawImage(makeImage("\\Game\\assets\\green grass.jpg"), null, 0, 25);
        g.drawImage(makeImage("\\Game\\assets\\green grass.jpg"), null, 0, 450);


        //draw fps
        g.setColor(Color.cyan);
        g.drawString(Long.toString(fps), 10, 40);

        //draw player
        g.setColor(myColor);
        g.fillOval((int) x, (int) y, w, h);

        //draw sprite
        g.setColor(Color.gray);
        g.fillOval((int) x2, HEIGHT / 2, 50, 50);


        //release resources, show the buffer
        g.dispose();
        strategy.show();
    }


    public void run() {
        init();

        while (isRunning) {
            //new loop, clock the start
            startFrame = System.currentTimeMillis();
            //calculate delta time
            dt = (float) (startFrame - lastFrame) / 1000;
            //log the current time
            lastFrame = startFrame;

            //call update and draw methods
            update();
            draw();

            //dynamic thread sleep, only sleep the time we need to cap the framerate
            rest = (1000 / MAX_FPS) - (System.currentTimeMillis() - startFrame);
            if (rest > 0) {
                try {
                    Thread.sleep(rest);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    BufferedImage makeImage(String path) {
        try {
            //System.out.println(System.getProperty("user.dir") + path);
            return ImageIO.read(
                    new File(System.getProperty("user.dir") + path));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void main(String[] args) {
        Main game = new Main(626, 1000, 80);
        game.run();
    }

}
