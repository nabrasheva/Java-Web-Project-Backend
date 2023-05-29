package com.fmi.project.controller.validation;

public class ApiBadRequest extends RuntimeException {

    public ApiBadRequest(String message) {
        super(message);
    }
}
