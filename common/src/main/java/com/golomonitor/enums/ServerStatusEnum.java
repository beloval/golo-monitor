package com.golomonitor.enums;

/**
 * Created by abelov on 03/08/17.
 */
public enum ServerStatusEnum {
    READY("Ready"), ERROR("Error"), UNKNOWN_STATUS("Unknow status");
    /*i didnt find others statuses of payload https://api.test.paysafe.com/accountmanagement/monitor
    so we can assume that if it response so its up if no -it down(error)
    so there is no other internal status as error, stopped - when server respond but its not functional
    */

    private String value;

    ServerStatusEnum(String value) {
        this.value = value;

    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
