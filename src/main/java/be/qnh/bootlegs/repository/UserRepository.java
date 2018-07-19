package be.qnh.bootlegs.repository;

import be.qnh.bootlegs.domain.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {

    AppUser findAppUserByEmailIgnoreCase(String email);
}
