package Theoffice.Menegment;

import Theoffice.Exception.ApartmentNotInTheSystemException;
import Theoffice.Exception.NotAuthorizedUserException;

/**
 * This class extends user
 * i add the relevant function
 */
public class Seller extends User {
    //protected because i want creation of users only from the RealEstateAgency using the UsersFactory
    protected Seller(String name) {
        super(name);
    }
    public void deleteApartment(Apartment apartment) throws ApartmentNotInTheSystemException, NotAuthorizedUserException {
        RealEstateAgency agency = RealEstateAgency.getInstance();
        agency.findApartment(apartment.getAddress()); // you can remove apartment only if is in the system
        if(apartment.getSeller()!=this){ // you can remove apartment belong to you
            throw new NotAuthorizedUserException("Error: you cant delete another seller apartment.");
        }
        if(apartment.isSold()){ // you can remove apartment belong to you, if it sold it stays for data (like average price)
            throw new NotAuthorizedUserException("Error: someone buy this apartment, its not yours to delete.");
        }
        agency.get_apartments().remove(apartment);
        agency.saveApartmentsToFile();
        apartment.getBroker().update(name+ " delete his apartment: "+apartment);
    }
}

