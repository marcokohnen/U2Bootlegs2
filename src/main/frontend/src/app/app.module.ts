import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {AppComponent} from './app.component';
import {TourListComponent} from './tours/tour-list/tour-list.component';
import {TourService} from "./tours/tour.service";
import {HttpClientModule} from "@angular/common/http";
import {FormsModule} from "@angular/forms";
import {TourAddFormComponent} from './tours/tour-add-form/tour-add-form.component';
import {HeaderComponent} from './header/header.component';
import {FooterComponent} from './footer/footer.component';
import {RouterModule, Routes} from "@angular/router";
import {HomeComponent} from './home/home.component';
import {AppData} from './data/app-data.service';
import {ConcertListComponent} from './concerts/concert-list/concert-list.component';
import {ConcertService} from "./concerts/concert.service";
import {ConcertAddFormComponent} from './concerts/concert-add-form/concert-add-form.component';
import {TrackListComponent} from './tracks/track-list/track-list.component';
import {TrackService} from "./tracks/track.service";
import {TrackAddFormComponent} from './tracks/track-add-form/track-add-form.component';
import {SearchConcertComponent} from './concerts/search-concert/search-concert.component';

const routes: Routes = [
    {path: '', redirectTo: 'home', pathMatch: 'full'},
    {path: 'home', component: HomeComponent},
    {path: 'listtour', component: TourListComponent},
    {path: 'addupdatetour/:tourId', component: TourAddFormComponent},
    {path: 'listconcertsbytour/:tourId', component: ConcertListComponent},
    {path: 'addupdateconcert/:tourId/:concertId', component: ConcertAddFormComponent},
    {path: 'searchconcert/:parameter/:value', component: SearchConcertComponent},
    {path: 'listtracksbyconcert/:tourId/:concertId', component: TrackListComponent},
    {path: 'addupdatetrack/:concertId/:trackId', component: TrackAddFormComponent},
    {path: '**', component: HomeComponent}
];

@NgModule({
    declarations: [
        AppComponent,
        HomeComponent,
        HeaderComponent,
        FooterComponent,
        TourListComponent,
        TourAddFormComponent,
        ConcertListComponent,
        ConcertAddFormComponent,
        SearchConcertComponent,
        TrackListComponent,
        TrackAddFormComponent,
    ],

    imports: [
        BrowserModule,
        HttpClientModule,
        FormsModule,
        RouterModule.forRoot(routes, {useHash: true}),
    ],

    providers: [
        TourService,
        ConcertService,
        TrackService,
        AppData //voor doorgeven van objecten tussen components
    ],

    bootstrap: [AppComponent] //dit is de component waarmee de angular-app opstart
})
export class AppModule {
}
