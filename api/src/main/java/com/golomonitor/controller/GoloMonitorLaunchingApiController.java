package com.golomonitor.controller;


import com.golomonitor.dto.LaunchingApiResponseDTO;
import com.golomonitor.exception.ExternalServiceException;
import com.golomonitor.exception.GoloMonitorStopedException;
import com.golomonitor.exception.LaunchingApiException;
import com.golomonitor.services.core.LaunchingApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by abelov on 02/08/17.
 */

@RestController
@RequestMapping("/golomonitor/launch")
public class GoloMonitorLaunchingApiController {

    private static final Logger logger = LoggerFactory.getLogger(GoloMonitorLaunchingApiController.class);

    @Autowired
    private LaunchingApiService launchingApiService;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public LaunchingApiResponseDTO postLaunching(
            @RequestParam(value = "assetNumber") String assetNumber) throws LaunchingApiException, GoloMonitorStopedException {
        try {
            logger.info("logger is working");
            return launchingApiService.launch(assetNumber);

        } catch (ExternalServiceException e) {
            logger.error("Error publishing : [{}]", e.getMessage(), e);
            throw new LaunchingApiException(e);
        } catch (GoloMonitorStopedException e) {
            logger.error("Error publishing : [{}]", e.getMessage(), e);
            throw new GoloMonitorStopedException(e);
        }

    }

}
