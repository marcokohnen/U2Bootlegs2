import {Component, OnInit, ViewChild} from '@angular/core';
import {TourService} from "../tour.service";
import {Tour} from "../tour.model";
import {ActivatedRoute, Router} from "@angular/router";
import {AppData} from "../../data/app-data.service";
import {NgForm} from "@angular/forms";
import {Location} from "@angular/common";

@Component({
    selector: 'app-tour-add-form',
    templateUrl: './tour-add-form.component.html',
    styleUrls: ['./tour-add-form.component.css']
})
export class TourAddFormComponent implements OnInit {

    //initialTour met allemaal null-values is nodig voor de ngModel-bindings in activeTour-add-form.component.html. Zonder initialisatie geeft een fout bij de binding !!
    initialTour: Tour;
    activeTourId : number;

    @ViewChild("touraddForm") private tourForm: NgForm;

    constructor(private tourService: TourService, private router: Router,
                private data: AppData, private location : Location, private activatedRoute : ActivatedRoute) {
    }

    ngOnInit() {
        //this.initialTour = this.data.tourObjectStorage;
        this.activeTourId = +this.activatedRoute.snapshot.paramMap.get("tourId");
        if (this.activeTourId == 0) { // new tour
            this.initialTour = new Tour(null, null, null, null, null, null, []);
        } else {
            this.tourService.findOne(this.activeTourId)
                .subscribe(
                    (tour : Tour) => {
                        this.initialTour = tour;
                    }
                )
        }
    }

    addOrUpdateTour() {
        if (this.tourForm.valid) {
            let aTour: Tour = this.tourForm.value;
            //console.log("aTour in addOrUpdateTour = " + JSON.stringify(aTour));
            if (aTour.id === null) { // new Tour
                this.tourAdd(aTour);
            } else { // existing activeTour
                this.tourUpdate(aTour);
            }
        }
    }

    tourAdd(aTour: Tour) {
        this.tourService.addOne(aTour).subscribe(
            (aTour: Tour) => {
                localStorage.setItem("Tour" + aTour.id, JSON.stringify(aTour));
                //trigger event onTourAdded
                //this.tourService.onTourAdded.emit(aTour);
                this.formReset();
            },
            (error) => console.log("addOne tour.subscribe error = " + error)
        );
    }

    tourUpdate(aTour: Tour) {
        this.tourService.updateOne(aTour.id, aTour).subscribe(
            (aTour: Tour) => {
                localStorage.setItem("Tour" + aTour.id, JSON.stringify(aTour));
                //trigger event onTourAdded
                //this.tourService.ontourUpdated.emit(aTour);
                //this.router.navigate(["listtour"]);
                this.location.back();
            },
            (error) => console.log(error)
        );
    }

    formReset() {
        this.tourForm.reset();
        this.initialTour.title = "";
        this.initialTour.concertList = [];
    }

    onFormCancel() {
        //this.router.navigate(["listtour"]);
        this.location.back();
    }
}
