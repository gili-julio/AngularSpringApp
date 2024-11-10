package com.nut.springboot.controller;

import com.nut.springboot.controller.MetricController;
import com.nut.springboot.services.MetricService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@WebMvcTest(MetricController.class)
public class MetricControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MetricService metricService;

    @Test
    public void testGetCpuMetric() throws Exception {
        when(metricService.getCpuUsage()).thenReturn(50); // Simula um valor de 50%

        mockMvc.perform(get("/metrics/cpu"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usage", greaterThanOrEqualTo(0)))
                .andExpect(jsonPath("$.usage", lessThanOrEqualTo(100)));
    }

    @Test
    public void testGetMemoryMetric() throws Exception {
        when(metricService.getMemoryUsage()).thenReturn(75); // Simula um valor de 75%

        mockMvc.perform(get("/metrics/memory"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usage", greaterThanOrEqualTo(0)))
                .andExpect(jsonPath("$.usage", lessThanOrEqualTo(100)));
    }

    @Test
    public void testGetLatencyMetric() throws Exception {
        when(metricService.getLatency()).thenReturn(300); // Simula uma latência de 300ms

        mockMvc.perform(get("/metrics/latency"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseTime", greaterThanOrEqualTo(50)))
                .andExpect(jsonPath("$.responseTime", lessThanOrEqualTo(500)));
    }
}
