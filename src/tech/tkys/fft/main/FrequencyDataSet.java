package tech.tkys.fft.main;

import java.util.ArrayList;

/**
 * Data set in frequency domain.
 */
public class FrequencyDataSet {
    private ArrayList<Double> frequencies;
    private ArrayList<Double> fourierCoefficients;

    public FrequencyDataSet(ArrayList<Double> frequencies, ArrayList<Double> fourierCoefficients) {
        if (frequencies == null) {
            throw new IllegalArgumentException("'frequencies' is null.");
        }

        if (fourierCoefficients == null) {
            throw new IllegalArgumentException("'fourierCoefficients is null.");
        }

        this.frequencies = frequencies;
        this.fourierCoefficients = fourierCoefficients;
    }

    public int getSize() {
        return this.frequencies.size();
    }

    public ArrayList<Double> getFrequencies() {
        return this.frequencies;
    }

    public ArrayList<Double> getFourierCoefficients() {
        return this.fourierCoefficients;
    }

    public void clear() {
        this.frequencies.clear();
        this.fourierCoefficients.clear();
    }
}
