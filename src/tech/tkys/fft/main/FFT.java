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

    /**
     * Prepares complex sine wave
     * @param sampleNumber
     * @return
     */
    private static Double[] makeSinArray(int sampleNumber) {
        Double[] sinArray = new Double[sampleNumber + sampleNumber / 4];

        Double t = Math.sin(Math.PI / sampleNumber);
        Double dc = 2 * t * t;                  // cos?
        Double ds = Math.sqrt(dc * (2 - dc));   // sin?
        t = 2 * dc;

        Double c = sinArray[sampleNumber / 4] = 1.0;    // cos?
        Double s = sinArray[0] = 0.0;                   // sin?

        for (int i = 1; i < sampleNumber / 8; i++) {
            c -= dc;
            dc += t * c;
            s += ds;
            ds -= t * s;
            sinArray[i] = s;
            sinArray[sampleNumber / 4 - i] = c;
        }

        //
        if (sampleNumber / 8 != 0) {
            sinArray[sampleNumber / 8] = Math.sqrt(0.5);
        }

        // 周期性
        for (int i = 0; i < sampleNumber / 4; i++) {
            sinArray[sampleNumber / 2 - i] = sinArray[i];
        }

        // 周期性
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
                Double temp = real[i];
                real[i] = real[j];
                real[j] = temp;

                temp = imaginary[i];
                imaginary[i] = imaginary[j];
                imaginary[j] = temp;
            }
        }
        
        for (int k = 1; k < sampleNumber; k *= 2) {     // k : 2のべき乗(1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, ...)
            int sinIndex = 0;
            int d = sampleNumber / (k * 2);

            for (int j = 0; j < k; j++) {
                Double cosValue = sinArray[sinIndex + sampleNumber / 4];
                Double sinValue = sign * sinArray[sinIndex];

                for (int i = j; i < sampleNumber; i += k * 2) {
                    int ik = i + k;
                    Double dReal = sinValue * imaginary[ik] + cosValue * real[ik];
                    Double dImaginary = cosValue * imaginary[ik] - sinValue * real[ik];

                    real[ik] = real[i] - dReal;
                    real[i] += dReal;

                    imaginary[ik] = imaginary[i] - dImaginary;
                    imaginary[i] += dImaginary;
                }

                sinIndex += d;
            }
        }
    }
}
