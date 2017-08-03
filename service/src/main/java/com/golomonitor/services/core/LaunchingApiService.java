package com.golomonitor.services.core;


import com.golomonitor.dto.LaunchingApiResponseDTO;
import com.golomonitor.exception.ExternalServiceException;
import com.golomonitor.exception.GoloMonitorStopedException;

/**
 * Created by abelov on 02/08/17.
 */
public interface LaunchingApiService {

    LaunchingApiResponseDTO launch(String assetNumber) throws ExternalServiceException, GoloMonitorStopedException;
}
