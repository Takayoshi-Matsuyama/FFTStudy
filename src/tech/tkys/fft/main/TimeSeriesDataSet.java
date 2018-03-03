package tech.tkys.fft.main;

import java.util.ArrayList;

/**
 * Data set in time series domain.
 */
public class TimeSeriesDataSet {
    private ArrayList<Double> timeStamps;
    private ArrayList<Double> timeSeries;

    public TimeSeriesDataSet(ArrayList<Double> timeStamps, ArrayList<Double> timeSeries) {
        if (timeStamps == null) {
            throw new IllegalArgumentException("'timeStamps' is null.");
        }

        if (timeSeries == null) {
            throw new IllegalArgumentException("'timeSeries is null.");
        }

        this.timeStamps = timeStamps;
        this.timeSeries = timeSeries;
    }

    public int getSize() {
        return this.timeStamps.size();
    }

    public ArrayList<Double> getTimeStamps() {
        return this.timeStamps;
    }

    public ArrayList<Double> getTimeSeries() {
        return this.timeSeries;
    }

    public void clear() {
        this.timeStamps.clear();
        this.timeSeries.clear();
    }
}
