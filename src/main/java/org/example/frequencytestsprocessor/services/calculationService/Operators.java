package org.example.frequencytestsprocessor.services.calculationService;

import lombok.Getter;

public enum Operators {
    PLUS("+"),
    MINUS("-"),
    MULTIPY("*"),
    DIVIDE("/");
    TODO : implement caluculation performance
    @Getter
    private String symbolicRepresentation;

    Operators (String symbolicRepresentation) {
        this.symbolicRepresentation = symbolicRepresentation;
    }
}
