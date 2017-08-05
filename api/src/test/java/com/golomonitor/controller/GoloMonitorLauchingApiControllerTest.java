package com.golomonitor.controller;

import com.golomonitor.dto.LaunchingApiResponseEntity;
import com.golomonitor.enums.ServerStatusEnum;
import com.golomonitor.monitorStatistics.GoloMonitorStatistic;
import com.golomonitor.services.core.LaunchingApiService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.Assert;

import java.util.concurrent.atomic.AtomicBoolean;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


/**
 * Created by abelov on 05/08/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class GoloMonitorLauchingApiControllerTest {
    private static final Boolean LAUNCH_PARAMETER = true;
    private static final String HOSTNAME_PARAMETER = "https://api.test.paysafe.com/accountmanagement/monitor";
    private static final Integer INTERVAL_PARAMETER = 10000;
    private static final Long NUMBER_REQUEST_TO_SERVER = 1L;
    private static final Long NUMBER_STATUS_ACTIVE = 1L;
    private static final Boolean GOLO_MONITOR_STATUS = false;

    @InjectMocks
    GoloMonitorLaunchingApiController controller;

    @Mock
    LaunchingApiService launchingApiService;

    @Mock
    GoloMonitorStatistic goloMonitorStatistic;

    @Ignore
    @Test
    public void postLaunching() throws Exception {

        LaunchingApiResponseEntity response = creatLaunchingApiResponseEntity();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                "/golomonitor/launch")
                .accept(MediaType.APPLICATION_JSON).param("launch", "1").param("hostname", HOSTNAME_PARAMETER).param("interval", INTERVAL_PARAMETER.toString());

        AtomicBoolean atomicBoolean = new AtomicBoolean();
        atomicBoolean.set(GOLO_MONITOR_STATUS);
        Mockito.when(goloMonitorStatistic.getGoloMonitorStatus()).thenReturn(atomicBoolean);

        when(launchingApiService.launch(LAUNCH_PARAMETER, HOSTNAME_PARAMETER, INTERVAL_PARAMETER)).thenReturn(response);

        MockMvc mockMvc =
                standaloneSetup(controller).build();
        ResultActions result = mockMvc.perform(requestBuilder);
        result
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.serverLastStatus", is(ServerStatusEnum.READY)))
                .andExpect(jsonPath("$.numberRequestToServer", is(NUMBER_REQUEST_TO_SERVER)))
                .andExpect(jsonPath("$.numberStatusActive", is(NUMBER_STATUS_ACTIVE)));


        Assert.notNull(result);

        verify(launchingApiService, times(1)).launch(LAUNCH_PARAMETER, HOSTNAME_PARAMETER, INTERVAL_PARAMETER);
        verifyNoMoreInteractions(launchingApiService);

    }

    private LaunchingApiResponseEntity creatLaunchingApiResponseEntity() {
        LaunchingApiResponseEntity launchingApiResponse = new LaunchingApiResponseEntity();
        launchingApiResponse.setNumberRequestToServer(NUMBER_REQUEST_TO_SERVER);
        launchingApiResponse.setNumberStatusActive(NUMBER_STATUS_ACTIVE);
        return launchingApiResponse;
    }

}
