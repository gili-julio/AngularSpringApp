package com.nut.springboot.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class MetricService {
    private final Random random = new Random();

    @Scheduled(fixedRate = 5000)
    public int generateCpuMetrics() {

        return random.nextInt(101);
    }

    @Scheduled(fixedRate = 8000)
    public int generateMemoryMetric() {

        return random.nextInt(101);
    }

    @Scheduled(fixedRate = 3000)
    public int generateLatencyMetric() {

        return random.nextInt(451);
    }
}
