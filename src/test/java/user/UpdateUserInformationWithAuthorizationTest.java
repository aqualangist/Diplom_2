package user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import stellarburgres.user.User;
import stellarburgres.user.UserCredentials;

public class UpdateUserInformationWithAuthorizationTest extends BaseTest {

    @Before
    public void setUp() {
        user = userClient.getRandomUserTestData();
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

        token = userClient.loginWithCorrectCredentials(userCredentials);
    }

    @After
    public void tearDown() {
        userClient.delete(token);
    }

    //тесты иногда падают с 429 Too many requests при запуске пачкой
    @Test
    @DisplayName("Update username with authorization returns 200")
    @Description("User's name should be equal to set value. Response has status code 200.")
    public void updateUserNameWithAuthorizationReturnsOkTest() {
        user.setName(RandomStringUtils.randomAlphabetic(15));
        userClient.changeUserNameWithAuthorization(token, user);
    }

    @Test
    @DisplayName("Update user's email with authorization returns 200")
    @Description("User's email should be equal to set value. Response has status code 200.")
    public void updateUserEmailWithAuthorizationReturnsOkTest() {
        user.setEmail(RandomStringUtils.randomAlphabetic(15).toLowerCase() + "@email.com");
        userClient.changeUserEmailWithAuthorization(token, user);
    }


}
