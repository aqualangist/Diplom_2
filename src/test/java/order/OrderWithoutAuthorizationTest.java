package order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import stellarburgres.order.Order;
import stellarburgres.order.Ingredient;
import stellarburgres.order.OrderClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OrderWithoutAuthorizationTest {

        OrderClient orderClient = new OrderClient();

        @Test
        @DisplayName("Make order without ingredients, user isn't authorized, returns 400 Bad Request")
        @Description("Make order without ingredients. " +
                "Response should has status code is 400 and message: \"Ingredient ids must be provided\".")
        public void makeOrderWithoutIngredientNotAuthorizedTest() {
            Order order = Order.builder().build();
            orderClient.makeOrderWithoutIngredientsWithoutAuthorization(order);
        }

    @Test
    @DisplayName("Make order with one valid ingredient, user isn't authorized, returns 200 OK")
    @Description("Make order with one valid hash of ingredient. " +
            "Response should has status code is 200, order's number isn't null.")
    public void makeOrderWithOneValidIngredientNotAuthorizedTest() {
        int index = new Random().nextInt(14);
        String ingredientHash = orderClient.getIngredient(index);

        List<Ingredient> ingredientsList = new ArrayList<>();
        ingredientsList.add(Ingredient.builder()._id(ingredientHash).build());

        Order order = Order.builder().ingredients(ingredientsList).build();
        orderClient.makeOrderWithIngredientsWithoutAuthorization(order);
    }

    @Test
    @DisplayName("Make order with one invalid ingredient, user isn't authorized, returns 500 Internal Server Error")
    @Description("Make order with one invalid hash of ingredient. " +
            "Response should has status code.")
    public void makeOrderWithOneInvalidIngredientNotAuthorizedReturns500() {
        String invalidIngredientHash = "61c0c5a71d1f82001bdaaa6r";

        List<Ingredient> ingredientsList = new ArrayList<>();
        ingredientsList.add(Ingredient.builder()._id(invalidIngredientHash).build());

        Order order = Order.builder().ingredients(ingredientsList).build();
        orderClient.makeOrderWithInvalidIngredientHashWithoutAuthorization(order);
    }

    @Test
    @DisplayName("Make order with more than one valid ingredients, user isn't authorized, returns 200 OK")
    @Description("Make order with invalid hashes of ingredients. " +
            "Response should has status code is 200 and order's number isn't null.")
    public void makeOrderWithValidIngredientsNotAuthorizedReturns200() {
        int firstIndex = new Random().nextInt(14);
        String firstIngredientHash = orderClient.getIngredient(firstIndex);

        int secondIndex = new Random().nextInt(14);
        String secondIngredientHash = orderClient.getIngredient(secondIndex);

        List<Ingredient> ingredientsList = new ArrayList<>();
        ingredientsList.add(Ingredient.builder()._id(firstIngredientHash).build());
        ingredientsList.add(Ingredient.builder()._id(secondIngredientHash).build());

        Order order = Order.builder().ingredients(ingredientsList).build();
        orderClient.makeOrderWithIngredientsWithoutAuthorization(order);
    }

    @Test
    @DisplayName("Make order with two ingredients, valid and invalid hash, user isn't authorized, " +
            "returns 500 Internal Server Error")
    @Description("Create an order with two ingredients - valid and invalid hashes. " +
            "Response should has status code is 500.")
    public void makeOrderWithInvalidIngredientsNotAuthorizedReturns500() {
        int index = new Random().nextInt(14);
        String validIngredientHash = orderClient.getIngredient(index);
        String invalidIngredientHash = orderClient.getIngredient(index) + "q";

        List<Ingredient> ingredientsList = new ArrayList<>();
        ingredientsList.add(Ingredient.builder()._id(invalidIngredientHash).build());
        ingredientsList.add(Ingredient.builder()._id(validIngredientHash).build());

        Order order = Order.builder().ingredients(ingredientsList).build();
        orderClient.makeOrderWithInvalidIngredientHashWithoutAuthorization(order);
    }

}
