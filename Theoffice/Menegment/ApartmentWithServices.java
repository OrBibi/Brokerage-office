package Theoffice.Menegment;

import Theoffice.Exception.NotPositiveNumberException;
import Theoffice.Exception.NullValueException;
import Theoffice.Menegment.Apartment;
import Theoffice.Menegment.Services.*;

import java.util.ArrayList;
import java.util.List;
/**
 * This class represents Apartment With Services
 * This class is designed to wrap the apartment with services
 * Decorator pattern
 */


public class ApartmentWithServices extends Apartment {
    private Apartment apartment;
    private List<ApartmentService> services;
    //Create Apartment With Services
    protected ApartmentWithServices(Apartment apartment) throws NotPositiveNumberException, NullValueException {
        super(apartment.getSizeInM2(), apartment.getPrice(), apartment.getAddress(), apartment.isSold(), apartment.getSeller());
        this.setBroker(apartment.getBroker());
        this.setBuyer(apartment.getBuyer());
        this.apartment = apartment;
        this.services = new ArrayList<>();
    }
    // Add the services
    public void addService(ServiceKind service) {
        ApartmentService service1 = null;
        switch (service){
            case Cleaning ->service1=new Cleaning();
            case Design ->service1=new Design();
            case Guarantee ->service1=new Guarantee();
            case Moving ->service1=new Moving();
        }
        boolean notInclude = true;
        for (ApartmentService apartmentService : services){
            if(apartmentService.getDescription().equals(service1.getDescription())){
                notInclude=false; // the service already include
            }
        }
        if (notInclude)services.add(service1);
    }
    // Calculate the new price
    @Override
    public double getPrice() {
        double totalServiceCost = services.stream().mapToDouble(ApartmentService::calculateServiceCost).sum();
        return apartment.getPrice() + totalServiceCost;
    }
    // Create new string include the services
    @Override
    public String toString(){
        StringBuilder ans= new StringBuilder(("Address: " + apartment.getAddress() + " | Size: " + apartment.getSizeInM2() + " | Price: " + apartment.getPrice() + " | Sold: " + apartment.isSold()));
        ans.append("\nServices:\n");
        for (ApartmentService service : services) {
            ans.append(service.getDescription());
            ans.append("\n");
        }
        ans.append("total price: ").append(this.getPrice()).append("\n");
        return ans.toString();
    }
}
