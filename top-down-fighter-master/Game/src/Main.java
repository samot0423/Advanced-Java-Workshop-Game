import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;

import static javafx.scene.paint.Color.rgb;


public class Main extends JFrame implements KeyListener {
    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_W:


        /*    case KeyEvent.VK_SPACE:
                g.setColor(Color.green);
                g.drawString(Long.toString(fps), 10, 40);
                break;
            case KeyEvent.VK_UP:

                break; */


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
    private float x = 50.0f;
    private float v = 10.0f;

    //sprite2 variables
    private float x2 = 50.0f;
    private float v2 = 100.0f;

    //player variables
   // private float x3 =


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
        //  x += v;
        //  if (x < 50 || x > (WIDTH - 50)) v *= -1;

        x2 += v2 * dt;
        if (x2 < 50 || x2 > (WIDTH - 50)) v2 *= -1.0f;

    }

    private void draw() {
        Color myColor = new Color(117, 68, 24);
        Color mycolor = new Color(52, 157, 39);
        //get canvas
        Graphics2D g = (Graphics2D) strategy.getDrawGraphics();

        //clear screen
        g.setColor(mycolor);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        //draw fps
        g.setColor(Color.cyan);
        g.drawString(Long.toString(fps), 10, 40);

        //draw sentence
      /*  g.setColor(Color.black);
        g.drawString("Hi!", );
*/
        //draw sprite
        g.setColor(myColor);
        g.fillOval((int) x, HEIGHT / 5 - 30, 10, 10);

        g.setColor(Color.gray);
        g.fillOval((int) x2, HEIGHT / 2, 50, 50);

        //put image on screen
        // g.drawImage(makeImage("\\assets\\green grass.jpg"), null, (int)x, 0);

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
            return ImageIO.read(
                    new File(System.getProperty("user.dir") + path));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void main(String[] args) {
        Main game = new Main(800, 1000, 80);
        game.run();
    }

}