package be.qnh.bootlegs.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "CONCERTS")
public class Concert extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = -4389413702933759822L;

    // object fields
    @NotNull
    @JsonFormat(pattern = "dd-MM-yyyy")
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
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "Concert_ID", nullable = false)
    private List<Track> trackList = new ArrayList<>();

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
        if (trackList != null && !trackList.isEmpty()) {
            trackList.sort(Comparator.comparingInt(Track::getSequenceNr));
        }
        return trackList;
    }

    public void setTrackList(List<Track> trackList) {
        this.trackList = trackList;
    }

    // toString
    @Override
    public String toString() {
        return "Concert{" +
                "id=" + super.getId() +
                ", date=" + date +
                ", title='" + title + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", venue='" + venue + '\'' +
                ", trackList=" + trackList +
                '}';
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        //System.out.println("Entered Concert Equals");
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
