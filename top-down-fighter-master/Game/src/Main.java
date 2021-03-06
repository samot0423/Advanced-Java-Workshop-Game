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
    public static Soldier player;
    public static Soldier enemy;
    Color myColor = new Color(117, 68, 24);
    Color mycolor = new Color(251, 251, 251);
    Color MyColor = new Color(52, 157, 39);
    boolean lose = false;
    float timer = 1.5f;
    int i = 0;

    Graphics2D g;

    //create array list
    ArrayList<Integer> keys = new ArrayList<>();
    //ArrayList<Integer> enemies = new ArrayList<>();

    private void handleKeys() {
        for (int i = keys.size() - 1; i >= 0; i--) {
            switch (keys.get(i)) {
                //handle movement
                case KeyEvent.VK_W:
                    player.moveForwards();
                    break;
                case KeyEvent.VK_A:
                    player.turnLeft();
                    break;
                case KeyEvent.VK_D:
                    player.turnRight();
                    break;
                case KeyEvent.VK_S:
                    player.moveBackwards();
                    break;

            }
        }
       /* Vector currentp = player.getPosition();
        for (Soldier s : soldiers) {
            if (s.getWeaponPosition() == currentp) {
                //player dies
                soldiers.remove(player);
                System.out.println("Player died!");
            } else if (s.getPosition() == player.getWeaponPosition()) {
                //s died
                soldiers.remove(s);
                System.out.println("soldier s Died!");
            }
        } */

    }

    public Color makeRandomColor() {
        return new Color(
                (int) (Math.random() * 255),
                (int) (Math.random() * 255),
                (int) (Math.random() * 255)
        );
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
            switch (keys.get(i)) {
                case KeyEvent.VK_SPACE:
                    player.Swing();
                    break;
            }
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

    public Main(int width, int height, int fps) {
        super("Top Down Fighter"); //name of the window
        this.MAX_FPS = fps;
        this.WIDTH = width;
        this.HEIGHT = height;
    }

    void init() {
        player = new Soldier(true, 626, 1000, Color.gray, mycolor); //create the player
        enemy = new Soldier(false, 626, 1000, makeRandomColor(), makeRandomColor()); //create the first enemy
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

        player.update(dt);
        enemy.update(dt);
        timer -= dt;
        if (timer <= 0) {
            if (Soldier.isColliding(player, enemy)) {
                System.out.println("You lose!");
                lose = true;
                draw();
                player.died(g);
            } else if (Soldier.isColliding(enemy, player)) {
                i++;
                System.out.println("Enemy " + i + " died!");
                enemy.died(g);
                enemy = new Soldier(false, 626, 1000, makeRandomColor(), makeRandomColor());
                timer = 1.5f;
            }
        }
    }

    private void draw() {
        //get canvas
        g = (Graphics2D) strategy.getDrawGraphics();

        //clear screen
        g.setColor(MyColor);
        g.fillRect(0, 0, WIDTH, HEIGHT);


        //put image on screen
        g.drawImage(makeImage("\\Game\\assets\\green grass.jpg"), null, 0, 25);
        g.drawImage(makeImage("\\Game\\assets\\green grass.jpg"), null, 0, 450);


        //draw fps
        g.setColor(Color.WHITE);
        g.drawString(Long.toString(fps), 10, 40);

        //draw player
        player.draw(g);

        //draw enemy
        enemy.draw(g);

        if (lose) {
            Font font = new Font("My Font", Font.BOLD, 30);
            g.setColor(Color.BLACK);
            g.clearRect(0, 0, WIDTH, HEIGHT);
            g.setFont(font);
            //System.out.println("Lose screen");
            g.drawString("You died!", 250, 400);
            if (i == 0) {
                g.drawString("You killed no one.", 200, 450);
                //g.drawString("Patience, you must have, young padawan", 5, 550);
            } else if (i == 1) {
                g.drawString("You killed a single man." /*"Or woman."*/, 150, 450);
               // g.drawString("Women could be knights too", 50, 500);
                // g.drawString("Maybe you should practice some more.", 50, 550);
            } else {
                g.drawString("You murdered " + i + " enemies!", 150, 450);
                //g.drawString("You did well, young padawan.", 150, 500);
            }
        } else {
            Font font = new Font("My Font", Font.BOLD, 30);
            g.setColor(Color.WHITE);
            g.setFont(font);
            if (i == 1) {
                g.drawString(i + " kill", 500, 975);
            } else {
                g.drawString(i + " kills", 500, 975);
            }
        }
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
            /*
            in case of error, use this format
            System.out.println(System.getProperty("user.dir") + path);
            */
            return ImageIO.read(
                    new File(System.getProperty("user.dir") + path));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //set window size
    public static void main(String[] args) {
        Main game = new Main(626, 1000, 100);
        game.run();
    }

}
