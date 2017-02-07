package World.Engine;

import World.Game.FPBird;
import org.lwjgl.BufferUtils;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Created by Tushar on 05-01-2017.
 */
public class Engine
{
    // The glfw window handle
    private long window;
    private GLFWKeyCallback keyCallback;

    private static Game game;

    public static double Width, Height;

    public Engine(double width, double height, String title, Game gameInst)
    {
        initDisplay(width, height, title);
        initGL();

        game = gameInst;


        Width = width;
        Height = height;
    }

    public void run()
    {
        if(game.getWindow() == 0L)
        {
            try
            {
                throw new Exception("Window not set...");
            }
            catch(Exception e)
            {
                e.printStackTrace();
                System.exit(-1);
            }
        }
        gameLoop();

        cleanUp();
    }

    private void initDisplay(double width, double height, String title)
    {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow((int)width, (int)height, title, NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, keyCallback = new KeyboardHandler());

        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);
    }

    private void initGL()
    {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        // Select the projection matrix
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        IntBuffer wb = BufferUtils.createIntBuffer(4);
        IntBuffer hb = BufferUtils.createIntBuffer(4);

        glfwGetWindowSize(window, wb, hb);
        int w = wb.get(0);
        int h = hb.get(0);

        glOrtho(0, w, 0, h, -1.0, 1.0);

        // Select ModelView Matrix
        glMatrixMode(GL_MODELVIEW);

        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        // No need for depth test(extra information) because we are doing 2D
        glDisable(GL_DEPTH_TEST);
    }

    private void gameLoop()
    {
        while(!((FPBird.isGameOver())))
        {
            input();
            update();
            render();
        }
    }

    public boolean isCloseRequested()
    {
        return glfwWindowShouldClose(window);
    }

    private void input()
    {
        // Poll for window events. The key callback will only be
        // invoked during this call.
        glfwPollEvents();

        game.input();
    }

    private void update()
    {
        game.update();
    }

    private void render()
    {
        // Set the clear color
        glClearColor(0.3f, 0.7f, 0.7f, 0.0f);

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

        glLoadIdentity();

        game.render();

        glfwSwapBuffers(window); // swap the color buffers
    }

    private void cleanUp()
    {
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public long getWindow()
    {
        return window;
    }
}
