package com.dmdev.junit.service;

import com.dmdev.junit.TestBase;
import com.dmdev.junit.dao.UserDao;
import com.dmdev.junit.dto.User;
import com.dmdev.junit.extension.*;
import org.hamcrest.MatcherAssert;
import org.hamcrest.collection.IsMapContaining;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.jupiter.api.Assertions.*;

@Tag("fast")
@Tag("user")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@ExtendWith(
        {UserServiceParamResolver.class,
                PostProcessingExtension.class,
                ConditionalExtension.class,
                MockitoExtension.class
//                ThrowableExtension.class
//                GlobalExtension.class
        }
)
public class UserServiceTest extends TestBase {

    private static final User IVAN = User.of(1, "Ivan", "123");
    private static final User PETR = User.of(2, "Petr", "111");

    @Captor
    private ArgumentCaptor<Integer> argumentCaptor;
    @Mock(lenient = true)
    private UserDao userDao;
    @InjectMocks
    private UserService userService;

    UserServiceTest(TestInfo testInfo) {
        System.out.println();
    }

    @BeforeAll
    void init() {
        System.out.println("BeforeAll :" + this);
    }

    @BeforeEach
    void prepare() {
        System.out.println("BeforeEach :" + this);
        Mockito.doReturn(true).when(userDao).delete(IVAN.getId());

//        this.userDao = Mockito.spy(UserDao.class);
//        this.userService = new UserService(userDao);
    }

    @Test
    void throwExceptionIfDataBaseIsNotAvailable() {
        Mockito.doThrow(RuntimeException.class).when(userDao).delete(IVAN.getId());

        assertThrows(RuntimeException.class, () -> userService.delete(IVAN.getId()));
    }

    @Test
    void shouldDeleteExistedUser() {

        userService.add(IVAN);

//        Mockito.doReturn(true).when(userDao).delete(IVAN.getId());

//        Mockito.when(userDao.delete(2))
//                .thenReturn(true)
//                .thenReturn(false);

        boolean deleteResult = userService.delete(IVAN.getId());
        System.out.println(userService.delete(IVAN.getId()));
        System.out.println(userService.delete(IVAN.getId()));

        assertThat(deleteResult).isTrue();

//        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(userDao, Mockito.times(3)).delete(argumentCaptor.capture());

        assertThat(argumentCaptor.getValue()).isEqualTo(1);
    }

    @Test
    @Order(1)
    @Disabled("users will be empty if user not added")
    void usersEmptyIfNoUsersAdded(UserService userService) throws IOException {
        if (true) {
            throw new RuntimeException();
        }
        System.out.println("Test1 :" + this);
        List<User> users = userService.getAll();

        MatcherAssert.assertThat(users, empty());
        assertTrue(users.isEmpty(), () -> "User List should be empty");
    }

    //    @Test
    @DisplayName("users size if user added")
    @RepeatedTest(value = 5, name = RepeatedTest.LONG_DISPLAY_NAME)
    void usersSizeIfUserAdded() {
        System.out.println("Test2 :" + this);

        userService.add(IVAN);
        userService.add(PETR);
        List<User> users = userService.getAll();

        assertThat(users).hasSize(2);
//        assertEquals(2, users.size());
    }

    @Test
    void usersConvertedToMapById() {
        userService.add(IVAN, PETR);

        Map<Integer, User> users = userService.getAllConvertedById();

        MatcherAssert.assertThat(users, IsMapContaining.hasKey(IVAN.getId()));

        assertAll(
                () -> assertThat(users).containsValues(IVAN, PETR),
                () -> assertThat(users).containsKeys(IVAN.getId(), PETR.getId())
        );
    }

    @AfterEach
    void deleteDataFromDataBase() {
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
    @Timeout(value = 200, unit = TimeUnit.MILLISECONDS)
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

        @Test
        void checkLoginFunctionalityPerformance() {
            Optional<User> maybeUser = assertTimeout(Duration.ofMillis(200L), () -> {
                Thread.sleep(100L);
                return userService.login("dummy", IVAN.getPassword());
            });
        }

        @ParameterizedTest(name = "{arguments} test")
//        @NullSource
//        @EmptySource
//        @NullAndEmptySource
//        @ValueSource(strings = {
//                "Ivan", "Petr"
//        })
        @MethodSource("com.dmdev.junit.service.UserServiceTest#getArgumentsForLoginUser")
//        @CsvFileSource(resources = "/login-test-data.csv", delimiter = ',', numLinesToSkip = 1)
//        @CsvSource({
//                "Ivan,123",
//                "Petr,111"
//        })
        void loginParametrizedTest(String username, String password, Optional<User> user) {
            userService.add(IVAN, PETR);

            Optional<User> maybeUser = userService.login(username, password);
            assertThat(maybeUser).isEqualTo(user);

        }
    }

    static Stream<Arguments> getArgumentsForLoginUser() {
        return Stream.of(
                Arguments.of("Ivan", "123", Optional.of(IVAN)),
                Arguments.of("Petr", "111", Optional.of(PETR)),
                Arguments.of("Ivan", "dummy", Optional.empty()),
                Arguments.of("dummy", "123", Optional.empty())
        );
    }
}
