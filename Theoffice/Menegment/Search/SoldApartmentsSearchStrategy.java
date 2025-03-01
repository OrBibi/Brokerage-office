package Theoffice.Menegment.Search;

import Theoffice.Menegment.RealEstateAgency;
import Theoffice.Menegment.Address;
import Theoffice.Menegment.Apartment;

import java.util.ArrayList;
import java.util.List;

// This class implements the SearchStrategy interface to search for sold apartments
public class SoldApartmentsSearchStrategy implements SearchStrategy {
    @Override
    public List<Apartment> search(Address address, double radius) {
        RealEstateAgency agency = RealEstateAgency.getInstance(); // Get the instance of the real estate agency
        List<Apartment> apartments = agency.getApartmentsByRadius(address, radius); // Get apartments in the given radius
        List<Apartment> soldApartments = new ArrayList<>(); // List to store sold apartments

        for (Apartment apartment : apartments) {
            if (apartment.isSold()) { // Check if the apartment is sold
                soldApartments.add(apartment); // Add to the list if sold
            }
        }
        return soldApartments; // Return the list of sold apartments
    }

}

