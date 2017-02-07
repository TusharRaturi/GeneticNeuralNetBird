package World.Engine;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Tushar on 06-01-2017.
 */
public class Draw
{
    public static void rect(float x, float y, float width, float height)
    {
        glPushMatrix();

        glTranslatef(x, y,0);

        glBegin(GL_QUADS);

        glVertex2f(0, 0);
        glVertex2f(0, height);
        glVertex2f(width, height);
        glVertex2f(width, 0);

        glEnd();

        glPopMatrix();
    }
}
