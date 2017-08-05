package com.golomonitor.monitorStatistics;

import com.golomonitor.enums.ServerStatusEnum;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by abelov on 03/08/17.
 */

@Component
public class GoloMonitorStatistic {


    private AtomicBoolean goloMonitorStatus = new AtomicBoolean();

    private AtomicLong numberRequestToServer = new AtomicLong();

    private AtomicLong numberStatusActive = new AtomicLong();

    private AtomicLong numberStatusInActive = new AtomicLong();

    private AtomicLong numberStatusOfErrors = new AtomicLong();

    private ServerStatusEnum serverLastStatus;

    private Map<Date, ServerStatusEnum> serverStatusList = new LinkedHashMap<>();

    private ExecutorService service;

    public ExecutorService getService() {
        return service;
    }

    public void setService(ExecutorService service) {
        this.service = service;
    }

    public Map<Date, ServerStatusEnum> getServerStatusList() {
        return serverStatusList;
    }

    public void setServerStatusList(Map<Date, ServerStatusEnum> serverStatusList) {
        this.serverStatusList = serverStatusList;
    }

    public AtomicBoolean getGoloMonitorStatus() {
        return goloMonitorStatus;
    }

    public AtomicLong getNumberRequestToServer() {
        return numberRequestToServer;
    }

    public AtomicLong getNumberStatusInActive() {
        return numberStatusInActive;
    }

    public AtomicLong getNumberStatusOfErrors() {
        return numberStatusOfErrors;
    }

    public AtomicLong getNumberStatusActive() {
        return numberStatusActive;
    }

    public ServerStatusEnum getServerLastStatus() {
        return serverLastStatus;
    }

    public void setServerLastStatus(ServerStatusEnum serverLastStatus) {
        this.serverLastStatus = serverLastStatus;
    }
}

