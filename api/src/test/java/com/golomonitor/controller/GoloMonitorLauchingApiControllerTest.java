package com.golomonitor.controller;

import com.golomonitor.dto.LaunchingApiResponseEntity;
import com.golomonitor.enums.GoloMonitorStatusEnum;
import com.golomonitor.enums.ServerStatusEnum;
import com.golomonitor.monitorStatistics.GoloMonitorStatistic;
import com.golomonitor.services.core.LaunchingApiService;
import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.golomonitor.controller.GoloMonitorLaunchingApiController.SERVER_ALREADY_STARTED;
import static com.golomonitor.controller.GoloMonitorLaunchingApiController.SERVER_ALREADY_STOPPED;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


/**
 * Created by abelov on 05/08/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class GoloMonitorLauchingApiControllerTest {
    private static final Boolean LAUNCH_PARAMETER = true;
    private static final String HOSTNAME_PARAMETER = "https://api.test.paysafe.com/accountmanagement/monitor";
    private static final Integer INTERVAL_PARAMETER = 10000;
    private static final Long NUMBER_REQUEST_TO_SERVER = 100L;
    private static final Long NUMBER_STATUS_ACTIVE = 90L;
    private static final Boolean GOLO_MONITOR_STATUS = false;
    private static final Long NUMBER_STATUS_ERREUR = 5L;
    private static final Long NUMBER_STATUS_INACTIVE = 5L;
    private static final ServerStatusEnum SERVER_LAST_STATUS = ServerStatusEnum.READY;
    private static final int INTERVAL_IN_MILLISECOND = 10000;
    private static final GoloMonitorStatusEnum GOLO_MONITOR_STATUS_ENUM = GoloMonitorStatusEnum.STARTED;

    @InjectMocks
    GoloMonitorLaunchingApiController controller;

    @Mock
    LaunchingApiService launchingApiService;

    @Mock
    GoloMonitorStatistic goloMonitorStatistic;

    @Mock
    private Appender mockAppender;

    @Captor
    private ArgumentCaptor<LoggingEvent> captorLoggingEvent;

    @Before
    public void setup() {

        Logger root = Logger.getRootLogger();
        root.addAppender(mockAppender);
        root.setLevel(Level.INFO);
    }

    @Test
    public void postLaunchingStartTest() throws Exception {

        LaunchingApiResponseEntity response = createLaunchingApiResponseEntity();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                "/golomonitor/launch")
                .accept(MediaType.APPLICATION_JSON).param("launch", LAUNCH_PARAMETER ? "1" : "0").param("hostname", HOSTNAME_PARAMETER).param("interval", INTERVAL_PARAMETER.toString());

        AtomicBoolean atomicBoolean = new AtomicBoolean();
        atomicBoolean.set(GOLO_MONITOR_STATUS);
        when(goloMonitorStatistic.getGoloMonitorStatus()).thenReturn(atomicBoolean);

        when(launchingApiService.launch(LAUNCH_PARAMETER, HOSTNAME_PARAMETER, INTERVAL_PARAMETER)).thenReturn(response);

        MockMvc mockMvc =
                standaloneSetup(controller).build();
        ResultActions result = mockMvc.perform(requestBuilder);
  /*      result
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serverLastStatus", is(ServerStatusEnum.READY)))
                .andExpect(jsonPath("$.numberRequestToServer", is(NUMBER_REQUEST_TO_SERVER)))
                .andExpect(jsonPath("$.numberStatusActive", is(NUMBER_STATUS_ACTIVE)))
                .andExpect(jsonPath("$.numberStatusOfErrors", is(NUMBER_STATUS_ERREUR)))
                .andExpect(jsonPath("$.numberStatusInActive", is(NUMBER_STATUS_INACTIVE)))
                .andExpect(jsonPath("$.serverLastStatus", is(SERVER_LAST_STATUS)))
                .andExpect(jsonPath("$.serverStatusList", hasSize(createServerStatusList().size())))
                .andExpect(jsonPath("$.serverStatusList[0].serverStatusEnum", is(ServerStatusEnum.READY)))
                .andExpect(jsonPath("$.serverStatusList[4].serverStatusEnum", is(ServerStatusEnum.UNKNOWN_STATUS)))
                .andExpect(jsonPath("$.goloMonitorStatus", is(GoloMonitorStatusEnum.STARTED)));
   */

        Assert.assertNotNull(result);

        verify(launchingApiService, times(1)).launch(LAUNCH_PARAMETER, HOSTNAME_PARAMETER, INTERVAL_PARAMETER);
        verify(goloMonitorStatistic, times(2)).getGoloMonitorStatus();
        verify(mockAppender, times(8)).doAppend(captorLoggingEvent.capture());
        List<LoggingEvent> loggingEventList = captorLoggingEvent.getAllValues();
        Assert.assertEquals("start monitor requests to " + HOSTNAME_PARAMETER + " with intervals: " + INTERVAL_IN_MILLISECOND, loggingEventList.get(7).getMessage());
        Assert.assertEquals(Level.INFO, loggingEventList.get(7).getLevel());

        noMoreInteractions();

    }

    @Test
    public void postLaunchingStopTest() throws Exception {

        LaunchingApiResponseEntity response = createLaunchingApiResponseEntity();
        response.setGoloMonitorStatus(GoloMonitorStatusEnum.STOPPED);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                "/golomonitor/launch")
                .accept(MediaType.APPLICATION_JSON).param("launch", LAUNCH_PARAMETER ? "0" : "0").param("hostname", HOSTNAME_PARAMETER).param("interval", INTERVAL_PARAMETER.toString());

        AtomicBoolean atomicBoolean = new AtomicBoolean();
        atomicBoolean.set(!GOLO_MONITOR_STATUS);
        when(goloMonitorStatistic.getGoloMonitorStatus()).thenReturn(atomicBoolean);

        when(launchingApiService.launch(LAUNCH_PARAMETER, HOSTNAME_PARAMETER, INTERVAL_PARAMETER)).thenReturn(response);

        MockMvc mockMvc =
                standaloneSetup(controller).build();
        ResultActions result = mockMvc.perform(requestBuilder);
    /*    result
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serverLastStatus", is(ServerStatusEnum.READY)))
                .andExpect(jsonPath("$.numberRequestToServer", is(NUMBER_REQUEST_TO_SERVER)))
                .andExpect(jsonPath("$.numberStatusActive", is(NUMBER_STATUS_ACTIVE)))
                .andExpect(jsonPath("$.numberStatusOfErrors", is(NUMBER_STATUS_ERREUR)))
                .andExpect(jsonPath("$.numberStatusInActive", is(NUMBER_STATUS_INACTIVE)))
                .andExpect(jsonPath("$.serverLastStatus", is(SERVER_LAST_STATUS)))
                .andExpect(jsonPath("$.serverStatusList", hasSize(createServerStatusList().size())))
                .andExpect(jsonPath("$.serverStatusList[0].serverStatusEnum", is(ServerStatusEnum.READY)))
                .andExpect(jsonPath("$.serverStatusList[4].serverStatusEnum", is(ServerStatusEnum.UNKNOWN_STATUS)))
                .andExpect(jsonPath("$.goloMonitorStatus", is(GoloMonitorStatusEnum.STOPPED)));
*/

        Assert.assertNotNull(result);

        verify(launchingApiService, times(1)).launch(!LAUNCH_PARAMETER, HOSTNAME_PARAMETER, INTERVAL_PARAMETER);
        verify(goloMonitorStatistic, times(2)).getGoloMonitorStatus();
        verify(mockAppender, times(8)).doAppend(captorLoggingEvent.capture());
        List<LoggingEvent> loggingEventList = captorLoggingEvent.getAllValues();
        Assert.assertEquals(" stopping monitor", loggingEventList.get(7).getMessage());
        Assert.assertEquals(Level.INFO, loggingEventList.get(7).getLevel());

        noMoreInteractions();

    }

    @Test
    public void postLaunchingStopStopedServerExceptionTest() throws Exception {

        LaunchingApiResponseEntity response = createLaunchingApiResponseEntity();
        response.setGoloMonitorStatus(GoloMonitorStatusEnum.STOPPED);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                "/golomonitor/launch")
                .accept(MediaType.APPLICATION_JSON).param("launch", LAUNCH_PARAMETER ? "0" : "0").param("hostname", HOSTNAME_PARAMETER).param("interval", INTERVAL_PARAMETER.toString());

        AtomicBoolean atomicBoolean = new AtomicBoolean();
        atomicBoolean.set(GOLO_MONITOR_STATUS);
        when(goloMonitorStatistic.getGoloMonitorStatus()).thenReturn(atomicBoolean);

        MockMvc mockMvc =
                standaloneSetup(controller).build();
        ResultActions result = null;
        try {
            result = mockMvc.perform(requestBuilder);
            Assert.fail("Expected GoloMonitorStopedException");
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "Request processing failed; nested exception is com.golomonitor.exception.GoloMonitorStopedException: com.golomonitor.exception.GoloMonitorStopedException: " + SERVER_ALREADY_STOPPED);
        }
 /*       result
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.", is(message)))

*/

        Assert.assertNull(result);

        verify(launchingApiService, times(0)).launch(!LAUNCH_PARAMETER, HOSTNAME_PARAMETER, INTERVAL_PARAMETER);
        verify(goloMonitorStatistic, times(1)).getGoloMonitorStatus();
        verify(mockAppender, times(9)).doAppend(captorLoggingEvent.capture());
        List<LoggingEvent> loggingEventList = captorLoggingEvent.getAllValues();
        Assert.assertEquals(" stopping monitor", loggingEventList.get(7).getMessage());
        Assert.assertEquals(Level.INFO, loggingEventList.get(7).getLevel());
        Assert.assertEquals("Error to stop monitor : [" + SERVER_ALREADY_STOPPED + "]", loggingEventList.get(8).getMessage());
        Assert.assertEquals(Level.ERROR, loggingEventList.get(8).getLevel());

        noMoreInteractions();

    }

    @Test
    public void postLaunchingStartStartedServerExceptionTest() throws Exception {

        LaunchingApiResponseEntity response = createLaunchingApiResponseEntity();
        response.setGoloMonitorStatus(GoloMonitorStatusEnum.STARTED);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                "/golomonitor/launch")
                .accept(MediaType.APPLICATION_JSON).param("launch", LAUNCH_PARAMETER ? "1" : "1").param("hostname", HOSTNAME_PARAMETER).param("interval", INTERVAL_PARAMETER.toString());

        AtomicBoolean atomicBoolean = new AtomicBoolean();
        atomicBoolean.set(!GOLO_MONITOR_STATUS);
        when(goloMonitorStatistic.getGoloMonitorStatus()).thenReturn(atomicBoolean);

        MockMvc mockMvc =
                standaloneSetup(controller).build();
        ResultActions result = null;
        try {
            result = mockMvc.perform(requestBuilder);
            Assert.fail("Expected GoloMonitorStartedException");
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "Request processing failed; nested exception is com.golomonitor.exception.GoloMonitorStartedException: com.golomonitor.exception.GoloMonitorStartedException: " + SERVER_ALREADY_STARTED);

        }
 /*       result
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.", is(message)))

*/

        Assert.assertNull(result);

        verify(launchingApiService, times(0)).launch(!LAUNCH_PARAMETER, HOSTNAME_PARAMETER, INTERVAL_PARAMETER);
        verify(goloMonitorStatistic, times(2)).getGoloMonitorStatus();
        verify(mockAppender, times(9)).doAppend(captorLoggingEvent.capture());
        List<LoggingEvent> loggingEventList = captorLoggingEvent.getAllValues();
        Assert.assertEquals("start monitor requests to " + HOSTNAME_PARAMETER + " with intervals: " + INTERVAL_IN_MILLISECOND, loggingEventList.get(7).getMessage());
        Assert.assertEquals(Level.INFO, loggingEventList.get(7).getLevel());
        Assert.assertEquals("Error to start monitor : [" + SERVER_ALREADY_STARTED + "]", loggingEventList.get(8).getMessage());
        Assert.assertEquals(Level.ERROR, loggingEventList.get(8).getLevel());

        noMoreInteractions();

    }

    @Test
    public void postLaunchingMonitorThrowExceptionTest() throws Exception {

        LaunchingApiResponseEntity response = createLaunchingApiResponseEntity();
        response.setGoloMonitorStatus(GoloMonitorStatusEnum.STARTED);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                "/golomonitor/launch")
                .accept(MediaType.APPLICATION_JSON).param("launch", LAUNCH_PARAMETER ? "1" : "1").param("hostname", HOSTNAME_PARAMETER).param("interval", INTERVAL_PARAMETER.toString());

        AtomicBoolean atomicBoolean = new AtomicBoolean();
        atomicBoolean.set(GOLO_MONITOR_STATUS);
        when(goloMonitorStatistic.getGoloMonitorStatus()).thenReturn(atomicBoolean);
        when(launchingApiService.launch(LAUNCH_PARAMETER, HOSTNAME_PARAMETER, INTERVAL_PARAMETER)).thenThrow(new NullPointerException("Null pointer"));
        MockMvc mockMvc =
                standaloneSetup(controller).build();
        ResultActions result = null;
        try {
            result = mockMvc.perform(requestBuilder);
            Assert.fail("Expected Null pointer exception");
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "Request processing failed; nested exception is com.golomonitor.exception.LaunchingApiException: Null pointer");

        }
 /*       result
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.", is(message)))

*/

        Assert.assertNull(result);

        verify(launchingApiService, times(1)).launch(LAUNCH_PARAMETER, HOSTNAME_PARAMETER, INTERVAL_PARAMETER);
        verify(goloMonitorStatistic, times(2)).getGoloMonitorStatus();
        verify(mockAppender, times(9)).doAppend(captorLoggingEvent.capture());
        List<LoggingEvent> loggingEventList = captorLoggingEvent.getAllValues();
        Assert.assertEquals("start monitor requests to " + HOSTNAME_PARAMETER + " with intervals: " + INTERVAL_IN_MILLISECOND, loggingEventList.get(7).getMessage());
        Assert.assertEquals(Level.INFO, loggingEventList.get(7).getLevel());
        Assert.assertEquals("Exception happened when  start monitor : [Null pointer]", loggingEventList.get(8).getMessage());
        Assert.assertEquals(Level.ERROR, loggingEventList.get(8).getLevel());

        noMoreInteractions();

    }


    private void noMoreInteractions() {
        verifyNoMoreInteractions(launchingApiService);
        verifyNoMoreInteractions(goloMonitorStatistic);
        verifyNoMoreInteractions(mockAppender);
    }

    private LaunchingApiResponseEntity createLaunchingApiResponseEntity() {
        LaunchingApiResponseEntity launchingApiResponse = new LaunchingApiResponseEntity();
        launchingApiResponse.setNumberRequestToServer(NUMBER_REQUEST_TO_SERVER);
        launchingApiResponse.setNumberStatusActive(NUMBER_STATUS_ACTIVE);
        launchingApiResponse.setNumberStatusOfErrors(NUMBER_STATUS_ERREUR);
        launchingApiResponse.setNumberStatusInActive(NUMBER_STATUS_INACTIVE);
        launchingApiResponse.setServerLastStatus(SERVER_LAST_STATUS);
        launchingApiResponse.setGoloMonitorStatus(GOLO_MONITOR_STATUS_ENUM);
        launchingApiResponse.setServerStatusList(createServerStatusList());
        return launchingApiResponse;
    }

    private Map<Date, ServerStatusEnum> createServerStatusList() {
        Map<Date, ServerStatusEnum> serverStatusList = new LinkedHashMap<>();
        Calendar calendar = Calendar.getInstance();
        serverStatusList.put(calendar.getTime(), ServerStatusEnum.READY);
        calendar.add(Calendar.MILLISECOND, INTERVAL_IN_MILLISECOND);
        serverStatusList.put(calendar.getTime(), ServerStatusEnum.READY);
        calendar.add(Calendar.MILLISECOND, INTERVAL_IN_MILLISECOND);
        serverStatusList.put(calendar.getTime(), ServerStatusEnum.READY);
        calendar.add(Calendar.MILLISECOND, INTERVAL_IN_MILLISECOND);
        serverStatusList.put(calendar.getTime(), ServerStatusEnum.ERROR);
        calendar.add(Calendar.MILLISECOND, INTERVAL_IN_MILLISECOND);
        serverStatusList.put(calendar.getTime(), ServerStatusEnum.UNKNOWN_STATUS);
        return serverStatusList;
    }

}
