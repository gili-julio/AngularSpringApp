import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { EChartsOption } from 'echarts';
import { NgxEchartsDirective, provideEcharts } from 'ngx-echarts';
import { CommonModule } from '@angular/common';
import { MetricService } from '../../services/metrics.service';
import { Subscription, interval, switchMap } from 'rxjs';

@Component({
  selector: 'app-chart',
  standalone: true,
  imports: [CommonModule, NgxEchartsDirective],
  templateUrl: './chart.component.html',
  styleUrls: ['./chart.component.css'],
  providers: [
    provideEcharts()
  ]
})
export class ChartComponent implements OnInit, OnDestroy {
  @Input() endpoint!: string; // API endpoint input
  options!: EChartsOption;
  updateOptions!: EChartsOption;
  private dataSubscription!: Subscription;
  data: DataT[] = [];

  constructor(private chartDataService: MetricService) {}

  ngOnInit(): void {
    // Fetch data every 5 seconds from API
    this.dataSubscription = interval(5000).pipe(
      switchMap(() => this.chartDataService.fetchData(this.endpoint))
    ).subscribe({
      next: (data) => {
        this.data = data;  // Update data with new values
        this.updateChart(data);
      },
      error: (err) => console.error('Error fetching data:', err)
    });

    // Initial chart setup
    this.initializeChart();
  }

  ngOnDestroy() {
    this.dataSubscription.unsubscribe();
  }

  private initializeChart() {
    this.options = {
      title: { text: 'Usage Data Over Time' },
      tooltip: {
        trigger: 'axis',
        formatter: (params: any) => {
          const param = params[0];
          const date = new Date(param.name);
          const [alertStart, alertEnd] = param.data.alert; // Using `alert` data
          return `
            ${date.getSeconds()}s: ${param.value[1]}%<br />
            Alert Range: ${alertStart} - ${alertEnd}
          `;
        },
        axisPointer: {
          animation: false,
        },
      },
      xAxis: {
        type: 'time',
        axisLabel: { formatter: '{value}s' }, // Show time in seconds
        splitLine: { show: false },
      },
      yAxis: {
        type: 'value',
        min: 0,
        max: 100,  // Y-axis from 0% to 100%
        axisLabel: { formatter: '{value}%' },
        boundaryGap: [0, '100%'],
        splitLine: { show: false },
      },
      series: [
        {
          name: 'Usage Percentage',
          type: 'line',
          showSymbol: false,
          data: this.data.map(d => d.value), // Initial data
        },
      ],
    };
  }

  private updateChart(newData: DataT[]) {
    this.updateOptions = {
      series: [
        {
          data: newData.map(d => d.value) // Map `value` field for chart data
        },
      ],
    };
  }
}

export type DataT = {
  name: string;
  usage: number;
  alert: [string, string];
  value?: [string, number]; // Added to store processed value for ECharts
};
