package tech.tkys.fft.main;

public class FFT {
    int sampleNumber;
    int[] bitRevArray;
    Double[] sinArray;

    public FFT(int sampleNumber) {
        this.sampleNumber = sampleNumber;
        this.bitRevArray = makeBitReversedArray(sampleNumber);
        this.sinArray = makeSinArray(sampleNumber);
    }

    public void fft(Double[] real, Double[] imaginary) {
        this.fftsub(this.sampleNumber, this.bitRevArray, this.sinArray, real, imaginary, 1);
    }

    public void ifft(Double[] real, Double[] imaginary) {
        fftsub(this.sampleNumber, this.bitRevArray, this.sinArray, real, imaginary, -1);
        for (int i = 0; i < this.sampleNumber; i++) {
            real[i] /= this.sampleNumber;
            imaginary[i] /= this.sampleNumber;
        }
    }

    /**
     * Executes Bit-reversal permutation
     * @param sampleNumber number of sample
     * @return             Bit-reversed array
     */
    private static int[] makeBitReversedArray(int sampleNumber) {
        int[] bitRevArray = new int[sampleNumber];
        bitRevArray[0] = 0;

        int index = 0;
        int p = 0;      // permutated index

        while (++index < sampleNumber) {
            int half = sampleNumber / 2;
            while (half <= p) {
                p -= half;
                half /= 2;
            }

            p += half;
            bitRevArray[index] = p;
        }

        return bitRevArray;
    }

    private static Double[] makeSinArray(int sampleNumber) {
        Double[] sinArray = new Double[sampleNumber + sampleNumber / 4];

        Double t = Math.sin(Math.PI / sampleNumber);    // 回転子
        Double dc = 2 * t * t;
        Double ds = Math.sqrt(dc * (2 - dc));
        t = 2 * dc;
        Double c = sinArray[sampleNumber / 4] = 1.0;
        Double s = sinArray[0] = 0.0;
        for (int i = 1; i < sampleNumber / 8; i++) {
            c -=dc;
            dc += t * c;
            s += ds;
            ds -= t * s;
            sinArray[i] = s;
            sinArray[sampleNumber / 4 - i] = c;
        }

        if (sampleNumber / 8 != 0) {
            sinArray[sampleNumber / 8] = Math.sqrt(0.5);
        }

        for (int i = 0; i < sampleNumber / 4; i++) {
            sinArray[sampleNumber / 2 - i] = sinArray[i];
        }

        for (int i = 0; i < sampleNumber /2 + sampleNumber / 4; i++) {
            sinArray[i + sampleNumber / 2] = - sinArray[i];
        }

        return sinArray;
    }

    private static void fftsub(
            int sampleNumber,
            int[] bitRevArray,
            Double[] sinArray,
            Double[] real,
            Double[] imaginary,
            int sign) {
        // ビット反転順に並べ替え
        for (int i = 0; i < sampleNumber; i++) {
            int j = bitRevArray[i];
            if (i < j) {
                Double t = real[i];
                real[i] = real[j];
                real[j] = t;

                t = imaginary[i];
                imaginary[i] = imaginary[j];
                imaginary[j] = t;
            }
        }

        for (int k = 1; k < sampleNumber; k *= 2) {
            int h = 0;
            int d = sampleNumber / (k * 2);
            for (int j = 0; j < k; j++) {
                Double c = sinArray[h + sampleNumber / 4];
                Double s = sign * sinArray[h];
                for (int i = j; i < sampleNumber; i += k *2) {
                    int ik = i + k;
                    Double dx = s * imaginary[ik] + c * real[ik];
                    Double dy = c * imaginary[ik] - s * real[ik];
                    real[ik] = real[i] - dx;
                    real[i] += dx;
                    imaginary[ik] = imaginary[i] - dy;
                    imaginary[i] += dy;
                }

                h += d;
            }
        }
    }
}
