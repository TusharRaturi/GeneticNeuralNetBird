package AI.FFNN;

/**
 * Created by Tushar on 30-01-2017.
 */

class Connection
{
    double weight;
    double deltaWeight;

    Connection()
    {
        weight = Math.random();
    }
}

public class Neuron
{
    private int index;

    double output;
    private int numOutputs;
    private Connection outputWeights[];
    private double gradient;
    private static double eta = 0.15;
    private static double alpha = 0.5;

    Neuron(int index, int numOutputs)
    {
        this.index = index;
        this.numOutputs = numOutputs;
        outputWeights = new Connection[numOutputs];

        for (int c = 0; c < numOutputs; c++)
        {
            outputWeights[c] = new Connection();

        }
    }

    public double getOutputWeight(int index) {
        return outputWeights[index].weight;
    }

    public double getOutputDeltaWeight(int index) {
        return outputWeights[index].deltaWeight;
    }

    public void setOutputWeight(int index, double weight) {
        outputWeights[index].weight = weight;
    }

    public void setOutputDeltaWeight(int index, double deltaWeight) {
        outputWeights[index].deltaWeight = deltaWeight;
    }

    public double getOutput() {
        return output;
    }

    public void setOutput(double output) {
        this.output = output;
    }

    public void feedForward(Layer prevLayer) {
        double sum = 0.0;

        for (int i = 0; i < prevLayer.getNumNeurons(); i++) {
            sum += prevLayer.getNeuron(i).getOutput() * prevLayer.getNeuron(i).getOutputWeight(index);
        }

        output = transferFunction(sum);
    }

    private static double transferFunction(double x)
    {
        return Math.tanh(x);
    }

    private static double transferFunctionDerivative(double x)
    {
        return 1.0 - x * x;
    }

    public void calcOutputGradients(double targetVal)
    {
        double delta = targetVal - output;
        gradient = delta * transferFunctionDerivative(output);
    }

    public void calcHiddenGradients(Layer nextLayer)
    {
        double dow = sumDOW(nextLayer);

        gradient = dow * transferFunctionDerivative(output);
    }

    private double sumDOW(Layer nextLayer)
    {
        double sum = 0.0;

        for (int n = 0; n < nextLayer.getNumNeurons() - 1; n++)
        {
            sum += outputWeights[n].weight * nextLayer.getNeuron(n).getGradient();
        }

        return sum;
    }

    public void updateInputWeights(Layer prevLayer)
    {
        for(int n = 0; n < prevLayer.getNumNeurons(); n++)
        {
            Neuron neuron = prevLayer.getNeuron(n);

            double oldDeltaWeight = neuron.getOutputDeltaWeight(index);
            double newDeltaWeight = eta * neuron.getOutput() * gradient + alpha * oldDeltaWeight;

           neuron.setOutputDeltaWeight(index, newDeltaWeight);
           neuron.setOutputWeight(index, neuron.getOutputWeight(index) + newDeltaWeight);
        }
    }

    public double getGradient()
    {
        return gradient;
    }
}
