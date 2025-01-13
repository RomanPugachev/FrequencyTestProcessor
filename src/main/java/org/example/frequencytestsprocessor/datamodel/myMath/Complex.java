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
        Complex result = new Complex();
        Complex conjugatedDivisor = Complex.getConjugated(c2);
        // TODO: implement method
        return result;
    }

    public static Complex getConjugated(Complex c){
        return new Complex(c.getReal(), -c.getImag());
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Complex;
    }
}

