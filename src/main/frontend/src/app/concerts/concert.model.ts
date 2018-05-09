export class Concert {

    id : number;
    date : Date;
    title : string;
    country : string;
    city : string;
    venue : string;
    quality : string;
    trackList = [];


    constructor(id: number, date: Date, title: string, country: string, city: string, venue: string, recordingQuality: string, trackList: any[]) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.country = country;
        this.city = city;
        this.venue = venue;
        this.quality = recordingQuality;
        this.trackList = trackList;
    }
}
