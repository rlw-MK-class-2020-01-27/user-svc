package com.galvanize.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    User user;

    @BeforeEach
    void setUp() {
        user = new User("user@email.com", "password", "password", "screen name");
        user.setId(99L);
    }

    @Test
    void noArgConstructor() {
        User user = new User();
        user.setId(99L);
        user.setScreenName("screen name");
        user.setRepeatPassword("password");
        user.setPassword("password");
        user.setEmail("user@email.com");
        assertTrue(this.user.equals(user));
        assertTrue(this.user.hashCode() == user.hashCode());
        assertEquals(this.user, user);
    }

    @Test
    void testToString() {
        String s = user.toString();
        assertNotNull(s);
        assertTrue(s.contains("screen name"));
        assertTrue(s.contains("user@email.com"));
    }

    @Test
    void getSetId() {
        user.setId(99L);
        assertEquals(99L, user.getId());
    }

    @Test
    void getEmail() {
        assertEquals("user@email.com", user.getEmail());
    }

    @Test
    void getPassword() {
        assertEquals("password", user.getPassword());
    }

    @Test
    void getRepeatPassword() {
        assertEquals("password", user.getRepeatPassword());
    }

    @Test
    void getScreenName() {
        assertEquals("screen name", user.getScreenName());
    }

}