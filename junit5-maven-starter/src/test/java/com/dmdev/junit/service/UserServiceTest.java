package com.dmdev.junit.service;

import com.dmdev.junit.dto.User;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTest {

    private static final User IVAN = User.of(1, "Ivan", "123");
    private static final User PETR = User.of(2, "Petr", "111");
    private UserService userService;

    @BeforeAll
    void init() {
        System.out.println("BeforeAll :" + this);
    }

    @BeforeEach
    void prepare() {
        System.out.println("BeforeEach :" + this);
        userService = new UserService();
    }

    @Test
    void usersEmptyIfNoUsersAdded() {
        System.out.println("Test1 :" + this);
        List<User> users = userService.getAll();
        assertTrue(users.isEmpty(), ()->"User List should be empty");
    }

    @Test
    void usersSizeIfUserAdded() {
        System.out.println("Test2 :" + this);

        userService.add(IVAN);
        userService.add(PETR);
        List<User> users = userService.getAll();

        assertEquals(2, users.size());
    }

    @Test
    void loginSuccessIfUserExist() {
        userService.add(IVAN);

        Optional<User> maybeUser = userService.login(IVAN.getUsername(), IVAN.getPassword());

        assertTrue(maybeUser.isPresent());
        maybeUser.ifPresent(user -> assertEquals(IVAN, user));
    }

    @Test
    void loginFailedIfPasswordIsNotCorrect() {
        userService.add(IVAN);

        Optional<User> maybeUser = userService.login(IVAN.getUsername(), "dummy");

        assertTrue(maybeUser.isEmpty());
    }

    @Test
    void loginFailedIfUserDoesNotExist() {
        userService.add(IVAN);

        Optional<User> maybeUser = userService.login("dummy", IVAN.getPassword());

        assertTrue(maybeUser.isEmpty());
    }

    @AfterEach
    void deleteDataFromDataBase(){
        System.out.println("AfterEach :" + this);
    }

    @AfterAll
    void closeConnectionPull() {
        System.out.println("AfterAll :" + this);
    }
}
