package be.qnh.bootlegs.repository;

import be.qnh.bootlegs.domain.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {

    AppUser findAppUserByName(String name);

    AppUser findAppUserByNameAndEmail(String name, String email);

    AppUser findAppUserByEmail(String email);
}
