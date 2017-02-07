package World.Game;

/**
 * Created by Tushar on 21-01-2017.
 */

import World.Engine.Draw;
import World.Engine.GameObject.GORect;

import java.awt.*;

import static org.lwjgl.opengl.GL11.glColor3f;

public class Pipe extends GORect
{
    private float speedX;
    public static Pipe firstPipe;
    private static Pipe lastPipe;
    public static final float PIPE_DIST_Y = (float)(FPBird.HEIGHT / 3.428571428571429);
    public static final float PIPE_DIST_X = (float)(FPBird.WIDTH / 0.5);

    public Pipe(){}

    Pipe(float x, float sx, Color clr, boolean isFirst, boolean isLast)
    {
        super(x, 0, sx, getRandomHeight(), clr);

        speedX = - FPBird.WIDTH / 300;

        if(isFirst)
            firstPipe = this;

        if(isLast)
            lastPipe = this;
    }

    public static float getRandomHeight()
    {
        //return random((int)(FPBird.HEIGHT / 4), (int)(FPBird.HEIGHT / 1.714285714285714));
        return FPBird.HEIGHT / 2 - PIPE_DIST_Y / 2;
    }

    public void update()
    {
        if(FPBird.isGameOver())
            return;
        getTransform().setX(getTransform().getX() + speedX);

        if(this.getTransform().getX() < -getSx())
        {
            this.getTransform().setX(lastPipe.getTransform().getX() + PIPE_DIST_X);
            lastPipe = this;
            sy = getRandomHeight();
        }
    }

    public void render()
    {
        super.render();
        glColor3f(getColor().getRed() / 255.0f, getColor().getGreen() / 255.0f, getColor().getBlue() / 255.0f);
        Draw.rect(getTransform().getX(), getTransform().getY(), sx, sy);
        Draw.rect(getUpperX(), getUpperY(), getUpperSx(), getUpperSy());
    }

    private float getUpperX()
    {
        return getTransform().getX();
    }

    private float getUpperY()
    {
        return getTransform().getY() + sy + PIPE_DIST_Y;
    }

    private float getUpperSx()
    {
        return sx;
    }

    private float getUpperSy()
    {
        return FPBird.HEIGHT - (sy + PIPE_DIST_Y);
    }

    public GORect getUpper()
    {
        return new GORect(getUpperX(), getUpperY(), getUpperSx(), getUpperSy()) {};
    }

    public static void setFirst(Pipe p)
    {
        firstPipe = p;
    }

    static float random(int min, int max)
    {
        float range = (max - min) + 1;
        return (float)(Math.random() * range) + min;
    }
}
