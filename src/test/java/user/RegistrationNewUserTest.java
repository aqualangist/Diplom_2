package user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Test;
import stellarburgres.user.User;
import stellarburgres.user.UserClient;
import stellarburgres.user.UserCredentials;

public class RegistrationNewUserTest {

    UserClient userClient = new UserClient();
    User credentials;
    User user;

    @After
    public void tearDown() {
        UserCredentials credentials = UserCredentials.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();

        String token = userClient.loginWithCorrectCredentials(credentials);
        userClient.delete(token);
    }

    //тесты периодически падают с кодом 429 Too Many Requests, если запускать пачкой
    @Test
    @DisplayName("Create a new user with correct data returns 200")
    @Description("Registration of a new user returns status code 200")
    public void createNewUserReturnsOkTest() {
        credentials = userClient.getRandomUserTestData();
        user = User.builder()
                .email(credentials.getEmail())
                .password(credentials.getPassword())
                .name(credentials.getName())
                .build();

        userClient.registerWithCorrectUserData(user);
    }

    @Test
    @DisplayName("Create an existing user returns 403")
    @Description("Registration with existing data returns status code 403 and message:\"User already exists\"")
    public void createExistingUserReturnsForbiddenTest() {
        credentials = userClient.getRandomUserTestData();
        user = User.builder()
                .email(credentials.getEmail())
                .password(credentials.getPassword())
                .name(credentials.getName())
                .build();

        userClient.registerWithCorrectUserData(user);
        userClient.registerWithExistingUserData(user);
    }

}
