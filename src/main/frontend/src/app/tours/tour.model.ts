import {Concert} from "../concerts/concert.model";

enum AppColors {
    blue,
    yellow
}

export class Tour {
    //access-modifiers van membervariabelen moeten altijd public zijn anders kan Angular ze niet gebruiken voor binding in html
    public id : number;
    public title : string;
    public startyear : number;
    public endyear : number;
    public leg : number;
    public continent : string;
    public concertList : Concert[] = [];
    public testColors : AppColors;

    constructor(id : number, title: string, startyear: number, endyear: number, leg: number, continent: string, concertList : any[] ) {
        this.id = id;
        this.title = title;
        this.startyear = startyear;
        this.endyear = endyear;
        this.leg = leg;
        this.continent = continent;
        this.concertList = concertList;
    }

    toString() {
        return this.id + "," + this.title + "," + this.leg + "," + this.continent + "," + this.startyear + "," + this.endyear;
    }
}
