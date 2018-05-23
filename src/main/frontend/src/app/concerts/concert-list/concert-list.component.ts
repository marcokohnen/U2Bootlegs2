import {Component, Input, OnInit} from '@angular/core';
import {Tour} from "../../tours/tour.model";
import {AppData} from "../../data/app-data.service";
import {Concert} from "../concert.model";
import {ConcertService} from "../concert.service";
import {ActivatedRoute, Router} from "@angular/router";
import {TourService} from "../../tours/tour.service";

@Component({
  selector: 'app-concert-list',
  templateUrl: './concert-list.component.html',
  styleUrls: ['./concert-list.component.css']
})
export class ConcertListComponent implements OnInit {

    activeTour : Tour;
    concertsByTourId : number;
    concertList : Concert[];

  constructor(private concertService : ConcertService, private tourService : TourService, private router : Router, private appData : AppData, private activatedRoute : ActivatedRoute) {

  }

  ngOnInit() {
      this.activatedRoute.params
          .subscribe(params => {console.log("activatedRoute-params = " + JSON.stringify(params));
              this.concertsByTourId = params['tourId'];
          });
      this.concertsByTourId = +this.activatedRoute.snapshot.paramMap.get("tourId");
      //this.concertsByTour = this.appData.tourObjectStorage;
      this.tourService.findOne(this.concertsByTourId)
          .subscribe((tour : Tour) =>
              {this.activeTour = tour;
               this.concertList = this.activeTour.concertList;
               // sort concerts by date ascending
               this.concertList.sort((concert1, concert2): number => new Date(concert1.date).getMilliseconds() - new Date(concert2.date).getMilliseconds());
               console.log("activeTour = " + JSON.stringify(this.activeTour));},
           (error) => console.log(error));


      // voeg een nieuw concert, die aan de database is toegevoegd, ook toe aan de array concertList zodat deze
      // getoond wordt in de browser
      // this.concertService.onConcertAdded.subscribe(
      //     (concert: Concert) =>
      //     {this.concertList.push(concert);
      //      console.log("this.concertList.push(concert)");
      //     },
      //     (error) => console.log(error)
      // );
      //
      // // wijzig de array concertList als er een concert is gewijzigd
      // this.concertService.onConcertUpdated.subscribe(
      //     (concert: Concert) => {
      //         let arrayIndexToUpdate: number = this.concertList.findIndex((obj => obj.id == concert.id));
      //         //console.log("Index of UpdatedTour in array TourList = " + arrayIndexToUpdate);
      //         this.concertList[arrayIndexToUpdate] = concert;
      //         console.log("this.concertList[arrayIndexToUpdate] = concert;");
      //     },
      //     (error) => console.log(error)
      // )
  }

    onAddClick() {
      console.log("onAddClick");
        // initialise concert object for concert-add form with a new concert object
        // this.appData.concertObjectStorage = new Concert(null, null, null, null, null, null, null, []);
        //this.appData.tourIdStorage = this.concertsByTour.id;
        this.router.navigate(['addupdateconcert', this.concertsByTourId, 0]);
    }

    onUpdateClick(concertId: number) {
        console.log("onUpdateClick");
        // initialise concert object for concert-add-form with parameter concert
        // this.appData.concertObjectStorage = new Concert(concert.id, concert.date, concert.title, concert.country, concert.city, concert.venue, concert.quality, concert.trackList);
        // this.appData.tourObjectStorage = this.activeTour;
        this.router.navigate(['addupdateconcert', this.concertsByTourId, concertId]);
    }

    onDeleteClick(concert: Concert) {
      console.log("onDeleteClick");
      if (confirm("Weet u zeker dat u dit concert wilt verwijderen ?\n\n"
                    + "Date: " + concert.date + "\n"
                    + "Title: " + concert.title + "\n"
                    + "City: " + concert.city + "\n"
                    + "Country: " + concert.country)) {
          this.concertService.deleteOne(concert.id)
              .subscribe(
              (concertDeleted : Concert) => this.deleteConcertFromConcertsArrayandLocaleStorage(concertDeleted),
                  (error => console.log(error))
          )
      }
    }

    deleteConcertFromConcertsArrayandLocaleStorage(aConcert : Concert) {
        let arrayIndexToDelete: number = this.concertList.findIndex((obj => obj.id == aConcert.id));
        if (!(arrayIndexToDelete == -1)) {
            this.concertList.splice(arrayIndexToDelete, 1);
            //localStorage.setItem("Tour"+activTour.id, JSON.stringify(activTour));
            }
    }

    onTrackListClick(tourId : number, concertId : number) {
      console.log("onTrackListClick");
        // this.appData.tourObjectStorage = activeTour;
        // this.appData.concertObjectStorage = concert;
        this.router.navigate(['listtracksbyconcert', tourId, concertId]);
    }

    onAddTrackClick(concertId: number) {
        this.router.navigate(["addupdatetrack", concertId, 0]);
    }
}
