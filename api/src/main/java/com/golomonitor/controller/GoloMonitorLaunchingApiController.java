package com.golomonitor.controller;


import com.golomonitor.dto.LaunchingApiResponseEntity;
import com.golomonitor.exception.ExternalServiceException;
import com.golomonitor.exception.GoloMonitorStopedException;
import com.golomonitor.exception.LaunchingApiException;
import com.golomonitor.monitorStatistics.GoloMonitorStatistic;
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
    private static final String SERVER_ALREADY_STOPPED = "GOLO monitor have been already stopped";
    @Autowired
    GoloMonitorStatistic goloMonitorStatistic;
    @Autowired
    private LaunchingApiService launchingApiService;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public LaunchingApiResponseEntity postLaunching(
            @RequestParam(value = "launch") Boolean launch,
            @RequestParam(value = "hostname") String hostname,
            @RequestParam(value = "interval") Integer interval
    ) throws LaunchingApiException, GoloMonitorStopedException {
        try {
            logger.info((launch ? "start monitor requests to " + hostname + "with intervals: " + interval : " stopping monitor"));
            if (!goloMonitorStatistic.getServerStatus().get() && !launch) {
                throw new GoloMonitorStopedException(SERVER_ALREADY_STOPPED);
            }
            return launchingApiService.launch(launch, hostname, interval);

        } catch (ExternalServiceException e) {
            logger.error("Error publishing : [{}]", e.getMessage(), e);
            throw new LaunchingApiException(e);
        } catch (GoloMonitorStopedException e) {
            logger.error("Error publishing : [{}]", e.getMessage(), e);
            throw new GoloMonitorStopedException(e);
        }

    }

}
