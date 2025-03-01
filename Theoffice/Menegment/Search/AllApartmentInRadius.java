package Theoffice.Menegment.Search;

import Theoffice.Menegment.RealEstateAgency;
import Theoffice.Menegment.Address;
import Theoffice.Menegment.Apartment;

import java.util.List;
/** This class implements the SearchStrategy interface to search for apartments in the radius **/
public class AllApartmentInRadius implements SearchStrategy {
    @Override
    public List<Apartment> search(Address address, double radius) {
        RealEstateAgency agency = RealEstateAgency.getInstance(); // Get the instance of the real estate agency
        return agency.getApartmentsByRadius(address, radius); // Get apartments in the given radius
    }
}
