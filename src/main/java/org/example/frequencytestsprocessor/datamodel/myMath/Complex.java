package org.example.frequencytestsprocessor.datamodel.myMath;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
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
    protected boolean canEqual(final Object other) {
        return other instanceof Complex;
    }
}









