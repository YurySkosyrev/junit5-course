package com.dmdev.junit.service;

import com.dmdev.junit.dto.User;
import com.dmdev.junit.paramresolver.UserServiceParamResolver;
import org.hamcrest.MatcherAssert;
import org.hamcrest.collection.IsMapContaining;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.jupiter.api.Assertions.*;

@Tag("fast")
@Tag("user")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@ExtendWith(
        UserServiceParamResolver.class
)
public class UserServiceTest {

    private static final User IVAN = User.of(1, "Ivan", "123");
    private static final User PETR = User.of(2, "Petr", "111");
    private UserService userService;

    UserServiceTest(TestInfo testInfo) {
        System.out.println();
    }

    @BeforeAll
    void init() {
        System.out.println("BeforeAll :" + this);
    }

    @BeforeEach
    void prepare(UserService userService) {
        System.out.println("BeforeEach :" + this);
        this.userService = userService;
    }

    @Test
    void usersEmptyIfNoUsersAdded(UserService userService) {
        System.out.println("Test1 :" + this);
        List<User> users = userService.getAll();

        MatcherAssert.assertThat(users, empty());
        assertTrue(users.isEmpty(), ()->"User List should be empty");
    }

    @Test
    @DisplayName("users size if user added")
    void usersSizeIfUserAdded() {
        System.out.println("Test2 :" + this);

        userService.add(IVAN);
        userService.add(PETR);
        List<User> users = userService.getAll();

        assertThat(users).hasSize(2);
//        assertEquals(2, users.size());
    }



    @Test
    void usersConvertedToMapById(){
        userService.add(IVAN, PETR);

        Map<Integer, User> users = userService.getAllConvertedById();

        MatcherAssert.assertThat(users, IsMapContaining.hasKey(IVAN.getId()));

        assertAll(
                () -> assertThat(users).containsValues(IVAN, PETR),
                () -> assertThat(users).containsKeys(IVAN.getId(), PETR.getId())
        );
    }



    @AfterEach
    void deleteDataFromDataBase(){
        System.out.println("AfterEach :" + this);
    }

    @AfterAll
    void closeConnectionPull() {
        System.out.println("AfterAll :" + this);
    }

    @TestMethodOrder(MethodOrderer.DisplayName.class)
    @Nested
    @DisplayName("test user login functionality")
    @Tag("login")
    class LoginTest {

        @Test
        void loginSuccessIfUserExist() {
            userService.add(IVAN);

            Optional<User> maybeUser = userService.login(IVAN.getUsername(), IVAN.getPassword());

            assertThat(maybeUser).isPresent();
            maybeUser.ifPresent(user -> assertThat(user).isEqualTo(IVAN));
//        assertTrue(maybeUser.isPresent());
//        maybeUser.ifPresent(user -> assertEquals(IVAN, user));
        }

        @Test
        void throwExceptionIfUserNameOrPasswordIsNull() {
            assertAll(
                    () -> {
                        IllegalArgumentException exc = assertThrows(IllegalArgumentException.class, () -> userService.login(null, "dummy"));
                        assertThat(exc.getMessage()).isEqualTo("username or password is null");
                    },
                    () -> assertThrows(IllegalArgumentException.class, () -> userService.login("dummy", null))
            );

//        try {
//            userService.login(null, "dummy");
//            fail("login should throw exception on null username");
//        } catch (IllegalArgumentException ex) {
//            assertTrue(true);
//        }
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
    }
}
