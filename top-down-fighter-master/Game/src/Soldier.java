import java.awt.*;

public class Soldier {

    Vector WindowSize;

    Vector p; //position
    Vector v; //velocity
    Vector sz = new Vector(40, 40); //soldier size
    float d; //direction

    Color c;

    float sp = 105.0f; //speed
    final float ts = (float)Math.toRadians(12); //turning speed

    Vector wp; //weapon position (relative to soldier)

    boolean isMoving; //is the player moving?
    boolean isMovingBackwards; //is the player moving backwards

    boolean isPlayer; //is this player or npc

    public Soldier(boolean isPlayer, int WindowWidth, int WindowHeight, Color c){
        this.isPlayer = isPlayer;
        this.WindowSize = new Vector(WindowWidth, WindowHeight);
        this.c = c;
    }

    public void draw(Graphics2D g){
        g.translate(p.ix, p.iy);
        g.rotate(d);



        g.rotate(-d);
        g.translate(-p.ix, -p.iy);
    }
}
