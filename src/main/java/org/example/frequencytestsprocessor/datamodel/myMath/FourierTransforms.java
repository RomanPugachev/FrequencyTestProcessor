package org.example.frequencytestsprocessor.datamodel.myMath;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import java.util.Iterator;
import java.util.List;

public class FourierTransforms {
    public static Complex[] fft(List<Double> timeSeries) {
        int n = timeSeries.size();
        int cut_number = n / 2;
        double[] realTimeSeries = new double[n];

        Iterator<Double> iterator = timeSeries.iterator();
        for (int i = 0; i < n; i++) {
            realTimeSeries[i] = iterator.next();
        }

        // Remove DC component (mean)
        removeMean(realTimeSeries);

        // Apply Hanning window
        double[] windowedTimeSeries = applyHanningWindow(realTimeSeries);

        if (isPowerOfTwo(n)) {
            FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
            org.apache.commons.math3.complex.Complex[] result = fft.transform(windowedTimeSeries, TransformType.FORWARD);

            Complex[] complexResult = new Complex[cut_number];
            for (int i = 0; i < cut_number; i++) {
                complexResult[i] = new Complex(result[i].getReal(), result[i].getImaginary());
            }

            return complexResult;

        } else {
            int nextPowerOfTwo = nextPowerOfTwo(n);
            double[] paddedTimeSeries = new double[nextPowerOfTwo];
            System.arraycopy(windowedTimeSeries, 0, paddedTimeSeries, 0, n);  // use windowed data

            FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.UNITARY);
            org.apache.commons.math3.complex.Complex[] result = fft.transform(paddedTimeSeries, TransformType.FORWARD);

            Complex[] complexResult = new Complex[cut_number];
            for (int i = 0; i < cut_number; i++) {
                complexResult[i] = new Complex(result[i].getReal(), result[i].getImaginary());
            }

            return complexResult;
        }
    }

    private static void removeMean(double[] data) {
        Mean meanCalc = new Mean();
        double mean = meanCalc.evaluate(data);
        for (int i = 0; i < data.length; i++) {
            data[i] -= mean;
        }
    }

    private static double[] applyHanningWindow(double[] data) {
        int n = data.length;
        double[] windowed = new double[n];
        for (int i = 0; i < n; i++) {
            double multiplier = 0.5 * (1 - Math.cos(2 * Math.PI * i / (n - 1)));
            windowed[i] = data[i] * multiplier;
        }
        return windowed;
    }

    private static int nextPowerOfTwo(int n) {
        return 1 << (32 - Integer.numberOfLeadingZeros(n - 1));
    }

    private static boolean isPowerOfTwo(int n) {
        // This function checks if a given integer 'n' is a power of two.
        // It works as follows:
        // 1. First, it checks if 'n' is less than or equal to 0. If so, it returns false,
        //    as non-positive numbers cannot be powers of two.
        // 2. Then, it uses a bitwise operation: (n & (n - 1)) == 0
        //    - For powers of two, their binary representation has only one '1' bit.
        //    - Subtracting 1 from a power of two flips all bits after the rightmost '1'.
        //    - The bitwise AND (&) of n and (n-1) will be 0 only for powers of two.
        // 3. If the result of the bitwise operation is 0, the function returns true,
        //    indicating that 'n' is a power of two. Otherwise, it returns false.
        if (n <= 0) {
            return false;
        }
        return (n & (n - 1)) == 0;
    }
}
