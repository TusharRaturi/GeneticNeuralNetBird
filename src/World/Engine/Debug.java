package World.Engine;

/**
 * Created by Tushar on 02-02-2017.
 */
public class Debug
{
    private static boolean debug = false;

    public static void toggleDebugging()
    {
        debug = !debug;
    }

    public static void activateDebugging()
    {
        debug = true;
    }

    public static boolean isDebuggingOn()
    {
        return debug;
    }
}
