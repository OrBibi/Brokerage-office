package Theoffice.Menegment;

import Theoffice.Menegment.Search.SearchStrategy;

import java.util.List;

// Singleton class responsible for managing property search with Strategy pattern
public class ApartmentSearch {
    private static ApartmentSearch instance; // Singleton instance
    private SearchStrategy searchStrategy; // The current search strategy

    // Private constructor to prevent direct instantiation
    private ApartmentSearch() {
    }

    // Method to get the singleton instance of PropertySearch
    protected static ApartmentSearch getInstance() {
        if (instance == null) {
            instance = new ApartmentSearch(); // Instantiate if not already created
        }
        return instance; // Return the single instance
    }

    // Method to set the search strategy dynamically
    protected void setSearchStrategy(SearchStrategy searchStrategy) {
        this.searchStrategy = searchStrategy;
    }

    // Method to execute the search based on the chosen strategy
    protected List<Apartment> search(Address address, double radius) {
        if (searchStrategy == null) {
            throw new IllegalStateException("Search strategy is not set"); // Ensure a strategy is set before searching
        }
        return searchStrategy.search(address, radius); // Perform search using the selected strategy
    }

    // Enum to define price comparison types
    public enum PriceComparison {
        GREATER_THAN, LESS_THAN, EQUAL
    }
}

