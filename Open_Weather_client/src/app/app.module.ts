import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { WeatherService } from './weather.service';
import { HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from './material.module';
import { RoundPipe } from './round.pipe';
import { TimePipe } from './time.pipe';
import { CitylistComponent } from './components/citylist.component';
import { WeatherdisplayComponent } from './components/weatherdisplay.component';
import { Routes, RouterModule } from '@angular/router';

const appRoutes: Routes = [
  { path: '', component: CitylistComponent },
  { path: 'display', component: WeatherdisplayComponent },
  { path: '**', redirectTo: '/', pathMatch: 'full' }
]

@NgModule({
  declarations: [
    AppComponent,
    RoundPipe,
    TimePipe,
    CitylistComponent,
    WeatherdisplayComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    MaterialModule,
    RouterModule.forRoot(appRoutes, { useHash: true })
  ],
  providers: [WeatherService],
  bootstrap: [AppComponent]
})
export class AppModule { }
