export class Track {

    id : number;
    sequenceNr : number;
    title : string;
    locationUrl : string;


    constructor(id: number, sequenceNr: number, title: string, locationUrl: string) {
        this.id = id;
        this.sequenceNr = sequenceNr;
        this.title = title;
        this.locationUrl = locationUrl;
    }
}
