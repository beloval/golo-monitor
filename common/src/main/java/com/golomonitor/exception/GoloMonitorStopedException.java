package com.golomonitor.exception;

/**
 * Created by abelov on 02/08/17.
 */
public class GoloMonitorStopedException extends Exception {

    public GoloMonitorStopedException(String message) {
        super(message);
    }

    public GoloMonitorStopedException(Throwable cause) {
        super(cause);
    }

    public GoloMonitorStopedException() {
    }
}
