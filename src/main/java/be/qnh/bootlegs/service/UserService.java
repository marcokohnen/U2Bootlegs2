package be.qnh.bootlegs.service;

import be.qnh.bootlegs.domain.AppUser;

public interface UserService {

    AppUser addOne(AppUser appUser);

    AppUser findAppUserByName(String name);

    AppUser findAppUserByNameAndEmail(String name, String email);

    AppUser findAppUserByEmail(String email);
}
