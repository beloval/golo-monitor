package com.golomonitor.helper;

import com.golomonitor.dto.LaunchingApiResponseError;
import com.golomonitor.exception.GoloMonitorStartedException;
import com.golomonitor.exception.GoloMonitorStopedException;
import com.golomonitor.exception.LaunchingApiException;
import com.golomonitor.services.core.LaunchingApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;

/**
 * Created by abelov on 04/08/17.
 */
@Component
public class LaunchingApiResponseFactory {

    @Autowired
    private LaunchingApiService launchingApiService;

    public Object createGoloMonitorStartedExceptionResponse(GoloMonitorStartedException ex) {
        return new LaunchingApiResponseError(ex.getMessage(), launchingApiService.getStatistic());
    }

    public LaunchingApiResponseError createBadRequestResponse(MissingServletRequestParameterException ex) {
        StringBuffer message = new StringBuffer();
        message.append("Use all parameter pour request!! Error: ");
        message.append(ex.getMessage());
        return new LaunchingApiResponseError(message.toString());
    }

    public LaunchingApiResponseError createGoloMonitorStopedExceptionResponse(GoloMonitorStopedException ex) {
        return new LaunchingApiResponseError(ex.getMessage());
    }

    public Object createExceptionResponse(LaunchingApiException ex) {
        return new LaunchingApiResponseError(ex.getMessage());
    }

    public LaunchingApiResponseError createArgumentNotValidBadRequestResponse(MethodArgumentNotValidException ex) {
        StringBuffer message = new StringBuffer();
        message.append("Parameter has invalide value!! Error: ");
        message.append(ex.getMessage());
        return new LaunchingApiResponseError(message.toString());
    }
}
