package com.golomonitor.services.core.impl;

import com.golomonitor.dto.LaunchingApiResponseEntity;
import com.golomonitor.enums.ServerStatusEnum;
import com.golomonitor.exception.ExternalServiceException;
import com.golomonitor.exception.GoloMonitorStopedException;
import com.golomonitor.externalservices.ExternalServiceProvider;
import com.golomonitor.monitorStatistics.GoloMonitorStatistic;
import com.golomonitor.services.core.LaunchingApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedHashMap;

/**
 * Created by abelov on 02/08/17.
 */

@Service
public class LaunchingApiServiceImpl implements LaunchingApiService {

    private static final Logger logger = LoggerFactory.getLogger(LaunchingApiServiceImpl.class);
    private static final String INTERRUPTED_EXCEPTION = "Interrupted Exception happened";

    @Autowired
    GoloMonitorStatistic goloMonitorStatistic;

    @Autowired
    ExternalServiceProvider paysafeService;


    @Override
    public LaunchingApiResponseEntity launch(Boolean launch, String hostname, Integer interval) throws ExternalServiceException, GoloMonitorStopedException {

        goloMonitorStatistic.getServerStatus().set(launch);
        goloMonitorStatistic.setServerStatusList(new LinkedHashMap<>());

        try {
            startMonitor(hostname, interval);

        } catch (InterruptedException e) {
            logger.error(INTERRUPTED_EXCEPTION, e);
            throw new ExternalServiceException(INTERRUPTED_EXCEPTION);
        }

        return null;
    }

    private void startMonitor(String hostname, Integer interval) throws InterruptedException {

        while (goloMonitorStatistic.getServerStatus().get()) {
            Thread.sleep(interval);
            goloMonitorStatistic.getNumberRequestToServer().addAndGet(1L);

            try {
                ServerStatusEnum status = paysafeService.getPaysafeServerStatus(hostname);
                goloMonitorStatistic.getServerStatusList().put(new Date(), status);
                if (ServerStatusEnum.READY.equals(status)) {
                    goloMonitorStatistic.getNumberStatusActive().addAndGet(1L);
                }
            } catch (ExternalServiceException e) {
                goloMonitorStatistic.getServerStatusList().put(new Date(), ServerStatusEnum.ERROR);
                goloMonitorStatistic.getNumberStatusOfErrors().addAndGet(1L);
            }

        }


    }
}
