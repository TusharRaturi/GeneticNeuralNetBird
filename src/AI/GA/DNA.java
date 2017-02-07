package AI.GA;

import Util.Communicator;
import World.Engine.Engine;
import World.Game.FPBird;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Tushar on 31-01-2017.
 */
public class DNA
{
    Eyes genes;
    double fitness;
    static double mutationRate;

    public DNA(int neurons, int segments, double mutRate)
    {
        genes = new Eyes(180.0, 500.0, neurons, segments);
        fitness = 0.0;
        mutationRate = mutRate;
    }

    public void calculateFitness(Communicator ct)
    {
        this.fitness = ct.getScore() * 1000 + ct.getDistance() * 50;
        System.out.println("Fitness = " + this.fitness);
    }

    public void fillRandom()
    {
        genes.getEyeNet().setRandomWeights();
    }

    public void print()
    {
        System.out.println(genes);
    }

    public double getFitness()
    {
        return fitness;
    }

    public char getRandomGene()
    {
        int c = ThreadLocalRandom.current().nextInt(63, 122 + 1);

        if(c == 63) c = 32;
        if(c == 64) c = 46;

        return (char)c;
    }

    public void mutate()
    {
        double geneticCode[] = genes.getEyeNet().getWeights();
        for(int i = 0; i < geneticCode.length; i++)
        {
            double r = (ThreadLocalRandom.current().nextInt(0, 1000 + 1));
            double ran = r / 1000;
            if(ran < mutationRate)
            {
                if(r<=500)
                    geneticCode[i] += genes.getEyeNet().getRandomNodeWeight() / 5;
                else
                    geneticCode[i] -= genes.getEyeNet().getRandomNodeWeight() / 5;
            }
        }
        genes.getEyeNet().setWeights(geneticCode);
    }

    public Eyes getGenes()
    {
        return genes;
    }

    public void setGenes(Eyes genes)
    {
        this.genes = genes;
    }
}
