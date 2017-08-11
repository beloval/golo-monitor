package com.golomonitor.services.core.impl;

import com.golomonitor.dto.LaunchingApiResponseEntity;
import com.golomonitor.enums.GoloMonitorStatusEnum;
import com.golomonitor.enums.ServerStatusEnum;
import com.golomonitor.exception.ExternalServiceException;
import com.golomonitor.externalservices.ExternalServiceProvider;
import com.golomonitor.monitorStatistics.GoloMonitorStatistic;
import com.golomonitor.services.core.LaunchingApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by abelov on 02/08/17.
 */

@Service
public class LaunchingApiServiceImpl implements LaunchingApiService {

    public static final String RETURN_MONITOR_STATISTIC = "Return monitors full statistic";
    private static final Logger logger = LoggerFactory.getLogger(LaunchingApiServiceImpl.class);
    private static final String INTERRUPTED_EXCEPTION = "Interrupted Exception happened";
    private static final long LIMIT_OF_REQEST = 10000;//limiting statistic it should be in external file.properties
    @Autowired
    private GoloMonitorStatistic goloMonitorStatistic;

    @Autowired
    private ExternalServiceProvider paysafeService;


    @Override
    public LaunchingApiResponseEntity launch(Boolean launch, String hostname, Integer interval) {

        goloMonitorStatistic.getGoloMonitorStatus().set(launch);
        startMonitor(hostname, interval);

        return createLaunchingApiResponsEntity();
    }

    @Override
    public LaunchingApiResponseEntity getStatistic() {
        return createLaunchingApiResponsEntity();
    }

    @Override
    public LaunchingApiResponseEntity stop(Boolean launch) {
        return launch(false, "", 0);
    }

    private LaunchingApiResponseEntity createLaunchingApiResponsEntity() {
        LaunchingApiResponseEntity response = new LaunchingApiResponseEntity();
        response.setNumberRequestToServer(goloMonitorStatistic.getNumberRequestToServer().get());
        response.setNumberStatusActive(goloMonitorStatistic.getNumberStatusActive().get());
        response.setNumberStatusInActive(goloMonitorStatistic.getNumberStatusInActive().get());
        response.setNumberStatusOfErrors(goloMonitorStatistic.getNumberStatusOfErrors().get());
        response.setGoloMonitorStatus(goloMonitorStatistic.getGoloMonitorStatus().get() ? GoloMonitorStatusEnum.STARTED : GoloMonitorStatusEnum.STOPPED);
        response.setServerLastStatus(goloMonitorStatistic.getServerLastStatus());
        response.getServerStatusList().putAll(goloMonitorStatistic.getServerStatusList());
        logger.info(RETURN_MONITOR_STATISTIC);
        return response;
    }

    private void resetStatistic() {
        goloMonitorStatistic.setServerStatusList(new LinkedHashMap<>());
        goloMonitorStatistic.setServerLastStatus(null);
        goloMonitorStatistic.getNumberStatusActive().set(0);
        goloMonitorStatistic.getNumberStatusOfErrors().set(0);
        goloMonitorStatistic.getNumberRequestToServer().set(0);
        goloMonitorStatistic.getNumberStatusInActive().set(0);
    }


    private void startMonitor(String hostname, Integer interval) {

        if (goloMonitorStatistic.getGoloMonitorStatus().get()) {
            ExecutorService service = Executors.newFixedThreadPool((interval > 1000 ? 1 : 8));   //setup from file.properties
            goloMonitorStatistic.setService(service);
            resetStatistic();
            service.submit(() -> {
                try {
                    startServer(hostname, interval);
                } catch (InterruptedException e) {
                    logger.error(INTERRUPTED_EXCEPTION, e);
                }
            });
        } else {
            logger.info("close current thread: " + Thread.currentThread().getName());
            goloMonitorStatistic.getService().shutdownNow();
        }


    }

    private void startServer(String hostname, Integer interval) throws InterruptedException {
        while (goloMonitorStatistic.getGoloMonitorStatus().get()) {

            goloMonitorStatistic.getNumberRequestToServer().addAndGet(1L);
            logger.info("send request to paysave");
            try {
                ServerStatusEnum status = paysafeService.getPaysafeServerStatus(hostname);
                goloMonitorStatistic.getServerStatusList().put(new Date(), status);
                goloMonitorStatistic.setServerLastStatus(status);
                if (ServerStatusEnum.READY.equals(status)) {
                    goloMonitorStatistic.getNumberStatusActive().addAndGet(1L);
                }
                logger.info("got response from paysave: status:" + status);

            } catch (ExternalServiceException e) {
                goloMonitorStatistic.getServerStatusList().put(new Date(), ServerStatusEnum.ERROR);
                goloMonitorStatistic.getNumberStatusOfErrors().addAndGet(1L);
            }
            Thread.sleep(interval);
            if (goloMonitorStatistic.getNumberRequestToServer().get() > LIMIT_OF_REQEST) {
                logger.info("reached max of allowed number of requests");
                resetStatistic();
            }
        }
    }
}
