package com.nut.springboot.services;

import com.nut.springboot.controller.MetricController.MetricResponse;
import com.nut.springboot.models.MetricAlert;
import lombok.Getter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class MetricService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Getter
    private final List<MetricAlert> alerts = new CopyOnWriteArrayList<>();
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

    private synchronized void checkAlerts(String metricName, int value, int limit) {
        // Procura o alerta existente com o mesmo nome de métrica
        Optional<MetricAlert> existingAlert = alerts.stream()
                .filter(alert -> alert.getMetricName().equals(metricName))
                .findFirst();
        if (value > limit) {
            if (existingAlert.isPresent()) {
                // Atualiza a mensagem do alerta existente
                existingAlert.get().setMessage(metricName + " is above limit: " + value);
            } else {
                // Adiciona um novo alerta se não existir ainda
                alerts.add(new MetricAlert(metricName, metricName + " is above limit: " + value));
            }
        } else {
            if (existingAlert.isPresent()) {
                alerts.remove(existingAlert.get());
            }
        }
    }

    @Scheduled(fixedRate = 5000)
    public synchronized int generateCpuMetric() {
        // Gera o valor da métrica de CPU
        int cpuUsage = random.nextInt(101);
        this.cpuUsage = cpuUsage;

        // Verifica se o valor da métrica excede o limite
        checkAlerts("cpu", cpuUsage, CPU_ALERT_LIMIT);

        return cpuUsage;
    }

    @Scheduled(fixedRate = 8000)
    public synchronized int generateMemoryMetric() {
        // Gera o valor da métrica de memória
        int memoryUsage = random.nextInt(101);
        this.memoryUsage = memoryUsage;

        // Verifica se o valor da métrica excede o limite
        checkAlerts("memory", memoryUsage, MEMORY_ALERT_LIMIT);

        return memoryUsage;
    }

    @Scheduled(fixedRate = 3000)
    public synchronized int generateLatencyMetric() {
        // Gera o valor da métrica de latência
        int latency = 50 + random.nextInt(451);
        this.latency = latency;

        // Verifica se o valor da métrica excede o limite
        checkAlerts("latency", latency, LATENCY_ALERT_LIMIT);

        return latency;
    }

    @Scheduled(fixedRate = 5000)
    public void sendCpuMetric() {

        int cpuUsage = generateCpuMetric();
        messagingTemplate.convertAndSend("/topic/metrics",
                new MetricResponse("cpu", cpuUsage, getAlerts()));
    }

    @Scheduled(fixedRate = 8000)
    public void sendMemoryMetric() {

        int memoryUsage = generateMemoryMetric();
        messagingTemplate.convertAndSend("/topic/metrics",
                new MetricResponse("memory", memoryUsage, getAlerts()));
    }

    @Scheduled(fixedRate = 3000)
    public void sendLatencyMetric() {

        int latency = generateLatencyMetric();
        messagingTemplate.convertAndSend("/topic/metrics",
                new MetricResponse("latency", latency, getAlerts()));
    }
}
