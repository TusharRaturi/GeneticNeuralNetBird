package AI.FFNN;

/**
 * Created by Tushar on 02-02-2017.
 */
public class NeuralNetTopology
{
    private int numLayers;
    private int numNeuronsInLayer[];

    public NeuralNetTopology(int numLayers)
    {
        this.numLayers = numLayers;
        numNeuronsInLayer = new int[numLayers];
    }

    public void setNumNeuronsInLayer(int layer, int numNeuronsInLayer)
    {
        this.numNeuronsInLayer[layer] = numNeuronsInLayer;
    }

    public void setNumNeuronsInLayer(int numNeuronsInLayer[])
    {
        this.numNeuronsInLayer = numNeuronsInLayer;
    }

    public int getNumLayers()
    {
        return numLayers;
    }

    public int getNumNeuronsInLayer(int layer)
    {
        if(layer >= (this.numNeuronsInLayer).length)
            return 0;
        else
            return numNeuronsInLayer[layer];
    }
}