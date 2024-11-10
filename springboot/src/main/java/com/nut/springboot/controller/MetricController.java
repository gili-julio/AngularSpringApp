package com.nut.springboot.controller;

import com.nut.springboot.services.MetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/metrics")
public class MetricController {
    private final MetricService metricService;

    @Autowired
    public MetricController(MetricService metricService) {
        this.metricService = metricService;
    }

    @GetMapping("/cpu")
    public int getCpuMetrics() {
        return metricService.generateCpuMetric();
    }

    @GetMapping("/memory")
    public int getMemoryMetrics() {
        return metricService.generateMemoryMetric();
    }

    @GetMapping("/latency")
    public int getLatencyMetrics() {
        return metricService.generateLatencyMetric();
    }

}
