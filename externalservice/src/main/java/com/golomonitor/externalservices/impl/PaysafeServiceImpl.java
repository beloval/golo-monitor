package com.golomonitor.externalservices.impl;

import com.golomonitor.enums.ServerStatusEnum;
import com.golomonitor.exception.ExternalServiceException;
import com.golomonitor.externalservices.AbstractProviderService;
import com.golomonitor.externalservices.ExternalServiceProvider;
import org.springframework.stereotype.Service;

/*
 * Created by abelov on 03/08/17.
 */
@Service("PaysafeService")
public class PaysafeServiceImpl extends AbstractProviderService implements ExternalServiceProvider{


    @Override
    public ServerStatusEnum getPaysafeServerStatus(String hostname) throws ExternalServiceException {
        return ServerStatusEnum.READY;
    }
}
