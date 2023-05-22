import { Component, OnInit, inject } from '@angular/core';
import { WeatherService } from '../weather.service';
import { Observable } from 'rxjs';
import { Weather } from '../models';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Params, Router } from '@angular/router';

@Component({
  selector: 'app-citylist',
  templateUrl: './citylist.component.html',
  styleUrls: ['./citylist.component.css']
})

export class CitylistComponent implements OnInit {
  
  wSvc = inject(WeatherService)

  weather$!: Observable<Weather>
  form!: FormGroup
  fb = inject(FormBuilder)
  cityArray: string[] = []
  city: string = ''
  router = inject(Router)

  ngOnInit(): void {
    const data = localStorage.getItem('city');
    if (!!data) {
      this.cityArray = JSON.parse(data);
    }
    this.form = this.createForm();
  }

  getWeatherAsObservable(i: number) {
    const params: Params = { city: this.cityArray[i]};
    this.router.navigate([ '/display' ], { queryParams: params });
  }

  addCity(city: string) {
    if (this.cityArray.includes(city.toLowerCase())) {
      this.form = this.createForm();
    } else {
      this.cityArray.push(city.toLowerCase());
      localStorage.setItem('city', JSON.stringify(this.cityArray));
      this.form = this.createForm();
    }
  }

  deleteCity(i: number) {
    this.cityArray.splice(i, 1);
    const cityList = JSON.parse(localStorage.getItem('city') || '[]');
    cityList.splice(i, 1);
    localStorage.setItem('city', JSON.stringify(cityList));
    console.info('City List: ', this.cityArray);
  }

  private createForm(): FormGroup {
    return this.fb.group({
      city: this.fb.control('')
    })
  }

}
