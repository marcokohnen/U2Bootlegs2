package be.qnh.bootlegs.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sun.istack.internal.Nullable;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "TOUR")
public class Tour extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 5549479803124945766L;

    @NotNull
    @Size(min = 4, max = 35)
    private String title;
    @Min(1978)
    private int startyear;
    private int endyear;
    @Min(1)
    @Max(6)
    private int leg;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Continent continent;

    // field(s) with mapping(s)

    // One Tour has many concerts
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "TOUR_ID", nullable = false)
    //@Joincolumn zorgt voor een foreignkey in de tabel concert, zonder deze annotatie wordt er een tussentabel Tour_Concerts gemaakt
    private List<Concert> concertList = new ArrayList<>();

    // constructor
    public Tour() {
    }

    // getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStartyear() {
        return startyear;
    }

    public void setStartyear(int startyear) {
        this.startyear = startyear;
    }

    public int getEndyear() {
        return endyear;
    }

    public void setEndyear(int endyear) {
        this.endyear = endyear;
    }

    public int getLeg() {
        return leg;
    }

    public void setLeg(int leg) {
        this.leg = leg;
    }

    public Continent getContinent() {
        return continent;
    }

    public void setContinent(Continent continent) {
        this.continent = continent;
    }

    public List<Concert> getConcertList() {
        Collections.sort(concertList, Comparator.comparing(Concert::getDate));
        return concertList;
    }

    public void setConcertList(List<Concert> concerts) {
        this.concertList = concerts;
    }

    // toString
    @Override
    public String toString() {
        return "Tour{" +
                "title='" + title + '\'' +
                ", startyear=" + startyear +
                ", endYear=" + endyear +
                ", leg=" + leg +
                ", continent=" + continent +
                '}';
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tour)) return false;
        Tour tour = (Tour) o;
        return getStartyear() == tour.getStartyear() &&
                getEndyear() == tour.getEndyear() &&
                getLeg() == tour.getLeg() &&
                Objects.equals(getTitle(), tour.getTitle()) &&
                getContinent() == tour.getContinent();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getStartyear(), getEndyear(), getLeg(), getContinent());
    }
}
