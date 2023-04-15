package com.dmdev.junit.service;

import com.dmdev.junit.dto.User;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {

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

        userService.add(new User());
        userService.add(new User());
        List<User> users = userService.getAll();

        assertEquals(2, users.size());
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
