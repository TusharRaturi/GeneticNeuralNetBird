package World.Game;

import World.Engine.GameObject.AIPerceptionComponent;
import World.Engine.GameObject.GORect;
import World.Engine.GameObject.GameObject;
import World.Engine.Transform;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Tushar on 09-01-2017.
 */
public class Bird extends GORect
{
    private float speedY;
    private static final float JUMPSPEED = FPBird.HEIGHT / 60;

    private boolean isJumping = false;

    Bird(float x, float y, float sx, float sy, Color clr)
    {
        super(x, y, sx, sy, clr);

        speedY = 0;
    }

    public void update()
    {
        super.update();

        speedY -=  FPBird.HEIGHT / 1200;

        if(isJumping)
        {
            speedY = JUMPSPEED;
            isJumping = false;
        }

        getTransform().setY(getTransform().getY() + speedY);

        if(getTransform().getY() >= FPBird.HEIGHT - sy)
        {
            getTransform().setY(FPBird.HEIGHT - sy);
            speedY = -FPBird.HEIGHT / 1200;;
        }
    }

    public void doJump()
    {
        isJumping = true;
    }

    public void setSpeedY(float speedY)
    {
        this.speedY = speedY;
    }

    public boolean isJumping()
    {
        return isJumping;
    }
}
