package AI.FFNN;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Tushar on 30-01-2017.
 */

public class NeuralNet
{
    private Layer[] network;
    private double error;
    double recentAverageError;
    static double recentAverageSmoothingFactor = 100.0;
    int wts;

    public NeuralNet(NeuralNetTopology nnt)
    {
        int layers = nnt.getNumLayers();
        network = new Layer[layers];

        for(int i = 0; i < layers; i++)
        {
            network[i] = new Layer(i, nnt.getNumNeuronsInLayer(i), nnt.getNumNeuronsInLayer(i + 1));
            network[i].getNeuron(network[i].getNumNeurons() - 1).setOutput(1.0);
        }

        int wts = 0;
        for(int i = 0; i < network.length - 1; i++)
        {
            wts += network[i].getNumNeurons() * (((i+1) == network.length - 1) ? (network[i+1].getNumNeurons() - 1) : (network[i+1].getNumNeurons()));
        }
        this.wts = wts;
    }

    public void setRandomWeights()
    {
        int x = 0;
        for(int i = 0; i < network.length; i++)
        {
            for(int j = 0; j < ((i == network.length - 1) ? (network[i].getNumNeurons() - 1) : (network[i].getNumNeurons())); j++)
            {
                for(int k = 0; k < network[i].getNumNeuronsInNextLayer(); k++)
                    network[i].getNeuron(j).setOutputWeight(k, getRandomNodeWeight());
            }
        }
    }

    public double getRandomNodeWeight()
    {
        double tmp = (double)ThreadLocalRandom.current().nextInt(-100000000, 100000000 + 1);
        return tmp / 20000000;
    }

    public double[] getWeights()
    {
        int x = 0;
        double netWeights[] = new double[wts];
        for(int i = 0; i < network.length; i++)
        {
            for(int j = 0; j < ((i == network.length - 1) ? (network[i].getNumNeurons() - 1) : (network[i].getNumNeurons())); j++)
            {
                for(int k = 0; k < network[i].getNumNeuronsInNextLayer(); k++)
                    netWeights[x++] = network[i].getNeuron(j).getOutputWeight(k);
            }
        }
        return netWeights;
    }

    public void setWeights(double weights[])
    {
        if(weights == null)
            return;

        int x = 0;

        for(int i = 0; i < network.length; i++)
        {
            for(int j = 0; j < ((i == network.length - 1) ? (network[i].getNumNeurons() - 1) : (network[i].getNumNeurons())); j++)
            {
                for(int k = 0; k < network[i].getNumNeuronsInNextLayer(); k++)
                    network[i].getNeuron(j).setOutputWeight(k, weights[x++]);
            }
        }
    }

    public void feedForward(double inputVals[])
    {
        try
        {
            if (!(inputVals.length == network[0].getNumNeurons() - 1))
                throw new Exception("Input values not equal to input nodes");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        for(int i = 0; i < inputVals.length; i++)
        {
            network[0].getNeuron(i).setOutput(inputVals[i]);
        }

        for(int i = 1; i < network.length; i++)
        {
            Layer prevLayer = network[i - 1];
            for (int j = 0; j < network[i].getNumNeurons() - 1; j++)
            {
                network[i].getNeuron(j).feedForward(prevLayer);
            }
        }
    }

    public void backPropogate(double targetVals[])
    {
        Layer outputLayer = network[network.length - 1];
        error = 0.0;

        for(int n = 0; n < outputLayer.getNumNeurons() - 1; n++)
        {
            double delta = targetVals[n] - outputLayer.getNeuron(n).getOutput();
            error += delta * delta;
        }

        error /= outputLayer.getNumNeurons() - 1;
        error = Math.sqrt(error);

        recentAverageError =
                (recentAverageError * recentAverageSmoothingFactor + error)
                        / (recentAverageSmoothingFactor + 1.0);

        for(int n = 0; n < outputLayer.getNumNeurons() - 1; n++)
        {
            outputLayer.getNeuron(n).calcOutputGradients(targetVals[n]);
        }

        for(int l = network.length - 2; l > 0; l--)
        {
            Layer hiddenLayer = network[l];
            Layer nextLayer = network[l + 1];

            for(int n = 0; n < hiddenLayer.getNumNeurons(); n++)
                hiddenLayer.getNeuron(n).calcHiddenGradients(nextLayer);
        }

        for(int l = network.length - 1; l > 0; l--)
        {
            Layer layer = network[l];
            Layer prevLayer = network[l - 1];

            for(int n = 0; n < layer.getNumNeurons() - 1; n++)
            {
                layer.getNeuron(n).updateInputWeights(prevLayer);
            }
        }
    }

    public double[] getResults()
    {
        Layer r = network[network.length - 1];
        double res[] = new double[r.getNumNeurons()];

        for(int n = 0; n < r.getNumNeurons() - 1; n++)
        {
            res[n] = r.getNeuron(n).getOutput();
        }

        return res;
    }

    public double getRecentAverageError()
    {
        return recentAverageError;
    }
}
