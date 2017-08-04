package com.golomonitor.externalservices;

import com.golomonitor.enums.ServerStatusEnum;
import com.golomonitor.exception.ExternalServiceException;

/**
 * Created by abelov on 03/08/17.
 */
public interface ExternalServiceProvider {

    ServerStatusEnum getPaysafeServerStatus(String hostname) throws ExternalServiceException;
}
