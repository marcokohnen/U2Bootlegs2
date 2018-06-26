package be.qnh.bootlegs.service;

import be.qnh.bootlegs.domain.AppUser;

public interface UserService {

    AppUser findAppUserByNameAndEmail(String name, String email);

    AppUser findAppUserByEmail(String email);
}
