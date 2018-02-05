package tech.tkys.fft.main;

public class FFT {
    int n;
    int[] bitRev;
    Double[] sintbl;

    public FFT(int n) {
        this.n = n;
        sintbl = new Double[n + n / 4];
        bitRev = new int[n];

        // 三角関数表を作る
        Double t = Math.sin(Math.PI / n);
        Double dc = 2 * t * t;
        Double ds = Math.sqrt(dc * (2 - dc));
        t = 2 * dc;
        Double c = sintbl[n / 4] = 1.0;
        Double s = sintbl[0] = 0.0;
        for (int i = 1; i < n /8; i++) {
            c -=dc;
            dc += t * c;
            s += ds;
            ds -= t * s;
            sintbl[i] = s;
            sintbl[n / 4 - i] = c;
        }

        if (n / 8 != 0) {
            sintbl[n / 8] = Math.sqrt(0.5);
        }

        for (int i = 0; i < n / 4; i++) {
            sintbl[n / 2 - i] = sintbl[i];
        }

        for (int i = 0; i < n /2 + n / 4; i++) {
            sintbl[i + n / 2] = - sintbl[i];
        }

        // ビット反転表を作る
        this.bitRev = makeBitReversedArray(n);
    }

    /**
     * Executes Bit-reversal permutation
     * @param n
     * @return The Bit-reversed array
     */
    private static int[] makeBitReversedArray(int n) {
        int[] _bitRev = new int[n];
        _bitRev[0] = 0;

        int index = 0;
        int p = 0;      // permutated index

        while (++index < n) {
            int half = n / 2;
            while (half <= p) {
                p -= half;
                half /= 2;
            }

            p += half;
            _bitRev[index] = p;
        }

        return _bitRev;
    }

    public void fft(Double[] x, Double[] y) {
        this.fftsub(x, y, 1);
    }

    public void ifft(Double[] x, Double[] y) {
        fftsub(x, y, -1);
        for (int i = 0; i < n; i++) {
            x[i] /= n;
            y[i] /= n;
        }
    }

    private void fftsub(Double[] x, Double[] y, int sign) {
        for (int i = 0; i < n; i++) {
            int j = bitRev[i];
            if (i < j) {
                Double t = x[i];
                x[i] = x[j];
                x[j] = t;

                t = y[i];
                y[i] = y[j];
                y[j] = t;
            }
        }

        for (int k = 1; k < n; k *= 2) {
            int h = 0;
            int d = n / (k * 2);
            for (int j = 0; j < k; j++) {
                Double c = sintbl[h + n / 4];
                Double s = sign * sintbl[h];
                for (int i = j; i < n; i += k *2) {
                    int ik = i + k;
                    Double dx = s * y[ik] + c * x[ik];
                    Double dy = c * y[ik] - s * x[ik];
                    x[ik] = x[i] - dx;
                    x[i] += dx;
                    y[ik] = y[i] - dy;
                    y[i] += dy;
                }

                h += d;
            }
        }
    }
}
