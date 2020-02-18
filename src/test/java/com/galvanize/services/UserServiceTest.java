package com.galvanize.services;

import com.galvanize.entities.User;
import com.galvanize.repositories.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepo userRepo;

    User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("user@email.com", "password", "password", "screen name");
        this.testUser = userRepo.saveAndFlush(testUser);
        assertNotNull(testUser);
        assertNotNull(testUser.getId());
    }

    /*
        GIVEN I am a user
        WHEN I send a POST request to the URI to login
        THEN I recieve a successful status code when I attempt to login
        */
    @Test
    void login() {
        User user = userService.login("user@email.com", "password");
        assertNotNull(user, "userService should return the user");
        assertEquals(testUser, user);
    }
    /*
    GIVEN I am not a registered user
    WHEN I send a POST request to the URI to login
    THEN I recieve a status code or json object indicating that I am not a registered user and therefore cannot be logged in
    */
    @Test
    void loginInvalidUser() {
        assertThrows(RuntimeException.class, () -> {
            userService.login("999999999999", "999999999999");
        });
    }

    /*
    GIVEN I am a user
    WHEN I send a POST request to the URI to logout
    THEN I recieve a successful status code when I attempt to logout
    */
    @Test
    void logoutValidUser() {
        int logout = userService.logout(testUser);
        assertEquals(HttpStatus.OK.value(), logout);
    }
    @Test
    void logoutInvalidUser() {
        int logout = userService.logout(new User());
        assertNotEquals(HttpStatus.OK, logout);
        assertEquals(400, logout);
    }

    /*
    WHEN I send a post request to register with valid details
    THEN I am able to login to the site
     */
    @Test
    void registerValidUser() {
        User user = new User("valid@email.com", "password", "password", "validUser");
        user = userService.register(user);
        assertNotNull(user.getId());
        User user2 = userService.login("valid@email.com", "password");
        assertTrue(user.equals(user2));

    }

    /*
    WHEN I send a post request to register with invalid details
    THEN I am NOT able to login to the site
     */
    @Test
    void registerInValidUser() {
        User user = new User("invalid@email.com", "password", "drowssap", "InvalidUser");
        assertThrows(RuntimeException.class, () -> {
            userService.register(user);
        });
    }

    @Test
    void registerEmailExists() {
        assertThrows(RuntimeException.class, () -> {
            userService.register(testUser);
        });
    }

    @Test
    void userExists() {
        assertTrue(userService.emailExists(testUser.getEmail()));
        assertFalse(userService.emailExists("9999999999999999999"));
    }
}