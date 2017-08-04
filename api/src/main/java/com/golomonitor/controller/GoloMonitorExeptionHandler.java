package com.golomonitor.controller;

import com.golomonitor.exception.GoloMonitorStopedException;
import com.golomonitor.exception.LaunchingApiException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


/**
 * Created by abelov on 02/08/17.
 */

@ControllerAdvice
public class GoloMonitorExeptionHandler extends ResponseEntityExceptionHandler {
    @Override
    public ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        return handleExceptionInternal(ex, new Object(),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = LaunchingApiException.class)
    public ResponseEntity<Object> handleLaunchingApiException(LaunchingApiException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), //TODO createExceptionResponse(ex)
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = GoloMonitorStopedException.class)
    public ResponseEntity<Object> handleServerAlreadyStopedException(GoloMonitorStopedException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(),//TODO createGoloMonitorStopedExceptionResponse()
                new HttpHeaders(), HttpStatus.NO_CONTENT, request);
    }

}
