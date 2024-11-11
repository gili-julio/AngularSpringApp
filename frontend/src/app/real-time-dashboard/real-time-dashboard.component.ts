// src/app/real-time-dashboard/real-time-dashboard.component.ts
import { Component, OnInit, OnDestroy } from '@angular/core';
import { WebSocketService } from '../services/web.socket.service';
import { ChartConfiguration } from 'chart.js';
import { Subscription } from 'rxjs';
import { NgIf } from '@angular/common';
import { NgChartsModule } from 'ng2-charts';

interface MetricData {
  cpu: number;
  memory: number;
  latency: number;
}

@Component({
  selector: 'app-real-time-dashboard',
  standalone: true,
  imports: [NgIf, NgChartsModule],
  providers: [WebSocketService],
  templateUrl: './real-time-dashboard.component.html',
  styleUrls: ['./real-time-dashboard.component.css']
})
export class RealTimeDashboardComponent implements OnInit, OnDestroy {
  public cpuData: number[] = [];
  public memoryData: number[] = [];
  public latencyData: number[] = [];
  public chartLabels: string[] = Array(10).fill('');

  private messageSubscription!: Subscription;

  public cpuChartConfig: ChartConfiguration<'line'> = {
    type: 'line',
    data: {
      labels: this.chartLabels,
      datasets: [
        { data: this.cpuData, label: 'CPU Usage (%)', borderColor: 'blue', fill: false },
      ]
    },
    options: {
      responsive: true,
      scales: {
        y: { beginAtZero: true, max: 100 },
      }
    }
  };

  public memoryChartConfig: ChartConfiguration<'line'> = {
    type: 'line',
    data: {
      labels: this.chartLabels,
      datasets: [
        { data: this.memoryData, label: 'Memory Usage (%)', borderColor: 'green', fill: false },
      ]
    },
    options: {
      responsive: true,
      scales: {
        y: { beginAtZero: true, max: 100 },
      }
    }
  };

  public latencyChartConfig: ChartConfiguration<'line'> = {
    type: 'line',
    data: {
      labels: this.chartLabels,
      datasets: [
        { data: this.latencyData, label: 'Latency (ms)', borderColor: 'red', fill: false },
      ]
    },
    options: {
      responsive: true,
      scales: {
        y: { beginAtZero: true },
      }
    }
  };

  constructor(private webSocketService: WebSocketService) {}

  ngOnInit(): void {
    // Subscribe to WebSocket messages
    this.messageSubscription = this.webSocketService.getMessages().subscribe((message) => {
      try {
        const data: MetricData = JSON.parse(message);
        this.updateChartData(data);
      } catch (error) {
        console.error('Error parsing WebSocket message:', error);
      }
    });
  }

  ngOnDestroy(): void {
    // Unsubscribe from WebSocket messages when the component is destroyed
    if (this.messageSubscription) {
      this.messageSubscription.unsubscribe();
    }
    // Optionally, disconnect the WebSocket connection
    this.webSocketService.disconnect();
  }

  private updateChartData(data: MetricData) {
    this.cpuData.push(data.cpu);
    this.memoryData.push(data.memory);
    this.latencyData.push(data.latency);

    // Keep only the last 10 points for each dataset
    if (this.cpuData.length > 10) this.cpuData.shift();
    if (this.memoryData.length > 10) this.memoryData.shift();
    if (this.latencyData.length > 10) this.latencyData.shift();

    // Trigger update for each chart dataset
    this.cpuChartConfig.data.datasets[0].data = this.cpuData;
    this.memoryChartConfig.data.datasets[0].data = this.memoryData;
    this.latencyChartConfig.data.datasets[0].data = this.latencyData;
  }
}
