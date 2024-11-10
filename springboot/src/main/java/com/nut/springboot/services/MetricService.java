package com.nut.springboot.services;

import com.nut.springboot.models.MetricAlert;
import lombok.Getter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class MetricService {

    @Getter
    private final List<MetricAlert> alerts = new ArrayList<>();
    private final Random random = new Random();

    @Getter
    private int cpuUsage;
    @Getter
    private int memoryUsage;
    @Getter
    private int latency;

    private static final int CPU_ALERT_LIMIT = 80;
    private static final int MEMORY_ALERT_LIMIT = 75;
    private static final int LATENCY_ALERT_LIMIT = 200;

    private void checkAlerts(String metricName, int value, int limit) {
        if (value > limit) {
            alerts.add(new MetricAlert(metricName, metricName + " is above limit: " + value));
        }
    }

    @Scheduled(fixedRate = 5000)
    public int generateCpuMetric() {
        return this.cpuUsage = random.nextInt(101);
    }

    @Scheduled(fixedRate = 8000)
    public int generateMemoryMetric() {
        return this.memoryUsage = random.nextInt(101);
    }

    @Scheduled(fixedRate = 3000)
    public int generateLatencyMetric() {
        return this.latency = 50 + random.nextInt(451);
    }

}
