package org.example.frequencytestsprocessor.datamodel.controlTheory;

import lombok.Getter;
import lombok.Setter;
import org.example.frequencytestsprocessor.datamodel.myMath.Complex;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DiscreteFRF implements FRF{
    private List<Double> frequencies;
    private List<Complex> complexValues;
    private FRF.GraphType graphType = GraphType.NYQUIST;

    @Override
    public DiscreteFRF setGraphType(GraphType graphType) {
        this.graphType = graphType;
        return this;
    }

    protected DiscreteFRF(){
        this.frequencies = new ArrayList<>();
        this.complexValues = new ArrayList<>();
    }

    public DiscreteFRF(List<Double> frequencies, List<Complex> complexValues) {
        this.frequencies = frequencies;
        this.complexValues = complexValues;
    }

    @Override
    public DiscreteFRF integrate() {
        DiscreteFRF result = new DiscreteFRF();
        List<Double> frequencies = this.getFrequencies();
        result.setFrequencies(frequencies);
        List<Complex> resultComplexValues = new ArrayList<>(this.getFrequencies().size());
        result.setComplexValues(resultComplexValues);
        for (int i = 0; i < this.getFrequencies().size(); i++) {
            resultComplexValues.add(Complex.multiplicationResult(this.getComplexValues().get(i), new Complex(0, frequencies.get(i) * 2 * Math.PI)));
        }
        return result;
    }

    @Override
    public DiscreteFRF differentiate() {
        DiscreteFRF result = new DiscreteFRF();
        List<Double> frequencies = this.getFrequencies();
        result.setFrequencies(frequencies);
        List<Complex> resultComplexValues = new ArrayList<>(this.getFrequencies().size());
        result.setComplexValues(resultComplexValues);
        for (int i = 0; i < this.getFrequencies().size(); i++) {
            resultComplexValues.add(Complex.multiplicationResult(this.getComplexValues().get(i), new Complex(0, 1 / (frequencies.get(i) * 2 * Math.PI))));
        }
        return result;
    }
}
