import {Component, Input, OnInit} from '@angular/core';
import {Tour} from "../tour.model";
import {TourService} from "../tour.service";
import {Router} from "@angular/router";
import {AppData} from "../../data/app-data.service";
import {Concert} from "../../concerts/concert.model";
import {AuthenticationService} from "../../authentication/authentication.service";

@Component({
    selector: 'app-tour-list',
    templateUrl: './tour-list.component.html',
    styleUrls: ['./tour-list.component.css']
})
export class TourListComponent implements OnInit {

    @Input() tourList: Tour[] = [];

    constructor(private tourService: TourService, private router: Router,
                private appData: AppData, private authService: AuthenticationService) {
    }

    ngOnInit() {
        // get all tours from localStorage
        // if (localStorage) {
        //     for (let i = 0; i < localStorage.length; i++) {
        //         let key = localStorage.key(i);
        //         if (key.includes("Tour")) {
        //             let aTour = JSON.parse(localStorage.getItem(key));
        //             this.tourList.push(aTour);
        //         }
        //         // sort tourList by startyear ascending
        //         this.tourList.sort((tour1, tour2) => tour1.startyear  - tour2.startyear )
        //     }
        // }
        // //subscribe op de Observable findAll() via de array tourList zodat bij een wijziging van deze array deze wijziging ook meteen zichtbaar is in de browser
        this.tourService.findAll()
            .subscribe(
                (tours: [Tour]) => {
                    this.tourList = tours;
                    //alert("tourList in activeTour-list.component.ts = " + JSON.stringify(this.tourList));
                },
                (error) => console.log(error)
            );


        // voeg een nieuwe activeTour, die aan de database is toegevoegd, ook toe aan de array tourList zodat deze
        // getoond wordt in de browser
        this.tourService.onTourAdded.subscribe(
            (tour: Tour) => this.tourList.push(tour),
            (error) => console.log(error)
        );

        // wijzig de array tourList met de gewijzigde activeTour
        this.tourService.ontourUpdated.subscribe(
            (tour: Tour) => {
                let arrayIndexToUpdate: number = this.tourList.findIndex((obj => obj.id == tour.id));
                //console.log("Index of UpdatedTour in array TourList = " + arrayIndexToUpdate);
                this.tourList[arrayIndexToUpdate] = tour;
            },
            (error) => console.log(error)
        )
    }

    onAddClick() {
        // initialise activeTour object for activeTour-add form
        // this.appData.tourObjectStorage = new Tour(null, "", null, null, null, null, []);
        this.router.navigate(['addupdatetour', 0]);
    }

    onUpdateClick(tourId : number) {
        //initialise activeTour object for activeTour-add form
        //this.appData.tourObjectStorage = tour;
        this.router.navigate(['addupdatetour', tourId]);
    }

    onDeleteClick(tour: Tour) {
        if (confirm("Weet u zeker dat u deze activeTour wilt verwijderen ?\n\n"
            + "Title: " + tour.title + "\n"
            + "leg: " + tour.leg + "\n"
            + "Continent: " + tour.continent + "\n"
            + "Year: " + tour.startyear + " - " + tour.endyear)) {
            this.tourService.deleteOneById(tour.id)
                .subscribe(
                    (tour: Tour) => this.deleteTourFromToursArrayAndLocalStorage(tour),
                    (error) => console.log(error));
        }
    }

    deleteTourFromToursArrayAndLocalStorage(aTour: Tour) {
        // let index = 0;
        // for (let findTour of this.tourList) {
        //     if (findTour.id === aTour.id) {
        //         this.tourList.splice(index, 1);
        //         if (localStorage){
        //             localStorage.removeItem("Tour" + aTour.id);
        //         }
        //         break;
        //     }
        //     index++;
        // }
        let arrayIndexToDelete: number = this.tourList.findIndex((obj => obj.id == aTour.id));
        if (!(arrayIndexToDelete == -1)) {
            this.tourList.splice(arrayIndexToDelete, 1);
            // if (localStorage) {
            //     localStorage.removeItem("Tour" + aTour.id);
            // }
        }
    }

    onAddConcertClick(tourId : number) {
        console.log("onAddConcertClick");
        // initialise concert object for concert-add form with a new concert object
        // this.appData.concertObjectStorage = new Concert(null, null, null, null, null, null, null, []);
        // this.appData.tourObjectStorage = tour;
        this.router.navigate(['addupdateconcert', tourId, 0]);
    }

    onConcertListClick(tourId : number) {
        //this.appData.tourObjectStorage = tour;
        this.router.navigate(['listconcertsbytour', tourId]);
    }

    getUserRole(): string {
        return this.authService.userRole
    }
}
