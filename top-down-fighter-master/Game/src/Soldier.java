import java.awt.*;

public class Soldier {

    Vector WindowSize;

    Vector p; //position
    Vector v; //velocity
    Vector sz = new Vector(50, 50); //soldier size
    float d; //direction

    Color c; //soldier color

    float sp = 105.0f; //speed
    final float ts = (float) Math.toRadians(125); //turning speed

    //weapon variables
    Vector wsz = new Vector(sz.y * 1.5f, sz.x / 3); //weapon size
    Vector wp = new Vector(sz.x / 6, sz.y / 2); //weapon position (relative to soldier)
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

        d = 0;
        v = new Vector(0, 0);
        p = new Vector((float) Math.random() * WindowSize.x, (float) Math.random() * WindowSize.y);

    }

    public void update(float dt) {

        if (isMoving) {
            v = Vector.unit2D(d);
            v.mult(sp);

            //invert if reversing
            if (isMovingBackwards) v.mult(-1.0f);
        } else
            v = new Vector(0, 0);

        //update position
        //p += v * dt;
        p.add(Vector.mult(v, dt));

        if (isMoving)
            isMoving = false;
        if (isMovingBackwards) isMovingBackwards = false;

        //set player to teleport to other side when hits one side
        if (p.ix <= 25) {
            p.setX(WindowSize.x - sz.x + 24);
        } else if (p.ix + sz.x >= WindowSize.x + 25) {
            p.setX(25);
        }
        if (p.iy <= 50) {
            p.setY(WindowSize.y - sz.y + 24);
        } else if (p.iy + sz.y >= WindowSize.y + 25) {
            p.setY(51);
        }
    }

    public void turnLeft() {
        d -= Math.toRadians(ts);
        d %= Math.PI * 2;
    }

    public void turnRight() {
        d += Math.toRadians(ts);
        d %= Math.PI * 2;
    }

    public void moveForwards() {
        isMoving = true;
    }

    public void moveBackwards() {
        isMoving = true;
        isMovingBackwards = true;
    }


    public void draw(Graphics2D g) {
        g.translate(p.ix, p.iy);
        g.rotate(d);
        //draw soldier
        g.setColor(c);
        g.fillOval(-sz.ix / 2, -sz.iy / 2, sz.ix, sz.iy);
        //draw weapon
        g.setColor(wc);
        g.fillRect(wp.ix, wp.iy, wsz.ix, wsz.iy);

        g.rotate(-d);
        g.translate(-p.ix, -p.iy);
    }
}