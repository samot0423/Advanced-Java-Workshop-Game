import java.awt.*;
import java.util.ArrayList;

public class Soldier {

    Vector WindowSize;

    Vector p; //position
    Vector v; //velocity
    Vector sz = new Vector(50, 50); //soldier size
    float d; //direction

    Color c; //soldier color

    float sp = 105.0f; //speed
    final float ts = (float) Math.toRadians(125); //turning speed
    final float ss = ts * 15;
    float di;

    //weapon variables
    Vector wsz = new Vector(sz.y * 1.5f, sz.x / 3); //weapon size
    Vector wp = new Vector(sz.x / 6, sz.y / 2); //weapon position (relative to soldier)
    Vector wb = Vector.add(wp, new Vector(wsz.x * (5.0f / 6.0f), wsz.y / 2.0f)); //weapon hitbox
    float wr = 12.5f; //weapon radius
    Color wc; //weapon color
    boolean dead = false;

    boolean isMoving; //is the player moving?
    boolean isMovingBackwards; //is the player moving backwards

    boolean isPlayer; //is this player or npc

    boolean isSwinging = false;

    //create array list to detect collision between weapon and soldier
    // ArrayList<Soldier> soldiers = new ArrayList<>();

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

        if (!isPlayer) {
            if (Main.player.v.sqmag() == 0) {
                isMoving = false;
                // v = new Vector(0, 0);
            } else {
                Vector fp = Vector.add(Main.player.p, Vector.mult(Main.player.v, 0.25f));
                v = Vector.sub(fp, p);
                v.setMag(sp - 5);
                d = v.dir();
                isMoving = true;
            }
            if (!isSwinging && (Vector.sub(Main.player.p, Main.enemy.p)).sqmag() < Math.pow(100, 2)) {
                Swing();
            }


        } else if (isMoving && isPlayer) {
            v = Vector.unit2D(d);
            v.mult(sp);

            //invert if reversing
            if (isMovingBackwards) v.mult(-.75f);
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
        if (isSwinging) {
            d += ss * dt;
            if (d > di + (Math.PI * 2)) {
                isSwinging = false;
                d %= Math.PI * 2;
            }
        }

    }
    public boolean isDead() {
        return dead;
    }

    //checks soldier a against soldier b's sword
    static boolean isColliding(Soldier a, Soldier b) {
        //checking if 2 circles are intersecting sword blade of b and hit box of soldier a
        if (a.isDead() || b.isDead()) {
            return false;
        } else {
            if (Vector.sub(a.p, Vector.add(b.p, Vector.rotate(b.wb, b.d))).sqmag() < (float) Math.pow(a.sz.ix / 2 + b.wr, 2)) {
                return true;
            } else if (b.isSwinging) {
                if (Vector.sub(a.p, b.p).sqmag() < (float) Math.pow(a.sz.ix / 2 + b.wsz.x, 2)) {
                    return true;
                }
            }
            return Vector.sub(a.p, b.p).sqmag() < (float) Math.pow(a.sz.ix / 2 + b.sz.x / 2, 2);
        }
    }

    public void Swing() {
        di = d;
        isSwinging = true;
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

    public Vector getPosition() {
        return p;
    }

    public Vector getWeaponPosition() {
        return wp;
    }

    public void died(Graphics2D g) {
        dead = true;
        draw(g);
    }

    public void draw(Graphics2D g) {
        if (!dead) {
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
}