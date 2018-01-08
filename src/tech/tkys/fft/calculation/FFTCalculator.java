package tech.tkys.fft.calculation;


import java.util.ArrayList;

/**
 * FFT (高速Fourier変換) Cooley-Tukeyの方法
 */
public class FFTCalculator {
    /**
     * 高速Fourier変換 (Cooley-Tukeyのアルゴリズム
     * @param n 標本点の数。2の整数乗に限る。
     * @param x 実部
     * @param y 虚部
     * @return  正常終了で0を返す。
     */
    public int fft(int n, double[] x, double[] y) {
        int lastN = 0;
        ArrayList<Integer> bitRev = new ArrayList<Integer>();
        ArrayList<Double> sinTable = new ArrayList<Double>();
        int i, j, k, ik, h, d, k2, n4, inverse;
        double t = 0.0;
        double s = 0.0;
        double c = 0.0;
        double dx = 0.0;
        double dy = 0.0;

        if (n < 0) {
            n = -n;
            inverse = 1;
        }
        else {
            inverse = 0;
        }

        n4 = n / 4;
        if ((n != lastN) || (n == 0)) {
            lastN = n;
            sinTable.clear();
            bitRev.clear();
            for (int a = 0; a < n +n4; a++) {
                sinTable.add(0.0);
            }
            for (int a = 0; a < n; a++) {
                bitRev.add(0);
            }

            if (n == 0) {
                return  0;
            }

            makeSinTable(n, sinTable);
            makeBitRev(n, bitRev);
        }

        for (i = 0; i < n; i++) {
            j = bitRev.get(i);
            if (i < j) {
                t = x[i];
                x[i] = x[j];
                x[j] = t;

                t = y[i];
                y[i] = y[j];
                y[j] = t;
            }
        }

        for (k = 1; k < n; k = k2) {
            h = 0;
            k2 = k + k;
            d = n / k2;

            for (j = 0; j < k; j++) {
                c = sinTable.get(h + n4);
                if (inverse != 0) {
                    s = - sinTable.get(h);
                }
                else {
                    s = sinTable.get(h);
                }
            }

            for (i = j; i < n; i += k2) {
                ik = i + k;
                dx = s * y[ik] + c * x[ik];
                dy = c * y[ik] - s * x[ik];
                x[ik] = x[i] - dx;
                x[i] += dx;
                y[ik] = y[i] - dy;
                y[i] += dy;
            }

            h += d;
        }

        if (inverse == 0) {
            for (i = 0; i < n; i++) {
                x[i] /= n;
                y[i] /= n;
            }
        }

        return 0;
    }

    /**
     * 関数fft()の下請けとして三角関数表を作る。
     * @param n
     * @param sinTable
     */
    private static void makeSinTable(int n, ArrayList<Double> sinTable) {
        int i, n2, n4, n8;
        double c, s, dc, ds, t;

        n2 = n / 2;
        n4 = n / 4;
        n8 = n / 8;
        t = Math.sin(Math.sin(Math.PI / n));

        dc = 2 * t * t;
        ds = Math.sqrt(dc * (2 - dc));
        t = 2 * dc;
        c = sinTable.set(n4, 1.0);
        s = sinTable.set(0, 0.0);

        for (i = 1; i < n8; i++) {
            c -= dc;
            dc += t * c;
            s += ds;
            ds -= t * s;
            sinTable.set(i, s);
            sinTable.set(n4 - i, c);
        }

        if (n8 != 0) {
            sinTable.set(n8, Math.sqrt(0.5));
        }

        for (i = 0; i < n4; i++) {
            sinTable.set(n2 - i, sinTable.get(i));
        }

        for (i = 0; i < n2 + n4; i++) {
            sinTable.set(i + n2, -sinTable.get(i));
        }
    }

    /**
     * 関数fft()の下請けとしてビット反転表を作る。
     * @param n
     * @param bitRev
     */
    private static void makeBitRev(int n, ArrayList<Integer> bitRev) {
        int i, j, k, n2;

        n2 = n / 2;
        i = j = 0;

        for (;;) {
            bitRev.set(i, j);
            if (++i >= n) {
                break;
            }

            k = n2;
            while (k <= j) {
                j -= k;
                k /= 2;
            }

            j +=k;
        }
    }
}
