package be.qnh.bootlegs.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Entity
public class Tour extends AbstractEntity {


    @NotNull
    private String title;
    @NotNull
    private int startyear;
    private int endYear;
    @NotNull
    private int leg;
    @NotNull
    private Continent continent;

    // field(s) with mapping(s)

    // One Tour has many concerts
    @OneToMany(cascade = CascadeType.ALL)
    private List<Concert> concertList;

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

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
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

    public List<Concert> getConcerts() {
        return concertList;
    }

    public void setConcerts(List<Concert> concerts) {
        this.concertList = concerts;
    }

    // toString
    @Override
    public String toString() {
        return "Tour{" +
                "title='" + title + '\'' +
                ", startyear=" + startyear +
                ", endYear=" + endYear +
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
                getEndYear() == tour.getEndYear() &&
                getLeg() == tour.getLeg() &&
                Objects.equals(getTitle(), tour.getTitle()) &&
                getContinent() == tour.getContinent();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getStartyear(), getEndYear(), getLeg(), getContinent());
    }
}
