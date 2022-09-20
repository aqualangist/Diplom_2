package user;

import org.junit.After;
import stellarburgres.user.User;
import stellarburgres.user.UserClient;
import stellarburgres.user.UserCredentials;

public class BaseTest {

    UserClient userClient = new UserClient();
    User credentials;
    User user;
    UserCredentials userCredentials;
    String token;

}
