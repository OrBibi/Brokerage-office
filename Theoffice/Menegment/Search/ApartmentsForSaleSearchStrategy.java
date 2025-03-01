package Theoffice.Menegment.Search;

import Theoffice.Menegment.RealEstateAgency;
import Theoffice.Menegment.Address;
import Theoffice.Menegment.Apartment;

import java.util.ArrayList;
import java.util.List;

/** This class implements the SearchStrategy interface to search for apartments for sale **/
public class ApartmentsForSaleSearchStrategy implements SearchStrategy {
    @Override
    public List<Apartment> search(Address address, double radius) {
        RealEstateAgency agency = RealEstateAgency.getInstance(); // Get the instance of the real estate agency
        List<Apartment> apartments = agency.getApartmentsByRadius(address, radius); // Get apartments in the given radius
        List<Apartment> apartmentsForSale = new ArrayList<>(); // List to store apartments for sale

        for (Apartment apartment : apartments) {
            if (!apartment.isSold()) { // Check if the apartment is not sold
                apartmentsForSale.add(apartment); // Add to the list if available for sale
            }
        }
        return apartmentsForSale; // Return the list of apartments for sale
    }
}

