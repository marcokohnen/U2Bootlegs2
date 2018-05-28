import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Concert} from "../concert.model";
import {ConcertService} from "../concert.service";

@Component({
    selector: 'app-search-concert',
    templateUrl: './search-concert.component.html',
    styleUrls: ['./search-concert.component.css']
})
export class SearchConcertComponent implements OnInit {

    resultList: Concert[] = null; // array containing found concerts
    copyResultList: Concert[] = null; // back-up of original searcg result
    searchParam: string;
    searchValue: string;

    constructor(private router: Router, private activatedRoute: ActivatedRoute, private concertService: ConcertService) {
    }

    ngOnInit() {
        // get searchParam and searchValue
        // this.searchParam = this.activatedRoute.snapshot.paramMap.get("parameter");
        // this.searchValue = this.activatedRoute.snapshot.paramMap.get("value");

        // hier kan je snapshot niet gebruiken omdat dan de route-parameters niet worden vernieuwd als je op deze pagina (=SearchConcertComponent) blijft en je toch een andere zoekterm hebt ingegeven !!
        // gebruik je activatedRoute.params, wat een Observable is, worden de rout-parameters wel vernieuwd als je een andere zoekterm hebt ingegeven !!
        this.activatedRoute.params
            .subscribe(
                (params) => {
                    this.searchParam = params['parameter'];
                    this.searchValue = params['value'];
                    console.log("searchParam in SearchConcertComponent = " + this.searchParam);
                    console.log("searchValue in SearchConcertComponent = " + this.searchValue);
                    this.lookUpConcerts(this.searchParam, this.searchValue);
                }
            );
    }

    lookUpConcerts(lookUpParam: string, lookUpValue: string) {
        switch (lookUpParam) {
            case "title".toLowerCase() : {
                //console.log("Switch searchParam : case = title");
                this.findConcertsByTitle(lookUpValue);
                break;
            }

            case "country".toLowerCase() : {
                //console.log("Switch searchParam : case = country");
                this.findConcertsByCountry(lookUpValue);
                break;
            }

            case "city".toLowerCase() : {
                //console.log("Switch searchParam : case = city");
                this.findConcertsByCity(lookUpValue);
                break;
            }

            default :
                this.resultList = null;
        }
    }

    findConcertsByTitle(lookUpValue: string) {
        this.concertService.findConcertsByTitle(lookUpValue)
            .subscribe(
                (concerts: Concert[]) => {
                    this.resultList = concerts;
                    this.copyResultList = concerts;
                },
                (error) => {
                    console.log(error);
                    //console.log("resultList = " + JSON.stringify(this.resultList));
                    this.resultList = null;
                }
            )
    }

    findConcertsByCountry(lookUpValue: string) {
        this.concertService.findConcertsByCountry(lookUpValue)
            .subscribe(
                (concerts: Concert[]) => {
                    this.resultList = concerts;
                    this.copyResultList = concerts
                },
                (error) => {
                    console.log(error);
                    this.resultList = null;
                }
            )
    }

    findConcertsByCity(lookUpValue: string) {
        this.concertService.findConcertsByCity(lookUpValue)
            .subscribe(
                (concerts: Concert[]) => {
                    this.resultList = concerts;
                    this.copyResultList = concerts
                },
                (error) => {
                    console.log(error);
                    this.resultList = null;
                }
            )
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
                    (concertDeleted: Concert) => this.deleteConcertFromResultList(concertDeleted),
                    (error => console.log(error))
                )
        }
    }

    deleteConcertFromResultList(aConcert: Concert) {
        let arrayIndexToDelete: number = this.resultList.findIndex((obj => obj.id == aConcert.id));
        if (!(arrayIndexToDelete == -1)) {
            this.resultList.splice(arrayIndexToDelete, 1);
        }
    }

    onUpdateClick(concertId: number) {
        console.log("onUpdateClick");
        // get tour_id from this concertId
        this.concertService.findTourIdByConcertId(concertId)
            .subscribe(
                (tourId: number) => {
                    //console.log("Concert met Id = " + concertId + " heeft tour_id = " + tourId);
                    this.router.navigate(["addupdateconcert", tourId, concertId])
                },
                (error => console.log(error))
            )
    }

    onTrackListClick(concertId: number) {
        console.log("onTrackListClick");
        this.concertService.findTourIdByConcertId(concertId)
            .subscribe(
                (tourId: number) => {
                    //console.log("Concert met Id = " + concertId + " heeft tour_id = " + tourId);
                    this.router.navigate(["listtracksbyconcert", tourId, concertId])
                },
                (error => console.log(error))
            )
    }

    onChangeQuality(selectQuality: string) {
        console.log("resultList with filter(" + selectQuality + ") = " + JSON.stringify(this.resultList));
        if (selectQuality == "ALL") {
            this.resultList = this.copyResultList
        } else {
            this.resultList = this.copyResultList.filter(concert => concert.quality == selectQuality);
        }
    }
}
