package org.example.frequencytestsprocessor.datamodel.myMath;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
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
        Complex cloneComplex;
        try {
            cloneComplex = (Complex) super.clone(); // Shallow copy
            return cloneComplex;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Couldn't clone Complex", e);
        }
    }

    public static Complex additionResult(Complex c1, Complex c2) {
        Complex result = new Complex();
        result.setReal(c1.getReal() + c2.getReal());
        result.setImag(c1.getImag() + c2.getImag());
        return result;
    }

    public static Complex extractionResult(Complex c1, Complex c2) {
        Complex result = new Complex();
        result.setReal(c1.getReal() - c2.getReal());
        result.setImag(c1.getImag() - c2.getImag());
        return result;
    }

    public static Complex multiplicationResult(Complex c1, Complex c2) {
        Complex result = new Complex();
        result.setReal(c1.getReal() * c2.getReal() - c1.getImag() * c2.getImag());
        result.setImag(c1.getImag() * c2.getReal() + c1.getReal() * c2.getImag());
        return result;
    }

    public static Complex divisionResult(Complex c1, Complex c2) {
        if (c2.getReal() == 0 && c2.getImag() == 0) {
            throw new ArithmeticException("Division by zero");
        }
        if (c2.getImag() == 0) {
            return new Complex(c1.getReal() / c2.getReal(), c1.getImag() / c2.getReal());
        }
        return Complex.divisionResult(Complex.multiplicationResult(c1, Complex.getConjugated(c2)), Complex.getModuleAsComplex(c2));
    }

    public static Complex getConjugated(Complex c){
        return new Complex(c.getReal(), -c.getImag());
    }

    public static Complex getModuleAsComplex(Complex c) {
        return new Complex(Math.sqrt(c.getReal() * c.getReal() + c.getImag() * c.getImag()), 0);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Complex;
    }
}

