package AI.GA;

import AI.FFNN.NeuralNet;
import AI.FFNN.NeuralNetTopology;

/**
 * Created by Tushar on 03-02-2017.
 */
public class Eyes
{
    private double viewAngle;
    private double viewDistance;
    private int neurons;
    private NeuralNet eyeNet;
    private NeuralNetTopology nnt;

    public int getSegments() {
        return segments;
    }

    private int segments;

    public Eyes(double viewAngle, double viewDistance, int neurons, int segments)
    {
        this.viewAngle = viewAngle;
        this.viewDistance = viewDistance;
        this.neurons = neurons;

        nnt = new NeuralNetTopology(4);
        nnt.setNumNeuronsInLayer(0, neurons * segments);
        nnt.setNumNeuronsInLayer(1, (neurons * segments) / 2);
        nnt.setNumNeuronsInLayer(2, (neurons * segments) / 4);
        nnt.setNumNeuronsInLayer(3, 1);

        eyeNet = new NeuralNet(nnt);
        this.segments = segments;
    }

    public int getNeurons()
    {
        return neurons;
    }

    public void setNeurons(int neurons)
    {
        this.neurons = neurons;
    }

    public NeuralNetTopology getNnt()
    {
        return nnt;
    }

    public void setNnt(NeuralNetTopology nnt)
    {
        this.nnt = nnt;
    }

    public NeuralNet getEyeNet()
    {
        return eyeNet;
    }

    public void setEyeNet(NeuralNet eyeNet)
    {
        this.eyeNet = eyeNet;
    }

    public double getViewAngle()
    {
        return viewAngle;
    }

    public double getViewDistance()
    {
        return viewDistance;
    }
}
