package org.example.frequencytestsprocessor.datamodel.myMath;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class Complex implements Cloneable{
    private double real;
    private double imag;

    public Complex(Complex complex) {
        this.real = complex.real;
        this.imag = complex.imag;
    }

    @Override
    public Complex clone() {
        return new Complex(this);
    }

    public static Complex additionResult(Complex c1, Complex c2) {
        Complex result = new Complex();
        result.setReal(c1.getReal() + c2.getReal());
        result.setImag(c1.getImag() + c2.getImag());
        return result;
    }

    public static Complex additionResult(Complex c, Double d) {
        Complex result = new Complex();
        result.setReal(c.getReal() + d);
        result.setImag(c.getImag());
        return result;
    }

    public static Complex subtractionResult(Complex c1, Complex c2) {
        Complex result = new Complex();
        result.setReal(c1.getReal() - c2.getReal());
        result.setImag(c1.getImag() - c2.getImag());
        return result;
    }

    public static Complex subtractionResult(Complex c, Double d) {
        Complex result = new Complex();
        result.setReal(c.getReal() - d);
        result.setImag(c.getImag());
        return result;
    }

    public static Complex subtractionResult(Double d, Complex c) {
        Complex result = new Complex();
        result.setReal(d - c.getReal());
        result.setImag(-c.getImag());
        return result;
    }


    public static Complex multiplicationResult(Complex c1, Complex c2) {
        Complex result = new Complex();
        result.setReal(c1.getReal() * c2.getReal() - c1.getImag() * c2.getImag());
        result.setImag(c1.getImag() * c2.getReal() + c1.getReal() * c2.getImag());
        return result;
    }

    public static Complex multiplicationResult(Complex c, Double d) {
        Complex result = new Complex();
        result.setReal(c.getReal() * d);
        result.setImag(c.getImag() * d);
        return result;
    }

    public static Complex divisionResult(Complex c1, Complex c2) {
        if (c2.getReal() == 0 && c2.getImag() == 0) {
            throw new ArithmeticException("Division by zero");
        }
        if (c2.getImag() == 0) {
            return new Complex(c1.getReal() / c2.getReal(), c1.getImag() / c2.getReal());
        }
        return Complex.divisionResult(Complex.multiplicationResult(c1, Complex.getConjugated(c2)), Complex.getSquaredModuleAsDouble(c2));
    }

    public static Complex divisionResult(Complex c, Double d) {
        if (d == 0) throw new ArithmeticException("Division by zero");
        return new Complex(c.getReal() / d, c.getImag() / d);
    }

    public static Complex divisionResult(Double d, Complex c) {
        if (d == 0) throw new ArithmeticException("Division by zero");
        return Complex.divisionResult(Complex.multiplicationResult(Complex.getConjugated(c), d), Complex.getSquaredModuleAsDouble(c));
    }

    public static Complex poweringResult(Complex c, Double n) {
        Double module = Math.pow(Complex.getModuleAsComplex(c).getReal(), n);
        Double angle = Complex.getAngle(c) * n;
        return Complex.ofModuleAndAngle(module, angle);
    }

    public static Complex getConjugated(Complex c){
        return new Complex(c.getReal(), -c.getImag());
    }

    public static Complex getModuleAsComplex(Complex c) {
        return new Complex(Math.sqrt(c.getReal() * c.getReal() + c.getImag() * c.getImag()), 0);
    }

    public static Double getModuleAsDouble(Complex c) {
        return Math.sqrt(c.getReal() * c.getReal() + c.getImag() * c.getImag());
    }

    public static Complex getSquaredModuleAsComplex(Complex c) {
        return new Complex(c.getReal() * c.getReal() + c.getImag() * c.getImag(), 0);
    }

    public static Double getSquaredModuleAsDouble(Complex c) {
        return c.getReal() * c.getReal() + c.getImag() * c.getImag();
    }

    public static Double getAngle(Complex c) {
        return Math.atan2(c.getImag(), c.getReal());
    }

    public static Complex ofModuleAndAngle(Double module, Double angle) {
        return new Complex(module * Math.cos(angle), module * Math.sin(angle));
    }

    @Override
    public String toString() {
        return real + (imag >= 0 ? "+" : "") + imag + "i";
    }

    public static Complex fromString(String s) {
        int plusIndex = s.indexOf('+');
        int minusIndex = s.indexOf('-', 1); // Start from index 1 to skip potential negative at start
        int separatorIndex = plusIndex >= 0 ? plusIndex : minusIndex;

        if (separatorIndex == -1) {
            throw new IllegalArgumentException("Invalid complex number format");
        }

        String realPart = s.substring(0, separatorIndex);
        String imagPart = s.substring(separatorIndex, s.length() - 1); // Remove 'i'

        double real = Double.parseDouble(realPart);
        double imag = Double.parseDouble(imagPart);

        return new Complex(real, imag);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Complex;
    }
}

