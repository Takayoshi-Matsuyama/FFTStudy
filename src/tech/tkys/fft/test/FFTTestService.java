package tech.tkys.fft.test;

import tech.tkys.fft.calculation.FFTCalculator;
import tech.tkys.fft.main.FFTLogic;
import tech.tkys.fft.main.FFTDataSet;
import tech.tkys.fft.main.TimeSeriesDataSet;

import java.util.ArrayList;

/**
 * FFTアルゴリズムのテストルーチン
 */
public class FFTTestService {
    private static int N = 64;

    public TimeSeriesDataSet generateTimeSeries(
            double samplingFrequency,
            int samplingNumber,
            String function) {
        ArrayList<Double> timestamps = new ArrayList<>();
        ArrayList<Double> timeSeries = new ArrayList<>();

        double dt = 1.0 / samplingFrequency;

        if (function.equals("0.1*sin(2*PI*100t)+0.2*sin(2*PI*200t)+0.3*sin(2*PI*300t)")) {
            for (int i = 0; i < samplingNumber; i++) {
                double t = dt * i;
                timestamps.add(t);
                timeSeries.add(0.1*Math.sin(2.0*Math.PI*100.0*t)+0.2*Math.sin(2.0*Math.PI*200.0*t)+0.3*Math.sin(2.0*Math.PI*300.0*t));
            }
        }

        return new TimeSeriesDataSet(timestamps, timeSeries);
    }

    public FFTDataSet executeFFT(
            double samplingFrequency,
            int samplingNumber,
            ArrayList<Double> timeSeries) {
        ArrayList<Double> frequencies = new ArrayList<>();
        ArrayList<Double> fourierCoefficients = new ArrayList<>();

        ArrayList<Double> realPart = new ArrayList<>();
        ArrayList<Double> imaginaryPart = new ArrayList<>();

        for (double timeSeriesElement : timeSeries) {
            realPart.add(timeSeriesElement);
            imaginaryPart.add(0.0);
        }

        FFTCalculator fftCalculator = new FFTCalculator();
        fftCalculator.fft(timeSeries.size(), realPart, imaginaryPart);

        Double df = samplingFrequency / samplingNumber;

        for (int i = 0; i < timeSeries.size(); i++) {
            frequencies.add(df * i);
            fourierCoefficients.add(Math.sqrt(Math.pow(realPart.get(i), 2) + Math.pow(imaginaryPart.get(i), 2)));
        }

        return new FFTDataSet(frequencies, fourierCoefficients);
    }

    public FFTDataSet executeFFT2(
            double samplingFrequency,
            int samplingNumber,
            ArrayList<Double> timeSeries) {
        ArrayList<Double> frequencies = new ArrayList<>();
        ArrayList<Double> fourierCoefficients = new ArrayList<>();

        int n = timeSeries.size();

        Double[] x = new Double[n];
        Double[] y = new Double[n];
        for (int i = 0; i < n; i++) {
            x[i] = timeSeries.get(i);
            y[i] = 0.0;
        }

        FFTLogic fftN = new FFTLogic(n);
        fftN.executeFFT(x, y);

        Double df = samplingFrequency / samplingNumber;

        for (int i = 0; i < timeSeries.size(); i++) {
            frequencies.add(df * i);
            fourierCoefficients.add(Math.sqrt(Math.pow(x[i], 2) + Math.pow(y[i], 2)));
        }

        return new FFTDataSet(frequencies, fourierCoefficients);
    }

    public static void executeFFTTest() {
        int i;
        ArrayList<Double> x1 = new ArrayList<Double>();
        ArrayList<Double> y1 = new ArrayList<Double>();
        ArrayList<Double> x2 = new ArrayList<Double>();
        ArrayList<Double> y2 = new ArrayList<Double>();
        ArrayList<Double> x3 = new ArrayList<Double>();
        ArrayList<Double> y3 = new ArrayList<Double>();


        for (i = 0; i < N; i++) {
            double timeSeriesElement = 6 * Math.cos(6 * Math.PI * i / N) + 4 * Math.sin(18 * Math.PI * i / N);
            x1.add(timeSeriesElement);
            x2.add(timeSeriesElement);
            y1.add(0.0);
            y2.add(0.0);
        }

        FFTCalculator fftCalculator = new FFTCalculator();

        int result = fftCalculator.fft(N, x2, y2);
        if (result != 0) {
            System.out.println("Error");
            return;
        }

        for (i = 0; i < N; i++) {
            x3.add(x2.get(i));
            y3.add(y2.get(i));
        }

        result = fftCalculator.fft(-N, x3, y3);
        if (result != 0) {
            System.out.println("Error");
        }

        System.out.println("元のデータ    フーリエ変換    逆変換");

        for (i = 0; i < N; i++) {
            System.out.format("%4d | %6.3f %6.3f | %6.3f %6.3f | %6.3f %6.3f%n",
                    i, x1.get(i), y1.get(i), x2.get(i), y2.get(i), x3.get(i), y3.get(i));
        }
    }

    public static void executeFFTTest2() {
        final int N = 64;
        Double x1[] = new Double[N];
        Double y1[] = new Double[N];
        Double x2[] = new Double[N];
        Double y2[] = new Double[N];
        Double x3[] = new Double[N];
        Double y3[] = new Double[N];

        for (int i = 0; i < N; i++) {
            x1[i] = x2[i] = 6 * Math.cos(6 * Math.PI * i / N)
                          + 4 * Math.sin(18 * Math.PI * i / N);
            y1[i] = y2[i] = 0.0;
        }

        FFTLogic fftN = new FFTLogic(N);
        fftN.executeFFT(x2, y2);
        for (int i = 0; i < N; i++) {
            x3[i] = x2[i];
            y3[i] = y2[i];
        }

        fftN.executeInverseFFT(x3, y3);

        System.out.println("元データ\tフーリエ変換\t逆変換");
        for (int i = 0; i < N; i++) {
            System.out.println(i + " (" +
                    x1[i] + "," +
                    y1[i] + ") (" +
                    x2[i] + ", " +
                    y2[i] + ") (" +
                    x3[i] + ", " +
                    y3[i] + ")");
        }
    }
}
