package com.golomonitor.exception;

/**
 * Created by abelov on 02/08/17.
 */
public class GoloMonitorStartedException extends Exception {

    public GoloMonitorStartedException(String message) {
        super(message);
    }

    public GoloMonitorStartedException(Throwable cause) {
        super(cause);
    }

    public GoloMonitorStartedException() {
    }
}
