package stellarburgres.order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import stellarburgres.RestClient;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestClient {

    private final String ORDERS = "/orders";
    private final String INGREDIENTS = "/ingredients";

    @Step("Get ingredient from the response array")
    public String getIngredient(int index) {
        String path = "data[" + index + "]._id";
        return given()
                .spec(getBaseSpec())
                .when()
                .get(INGREDIENTS)
                .then()
                .extract()
                .body()
                .path(path).toString();
    }

    @Step("Making an order with ingredients by authorized user")
    public void makeOrderWithIngredientsWithAuthorization(Order order, String token) {
        given()
                .spec(getBaseSpec())
                .auth().oauth2(token)
                .body(order)
                .when()
                .post(ORDERS)
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("order.ingredients", notNullValue())
                .body("order.number", notNullValue());
    }

    @Step("Making an order with ingredients by unauthorized user")
    public void makeOrderWithIngredientsWithoutAuthorization(Order order) {
        given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(ORDERS)
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Step("Making an order without ingredients by authorized user")
    public void makeOrderWithoutIngredientsWithAuthorization(Order order, String token) {
        given()
                .spec(getBaseSpec())
                .auth().oauth2(token)
                .body(order)
                .when()
                .post(ORDERS)
                .then()
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Step("Making an order without ingredient by unauthorized user")
    public void makeOrderWithoutIngredientsWithoutAuthorization(Order order) {
        given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(ORDERS)
                .then().log().ifStatusCodeIsEqualTo(SC_OK)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Step("Making an order with invalid ingredient's hash by authorized user")
    public void makeOrderWithInvalidIngredientHashWithAuthorization(Order order, String token) {
        given()
                .spec(getBaseSpec())
                .auth().oauth2(token)
                .body(order)
                .when()
                .post(ORDERS)
                .then().log().ifStatusCodeIsEqualTo(SC_OK)
                .assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @Step("Making an order with invalid ingredient's hash by unauthorized user")
    public void makeOrderWithInvalidIngredientHashWithoutAuthorization(Order order) {
        given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(ORDERS)
                .then().log().ifStatusCodeIsEqualTo(SC_OK)
                .assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @Step("Get user's orders with authorization")
    public ValidatableResponse getOrdersWithAuthorization(String token) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(token)
                .when()
                .get(ORDERS)
                .then();
    }

    @Step("Get user's orders without authorization")
    public void getOrdersWithoutAuthorization() {
        given()
                .spec(getBaseSpec())
                .when()
                .get(ORDERS)
                .then()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }


}
