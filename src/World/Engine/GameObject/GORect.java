package World.Engine.GameObject;

import World.Engine.Draw;

import java.awt.*;

import static org.lwjgl.opengl.GL11.glColor3f;

/**
 * Created by Tushar on 06-01-2017.
 */
public abstract class GORect extends GameObject
{
    protected float sx;
    protected float sy;

    private Color clr;

    protected GORect()
    {
        super();
        sx = sy = 0.0f;
        clr = new Color(255, 255, 255);
    }

    protected GORect(float x, float y)
    {
        super(x, y);
        sx = sy = 50;
        clr = new Color(255, 255, 255);
    }

    protected GORect(float x, float y, float sx, float sy)
    {
        this(x, y);
        this.sx = sx;
        this.sy = sy;
        clr = new Color(255, 255, 255);
    }

    protected GORect(float x, float y, float sx, float sy, Color clr)
    {
        this(x, y);
        this.sx = sx;
        this.sy = sy;
        this.clr = clr;
    }

    public double getCenterY()
    {
        return getTransform().getY() + sy / 2;
    }

    public void update(){}

    public void render()
    {
        glColor3f(clr.getRed() / 255.0f, clr.getGreen() / 255.0f, clr.getBlue() / 255.0f);
        Draw.rect((float)getTransform().getX(), (float)getTransform().getY(), sx, sy);
    }

    public float getSx()
    {
        return sx;
    }

    public float getSy()
    {
        return sy;
    }

    public Color getColor()
    {
        return clr;
    }
}
