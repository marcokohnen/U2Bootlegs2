import {EventEmitter, Injectable, Output} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Concert} from "./concert.model";

@Injectable()
export class ConcertService {

     private concertApiRoot="api/concert";

    // dit event wordt getriggerd nadat een nieuw activeTour is aangemaakt in de database zodat deze kan worden
    // toegevoegd aan de array van tourList in activeTour-list.component.ts. Op deze manier moet niet de hele lijst
    // van tourList opgehaald worden uit de database
    @Output() onConcertAdded = new EventEmitter<Concert>();
    @Output() onConcertUpdated: EventEmitter<Concert> = new EventEmitter();

    // voeg http toe via dependency injection
    constructor(private http: HttpClient) {
    }

    // voeg een nieuw concert toe aan een activeTour met tourId via http-request post naar TourController in de backend
    addOneToTour(concert: Concert, tourId: number) {
        return this.http.post("api/tour/addconcerttotour/" + tourId, concert);
    }

    // update een concert via http-request post naar ConcertController in de backend
    updateOne(concert: Concert) {
        return this.http.put(this.concertApiRoot + "/" + concert.id, concert);
    }

    // verwijder een concert op basis van id via http-request delete naar ConcertController in de backend
    deleteOne(concertId: number) {
        return this.http.delete(this.concertApiRoot + "/" + concertId);
    }

    findOne(concertId: number) {
        return this.http.get(this.concertApiRoot + "/findid/" + concertId);
    }

    findConcertsByTitle(searchTerm: string) {
        return this.http.get(this.concertApiRoot + "/findtitle/" + searchTerm);
    }

    findConcertsByCountry(searchTerm: string) {
        return this.http.get(this.concertApiRoot+ "/findcountry/" + searchTerm);
    }

    findConcertsByCity(searchTerm: string) {
        return this.http.get(this.concertApiRoot + "findcity/" + searchTerm);
    }

    findTourIdByConcertId(concertId : number) {
        return this.http.get(this.concertApiRoot + "/findtour/" + concertId);
    }
}
