package Theoffice.Menegment.Search;

import Theoffice.Menegment.ApartmentSearch;
import Theoffice.Menegment.RealEstateAgency;
import Theoffice.Menegment.Address;
import Theoffice.Menegment.Apartment;

import java.util.ArrayList;
import java.util.List;

/** This class implements the SearchStrategy interface to search for apartments based on price per m2 **/
public class ApartmentsByPriceSearchStrategy implements SearchStrategy {
    private final ApartmentSearch.PriceComparison comparison; // The comparison type (Greater, Less, Equal)
    private final double pricePerM2; // The price per square meter to compare

    // Constructor to initialize comparison type and price per square meter
    public ApartmentsByPriceSearchStrategy(ApartmentSearch.PriceComparison comparison, double pricePerM2) {
        this.comparison = comparison;
        this.pricePerM2 = pricePerM2;
    }

    @Override
    public List<Apartment> search(Address address, double radius) {
        RealEstateAgency agency = RealEstateAgency.getInstance(); // Get the instance of the real estate agency
        List<Apartment> apartments = agency.getApartmentsByRadius(address, radius); // Get apartments in the given radius
        List<Apartment> matchingApartments = new ArrayList<>(); // List to store matching apartments

        for (Apartment apartment : apartments) {
            double apartmentPrice = apartment.getPrice(); // Get the apartment price
            switch (comparison) { // Switch case to compare price based on the selected comparison type
                case GREATER_THAN:
                    if (apartmentPrice > pricePerM2) {
                        matchingApartments.add(apartment); // Add to the list if the price is greater
                    }
                    break;
                case LESS_THAN:
                    if (apartmentPrice < pricePerM2) {
                        matchingApartments.add(apartment); // Add to the list if the price is less
                    }
                    break;
                case EQUAL:
                    if (apartmentPrice == pricePerM2) {
                        matchingApartments.add(apartment); // Add to the list if the price is equal
                    }
                    break;
            }
        }
        return matchingApartments; // Return the list of matching apartments
    }

}

