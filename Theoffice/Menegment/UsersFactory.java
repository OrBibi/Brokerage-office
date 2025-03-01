package Theoffice.Menegment;

import Theoffice.Exception.NullValueException;
import java.util.Set;
/**
 * Factory method - here we create all kinds of users
 */

public class UsersFactory {
    private static UsersFactory instance;
    private UsersFactory() {
    }
    //I used instance because there is no need to create more than 1
    protected static UsersFactory getInstance() {
        if (instance == null) {
            instance = new UsersFactory();
        }
        return instance;
    }
    //It is protected because i want to allow creation of users only from the RealEstateAgency
    protected User CreateUser(String name, UserType type,Set<User> users) throws NullValueException {
        if (name == null || name.trim().isEmpty()) {
            throw new NullValueException("Error: name can't be null or empty.");
        }

        if (!name.matches("[A-Za-z ]+")) {
            throw new NullValueException("Error: name must contain only English letters and spaces.");
        }

        for(User user : users){
            if(user.getName().equals(name))throw new NullValueException("Error: name already exists.");
        }
        return switch (type) {
            case Seller -> new Seller(name);
            case Broker -> new Broker(name);
            case Buyer -> new Buyer(name);
        };
    }
}
