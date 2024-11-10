package com.nut.springboot.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MetricAlert {
    // Getters e Setters
    private String metricName;
    private String message;

    public MetricAlert(String metricName, String message) {
        this.metricName = metricName;
        this.message = message;
    }

}
