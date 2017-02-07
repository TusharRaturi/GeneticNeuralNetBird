package AI.FFNN;

/**
 * Created by Tushar on 30-01-2017.
 */
public class Layer
{
    private int index;
    private Neuron neurons[];

    public int getNumNeuronsInNextLayer() {
        return numNeuronsInNextLayer;
    }

    private int numNeuronsInNextLayer;

    Layer(int index, int numNeurons, int numNeuronsInNextLayer)
    {
        this.index = index;
        neurons = new Neuron[numNeurons + 1];
        this.numNeuronsInNextLayer = numNeuronsInNextLayer;

        for(int i = 0; i <= numNeurons; i++)
        {
            neurons[i] = new Neuron(i, numNeuronsInNextLayer);
        }
    }

    public int getNumNeurons()
    {
        return neurons.length;
    }

    public Neuron getNeuron(int neuronIndex)
    {
        return neurons[neuronIndex];
    }
}
