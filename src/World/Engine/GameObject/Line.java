package World.Engine.GameObject;

import World.Engine.Transform;

/**
 * Created by Tushar on 03-02-2017.
 */
public class Line
{
    double distance;
    double angle;
    Transform origins[];
    Transform extents[];
    boolean collisions[];
    double cos;
    double sin;
    double dbs;

    public int getSegments() {
        return segments;
    }

    private int segments;

    public Transform getOriginOn(int idx)
    {
        return origins[idx];
    }

    public Transform getExtentOn(int idx)
    {
        return extents[idx];
    }

    public boolean isColliding() {
        return colliding;
    }

    public void setColliding(boolean colliding) {
        this.colliding = colliding;
    }

    public void setCollisionOn(int idx, boolean val)
    {
        collisions[idx] = val;
    }

    boolean colliding;

    Line(Transform origin, double distance, double angle, int segments)
    {
        this.distance = distance;
        this.angle = angle;
        this.origins = new Transform[segments];
        this.extents = new Transform[segments];
        this.segments = segments;

        cos = Math.cos(angle);
        sin = Math.sin(angle);
        dbs = (distance)/(segments);

        setOrigin(origin);
        collisions = new boolean[segments];

        colliding = false;
    }

    public void setOrigin(Transform origin)
    {
        origins[0] = origin;

        extents[extents.length - 1] = new Transform((float)(origin.getX() + distance * cos), (float)(origin.getY() + distance * sin));
        for(int i = origins.length; i >= 2; i--)
        {
            int olmi = origins.length - i;
            extents[olmi] = origins[olmi + 1] = new Transform((float)((origins[olmi].getX() + dbs * cos)), (float)((origins[olmi].getY() + dbs * sin)));
        }
    }

    public double getDistance()
    {
        return distance;
    }

    public double getAngle()
    {
        return angle;
    }
}
