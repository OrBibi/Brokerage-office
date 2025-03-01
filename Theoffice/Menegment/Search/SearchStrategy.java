package Theoffice.Menegment.Search;

import Theoffice.Menegment.Address;
import Theoffice.Menegment.Apartment;
import java.util.List;

// SearchStrategy interface defines a contract for search algorithms
public interface SearchStrategy {
    // The search method will be implemented by concrete strategies
    List<Apartment> search(Address address, double radius);
}

