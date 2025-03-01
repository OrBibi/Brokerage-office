package Theoffice.Menegment;

import Theoffice.Exception.NullValueException;
import Theoffice.Menegment.Search.AllApartmentInRadius;
import Theoffice.Menegment.Search.ApartmentsByPriceSearchStrategy;
import Theoffice.Menegment.Search.ApartmentsForSaleSearchStrategy;
import Theoffice.Menegment.Search.SoldApartmentsSearchStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a User in the system.
 * It implements the Observer design pattern and uses the Strategy design pattern
 * to search for apartments based on different search strategies.
 */
public abstract class User implements Observer {
    protected String name;
    protected List<String> Notification;
    protected ApartmentSearch apartmentSearch= ApartmentSearch.getInstance();

    // Constructor to initialize the user's name and set the default search strategy.
    protected User(String name) {
        this.name = name;
        this.Notification = new ArrayList<>();
    }

    // Retrieve a list of apartments that are sold within a specified radius.
    public List<Apartment> apartmentsSoldInRadius(Apartment apartment, double radius) {
        apartmentSearch.setSearchStrategy(new SoldApartmentsSearchStrategy());
        return apartmentSearch.search(apartment.getAddress(),radius);
    }

    // Retrieve a list of apartments that are for sale within a specified radius.
    public List<Apartment> apartmentsForSaleInRadius(Apartment apartment, double radius) {
        apartmentSearch.setSearchStrategy(new ApartmentsForSaleSearchStrategy());
        return apartmentSearch.search(apartment.getAddress(),radius);
    }

    // Retrieve apartments that match the specified price comparison criteria within a given radius.
    public List<Apartment> apartmentsByPricePerM2InRadius(Apartment apartment, double radius, ApartmentSearch.PriceComparison comparison) {
        apartmentSearch.setSearchStrategy(new ApartmentsByPriceSearchStrategy(comparison,apartment.getPrice()));
        return apartmentSearch.search(apartment.getAddress(),radius);
    }

    // Calculate the average price per square meter for apartments within a radius of a given address.
    public double averagePricePerM2InRadius(Apartment apartment, double radius) throws NullValueException {
        apartmentSearch.setSearchStrategy(new AllApartmentInRadius());
        List<Apartment> apartments = apartmentSearch.search(apartment.getAddress(),radius);
        if(apartments==null||apartments.isEmpty())throw new NullValueException("Error: no apartments in the radius cant calculate average price");
        double sum=0,temp=0;
        for (Apartment a : apartments){
            temp=a.getPrice() /a.getSizeInM2();
            sum+=temp;
        }
        return sum/apartments.size();
    }

    // Implementing the observer pattern method to receive updates.
    @Override
    public void update(String message) {
        Notification.add(message);
    }
    public void displayNotification(){
        System.out.println("\nAll "+name+" Notification:");
        for (String message : Notification){
            System.out.println(message);
        }
    }

    // Getter for the user's name.
    public String getName() {
        return name;
    }

    public List<String> getNotification() {
        return Notification;
    }
}
