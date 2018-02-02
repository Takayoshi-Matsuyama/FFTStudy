package tech.tkys.fft.test;

import tech.tkys.fft.calculation.FFTCalculator;

import java.util.ArrayList;

/**
 * FFTアルゴリズムのテストルーチン
 */
public class FFTTestService {
    private static int N = 64;

    public ArrayList<Double> generateTimeSeriesData(
            double samplingFrequency,
            int samplingNumber,
            String function) {
        double dt = 1.0 / samplingFrequency;
        ArrayList<Double> tsData = new ArrayList<Double>();

        if (function.equals("0.1*sin(2*PI*100t)+0.2*sin(2*PI*200t)+0.3*sin(2*PI*300t)")) {
            for (int i = 0; i < samplingNumber; i++) {
                double t = dt * i;
                tsData.add(0.1*Math.sin(2.0*Math.PI*100.0*t)+0.2*Math.sin(2.0*Math.PI*200.0*t)+0.3*Math.sin(2.0*Math.PI*300.0*t));
            }
        } else if (function.equals("sin(t/3)")) {
            for (int t = 0; t < N; t++) {
                tsData.add(Math.sin(t/3.0));
            }
        } else if (function.equals("sin(t/2)")) {
            for (int t = 0; t < N; t++) {
                tsData.add(Math.sin(t/2.0));
            }
        } else if (function.equals("sin(t)")) {
            for (int t = 0; t < N; t++) {
                tsData.add(Math.sin((double)t));
            }
        } else if (function.equals("sin(2t)")) {
            for (int t = 0; t < N; t++) {
                tsData.add(Math.sin(2.0 * t));
            }
        } else if (function.equals("sin(3t)")) {
            for (int t = 0; t < N; t++) {
                tsData.add(Math.sin(3.0 * t));
            }
        } else if (function.equals("6 * cos(6 * PI * t) +  4 * sin(18 * PI * t)")) {
            for (int t = 0; t < N; t++) {
                tsData.add(6 * Math.cos(6 * Math.PI * t / N) +  4 * Math.sin(18 * Math.PI * t / N));
            }
        }

        return tsData;
    }

    public ArrayList<Double> executeFFT(ArrayList<Double> timeSeriesData) {
        ArrayList<Double> result = new ArrayList<>();

        ArrayList<Double> realPart = new ArrayList<>();
        ArrayList<Double> imaginaryPart = new ArrayList<>();

        for (double timeSeriesElement : timeSeriesData) {
            realPart.add(timeSeriesElement);
            imaginaryPart.add(0.0);
        }

        FFTCalculator fftCalculator = new FFTCalculator();
        fftCalculator.fft(timeSeriesData.size(), realPart, imaginaryPart);

        for (int i = 0; i < timeSeriesData.size(); i++) {
            result.add(Math.sqrt(Math.pow(realPart.get(i), 2) + Math.pow(imaginaryPart.get(i), 2)));
        }

        return result;
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
}
