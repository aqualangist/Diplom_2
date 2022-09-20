package order;

import stellarburgres.order.OrderClient;
import stellarburgres.user.User;
import stellarburgres.user.UserClient;
import stellarburgres.user.UserCredentials;

public class BaseOrderTest {

    OrderClient orderClient = new OrderClient();
    UserClient userClient = new UserClient();
    UserCredentials credentials;
    User user;
    String token;

}
