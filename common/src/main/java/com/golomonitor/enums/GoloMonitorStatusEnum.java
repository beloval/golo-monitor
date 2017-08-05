package com.golomonitor.enums;

/**
 * Created by abelov on 03/08/17.
 */
public enum GoloMonitorStatusEnum {
    STARTED("Started"), STOPPED("Stopped");

    private String value;

    GoloMonitorStatusEnum(String value) {
        this.value = value;

    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
