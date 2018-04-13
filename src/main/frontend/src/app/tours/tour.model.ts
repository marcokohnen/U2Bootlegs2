export class Tour {
    public id : number;
    public title : string;
    public startyear : number;
    public endyear : number;
    public leg : number;
    public continent : string;


    constructor(id: number, title: string, startyear: number, endyear: number, leg: number, continent: string) {
        this.id = id;
        this.title = title;
        this.startyear = startyear;
        this.endyear = endyear;
        this.leg = leg;
        this.continent = continent;
    }
}
