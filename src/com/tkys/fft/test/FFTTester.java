package com.tkys.fft.test;

import com.tkys.fft.calculation.FFTCalculator;

/**
 * FFTアルゴリズムのテストルーチン
 */
public class FFTTester {
    private static int N = 64;

    public static void executeFFTTest() {
        int i;
        double x1[] = new double[N];
        double y1[] = new double[N];
        double x2[] = new double[N];
        double y2[] = new double[N];
        double x3[] = new double[N];
        double y3[] = new double[N];

        for (i = 0; i < N; i++) {
            x1[i] = x2[i] = 6 * Math.cos(6 * Math.PI * i / N) +
                            4 * Math.sin(18 * Math.PI * i / N);
            y1[i] = y2[i] = 0;
        }

        FFTCalculator fftCalculator = new FFTCalculator();

        int result = fftCalculator.fft(N, x2, y2);
        if (result != 0) {
            System.out.println("Error");
            return;
        }

        for (i = 0; i < N; i++) {
            x3[i] = x2[i];
            y3[i] = y2[i];
        }

        result = fftCalculator.fft(-N, x3, y3);
        if (result != 0) {
            System.out.println("Error");
        }

        System.out.println("元のデータ    フーリエ変換    逆変換");

        for (i = 0; i < N; i++) {
            System.out.format("%4d | %6.3f %6.3f | %6.3f %6.3f | %6.3f %6.3f%n",
                    i, x1[i], y1[i], x2[i], y2[i], x3[i], y3[i]);
        }
    }
}
