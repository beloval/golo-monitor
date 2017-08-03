package com.golomonitor.exception;

/**
 * Created by abelov on 02/08/17.
 */
public class LaunchingApiException extends Exception {

    public LaunchingApiException(String message) {
        super(message);
    }

    public LaunchingApiException(Throwable cause) {
        super(cause);
    }

    public LaunchingApiException() {
    }
}
