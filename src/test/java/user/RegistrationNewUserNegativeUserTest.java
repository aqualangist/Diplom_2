package user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import stellarburgres.user.User;

public class RegistrationNewUserNegativeUserTest extends BaseUserTest {

    @Test
    @DisplayName("Create new user without name returns 403")
    @Description("Registration without name returns status code 403 Forbidden and message:\"Email, password and name are required fields\"")
    public void createUserWithoutNameReturnsForbiddenTest() {
        credentials = userClient.getRandomUserTestData();
        user = User.builder()
                .email(credentials.getEmail())
                .password(credentials.getPassword())
                .build();

        userClient.registerWithMissingField(user);
    }

    @Test
    @DisplayName("Create new user without email returns 403")
    @Description("Registration without email returns status code 403 Forbidden and message:\"Email, password and name are required fields\"")
    public void createUserWithoutEmailReturnsForbiddenTest() {
        credentials = userClient.getRandomUserTestData();
        user = User.builder()
                .password(credentials.getPassword())
                .name(credentials.getName())
                .build();

        userClient.registerWithMissingField(user);
    }

    @Test
    @DisplayName("Create new user without password field returns 403")
    @Description("Registration without password returns status code 403 Forbidden and message:\"Email, password and name are required fields\"")
    public void createUserWithoutPasswordReturnsForbiddenTest() {
        credentials = userClient.getRandomUserTestData();
        user = User.builder()
                .email(credentials.getEmail())
                .name(credentials.getName())
                .build();

        userClient.registerWithMissingField(user);
    }


}
