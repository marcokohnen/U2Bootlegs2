import {Injectable} from "@angular/core";
import {Tour} from "../tours/tour.model";
import {Concert} from "../concerts/concert.model";
import {Track} from "../tracks/track";

@Injectable()
export class AppData {

    tourObjectStorage : Tour;
    tourIdStorage : number;

    concertObjectStorage : Concert;
    concertIdStorage : number;

    trackObjectStorage : Track;
}
