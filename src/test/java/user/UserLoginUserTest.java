package user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import stellarburgres.user.User;
import stellarburgres.user.UserCredentials;

public class UserLoginUserTest extends BaseUserTest {

    @Before
    public void setUp() {
        User user = userClient.getRandomUserTestData();
        user = User.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .name(user.getName())
                .build();

        userClient.registerWithCorrectUserData(user);

        userCredentials = UserCredentials.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

    @After
    public void tearDown() {
        String token = userClient.loginWithCorrectCredentials(userCredentials);
        userClient.delete(token);
    }

    //тесты периодически падают с кодом 429 Too Many Requests, если запускать пачкой
    //если запускать по одному, то проходят успешно - наставник посоветовал оставить так, таким образом подстветив проблему
    @Test
    @DisplayName("Login with correct data returns 200 OK")
    @Description("Register a new user. Login with correct data. " +
            "Expected results: " +
            "Status code - 200. Access and refresh tokens are not null values.")
    public void loginWithCorrectUserDataReturnsOkTest() {
        UserCredentials credentialsForLogin = UserCredentials.builder()
                .email(userCredentials.getEmail())
                .password(userCredentials.getPassword())
                .build();

        userClient.loginWithCorrectCredentials(credentialsForLogin);
    }

    @Test
    @DisplayName("Login with incorrect login returns 401 Unauthorized")
    @Description("Register a new user. Login with incorrect email. " +
            "Expected results: " +
            "Status code - 401. Message - email or password are incorrect.")
    public void loginWithIncorrectLoginReturnsUnauthorizedTest() {
        UserCredentials credentialsForLogin = UserCredentials.builder()
                .email(userCredentials.getEmail() + "x")
                .password(userCredentials.getPassword())
                .build();

        userClient.loginWithIncorrectCredentials(credentialsForLogin);
    }

    @Test
    @DisplayName("Login with incorrect password returns 401 Unauthorized")
    @Description("Register a new user. Login with incorrect password. " +
            "Expected results: " +
            "Status code - 401. Message - email or password are incorrect.")
    public void loginWithIncorrectPasswordReturnsUnauthorizedTest() {
        UserCredentials credentialsForLogin = UserCredentials.builder()
                .email(userCredentials.getEmail())
                .password(userCredentials.getPassword() + "x")
                .build();

        userClient.loginWithIncorrectCredentials(credentialsForLogin);
    }

}
