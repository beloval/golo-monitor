package com.golomonitor.controller;


import com.golomonitor.dto.LaunchingApiResponseEntity;
import com.golomonitor.exception.GoloMonitorStartedException;
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
@RequestMapping("/golomonitor")
public class GoloMonitorLaunchingApiController {

    public static final String SERVER_ALREADY_STOPPED = "GOLO monitor have been already stopped";
    public static final String SERVER_ALREADY_STARTED = "GOLO monitor have been already started";
    private static final Logger logger = LoggerFactory.getLogger(GoloMonitorLaunchingApiController.class);
    @Autowired
    GoloMonitorStatistic goloMonitorStatistic;
    @Autowired
    private LaunchingApiService launchingApiService;

    @RequestMapping(path = "/launch", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public LaunchingApiResponseEntity postLaunching(
            @RequestParam(value = "launch") Boolean launch,
            @RequestParam(value = "hostname") String hostname,
            @RequestParam(value = "interval") Integer interval
    ) throws LaunchingApiException, GoloMonitorStopedException, GoloMonitorStartedException {
        try {
            logger.info((launch ? "start monitor requests to " + hostname + " with intervals: " + interval : " stopping monitor"));
            if (!goloMonitorStatistic.getGoloMonitorStatus().get() && !launch) {
                throw new GoloMonitorStopedException(SERVER_ALREADY_STOPPED);
            }
            if (goloMonitorStatistic.getGoloMonitorStatus().get() && launch) {
                throw new GoloMonitorStartedException(SERVER_ALREADY_STARTED);
            }
            return launchingApiService.launch(launch, hostname, interval);

        } catch (GoloMonitorStartedException e) {
            logger.error("Error to" + (launch ? " start " : " stop ") + "monitor : [{}]", e.getMessage(), e);
            throw new GoloMonitorStartedException(e);
        } catch (GoloMonitorStopedException e) {
            logger.error("Error to" + (launch ? " start " : " stop ") + "monitor : [{}]", e.getMessage(), e);
            throw new GoloMonitorStopedException(e);
        } catch (Exception e) {
            logger.error("Exception happened when " + (launch ? " start " : " stop ") + "monitor : [{}]", e.getMessage(), e);
            throw new LaunchingApiException(e.getMessage());
        }


    }

    @RequestMapping(path = "/stop", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public LaunchingApiResponseEntity postStopping() throws LaunchingApiException, GoloMonitorStopedException {
        try {
            logger.info("stopping monitor");
            if (!goloMonitorStatistic.getGoloMonitorStatus().get()) {
                throw new GoloMonitorStopedException(SERVER_ALREADY_STOPPED);
            }
            return launchingApiService.stop(false);

        } catch (GoloMonitorStopedException e) {
            logger.error("Error to stop monitor : [{}]", e.getMessage(), e);
            throw new GoloMonitorStopedException(e);
        } catch (Exception e) {
            logger.error("Exception happened when  stop monitor : [{}]", e.getMessage(), e);
            throw new LaunchingApiException(e.getMessage());
        }


    }

    @RequestMapping(path = "/statistics", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public LaunchingApiResponseEntity getStatistics() throws LaunchingApiException, GoloMonitorStopedException {
        try {
            logger.info("getting monitor statistics");
            return launchingApiService.getStatistic();
        } catch (Exception e) {
            logger.error("Exception happened when  stop monitor : [{}]", e.getMessage(), e);
            throw new LaunchingApiException(e.getMessage());
        }


    }

}
