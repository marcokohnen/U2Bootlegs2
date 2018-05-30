import {Component, OnInit, ViewChild} from '@angular/core';
import {ConcertService} from "../concert.service";
import {ActivatedRoute, Router} from "@angular/router";
import {AppData} from "../../data/app-data.service";
import {Concert} from "../concert.model";
import {NgForm} from "@angular/forms";
import {Location} from "@angular/common";

@Component({
    selector: 'app-concert-add-form',
    templateUrl: './concert-add-form.component.html',
    styleUrls: ['./concert-add-form.component.css']
})
export class ConcertAddFormComponent implements OnInit {

    //initialConcert met allemaal null-values is nodig voor de ngModel-bindings in concert-add-form.component.html. Zonder initialisatie geeft een fout bij de binding !!
    initialConcert: Concert;
    activeTourId: number;
    activeConcertId: number;

    @ViewChild("concertaddForm") private concertForm: NgForm;

    constructor(private concertService: ConcertService, private router: Router, private activatedRoute: ActivatedRoute, private appData: AppData, private location: Location) {
    }

    ngOnInit() {
        // this.initialConcert = this.appData.concertObjectStorage;
        // this.activeTour = this.appData.tourObjectStorage;
        this.activeTourId = +this.activatedRoute.snapshot.paramMap.get("tourId");
        this.activeConcertId = +this.activatedRoute.snapshot.paramMap.get("concertId");
        console.log(JSON.stringify(this.activatedRoute.params));

        if (this.activeConcertId == 0) { // new concert
            this.initialConcert = new Concert(null, null, null, null, null, null, null, null);
        } else { // find existing concert
            this.concertService.findOne(this.activeConcertId)
                .subscribe(
                    (concert: Concert) => {
                        this.initialConcert = concert;
                        console.log("this.initialConcert = " + JSON.stringify(this.initialConcert));
                    }
                );
        }
    }

    addOrUpdateConcert() {
        if (this.concertForm.valid) {
            let aConcert: Concert = this.concertForm.value;
            if (aConcert.id === null) { // new Concert
                this.concertAdd(aConcert, this.activeTourId);
            } else { // existing concert
                this.concertUpdate(aConcert);
            }
        }
    };

    concertAdd(aConcert: Concert, tourId: number) {
        this.concertService.addOneToTour(aConcert, tourId).subscribe(
            (newConcert: Concert) => {
                //console.log("Emitting OnConcertAdded");
                //this.concertService.onConcertAdded.emit(newConcert);
                this.formReset();
            },
            (error) => console.log("addOneToTour.subscribe error = " + error)
        );
    };

    concertUpdate(aConcert: Concert) {
        this.concertService.updateOne(aConcert).subscribe(
            (updatedConcert: Concert) => {
                //trigger event onConcertAdded
                //this.concertService.onConcertUpdated.emit(updatedConcert);
                //console.log("this.activeTourId = " + this.activeTourId);
                //this.router.navigate(["listconcertsbytour", this.activeTour.id]);
                this.location.back();
            },
            (error) => console.log(error)
        );
    }

    onFormCancel() {
        //this.router.navigate(['listconcertsbytour']);
        this.location.back();
    }

    formReset() {
        this.concertForm.reset();
    }

}
