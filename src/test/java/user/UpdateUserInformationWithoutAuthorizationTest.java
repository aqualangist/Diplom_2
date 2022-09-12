package user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import stellarburgres.user.User;
import stellarburgres.user.UserClient;

public class UpdateUserInformationWithoutAuthorizationTest {

    UserClient userClient = new UserClient();

    @Test
    @DisplayName("Update username with authorization returns 401")
    @Description("Response has status code 401. Message: \"You should be authorised\"")
    public void updateNameWithAuthorizationReturnsUnauthorizedTest() {
        User user = User.builder().name(RandomStringUtils.randomAlphabetic(15)).build();
        userClient.changeUserNameWithoutAuthorization(user);
    }

    @Test
    @DisplayName("Update user's email with authorization returns 401")
    @Description("Response has status code 401. Message: \"You should be authorised\"")
    public void updateEmailWithAuthorizationReturnsUnauthorizedTest() {
        User user = User.builder().email(RandomStringUtils.randomAlphabetic(15).toLowerCase() + "@email.com").build();
        userClient.changeUserEmailWithoutAuthorization(user);
    }

}
