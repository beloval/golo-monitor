package com.golomonitor.externalservices.impl;

import com.golomonitor.enums.ServerStatusEnum;
import com.golomonitor.exception.ExternalServiceException;
import com.golomonitor.externalservices.AbstractProviderService;
import com.golomonitor.externalservices.ExternalServiceProvider;
import com.golomonitor.externalservices.utils.JerseyClientCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

/*
 * Created by abelov on 03/08/17.
 */
@Service("PaysafeService")
public class PaysafeServiceImpl extends AbstractProviderService implements ExternalServiceProvider {

    private static final Logger logger = LoggerFactory.getLogger(PaysafeServiceImpl.class);
    private static final String PAYSAFE_SERVER_EXEPTION = "PAYSAFE server return un exception";

    @Override
    public ServerStatusEnum getPaysafeServerStatus(String hostname) throws ExternalServiceException {
        try {

            PaysafeServerStatusResponse response = JerseyClientCreator.createClient().target(hostname).request(MediaType.APPLICATION_JSON_TYPE).get(PaysafeServerStatusResponse.class);
            logger.debug("Received status from paysafe : " + response.getStatus());
            return mappingResponse(response);

        } catch (WebApplicationException ex) {
            logger.error(PAYSAFE_SERVER_EXEPTION, ex);
            throw new ExternalServiceException(PAYSAFE_SERVER_EXEPTION);
        }

    }

    private ServerStatusEnum mappingResponse(PaysafeServerStatusResponse response) {
        if (ServerStatusEnum.READY.name().equals(response.getStatus())) {
            return ServerStatusEnum.READY;
        }
        return ServerStatusEnum.UNKNOWN_STATUS;
    }
}
