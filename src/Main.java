import AI.FFNN.NeuralNetTopology;
import AI.GA.DNA;
import AI.GA.Eyes;
import AI.GA.Population;
import World.Engine.Engine;
import World.Game.FPBird;

/**
 * Created by Tushar on 02-02-2017.
 */
public class Main
{
    public static void main(String args[])
    {
        Population population = new Population(50, 24, 24, 0.01);

        while(true)
        {
            population.evolve(true); //reproduce and do crossover and mutation
        }
    }
}
