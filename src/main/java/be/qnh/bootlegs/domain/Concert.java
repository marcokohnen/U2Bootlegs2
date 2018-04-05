package be.qnh.bootlegs.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
public class Concert extends AbstractEntity {

    // object fields
    @NotNull
    private LocalDate date;
    @NotNull
    private String title;
    private String country;
    private String city;
    private String venue;
    @Enumerated(EnumType.STRING)
    private RecordingQuality quality;

    // field(s) with mapping(s)

    // One Concert has many Tracks
    @OneToMany(cascade = CascadeType.ALL)
    private List<Track> trackList;

    // constructor
    public Concert() {
    }

    // getters and setters
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public RecordingQuality getQuality() {
        return quality;
    }

    public void setQuality(RecordingQuality quality) {
        this.quality = quality;
    }

    public List<Track> getTrackList() {
        return trackList;
    }

    public void setTrackList(List<Track> trackList) {
        this.trackList = trackList;
    }

    // toString
    @Override
    public String toString() {
        return "Concert{" +
                "date=" + date +
                ", title='" + title + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", venue='" + venue + '\'' +
                '}';
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Concert)) return false;
        Concert concert = (Concert) o;
        return Objects.equals(getDate(), concert.getDate()) &&
                Objects.equals(getTitle(), concert.getTitle()) &&
                Objects.equals(getCountry(), concert.getCountry()) &&
                Objects.equals(getCity(), concert.getCity()) &&
                Objects.equals(getVenue(), concert.getVenue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDate(), getTitle(), getCountry(), getCity(), getVenue());
    }
}
