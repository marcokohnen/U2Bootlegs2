import {EventEmitter, Injectable, Output} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Track} from "./track";
import {AppComponent} from "../app.component";

@Injectable()
export class TrackService {

    @Output() onTrackAdded = new EventEmitter<Track>();
    @Output() onTrackUpdated = new EventEmitter<Track>();

    constructor(private http : HttpClient) {}

    // voeg een nieuwe track toe aan een concert met concertId via http-request post naar ConcertController in de backend
    addOneToConcert(track: Track, concertId : number) {
        return this.http.post(AppComponent.API_ROOT_CONCERT + "/addtracktoconcert/"+ concertId, track);
    }

    // update een track via http-request naar Trackcontroller in de backend
    updateOne(track: Track) {
        return this.http.put(AppComponent.API_ROOT_TRACK+ "/" + track.id, track);
    }

    deleteOne(trackId: number) {
        return this.http.delete(AppComponent.API_ROOT_TRACK + "/" + trackId);
    }

    findOne(trackId: number) {
        return this.http.get(AppComponent.API_ROOT_TRACK + "/findid/" + trackId);
    }
}
