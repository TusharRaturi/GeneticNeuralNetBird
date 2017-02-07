package World.Game;

import AI.GA.Eyes;
import Util.Communicator;
import World.Engine.*;
import World.Engine.GameObject.AIPerceptionComponent;

import java.awt.*;
import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;

/**
 * Created by Tushar on 09-01-2017.
 */
public class FPBird extends Game
{
    Floor floor;
    Floor ceiling;
    Bird player[];
    ArrayList<Pipe> pipes;
    static boolean gameOver[];
    private int scr[];
    private double distance[];
    public static final float WIDTH = 600;
    public static final float HEIGHT = 600;
    private AIPerceptionComponent aipc[];
    Communicator ct[];

    private double keyTimer[], timePassed[];

    public FPBird(Eyes eyes[], Communicator ct[])
    {
        super();
        this.ct = ct;

        player = new Bird[eyes.length];
        aipc = new AIPerceptionComponent[eyes.length];
        scr = new int[eyes.length];
        distance = new double[eyes.length];
        keyTimer = new double[eyes.length];
        timePassed = new double[eyes.length];
        gameOver = new boolean[eyes.length];

        pipes = new ArrayList<Pipe>();

        for(int d = 1; d <= 3; d++)
            pipes.add(new Pipe(WIDTH + (d-1) * Pipe.PIPE_DIST_X,  WIDTH / 10, new Color(22, 165, 17), d == 1, d == 3));

        for(int i = 0; i < pipes.size(); i++)
        {
            gameObjects.add(pipes.get(i));
        }

        gameObjects.add(floor = new Floor(0, 0, WIDTH, HEIGHT / 10, new Color(203, 196, 37, 255)));
        gameObjects.add(ceiling = new Floor(0, HEIGHT - HEIGHT / 10, WIDTH, HEIGHT / 10, new Color(203, 196, 37, 255)));

        for(int i = 0; i < player.length; i++)
        {
            gameObjects.add(player[i] = new Bird(WIDTH / 10, HEIGHT / 2 - 25, WIDTH / 20, HEIGHT / 20, new Color(255, 229, 33)));

            aipc[i] = new AIPerceptionComponent(player[i], eyes[i], eyes[i].getSegments());
            gameObjects.add(aipc[i]);

            scr[i] = 0;
            gameOver[i] = false;
        }


        Debug.activateDebugging();
    }

    public static boolean isGameOver()
    {
        boolean go = true;
        for(int i = 0; i < gameOver.length; i++)
        {
            if(!gameOver[i])
            {
                go = false;
                break;
            }
        }
        return go;
    }

    public void input()
    {
        super.input();

        for(int i = 0; i < player.length; i++)
        {
            float key = aipc[i].getResult();

            if (key >= 0.9 && key <= 1.0)
            {
                if (gameOver[i])
                    return;
                else
                {
                    timePassed[i] = System.nanoTime() - keyTimer[i];
                    if (!player[i].isJumping() && timePassed[i] >= 10e+8 / 5)
                    {
                        keyTimer[i] = System.nanoTime();
                        timePassed[i] = keyTimer[i];
                        player[i].doJump();
                    }
                }
            }
        }

        if(KeyboardHandler.isKeyDown(GLFW_KEY_D))
        {
            Debug.toggleDebugging();
        }
    }

    public void update()
    {
        super.update();

        for(int i = 0; i < player.length; i++)
        {
            distance[i] += 0.1;

            if (player[i].getTransform().getX() >= Pipe.firstPipe.getTransform().getX() + Pipe.firstPipe.getSx())
            {
                scr[i]++;
                System.out.println("Score = " + scr[i]);
                int index = pipes.indexOf(Pipe.firstPipe) + 1;
                if (index > 2)
                    index = 0;
                Pipe.setFirst(pipes.get(index));
            }

            if (Physics.checkRectCollision(player[i], Pipe.firstPipe) || Physics.checkRectCollision(player[i], Pipe.firstPipe.getUpper()))
                gameOver[i] = true;
            if (Physics.checkRectCollision(player[i], floor))
            {
                player[i].setSpeedY(0.0f);
                player[i].getTransform().setY(floor.getSy());
                gameOver[i] = true;
            }

            if (Physics.checkRectCollision(player[i], ceiling))
            {
                gameOver[i] = true;
            }

            if(!gameOver[i])
            {
                ct[i].setScore(scr[i]);
                ct[i].setDistance(distance[i]);
            }
            else
            {
                gameObjects.remove(player[i]);
                gameObjects.remove(aipc[i]);
            }
        }
    }
}