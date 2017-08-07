package com.golomonitor.services.core.impl;

import com.golomonitor.dto.LaunchingApiResponseEntity;
import com.golomonitor.enums.GoloMonitorStatusEnum;
import com.golomonitor.enums.ServerStatusEnum;
import com.golomonitor.exception.ExternalServiceException;
import com.golomonitor.externalservices.ExternalServiceProvider;
import com.golomonitor.monitorStatistics.GoloMonitorStatistic;
import com.golomonitor.services.core.LaunchingApiService;
import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import static com.golomonitor.services.core.impl.LaunchingApiServiceImpl.RETURN_MONITOR_STATISTIC;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Created by abelov on 06/08/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class LaunchingApiServiceImplTest {

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
    LaunchingApiServiceImpl controller;

    @Mock
    ExternalServiceProvider paysafeService;

    @Mock
    GoloMonitorStatistic goloMonitorStatistic;

    @Mock
    private Appender mockAppender;

    @Mock
    private ExecutorService executor;

    @Captor
    private ArgumentCaptor<LoggingEvent> captorLoggingEvent;

    @Before
    public void setup() {

        Logger root = Logger.getRootLogger();
        root.addAppender(mockAppender);
        root.setLevel(Level.INFO);
    }

    @Test
    public void getStatisticTest() {

       createGoloMonitorStatisticValues();

        LaunchingApiResponseEntity result = controller.getStatistic();

        Assert.assertNotNull(result);

        statisticResponseAssertion(result, 1, 1, 1, 1, 1, 1, 1);

        verify(mockAppender, times(1)).doAppend(captorLoggingEvent.capture());
        List<LoggingEvent> loggingEventList = captorLoggingEvent.getAllValues();
        Assert.assertEquals(RETURN_MONITOR_STATISTIC, loggingEventList.get(0).getMessage());
        Assert.assertEquals(Level.INFO, loggingEventList.get(0).getLevel());

        noMoreInteractions();


    }



    @Ignore//need to finish this test
    @Test//(timeout = 1000)
    public void getLaunchStartMonitorTest() throws ExternalServiceException {
        createGoloMonitorStatisticValues();
        LaunchingApiResponseEntity result = controller.launch(LAUNCH_PARAMETER, HOSTNAME_PARAMETER, INTERVAL_PARAMETER);
        Assert.assertNotNull(result);
        verify(paysafeService, times(1)).getPaysafeServerStatus(HOSTNAME_PARAMETER);
        verify(goloMonitorStatistic, times(4)).getGoloMonitorStatus();
        verify(goloMonitorStatistic, times(1)).setService(any(ExecutorService.class));
        verify(goloMonitorStatistic,times(1)).setServerStatusList(any(Map.class));
        verify(goloMonitorStatistic, times(2)).setServerLastStatus(null);
        verify(goloMonitorStatistic, times(2)).getNumberStatusActive();
        verify(goloMonitorStatistic, times(2)).getNumberStatusOfErrors();

        verify(goloMonitorStatistic, times(3)).getNumberRequestToServer();
        verify(goloMonitorStatistic, times(2)).getNumberStatusInActive();
        verify(goloMonitorStatistic, times(2)).getNumberStatusOfErrors();
        noMoreInteractions();
    }



    @Test
    public void getLaunchStopMonitorTest() throws ExternalServiceException {

        createGoloMonitorStatisticValues();
        LaunchingApiResponseEntity result = controller.launch(!LAUNCH_PARAMETER, HOSTNAME_PARAMETER, INTERVAL_PARAMETER);
        Assert.assertNotNull(result);


        statisticResponseAssertion(result, 1, 1, 1, 1, 3, 1, 1);
        verify(goloMonitorStatistic, times(1)).getService();
        verify(mockAppender, times(2)).doAppend(captorLoggingEvent.capture());
        List<LoggingEvent> loggingEventList = captorLoggingEvent.getAllValues();
        Assert.assertEquals("close current thread: main", loggingEventList.get(0).getMessage());
        Assert.assertEquals(Level.INFO, loggingEventList.get(0).getLevel());
        Assert.assertEquals("Return monitors full statistic", loggingEventList.get(1).getMessage());
        Assert.assertEquals(Level.INFO, loggingEventList.get(1).getLevel());

        noMoreInteractions();
    }



    private void noMoreInteractions() {
        verifyNoMoreInteractions(paysafeService);
        verifyNoMoreInteractions(goloMonitorStatistic);
        verifyNoMoreInteractions(mockAppender);
    }

    private void createGoloMonitorStatisticValues() {

        AtomicBoolean atomicBoolean = new AtomicBoolean();
        atomicBoolean.set(GOLO_MONITOR_STATUS);
        when(goloMonitorStatistic.getGoloMonitorStatus()).thenReturn(atomicBoolean);

        AtomicLong atomicLong1 = new AtomicLong();
        atomicLong1.set(NUMBER_REQUEST_TO_SERVER);
        when(goloMonitorStatistic.getNumberRequestToServer()).thenReturn(atomicLong1);

        AtomicLong atomicLong2 = new AtomicLong();
        atomicLong2.set(NUMBER_STATUS_ACTIVE);
        when(goloMonitorStatistic.getNumberStatusActive()).thenReturn(atomicLong2);

        AtomicLong atomicLong3 = new AtomicLong();
        atomicLong3.set(NUMBER_STATUS_ERREUR);
        when(goloMonitorStatistic.getNumberStatusOfErrors()).thenReturn(atomicLong3);

        AtomicLong atomicLong4 = new AtomicLong();
        atomicLong4.set(NUMBER_STATUS_INACTIVE);
        when(goloMonitorStatistic.getNumberStatusInActive()).thenReturn(atomicLong4);

        when(goloMonitorStatistic.getService()).thenReturn(Executors.newFixedThreadPool(1));

        when(goloMonitorStatistic.getServerStatusList()).thenReturn(createServerStatusList());
        when(goloMonitorStatistic.getServerLastStatus()).thenReturn(SERVER_LAST_STATUS);

    }
    private Map<Date, ServerStatusEnum> createServerStatusList() {
        Map<Date, ServerStatusEnum> serverStatusList = new LinkedHashMap<>();
        Calendar calendar = Calendar.getInstance();
        serverStatusList.put(calendar.getTime(), ServerStatusEnum.READY);
        calendar.add(Calendar.MILLISECOND, INTERVAL_IN_MILLISECOND);
        serverStatusList.put(calendar.getTime(), ServerStatusEnum.ERROR);
        calendar.add(Calendar.MILLISECOND, INTERVAL_IN_MILLISECOND);
        serverStatusList.put(calendar.getTime(), ServerStatusEnum.READY);
        calendar.add(Calendar.MILLISECOND, INTERVAL_IN_MILLISECOND);
        serverStatusList.put(calendar.getTime(), ServerStatusEnum.ERROR);
        calendar.add(Calendar.MILLISECOND, INTERVAL_IN_MILLISECOND);
        serverStatusList.put(calendar.getTime(), ServerStatusEnum.UNKNOWN_STATUS);
        return serverStatusList;
    }
    private void statisticResponseAssertion(LaunchingApiResponseEntity result, int numberRequestToServer, int numberStatusActive, int numberStatusInActive, int numberStatusOfErrors,
                                            int goloMonitorStatus, int ServerLastStatus, int ServerStatusList) {
        verify(goloMonitorStatistic, times(numberRequestToServer)).getNumberRequestToServer();
        verify(goloMonitorStatistic, times(numberStatusActive)).getNumberStatusActive();
        verify(goloMonitorStatistic, times(numberStatusInActive)).getNumberStatusInActive();
        verify(goloMonitorStatistic, times(numberStatusOfErrors)).getNumberStatusOfErrors();
        verify(goloMonitorStatistic, times(goloMonitorStatus)).getGoloMonitorStatus();
        verify(goloMonitorStatistic, times(ServerLastStatus)).getServerLastStatus();
        verify(goloMonitorStatistic, times(ServerStatusList)).getServerStatusList();
        Assert.assertEquals(result.getNumberRequestToServer(), NUMBER_REQUEST_TO_SERVER);
        Assert.assertEquals(result.getNumberStatusActive(), NUMBER_STATUS_ACTIVE);
        Assert.assertEquals(result.getNumberStatusInActive(), NUMBER_STATUS_INACTIVE);
        Assert.assertEquals(result.getNumberStatusOfErrors(), NUMBER_STATUS_ERREUR);
        Assert.assertEquals(result.getGoloMonitorStatus(),  GOLO_MONITOR_STATUS ? GoloMonitorStatusEnum.STARTED : GoloMonitorStatusEnum.STOPPED );
        Assert.assertEquals(result.getServerLastStatus(), SERVER_LAST_STATUS);
        Assert.assertTrue(result.getServerStatusList().containsValue(ServerStatusEnum.READY));
        Assert.assertTrue(result.getServerStatusList().containsValue(ServerStatusEnum.ERROR));
        Assert.assertTrue(result.getServerStatusList().containsValue(ServerStatusEnum.UNKNOWN_STATUS));
        Assert.assertEquals(result.getServerStatusList().size(), 5);
    }
}
