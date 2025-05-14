package org.example.frequencytestsprocessor.datamodel.myMath;

import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import java.util.Iterator;
import java.util.List;

public class FourierTransforms {
    public static Complex[] fft(List<Double> timeSeries) {
        int n = timeSeries.size();
        double[] realTimeSeries = new double[n];

        Iterator<Double> iterator = timeSeries.iterator();
        for (int i = 0; i < n; i++) {
            realTimeSeries[i] = iterator.next();
        }

        if (isPowerOfTwo(n)) {
            FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
            org.apache.commons.math3.complex.Complex[] result = fft.transform(realTimeSeries, TransformType.FORWARD);

            Complex[] complexResult = new Complex[n];
            for (int i = 0; i < n; i++) {
                complexResult[i] = new Complex(result[i].getReal(), result[i].getImaginary());
            }

            return complexResult;
        } else {
            int nextPowerOfTwo = nextPowerOfTwo(n);
            double[] paddedTimeSeries = new double[nextPowerOfTwo];
            System.arraycopy(realTimeSeries, 0, paddedTimeSeries, 0, n);

            FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.UNITARY);
            org.apache.commons.math3.complex.Complex[] result = fft.transform(paddedTimeSeries, TransformType.FORWARD);

            Complex[] complexResult = new Complex[n];
            for (int i = 0; i < n; i++) {
                complexResult[i] = new Complex(result[i].getReal(), result[i].getImaginary());
            }

            return complexResult;
        }
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
