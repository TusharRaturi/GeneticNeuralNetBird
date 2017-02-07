package World.Engine.GameObject;

import AI.GA.Eyes;
import World.Engine.*;
import World.Game.Bird;
import World.Game.Pipe;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

import static org.lwjgl.opengl.GL11.glColor3f;

/**
 * Created by Tushar on 02-02-2017.
 */

public class AIPerceptionComponent extends GameObject
{
    GORect parent;
    private Eyes eyes;
    private Line perceptionRays[];
    private float result;

    public AIPerceptionComponent(GORect parent, Eyes eyes, int segments)
    {
        this.parent = parent;
        this.eyes = eyes;
        setTransform();

        double start = -Math.toRadians(eyes.getViewAngle()) / 2.0;
        double end = -start;
        double inc = Math.PI / (double)(eyes.getNeurons());

        int i = 0;
        perceptionRays = new Line[eyes.getNeurons()];
        for (; start < end; start += inc)
        {
            perceptionRays[i++] = new Line(new Transform(getTransform().getX(), getTransform().getY()), eyes.getViewDistance(), start, segments);
        }
    }

    public float getResult()
    {
        return result;
    }

    public void setResult(float result)
    {
        this.result = result;
    }

    public void render()
    {
        if(Debug.isDebuggingOn())
        {
            glColor3f(1.0f, 0.1f, 0.1f);
            Draw.rect(getTransform().getX() - 5, getTransform().getY() - 5, 10, 10);

            glBegin(GL_LINES);

                for (int i = 0 ; i < perceptionRays.length; i++)
                {
                    for(int j = 0; j < perceptionRays[i].getSegments(); j++)
                    {
                        if(perceptionRays[i].collisions[j])
                            glColor3f(1.0f, 0.1f, 0.1f);
                        else
                            glColor3f(0.1f, 1.0f, 0.1f);
                        glVertex2f(perceptionRays[i].getOriginOn(j).getX(), perceptionRays[i].getOriginOn(j).getY());
                        glVertex2f(perceptionRays[i].getExtentOn(j).getX(), perceptionRays[i].getExtentOn(j).getY());
                    }
                }

            glEnd();
        }
    }

    private void setTransform()
    {
        getTransform().setX(parent.getTransform().getX() + parent.getSx() / 2);
        getTransform().setY(parent.getTransform().getY() + parent.getSy() / 2);
    }

    public void update()
    {
        setTransform();
        double inputVals[] = new double[eyes.getNnt().getNumNeuronsInLayer(0)];

        int x = 0;
        for (int i = 0; i < perceptionRays.length; i++)
        {
            perceptionRays[i].setOrigin(new Transform(getTransform().getX(), getTransform().getY()));
            for(int j = 0; j < perceptionRays[i].getSegments(); j++)
            {
                if(rayTrace(perceptionRays[i].getOriginOn(j), perceptionRays[i].getExtentOn(j)))
                {
                    perceptionRays[i].setCollisionOn(j, true);
                    inputVals[x++] = -10.0;
                }
                else
                {
                    perceptionRays[i].setCollisionOn(j, false);
                    inputVals[x++] = 10.0;
                }
            }
        }

        eyes.getEyeNet().feedForward(inputVals);

        result = (float)(eyes.getEyeNet().getResults())[0];
    }

    private boolean rayTrace(Transform or, Transform ex)
    {
        ArrayList<GameObject> tempGO = Game.getGameObjects();

        for (int i = 0; i < tempGO.size(); i++)
        {
            GORect tmp;
            try{ tmp = (GORect)tempGO.get(i);}
            catch(ClassCastException cce){ continue; }
            if(tmp != null && !(tmp instanceof Bird))
            {
                Pipe t = new Pipe();
                try{ t = (Pipe)tmp;}
                catch(ClassCastException cce)
                {
                    if (Physics.checkRayAABBCollision(tmp, or, ex))
                        return true;
                }

                if(Physics.checkRayAABBCollision(t, or, ex) || Physics.checkRayAABBCollision(t.getUpper(), or, ex))
                    return true;
            }
        }

        return false;
    }
}