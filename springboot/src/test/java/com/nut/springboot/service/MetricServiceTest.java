package com.nut.springboot.service;

import com.nut.springboot.services.MetricService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MetricServiceTest {

    private MetricService metricService;

    @BeforeEach
    public void setUp() {
        metricService = new MetricService();
    }

    @Test
    public void testGenerateCpuMetric() {
        metricService.generateCpuMetric();
        int cpuUsage = metricService.getCpuUsage();
        assertTrue(cpuUsage >= 0 && cpuUsage <= 100, "CPU usage should be between 0 and 100");
    }

    @Test
    public void testGenerateMemoryMetric() {
        metricService.generateMemoryMetric();
        int memoryUsage = metricService.getMemoryUsage();
        assertTrue(memoryUsage >= 0 && memoryUsage <= 100, "Memory usage should be between 0 and 100");
    }

    @Test
    public void testGenerateLatencyMetric() {
        metricService.generateLatencyMetric();
        int latency = metricService.getLatency();
        assertTrue(latency >= 50 && latency <= 500, "Latency should be between 50 and 500 ms");
    }
}
