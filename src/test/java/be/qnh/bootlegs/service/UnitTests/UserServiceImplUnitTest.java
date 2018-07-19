package be.qnh.bootlegs.service.UnitTests;

import be.qnh.bootlegs.domain.AppUser;
import be.qnh.bootlegs.repository.UserRepository;
import be.qnh.bootlegs.service.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplUnitTest {

    // mock objects
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    // test objects
    private AppUser user1, user2, user3;

    //initialize test objects
    @Before
    public void init() {
        user1 = new AppUser("Flippe", "geheim", "flippie@ergens.com", "USER");
        user2 = new AppUser("Jopie", "wachtwoord", "jopie@gmail.com", "ADMIN");
        user3 = new AppUser("Frans", "onbekend", "frans@yahoo.com", "GUEST");
    }

    // test methods
    @Test
    public void testFindAppUserByEmail() {
        when(userRepository.findAppUserByEmailIgnoreCase("jopie@gmail.com")).thenReturn(user2);

        AppUser founduser = userService.findAppUserByEmailIgnoreCase("jopie@gmail.com");

        assertThat(founduser).isEqualTo(user2);
        assertThat(founduser.getRole()).isEqualTo("ADMIN");

        verify(userRepository, times(1)).findAppUserByEmailIgnoreCase("jopie@gmail.com");


    }
}