package com.nut.springboot.controller;

import com.nut.springboot.services.MetricService;
import com.nut.springboot.models.MetricAlert;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MetricController {

    @Autowired
    private MetricService metricService;

    @GetMapping("/metrics/cpu")
    public Object getCpuMetric() {
        return new MetricResponse("cpu", metricService.getCpuUsage(), metricService.getAlerts());
    }

    @GetMapping("/metrics/memory")
    public Object getMemoryMetric() {
        return new MetricResponse("memory", metricService.getMemoryUsage(), metricService.getAlerts());
    }

    @GetMapping("/metrics/latency")
    public Object getLatencyMetric() {
        return new MetricResponse("latency", metricService.getLatency(), metricService.getAlerts());
    }

    @Getter
    public static class MetricResponse {

        private final String name;
        private final int usage;
        private final List<MetricAlert> alerts;

        public MetricResponse(String name, int usage, List<MetricAlert> alerts) {
            this.name = name;
            this.usage = usage;
            this.alerts = alerts;
        }

    }
}
