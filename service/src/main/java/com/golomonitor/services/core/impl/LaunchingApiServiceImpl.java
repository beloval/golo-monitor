package com.golomonitor.services.core.impl;

import com.golomonitor.dto.LaunchingApiResponseDTO;
import com.golomonitor.exception.ExternalServiceException;
import com.golomonitor.exception.GoloMonitorStopedException;
import com.golomonitor.services.core.LaunchingApiService;
import org.springframework.stereotype.Service;

/**
 * Created by abelov on 02/08/17.
 */

@Service
public class LaunchingApiServiceImpl implements LaunchingApiService {

    @Override
    public LaunchingApiResponseDTO launch(String assetNumber) throws ExternalServiceException, GoloMonitorStopedException {
        return null;
    }
}
