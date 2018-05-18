package be.qnh.bootlegs.repository;

import be.qnh.bootlegs.domain.Concert;
import be.qnh.bootlegs.domain.Continent;
import be.qnh.bootlegs.domain.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long> {


    Iterable<Tour> findByTitleLikeIgnoreCase(String keyWord);

    Iterable<Tour> findByStartyearGreaterThanEqual(int startYear);

    Iterable<Tour> findByStartyearEquals(int startyear);

    Iterable<Tour> findByContinentEquals(Continent continent);

}
