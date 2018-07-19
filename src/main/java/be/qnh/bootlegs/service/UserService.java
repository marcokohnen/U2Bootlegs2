package be.qnh.bootlegs.service;

import be.qnh.bootlegs.domain.AppUser;

public interface UserService {

    AppUser addOne(AppUser appUser);

    AppUser findAppUserByEmailIgnoreCase(String email);

    AppUser deleteOneById(Long id);
}
