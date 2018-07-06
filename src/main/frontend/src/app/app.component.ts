import {Component, OnInit} from '@angular/core';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

    //tourList: Tour[] = [];

    constructor() {
    }

    ngOnInit(): void {
        // this.tourService.findAll()
        //     .subscribe(
        //         (tours: [Tour]) => {
        //             this.tourList = tours;
        //             //console.log("TourList in ngOnInite =" + JSON.stringify(this.tourList));
        //             this.writeListToLocaleStorage(this.tourList);
        //         },
        //         (error) => console.log(error)
        //     );
    }

    // writeListToLocaleStorage(tourList: Tour[]) {
    //     if (localStorage) {
    //         this.clearLocaleStorage();
    //         for (let aTour of tourList) {
    //             localStorage.setItem("Tour" + aTour.id, JSON.stringify(aTour));
    //         }
    //     } else {
    //         console.log("Error : unable to write/read to localstorage")
    //     }
    // }
    //
    // clearLocaleStorage() {
    //     // Remove all items with key "Tour#"
    //     if (localStorage) {
    //         for (let i = 0; i < localStorage.length; i++) {
    //             let key = localStorage.key(i);
    //             //console.log("localStorage key = " + key);
    //             if (key.includes("Tour")) {
    //                 localStorage.removeItem(key);
    //             }
    //         }
    //     } else {
    //         console.log("Error : unable to write/read to localstorage")
    //     }
    // }
}
