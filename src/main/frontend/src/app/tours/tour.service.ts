import "rxjs/add/operator/map";
import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class TourService {

    //voeg http toe via dependency injection
    constructor(private http: HttpClient) {

    }

    //vraag alle tours op via http-request naar TourController in backend
    findAll() {
        return this.http.get("/api/tour/findall");
    }
}
