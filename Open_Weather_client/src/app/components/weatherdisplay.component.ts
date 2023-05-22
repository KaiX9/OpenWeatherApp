import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { Weather } from '../models';
import { WeatherService } from '../weather.service';

@Component({
  selector: 'app-weatherdisplay',
  templateUrl: './weatherdisplay.component.html',
  styleUrls: ['./weatherdisplay.component.css']
})
export class WeatherdisplayComponent implements OnInit {
  
  city = ''

  activatedRoute = inject(ActivatedRoute)
  display$!: Observable<Weather>
  errorMessage: string = ''
  
  wSvc = inject(WeatherService)

  ngOnInit(): void {
    this.city = this.activatedRoute.snapshot.queryParams['city'];
    this.display$ = this.wSvc.getWeatherFromServer(this.city);
    this.display$.subscribe(value => {
      console.log('>>> at component: ', value)
    })
    this.wSvc.error.subscribe(message => {
      this.errorMessage = message
      console.log('message: ', this.errorMessage)
    })
  }

}
