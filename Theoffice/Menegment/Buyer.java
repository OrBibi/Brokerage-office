package Theoffice.Menegment;

import Theoffice.Exception.ApartmentNotForSaleException;
import Theoffice.Exception.ApartmentNotInTheSystemException;
import Theoffice.Exception.NotPositiveNumberException;
import Theoffice.Exception.NullValueException;
import Theoffice.Menegment.Services.ServiceKind;
import java.util.List;

/**
 * This class extends user
 * i add the relevant function
 */

public class Buyer extends User {
    //protected because i want creation of users only from the RealEstateAgency using the UsersFactory
    protected Buyer(String name) {
        super(name);
    }
    public void buyApartment(Apartment apartment, List<ServiceKind> services) throws ApartmentNotForSaleException, ApartmentNotInTheSystemException, NotPositiveNumberException, NullValueException {
        RealEstateAgency agency = RealEstateAgency.getInstance();
        if (apartment.isSold()){ //if the apartment sold you cant buy it
            throw new ApartmentNotForSaleException("Error: this apartment is not for sale.");
        }
        agency.findApartment(apartment.getAddress());//if the apartment not in the system it will throw exception
        ApartmentWithServices apartmentWithServices = new ApartmentWithServices(apartment);//wrap the apartment with the decorator(ApartmentWithServices)
        if(services!=null&&!services.isEmpty()){ //if there is services, add them to the apartment
            for (ServiceKind service : services){
                apartmentWithServices.addService(service);
            }
        }
        apartment.setSold(true);
        apartment.setBuyer(this);
        String message = (name+ " buy this apartment: "+apartmentWithServices);
        apartment.notifySellerAndBroker(message); //use the function of the apartment to let the observers broker and seller know that the apartment sold
        agency.saveApartmentsToFile(); // update the file
    }
    // i created this function to allow to enter the function only apartment without services
    public void buyApartment(Apartment apartment) throws ApartmentNotForSaleException, ApartmentNotInTheSystemException, NotPositiveNumberException, NullValueException {
        this.buyApartment(apartment, null);
    }
}

