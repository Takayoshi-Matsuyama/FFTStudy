package tech.tkys.fft.main;

/**
 * Logic class for FFT.
 */
public class FFTLogic {
    /**
     * Number of sample data for FFT. This is equal to number of complex sine wave in FFT.
     */
    int sampleNumber;

    /**
     * Result of Bit-reversal permutation.
     */
    int[] bitRevArray;

    /**
     * Values of complex sine wave for FFTLogic calculation.
     */
    Double[] sinArray;

    /**
     * Constructs an instance of FFTLogic object.
     * @param sampleNumber  Number of sample data for FFT. This is equal to number of complex sine wave in FFT.
     */
    public FFTLogic(int sampleNumber) {
        this.sampleNumber = sampleNumber;
        this.bitRevArray = makeBitReversedArray(sampleNumber);
        this.sinArray = makeSinArray(sampleNumber);
    }

    /**
     * Excutes FFT (Fast Fourier Transform) calculation.
     * @param real       Array of real part of complex sine waves.
     * @param imaginary  Array of imaginary part of complex sine waves.
     */
    public void executeFFT(Double[] real, Double[] imaginary) {
        this.transform(this.sampleNumber, this.bitRevArray, this.sinArray, real, imaginary, 1);
        for (int i = 0; i < this.sampleNumber; i++) {
            real[i] /= this.sampleNumber;
            imaginary[i] /= this.sampleNumber;
        }
    }

    /**
     * Executes Inverse FFT (Inverse Fast Fourier Transform) calculation.
     * @param real       Array of real part of complex sine waves.
     * @param imaginary  Array of imaginary part of complex sine waves.
     */
    public void executeInverseFFT(Double[] real, Double[] imaginary) {
        transform(this.sampleNumber, this.bitRevArray, this.sinArray, real, imaginary, -1);
    }

    /**
     * Executes Bit-reversal permutation
     * @param sampleNumber Number of sample data.
     * @return Bit-reversed array
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
     * Prepares array of sine values
     * Note: Cosine values are not necessary because they can be obtained by shifting phase (π/2) of sine values.
     * @param sampleNumber Number of sample data.
     * @return Array of sine values.
     */
    private static Double[] makeSinArray(int sampleNumber) {
        Double[] sinArray = new Double[sampleNumber + sampleNumber / 4];

        Double t = Math.sin(Math.PI / sampleNumber); // コストのかかるライブラリ呼び出しは1回だけ
        Double dc = 2 * t * t;                  // π/2[rad]起点の変分の初期値
        Double ds = Math.sqrt(dc * (2 - dc));   // 0[rad]起点の変分の初期値
        t = 2 * dc;

        Double ｃ = sinArray[sampleNumber / 4] = 1.0;    // sin(π/2) = 1
        Double ｓ = sinArray[0] = 0.0;                   // sin(0) = 0

        // 三角関数の絶対値が実質的に変化する 0 ~ π/2 の間の値を計算する。
        // 効率のため 0 側と π/2 側の両側から計算を進行させ繰り返し数を半分にする。
        for (int i = 1; i < sampleNumber / 8; i++) {
            ｃ -= dc;
            dc += t * ｃ;    // π/2[rad]起点の変分はだんだん増えていく
            ｓ += ds;
            ds -= t * ｓ;    // 0[rad]起点の変分はだんだん減っていく
            sinArray[i] = ｓ;
            sinArray[sampleNumber / 4 - i] = ｃ;
        }

        // sin(π/4)
        if (sampleNumber / 8 != 0) {
            sinArray[sampleNumber / 8] = Math.sqrt(0.5);
        }

        // π/2 ~ π の範囲のsin値：0 ~ π/2 のsin値をコピーする。
        for (int i = 0; i < sampleNumber / 4; i++) {
            sinArray[sampleNumber / 2 - i] = sinArray[i];
        }

        // π ~ 2π の範囲のsin値：0 ~ π/2 のsin値を、符号を反転させてコピーする。
        // π/2余分に用意する：sin(θ+π/2)=cos(θ)であり、FFTの計算でsinArrayからcosの値を取り出しやすくするため。
        for (int i = 0; i < sampleNumber /2 + sampleNumber / 4; i++) {
            sinArray[i + sampleNumber / 2] = - sinArray[i];
        }

        return sinArray;
    }

    /**
     * Executes transformation with butterfly calculation.
     * @param sampleNumber Number of sample data.
     * @param bitRevArray  Bit-reversed array.
     * @param sinArray     Array of sine values.
     * @param real         Real part of sample data.
     * @param imaginary    Imaginary part of sample data.
     * @param sign         +1: Executes FFT. -1: Executes Inverse FFT.
     */
    private static void transform(
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

        for (int k = 1; k < sampleNumber; k *= 2) {     // k : 2のべき乗(1, 2, 4, 8, 16, 32, 64, 128, 256, ...)
            int sinIndex = 0;
            int d = sampleNumber / (k * 2);

            for (int j = 0; j < k; j++) {
                int cosIndex = sinIndex + sampleNumber / 4;
                Double cosValue = sinArray[cosIndex];
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
