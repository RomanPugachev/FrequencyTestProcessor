package org.example.frequencytestsprocessor.services.calculationService;

import lombok.Getter;

public enum Operators {
    PLUS("+"),
    MINUS("-"),
    MULTIPY("*"),
    DIVIDE("/");

    @Getter
    private String symbolicRepresentation;

    Operators (String symbolicRepresentation) {
        this.symbolicRepresentation = symbolicRepresentation;
    }


}
