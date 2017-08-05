package com.golomonitor.services.core;


import com.golomonitor.dto.LaunchingApiResponseEntity;
import com.golomonitor.exception.ExternalServiceException;
import com.golomonitor.exception.GoloMonitorStopedException;

/**
 * Created by abelov on 02/08/17.
 */
public interface LaunchingApiService {

    LaunchingApiResponseEntity launch(Boolean launch, String hostname, Integer interval) throws ExternalServiceException, GoloMonitorStopedException;
    LaunchingApiResponseEntity getStatistic();
}
