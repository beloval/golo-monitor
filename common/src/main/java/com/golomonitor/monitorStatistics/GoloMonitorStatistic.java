package com.golomonitor.monitorStatistics;

import com.golomonitor.enums.ServerStatusEnum;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by abelov on 03/08/17.
 */

@Component
public class GoloMonitorStatistic {


    private AtomicBoolean serverStatus = new AtomicBoolean();

    private AtomicLong numberRequestToServer = new AtomicLong();

    private AtomicLong numberStatusActive = new AtomicLong();

    private AtomicLong numberStatusInActive = new AtomicLong();

    private AtomicLong numberStatusOfErrors = new AtomicLong();

    private Map<Date, ServerStatusEnum> serverStatusList;


    public Map<Date, ServerStatusEnum> getServerStatusList() {
        return serverStatusList;
    }

    public void setServerStatusList(Map<Date, ServerStatusEnum> serverStatusList) {
        this.serverStatusList = serverStatusList;
    }

    public AtomicBoolean getServerStatus() {
        return serverStatus;
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


}
