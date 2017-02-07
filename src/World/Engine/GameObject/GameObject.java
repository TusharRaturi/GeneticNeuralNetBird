package World.Engine.GameObject;

import World.Engine.Transform;

/**
 * Created by Tushar on 05-01-2017.
 */
public abstract class GameObject
{
    private Transform transform;

    protected GameObject()
    {
        transform = new Transform();
    }

    protected GameObject(float x, float y)
    {
        transform = new Transform(x, y);
    }

    public Transform getTransform()
    {
        return transform;
    }

    public abstract void update();

    public abstract void render();
}
