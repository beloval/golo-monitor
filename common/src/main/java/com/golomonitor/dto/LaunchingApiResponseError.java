package com.golomonitor.dto;

/**
 * Created by abelov on 04/08/17.
 */
public class LaunchingApiResponseError {
    String message;
    LaunchingApiResponseEntity statistics;

    public LaunchingApiResponseError(String message) {
        this.message = message;
    }

    public LaunchingApiResponseError(String message, LaunchingApiResponseEntity statistic) {
        this.message = message;
        this.statistics = statistic;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LaunchingApiResponseEntity getStatistics() {
        return statistics;
    }

    public void setStatistics(LaunchingApiResponseEntity statistics) {
        this.statistics = statistics;
    }
}
