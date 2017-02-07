package AI.GA;

import AI.FFNN.NeuralNetTopology;
import Util.Communicator;
import World.Engine.Engine;
import World.Game.FPBird;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Tushar on 31-01-2017.
 */

//GENOTYPE AND PHENOTYPE

public class Population
{
    DNA population[];
    DNA fittest;

    public Population(int maxPopulation, int neurons, int segments, double mutationRate)
    {
        population = new DNA[maxPopulation];

        for(int i = 0; i < population.length; i++)
        {
            population[i] = new DNA(neurons, segments, mutationRate);
        }

        fillRandom();
    }

    public void fillRandom()
    {
        for(int i = 0; i < population.length; i++)
        {
            population[i].fillRandom();
        }
    }

    public void evolve(boolean mutate)
    {
        calculateFitness();

        DNA newPopulation[] = new DNA[population.length];
        newPopulation[0] = fittest;

        for(int i = 1; i < newPopulation.length; i++)
        {
            reproduce(generateMatingPool(), newPopulation, i, mutate);
        }
        this.population = newPopulation;
    }

    public void newGame(Eyes eyes[], Communicator ct[])
    {
        FPBird fpBird = new FPBird(eyes, ct);
        Engine e = new Engine(600, 600, "AIBird", fpBird);

        fpBird.setWindow(e.getWindow());

        e.run();
    }

    public void calculateFitness()
    {
        Communicator ct[] = new Communicator[population.length];
        Eyes pop[] = new Eyes[population.length];

        for(int i = 0; i< population.length; i++)
        {
            pop[i] = population[i].getGenes();
            ct[i] = new Communicator();
        }

        newGame(pop, ct);

        population[0].calculateFitness(ct[0]);
        fittest = population[0];
        for(int i = 1; i < population.length; i++)
        {
            population[i].calculateFitness(ct[i]);
            if(population[i].getFitness() > fittest.getFitness())
                fittest = population[i];
        }

        DNA.mutationRate = 1 / Math.sqrt(fittest.getFitness() / 50.0);
        System.out.println("Mutation Rate = " + DNA.mutationRate);
    }

    public DNA[] generateMatingPool()
    {
        return findParents();//acceptReject();
    }

    private double normalize(double fitness, double maxFitness)
    {
        return (fitness / maxFitness);
    }

    private DNA[] findParents()
    {
        DNA parents[] = new DNA[2];

        ArrayList<DNA> tmpPop = new ArrayList<DNA>();

        for(int i = 0; i < population.length; i++)
        {
            int n = (int)(normalize(population[i].getFitness(), fittest.getFitness()) * 100);
            for(int j = 0; j < n; j++)
            {
                tmpPop.add(population[i]);
            }
        }

        int r1 = ThreadLocalRandom.current().nextInt(0, tmpPop.size());
        int r2 = ThreadLocalRandom.current().nextInt(0, tmpPop.size());

        parents[0] = tmpPop.get(r1);
        parents[1] = tmpPop.get(r2);

        tmpPop.clear();

        return parents;
    }

    private double[] getNormalizedFitness()
    {
        double fitness[] = new double[population.length];
        double mostFit = fittest.getFitness();

        for(int i = 0; i < fitness.length; i++)
        {
            fitness[i] = normalize(population[i].getFitness(), mostFit);
        }

        return fitness;
    }

    private DNA[] acceptReject()
    {
        DNA parents[] = new DNA[2];
        double fitness[] = getNormalizedFitness();

        double ran = 0.0;
        int ran2;
        while(ran == 0.0)
        {
            ran = (double) ThreadLocalRandom.current().nextInt(0, 1000000 + 1);
        }
        ran /= 1000000.0;

        // accept reject algorithm
        int dnai = 0;
        while(dnai < 2)
        {
            ran2 = ThreadLocalRandom.current().nextInt(0, population.length);
            if (ran < fitness[ran2])
            {
                parents[dnai] = population[ran2];
                ++dnai;
                if(dnai >= 2)
                    break;
            }
            ran -= ran/2;
        }

        return parents;
    }

    public void printPopulation()
    {
        for(int i = 0; i < population.length; i++)
        {
            population[i].print();
        }
    }

    public void printFittestID()
    {
        System.out.println(fittest);
    }

    public static byte[] toByteArray(double value)
    {
        byte[] bytes = new byte[8];
        ByteBuffer.wrap(bytes).putDouble(value);
        return bytes;
    }

    public static double toDouble(byte[] bytes)
    {
        return ByteBuffer.wrap(bytes).getDouble();
    }

    byte[] getByteArrayFromDoubleArray(double d[])
    {
        byte[] bytes = new byte[d.length * 8];
        for(int i = 0; i < d.length; i++)
        {
            byte tmp[] = toByteArray(d[i]);
            for(int j = i*8, k = 0; j < i*8 + 8; j++, k++)
            {
                bytes[j] = tmp[k];
            }
        }
        return bytes;
    }

    double[] getDoubleArrayFromByteArray(byte d[])
    {
        double[] doubles = new double[d.length / 8];
        for(int i = 0; i < doubles.length; i++)
        {
            byte tmp[] = new byte[8];
            for(int j = i*8, k = 0; j < i*8 + 8; j++, k++)
            {
                tmp[k] = d[j];
            }
            doubles[i] = toDouble(tmp);
        }
        return doubles;
    }

    public DNA crossOver(DNA parents[])
    {
        DNA offspring = new DNA(parents[0].getGenes().getNeurons(), parents[0].getGenes().getSegments(), DNA.mutationRate);
        double g1[] = parents[0].getGenes().getEyeNet().getWeights();
        double g2[] = parents[1].getGenes().getEyeNet().getWeights();

        byte p1[] = getByteArrayFromDoubleArray(g1);
        byte p2[] = getByteArrayFromDoubleArray(g2);
        byte p3[] = new byte[p1.length];

        for(int i = 0; i < p3.length; i++)
        {
            double ran = (double) ThreadLocalRandom.current().nextInt(0, p1.length);
            ran /= p1.length;

            if (ran < 0.5)
                p3[i] = p1[i];
            else
                p3[i] = p2[i];
        }

        offspring.getGenes().getEyeNet().setWeights(getDoubleArrayFromByteArray(p3));

        return offspring;
    }

    public void reproduce(DNA parents[], DNA newPopulation[], int i, boolean mutate)
    {
        newPopulation[i] = crossOver(parents);
        if (mutate)
            newPopulation[i].mutate();
    }
}
