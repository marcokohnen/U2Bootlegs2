import {Component, OnInit, ViewChild} from '@angular/core';
import {Track} from "../track";
import {TrackService} from "../track.service";
import {AppData} from "../../data/app-data.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NgForm} from "@angular/forms";
import {Location} from "@angular/common";

@Component({
    selector: 'app-track-add-form',
    templateUrl: './track-add-form.component.html',
    styleUrls: ['./track-add-form.component.css']
})
export class TrackAddFormComponent implements OnInit {

    initialTrack: Track;
    activeConcertId: number;
    activeTrackId: number;

    @ViewChild("trackaddForm") trackForm: NgForm;

    constructor(private trackService: TrackService, private router: Router, private appData: AppData, private location: Location, private activatedRoute: ActivatedRoute) {
    }

    ngOnInit() {
        // this.initialTrack = this.appData.trackObjectStorage;
        // this.concert = this.appData.concertObjectStorage;
        this.activeConcertId = +this.activatedRoute.snapshot.paramMap.get("concertId");
        this.activeTrackId = +this.activatedRoute.snapshot.paramMap.get("trackId");

        if (this.activeTrackId == 0) { // new Track
            this.initialTrack = new Track(null, null, null, null);
        } else {
            this.trackService.findOne(this.activeTrackId)
                .subscribe(
                    (track: Track) => {
                        this.initialTrack = track;
                    }
                )
        }
    }

    addOrUpdateTrack() {
        if (this.trackForm.valid) {
            let aTrack: Track = this.trackForm.value;
            if (aTrack.id === null) { // new Track
                this.trackAdd(aTrack, this.activeConcertId);
            } else { // existing track
                this.trackUpdate(aTrack);
            }
        }
    }

    trackAdd(aTrack: Track, concertId: number) {
        this.trackService.addOneToConcert(aTrack, concertId)
            .subscribe(
                (aTrack: Track) => {
                    this.trackService.onTrackAdded.emit(aTrack);
                    this.formReset();
                },
                (error) => console.log("addOne.subscribe error = " + error)
            );
    };

    trackUpdate(aTrack: Track) {
        this.trackService.updateOne(aTrack)
            .subscribe(
                (aTrack: Track) => {
                    // this.trackService.onTrackUpdated.emit(aTrack);
                    // this.router.navigate(["listtracksbyconcert"]);
                    this.location.back();
                },
                (error) => {
                    console.log(error)
                }
            );
    };

    formReset() {
        this.trackForm.reset();
    }

    onFormCancel() {
        //this.router.navigate(["listtracksbyconcert"])
        this.location.back();
    }
}
