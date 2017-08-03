package com.golomonitor.exception;

/**
 * Created by abelov on 03/08/17.
 */
public class ExternalServiceException extends Exception {

    public ExternalServiceException(String message) {
        super(message);
    }

    public ExternalServiceException(Throwable cause) {
        super(cause);
    }

    public ExternalServiceException() {
    }
}
