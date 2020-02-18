package com.galvanize.controllers;

import com.galvanize.entities.User;
import com.galvanize.repositories.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {
    @Autowired
    MockMvc mvc;
    @Autowired
    UserRepo userRepo;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("validuser@email.com", "password", "password", "valid user");
        this.testUser = userRepo.save(testUser);
        assertNotNull(testUser);
    }

    /*
        GIVEN I am a user
        WHEN I send a POST request to the URI to login
        THEN I recieve a successful status code when I attempt to login
        */
    @Test
    void postLoginValidUser() throws Exception {
        String login = "{\"email\":\"validuser@email.com\",\"password\":\"password\"}";
        mvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(login))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }
    /*
    GIVEN I am not a registered user
    WHEN I send a POST request to the URI to login
    THEN I recieve a status code or json object indicating that I am not a registered user and therefore cannot be logged in
    */
    @Test
    void postLoginInvalidUser() throws Exception {
        String login = "{\"email\":\"validuser@email.com\",\"passwordx\":\"password\"}";
        mvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(login))
                .andExpect(status().isBadRequest());
    }
/*
    GIVEN I am a user
    WHEN I send a POST request to the URI to logout
    THEN I recieve a successful status code when I attempt to logout
    */
    @Test
    void postLogout() throws Exception {
        String logout = "{\"email\":\"validuser@email.com\"}";
        mvc.perform(post("/logout").content(logout).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
/*
    WHEN I send a post request to register with valid details
    THEN I am able to login to the site
     */

    @Test
    void postRegisterValidUser() throws Exception {
        String user = "{\"email\":\"999newuser@email.com999\", \"password\":\"password\", \"repeatPassword\":\"password\", \"screenName\":\"valid user\"}";
        mvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(user))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }
    /*
    WHEN I send a post request to register with invalid details
    THEN I am NOT able to login to the site
     */
    @Test
    void postRegisterInValidUser() throws Exception {
        String user = "{\"email\":\"999newuser@email.com999\", \"password\":\"passwordX\", \"repeatPassword\":\"password\", \"screenName\":\"valid user\"}";
        mvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(user))
                .andExpect(status().isBadRequest());
    }
    /*
    WHEN I send a post request with an existing email
    THEN I get a Bad Request response
     */
    @Test
    void postRegisterExistingEmail() throws Exception {
        String user = "{\"email\":\"validuser@email.com\", \"password\":\"password\", \"repeatPassword\":\"password\", \"screenName\":\"valid user\"}";
        mvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(user).header("clientMsg", "this is my message"))
                .andExpect(status().isBadRequest())
                .andExpect(header().string("errorMessage", "Email already exists"));

    }
}