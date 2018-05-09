import {EventEmitter, Injectable, Output} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Tour} from "./tour.model";

@Injectable()
export class TourService {

    // dit event wordt getriggerd nadat een nieuwe tour is aangemaakt in de database zodat deze kan worden
    // toegevoegd aan de array van tourList in tour-list.component.ts. Op deze manier moet niet de hele lijst
    // van tourList opgehaald worden uit de database
    @Output() onTourAdded = new EventEmitter<Tour>();
    @Output() ontourUpdated = new EventEmitter<Tour>();

    // voeg http toe via dependency injection
    constructor(private http: HttpClient) {
    }

    // vraag alle tours op via http-request get naar TourController in de backend
    findAll() {
        return this.http.get("/api/tour/findall");
    }

    // voeg een nieuwe tour toe via http-request post naar TourController in de backend
    addOne(tour: Tour) {
        return this.http.post("api/tour/", tour);
    }

    updateOne(id: number, tour: Tour) {
        return this.http.put("api/tour/" + tour.id, tour);
    }

    // verwijder een tour op basis van id via http-request delete naar TourController in de backend
    deleteOneById(tourId: number) {
        return this.http.delete("api/tour/" + tourId)
    }
}
