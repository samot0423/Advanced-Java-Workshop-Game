import java.awt.*;

public class Soldier {

    Vector WindowSize;

    Vector p; //position
    Vector v; //velocity
    Vector sz = new Vector(40, 40); //soldier size
    float d; //direction

    Color c; //soldier color

    float sp = 105.0f; //speed
    final float ts = (float) Math.toRadians(12); //turning speed

    //weapon variables
    Vector wsz = new Vector(sz.x / 3, sz.y * 1.5f); //weapon size
    Vector wp = new Vector(sz.x / 2, sz.y); //weapon position (relative to soldier)
    Color wc; //weapon color

    boolean isMoving; //is the player moving?
    boolean isMovingBackwards; //is the player moving backwards

    boolean isPlayer; //is this player or npc

    public Soldier(boolean isPlayer, int WindowWidth, int WindowHeight, Color c, Color wc) {
        this.isPlayer = isPlayer;
        this.WindowSize = new Vector(WindowWidth, WindowHeight);
        this.c = c;
        this.wc = wc;

        isMoving = false;
        isMovingBackwards = false;

        v = new Vector(0, 0);
    }

    public void update(float dt) {

        if (isMoving) {
            v.setX(sp); //Vector(sp, 0)
            v.rotate(d); //rotating to up from right

            //invert if reversing
            if (isMovingBackwards) v.mult(-1.0f);
        }

        //update position
        //p += v * dt;
        p.add(Vector.mult(v, dt));

        if (isMoving) isMoving = false;
        if (isMovingBackwards) isMovingBackwards = false;
    }

    public void turnLeft() {
        d += Math.toRadians(ts);
        d %= Math.PI * 2;
    }

    public void turnRight() {
        d -= Math.toRadians(ts);
        d %= Math.PI * 2;
    }

    public void moveForwards() {
        isMoving = true;
    }

    public void moveBackwards() {
        isMovingBackwards = true;
    }


    public void draw(Graphics2D g) {
        g.translate(p.ix, p.iy);
        g.rotate(d);
        //draw soldier
        g.setColor(c);
        g.fillOval(0, 0, sz.ix, sz.iy);
        //draw weapon
        g.setColor(wc);
        g.fillRect(wp.ix, wp.iy, wsz.ix, wsz.iy);

        g.rotate(-d);
        g.translate(-p.ix, -p.iy);
    }
}