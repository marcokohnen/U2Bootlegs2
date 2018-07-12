import {EventEmitter, Injectable, Output} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Track} from "./track";

@Injectable()
export class TrackService {

    private trackApiRoot="api/track";

    @Output() onTrackAdded = new EventEmitter<Track>();
    @Output() onTrackUpdated = new EventEmitter<Track>();

    constructor(private http : HttpClient) {}

    // voeg een nieuwe track toe aan een concert met concertId via http-request post naar ConcertController in de backend
    addOneToConcert(track: Track, concertId : number) {
        return this.http.post("api/concert/addtracktoconcert/"+ concertId, track);
    }

    // update een track via http-request naar Trackcontroller in de backend
    updateOne(track: Track) {
        return this.http.put(this.trackApiRoot+ "/" + track.id, track);
    }

    deleteOne(trackId: number) {
        return this.http.delete(this.trackApiRoot + "/" + trackId);
    }

    findOne(trackId: number) {
        return this.http.get(this.trackApiRoot + "/findid/" + trackId);
    }
}
