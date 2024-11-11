import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { DataT } from '../components/chart/chart.component';

@Injectable({
  providedIn: 'root'
})
export class MetricService {
  constructor(private http: HttpClient) {}

  fetchData(endpoint: string): Observable<DataT[]> {
    return this.http.get<DataT[]>(endpoint).pipe(
      // Optionally map or transform data if needed
      map((data) =>
        data.map((item) => ({
          name: item.name,
          usage: item.usage,    // Include `usage` to match DataT type
          value: [
            item.name,          // X-axis will use the timestamp directly from `name`
            item.usage          // Y-axis uses the `usage` value as the percentage
          ],
          alert: item.alert    // Include `alert` in case it's needed in the tooltip
        }))
      )
    );
  }
}
