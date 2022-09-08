package stellarburgres.user;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.apache.commons.lang3.RandomStringUtils;
import stellarburgres.RestClient;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserClient  extends RestClient {

    private final String AUTH_REGISTER = "/auth/register";
    private final String AUTH_LOGIN = "/auth/login";
    private final String AUTH_LOGOUT = "/auth/logout";
    private final String AUTH_USER = "/auth/user";

    @Step("Create random credentials")
    public User getRandomUserTestData() {
        final String email = RandomStringUtils.randomAlphabetic(5).toLowerCase() + "@email.com";
        final String password = RandomStringUtils.randomAlphabetic(5);
        final String name = RandomStringUtils.randomAlphabetic(5);

        Allure.addAttachment("Email: ", email);
        Allure.addAttachment("Password: ", password);
        Allure.addAttachment("Username: ", name);

        return new User(email, password, name);
    }

    @Step("Register a new user with correct data successfully")
    public void registerWithCorrectUserData(User user) {
        RestAssured.given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(AUTH_REGISTER)
                .then()
                .assertThat()
                .body("success", equalTo(true))
                .body("user", notNullValue())
                .statusCode(SC_OK);
    }

    @Step("Register a new user with existing unsuccessfully")
    public void registerWithExistingUserData(User user) {
        RestAssured.given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(AUTH_REGISTER)
                .then()
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"))
                .statusCode(SC_FORBIDDEN);
    }


    @Step("Register a new user with missing required field unsuccessfully")
    public void registerWithMissingField(User user) {
        RestAssured.given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(AUTH_REGISTER)
                .then()
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"))
                .statusCode(SC_FORBIDDEN);
    }

    @Step("Login with correct credentials, returns status code = 200")
    public String loginWithCorrectCredentials(UserCredentials credentials) {
        return RestAssured.given()
                .spec(getBaseSpec())
                .body(credentials)
                .when()
                .post(AUTH_LOGIN)
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .extract()
                .path("accessToken")
                .toString()
                .substring(7);
    }

    @Step("Login with incorrect credentials, returns status code = 401")
    public void loginWithIncorrectCredentials(UserCredentials credentials) {
        RestAssured.given()
                .spec(getBaseSpec())
                .body(credentials)
                .when()
                .post(AUTH_LOGIN)
                .then()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Step("Log out")
    public void logout(UserCredentials credentials) {
        Token refreshToken = new Token(getRefreshToken(credentials));

        RestAssured.given()
                .spec(getBaseSpec())
                .body(refreshToken)
                .when()
                .post(AUTH_LOGOUT)
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("message", equalTo("Successful logout"));
    }

    public String getRefreshToken(UserCredentials credentials) {
        return RestAssured.given()
                .spec(getBaseSpec())
                .body(credentials)
                .when()
                .post(AUTH_LOGIN)
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .extract()
                .path("refreshToken");
    }

    @Step("Change user's name with authorization")
    public void changeUserNameWithAuthorization(String token, User user) {
        RestAssured.given()
                .spec(getBaseSpec())
                .auth().oauth2(token)
                .and()
                .body(user)
                .when()
                .patch(AUTH_USER)
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("user", notNullValue())
                .body("user.name", equalTo(user.getName()));
    }

    @Step("Change user's email with authorization")
    public void changeUserEmailWithAuthorization(String token, User user) {
        RestAssured.given()
                .spec(getBaseSpec())
                .auth().oauth2(token)
                .and()
                .body(user)
                .when()
                .patch(AUTH_USER)
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("user", notNullValue())
                .body("user.email", equalTo(user.getEmail()))
        ;
    }

    @Step("Change user's email to existing email")
    public void changeUserEmailWithAuthWithExistingEmail(String token, User user) {
        RestAssured.given()
                .spec(getBaseSpec())
                .auth().oauth2(token)
                .and()
                .body(user)
                .when()
                .patch(AUTH_USER)
                .then()
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("User with such email already exists"));
    }

    @Step("Change user's name without authorization")
    public void changeUserNameWithoutAuthorization(User user) {
        RestAssured.given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .patch(AUTH_USER)
                .then()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Step("Change user's email without authorization")
    public void changeUserEmailWithoutAuthorization(User user) {
        RestAssured.given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .patch(AUTH_USER)
                .then()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Step("Delete user")
    public void delete(String token) {
        RestAssured.given()
                .spec(getBaseSpec())
                .auth().oauth2(token)
                .when()
                .delete(AUTH_USER)
                .then()
                .assertThat()
                .statusCode(SC_ACCEPTED)
                .body("success", equalTo(true))
                .body("message", equalTo("User successfully removed"));
    }

}
