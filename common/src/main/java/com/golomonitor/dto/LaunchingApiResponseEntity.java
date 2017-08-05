package com.golomonitor.dto;

import com.golomonitor.enums.GoloMonitorStatusEnum;
import com.golomonitor.enums.ServerStatusEnum;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by abelov on 03/08/17.
 */
public class LaunchingApiResponseEntity {

    private ServerStatusEnum serverLastStatus;

    private Long numberRequestToServer;

    private Long numberStatusActive;

    private Long numberStatusInActive;

    private Long numberStatusOfErrors;

    private GoloMonitorStatusEnum goloMonitorStatus;

    private Map<Date, ServerStatusEnum> serverStatusList = new LinkedHashMap<>();

    public ServerStatusEnum getServerLastStatus() {
        return serverLastStatus;
    }

    public void setServerLastStatus(ServerStatusEnum serverLastStatus) {
        this.serverLastStatus = serverLastStatus;
    }

    public Long getNumberRequestToServer() {
        return numberRequestToServer;
    }

    public void setNumberRequestToServer(Long numberRequestToServer) {
        this.numberRequestToServer = numberRequestToServer;
    }

    public Long getNumberStatusActive() {
        return numberStatusActive;
    }

    public void setNumberStatusActive(Long numberStatusActive) {
        this.numberStatusActive = numberStatusActive;
    }

    public Long getNumberStatusInActive() {
        return numberStatusInActive;
    }

    public void setNumberStatusInActive(Long numberStatusInActive) {
        this.numberStatusInActive = numberStatusInActive;
    }

    public Long getNumberStatusOfErrors() {
        return numberStatusOfErrors;
    }

    public void setNumberStatusOfErrors(Long numberStatusOfErrors) {
        this.numberStatusOfErrors = numberStatusOfErrors;
    }

    public Map<Date, ServerStatusEnum> getServerStatusList() {
        return serverStatusList;
    }

    public void setServerStatusList(Map<Date, ServerStatusEnum> serverStatusList) {
        this.serverStatusList = serverStatusList;
    }

    public GoloMonitorStatusEnum getGoloMonitorStatus() {
        return goloMonitorStatus;
    }

    public void setGoloMonitorStatus(GoloMonitorStatusEnum goloMonitorStatus) {
        this.goloMonitorStatus = goloMonitorStatus;
    }

}
