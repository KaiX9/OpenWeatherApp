import { Component, OnInit, inject } from '@angular/core';
import { WeatherService } from './weather.service';
import { Observable, Subject, map } from 'rxjs';
import { Weather } from './models';
import { FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  
  wSvc = inject(WeatherService)

  weather$!: Observable<Weather>
  form!: FormGroup
  fb = inject(FormBuilder)
  cityInput = new Subject<string>()
  errorMessage: string = ''

  ngOnInit(): void {
      this.form = this.createForm()
  }

  getWeatherAsObservable(city: string) {
    this.cityInput.next(city)
    this.weather$ = this.wSvc.getWeatherFromServer(city)
    this.weather$.subscribe(value => {
      console.log('>>> at component: ', value)
    })
    this.wSvc.error.subscribe(message => {
      this.errorMessage = message
      console.log('message: ', this.errorMessage)
    })
  }

  private createForm(): FormGroup {
    return this.fb.group({
      city: this.fb.control('')
    })
  }

}
