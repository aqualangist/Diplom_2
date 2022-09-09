package order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import stellarburgres.order.OrderClient;
import stellarburgres.user.User;
import stellarburgres.user.UserClient;
import stellarburgres.user.UserCredentials;

import java.util.ArrayList;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class OrderListTest {

    OrderClient orderClient = new OrderClient();
    UserClient userClient = new UserClient();
    UserCredentials credentials;
    User user;
    String token;

    @Before
    public void setUp() {
        user = userClient.getRandomUserTestData();
        user = User.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .name(user.getName())
                .build();

        userClient.registerWithCorrectUserData(user);

        credentials = UserCredentials.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();

        token = userClient.loginWithCorrectCredentials(credentials);

    }

    @After
    public void tearDown() {
        userClient.delete(token);
    }

    @Test
    @DisplayName("Get order's list when user isn't authorized returns 401 Unauthorised")
    public void getOrderListWithoutAuthorizationTest() {
        userClient.logout(credentials);
        orderClient.getOrdersWithoutAuthorization();
    }

    //вынес ассерты в сам тест, потому что только таким образом получилось проверить, что массив заказов нового пользователя пуст
    @Test
    @DisplayName("Get order's list when user is authorized returns 200 OK")
    @Description("Register a new user. Authorize and get order list. " +
            "Check that order's list is empty because the user is new and has not any orders")
    public void getOrderListWithAuthorizationTest() {
        ValidatableResponse getOrderListResponse = orderClient.getOrdersWithAuthorization(token);
        int statusCode = getOrderListResponse.extract().statusCode();
        int total = getOrderListResponse.extract().path("total");
        ArrayList<String> ordersList = getOrderListResponse.extract().path("orders");

        assertThat("The status code is " + statusCode, statusCode, equalTo(SC_OK));
        assertThat("The total number is empty", total, notNullValue());
        assertThat("The user has orders", ordersList.isEmpty());
    }

}
