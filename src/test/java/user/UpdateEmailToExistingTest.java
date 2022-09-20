package user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import stellarburgres.user.User;
import stellarburgres.user.UserCredentials;

public class UpdateEmailToExistingTest extends BaseTest {

    UserCredentials firstUserCredentials;
    User firstUser;
    String firstUserToken;
    String secondUserToken;

    @Before
    public void setUp() {
        firstUser = userClient.getRandomUserTestData();
        firstUser = User.builder()
                .email(firstUser.getEmail())
                .password(firstUser.getPassword())
                .name(firstUser.getName())
                .build();

        userClient.registerWithCorrectUserData(firstUser);

        firstUserCredentials = UserCredentials.builder()
                .email(firstUser.getEmail())
                .password(firstUser.getPassword())
                .build();

        firstUserToken = userClient.loginWithCorrectCredentials(firstUserCredentials);
        userClient.logout(firstUserCredentials);
    }

    @After
    public void tearDown() {
        userClient.delete(firstUserToken);
        userClient.delete(secondUserToken);
    }

    //тест периодически падает с 429 Too Many Requests
    @Test
    @DisplayName("Update email to existing email with authorization returns 403")
    @Description("Response has message: \"User with such email already exists\". Response has status code 403.")
    public void updateEmailToExistingReturnsForbiddenTest() {
        User secondUser = userClient.getRandomUserTestData();
        secondUser = User.builder()
                .email(secondUser.getEmail())
                .password(secondUser.getPassword())
                .name(secondUser.getName())
                .build();
        userClient.registerWithCorrectUserData(secondUser);

        UserCredentials secondUserCredentials = UserCredentials.builder()
                .email(secondUser.getEmail())
                .password(secondUser.getPassword())
                .build();

        secondUserToken = userClient.loginWithCorrectCredentials(secondUserCredentials);

        secondUser.setEmail(firstUser.getEmail());
        userClient.changeUserEmailWithAuthWithExistingEmail(secondUserToken, secondUser);
    }

}
