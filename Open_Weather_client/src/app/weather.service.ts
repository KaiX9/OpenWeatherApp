import { Injectable, inject } from "@angular/core";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable, Subject, catchError, throwError } from "rxjs";
import { Weather } from "./models";

const URL = 'http://localhost:8080/weather'
// const URL = 'https://weather-production-e25c.up.railway.app/weather'

@Injectable()
export class WeatherService {

    http = inject(HttpClient)

    error = new Subject<string>()

    getWeatherFromServer(city: string): Observable<Weather> {

        const params = new HttpParams().set("city", city);
        return this.http.get<Weather>(URL, { params }).pipe(
            catchError(error => {
                if (error.status === 404) {
                    console.log(error.error.message)
                    this.error.next(error.error.message)
                }
                return throwError(error)
            })
        )

    }

}