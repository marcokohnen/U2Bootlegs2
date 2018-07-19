import {Component, OnInit} from '@angular/core';
import {Concert} from "../../concerts/concert.model";
import {Track} from "../track";
import {AppData} from "../../data/app-data.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Tour} from "../../tours/tour.model";
import * as $ from "jquery";
import {TrackService} from "../track.service";
import {TourService} from "../../tours/tour.service";
import {ConcertService} from "../../concerts/concert.service";
import {AuthenticationService} from "../../authentication/authentication.service";

@Component({
  selector: 'app-track-list',
  templateUrl: './track-list.component.html',
  styleUrls: ['./track-list.component.css']
})
export class TrackListComponent implements OnInit {

    activeTour : Tour;
    activeTourId : number;
    activeConcertId : number;
    tracksByConcert : Concert;
    trackList : Track[];

  constructor(private appData : AppData, private tourService : TourService, private concertService : ConcertService, private trackService : TrackService, private router : Router, private activatedRoute : ActivatedRoute, private authService: AuthenticationService) {
  }

  ngOnInit() {
      // this.activeTour = this.appData.tourObjectStorage;
      // this.tracksByConcert = this.appData.concertObjectStorage;
      // this.trackList = this.tracksByConcert.trackList;
      // this.activatedRoute.params
      //     .subscribe(
      //       params => {
      //           console.log("activatedRoute-params = " + JSON.stringify(params));
      //           this.activeTourId = params['tourId'];
      //           this.activeConcertId = params['concertId'];
      //       },
      //         (error) => console.log(error)
      //     );

      this.activeTourId = +this.activatedRoute.snapshot.paramMap.get("tourId");
      this.activeConcertId = +this.activatedRoute.snapshot.paramMap.get("concertId");

      this.tourService.findOne(this.activeTourId)
          .subscribe(
              (tour : Tour) => {
                  this.activeTour = tour;
              }
          );

      this.concertService.findOne(this.activeConcertId)
          .subscribe(
              (concert : Concert) => {
                  this.tracksByConcert = concert;
                  this.trackList = this.tracksByConcert.trackList;
                  // sort track in trackList by sequencNr ascending
                  this.trackList.sort((track1, track2) => track1.sequenceNr - track2.sequenceNr);
              }
          );
  }

    onAddClick() {
      console.log("onAddClick");
      // this.appData.trackObjectStorage = new Track(null, null, null, null);
      // this.appData.concertIdStorage = this.tracksByConcert.id;
      this.router.navigate(["addupdatetrack", this.activeConcertId, 0]);
    }

    onUpdateClick(trackId : number) {
      console.log("onUpdateClick");
      // this.appData.trackObjectStorage = track;
      // this.appData.concertIdStorage = this.tracksByConcert.id;
      this.router.navigate(["addupdatetrack", this.activeConcertId, trackId]);
    }

    onDeleteClick(track:  Track) {
        console.log("onDeleteClick");
        if (confirm("Weet u zeker dat u deze track wilt verwijderen ?\n\n"
            + "SequenceNr: " + track.sequenceNr + "\n"
            + "Title: " + track.title + "\n"
            + "url: " + track.locationUrl)){
         this.trackService.deleteOne(track.id)
             .subscribe(
                 (trackDeleted : Track) => this.deleteTrackFromConcertsArray(trackDeleted),
                 (error) => console.log(error)
             )
        }
    }

    deleteTrackFromConcertsArray(aTrack: Track) {
        let arrayIndexToDelete: number = this.trackList.findIndex(track => track.id == aTrack.id);
        if (!(arrayIndexToDelete == -1)) {
            this.trackList.splice(arrayIndexToDelete, 1);
        }
    }

    onPlayClick(trackUrl: string) {
        console.log("onPlayClick : " + trackUrl);
    }

    getUserRole(): string {
        return this.authService.userRole
    }
}
