package com.nut.springboot.services;

import lombok.Getter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class MetricService {
    private final Random random = new Random();

    @Getter
    private int cpuUsage;
    @Getter
    private int memoryUsage;
    @Getter
    private int latency;

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
