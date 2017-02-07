package World.Engine;

/**
 * Created by Tushar on 05-01-2017.
 */
public class Transform
{
    private float x;
    private float y;

    public Transform()
    {
        x = y = 0.0f;
    }

    public Transform(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public float getX()
{
    return x;
}

    public float getY()
    {
        return y;
    }

    public void setX(float x)
    {
        this.x = x;
    }

    public void setY(float y)
    {
        this.y = y;
    }
}
