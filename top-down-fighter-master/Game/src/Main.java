import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;


public class Main extends JFrame implements KeyListener {
    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    Color c;

    @Override
    public void keyPressed(KeyEvent keyEvent) {


        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_SPACE:
           /*     g.setColor(Color.green);
                g.drawString(Long.toString(fps), 10, 40);   */
                break;
            case KeyEvent.VK_UP:

                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

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

    //sprite1 variables
    private float x = 50;
    private float v = 10;

    //sprite2 variables
    private float x2 = 50.0f;
    private float v2 = 100.0f;


    public Main(int width, int height, int fps) {
        super("JFrame Demo");
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
    }

    private void update() {
        //update current fps
        fps = (int) (1f / dt);

        //update sprite
        x += v;
        if (x < 50 || x > (WIDTH - 50)) v *= -1;

        x2 += v2 * dt;
        if (x2 < 50 || x2 > (WIDTH - 50)) v2 *= -1.0f;
    }

    private void draw() {
        //get canvas
        Graphics2D g = (Graphics2D) strategy.getDrawGraphics();

        //clear screen
        g.setColor(Color.black);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        //draw fps
        g.setColor(Color.green);
        g.drawString(Long.toString(fps), 10, 40);

        //draw sentence
        g.setColor(Color.black);
        g.drawString("Hi!", );

        //draw sprite
        g.setColor(Color.darkGray);
        g.fillRect((int) x, HEIGHT / 2 - 25, 100, 100);

        g.setColor(Color.lightGray);
        g.fillRect((int) x2, HEIGHT / 2, 50, 50);

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


    public static void main(String[] args) {
        Main game = new Main(800, 1000, 50);
        game.run();
    }

}