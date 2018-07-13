package be.qnh.bootlegs.controller;

import be.qnh.bootlegs.domain.AppUser;
import be.qnh.bootlegs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /*
        @PostMapping
         /api/user : add one User

        @DeleteMapping
         /api/user/{id}
    */

    @RequestMapping("/login")
    public Principal getUser(Principal user) {
        return user;
    }

    @PostMapping
    public ResponseEntity<AppUser> addOne(@RequestBody AppUser user) {
        AppUser newUser = userService.addOne(user);
        if (newUser == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(newUser, HttpStatus.OK);
        }
    }

    @DeleteMapping
    public ResponseEntity<AppUser> deleteOne(@PathVariable Long id) {
        AppUser deletedUser = userService.deleteOneById(id);
        if (deletedUser == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(deletedUser, HttpStatus.OK);
        }

    }
}
