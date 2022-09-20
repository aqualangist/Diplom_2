package order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import stellarburgres.order.Order;
import stellarburgres.user.User;
import stellarburgres.order.Ingredient;
import stellarburgres.user.UserCredentials;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OrderWithAuthorizationTest extends BaseOrderTest {

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
    @DisplayName("Make order without ingredients by authorized user, returns 400 Bad Request")
    @Description("Register and log in new user. Make order without ingredients. " +
            "Response should has status code is 400 and message: \"Ingredient ids must be provided\".")
    public void makeOrderWithoutIngredientsAuthorizedReturns400Test() {
        Order order = Order.builder().build();
        orderClient.makeOrderWithoutIngredientsWithAuthorization(order, token);
    }

    @Test
    @DisplayName("Make order with one valid ingredient by authorized user, returns 200 OK")
    @Description("Register and log in new user. Make order with one valid hash of ingredient. " +
            "Response should has status code is 200, order's number is not null.")
    public void makeOrderWithOneValidIngredientAuthorizedReturns200Test() {
        int index = new Random().nextInt(5);
        String ingredientHash = orderClient.getIngredient(index);

        List<Ingredient> ingredientsList = new ArrayList<>();
        ingredientsList.add(Ingredient.builder()._id(ingredientHash).build());

        Order order = Order.builder().ingredients(ingredientsList).build();
        orderClient.makeOrderWithIngredientsWithAuthorization(order, token);
    }

    @Test
    @DisplayName("Make order with invalid ingredient by authorized user, returns 500 Internal Server Error")
    @Description("Register and log in new user. Make order with one invalid hash of ingredient. " +
            "Response should has status code is 500.")
    public void makeOrderWithInvalidIngredientAuthorizedReturns500Test() {
        int index = new Random().nextInt(5);
        String invalidIngredientHash = orderClient.getIngredient(index) + "j";

        List<Ingredient> ingredientsList = new ArrayList<>();
        ingredientsList.add(Ingredient.builder()._id(invalidIngredientHash).build());

        Order order = Order.builder().ingredients(ingredientsList).build();
        orderClient.makeOrderWithInvalidIngredientHashWithAuthorization(order, token);
    }

    @Test
    @DisplayName("Make order with more than one valid ingredients by authorized user, returns 200 OK")
    @Description("Register and log in new user. Make order with invalid hashes of ingredients. " +
            "Response should has status code is 200 and order's number is not null.")
    public void makeOrderWithValidIngredientsAuthorizedReturns200Test() {
        int firstIndex = new Random().nextInt(5);
        String firstIngredientHash = orderClient.getIngredient(firstIndex);

        int secondIndex = new Random().nextInt(5);
        String secondIngredientHash = orderClient.getIngredient(secondIndex);

        List<Ingredient> ingredientsList = new ArrayList<>();
        ingredientsList.add(Ingredient.builder()._id(firstIngredientHash).build());
        ingredientsList.add(Ingredient.builder()._id(secondIngredientHash).build());

        Order order = Order.builder().ingredients(ingredientsList).build();
        orderClient.makeOrderWithIngredientsWithAuthorization(order, token);
    }

    @Test
    @DisplayName("Make order with two ingredients, one of them with invalid hash by authorized user, " +
            "returns 500 Internal Server Error")
    @Description("Register and log in new user. Make order with two ingredients - valid and invalid hashes. " +
            "Response should has status code is 500.")
    public void makeOrderWithInvalidIngredientsUnauthorizedReturns500Test() {
        int index = new Random().nextInt(5);
        String validIngredientHash = orderClient.getIngredient(index);
        String invalidIngredientHash = orderClient.getIngredient(index) + "z";

        List<Ingredient> ingredientsList = new ArrayList<>();
        ingredientsList.add(Ingredient.builder()._id(invalidIngredientHash).build());
        ingredientsList.add(Ingredient.builder()._id(validIngredientHash).build());

        Order order = Order.builder().ingredients(ingredientsList).build();
        orderClient.makeOrderWithInvalidIngredientHashWithAuthorization(order, token);
    }

}
