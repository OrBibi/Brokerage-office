package Theoffice.Menegment;

import Theoffice.Exception.*;

/**
 * This class extends user
 * i add the relevant function
 */

public class Broker extends User {
    private static final EditApartment editApartment = EditApartment.getInstance();
    private static final AddApartment addApartment = AddApartment.getInstance();
    //protected because i want creation of users only from the RealEstateAgency using the UsersFactory
    protected Broker(String name) {
        super(name);
    }

    //create and add apartment to the agency
    public Apartment addApartment(double sizeInM2, double price, Address address, boolean isSold, Seller seller) throws ApartmentAlreadyInTheSystemException, ApartmentNotForSaleException, NotAuthorizedUserException, NotPositiveNumberException, NullValueException {
        return addApartment.addApartment(sizeInM2, price, address, isSold, seller,this);

    }
    public void editApartmentPrice(Apartment apartment, int new_price) throws NotAuthorizedUserException, NotPositiveNumberException, ApartmentNotInTheSystemException {
        editApartment.editApartmentPrice(apartment,new_price,this);
    }
    public void editApartmentSize(Apartment apartment, int new_size) throws NotAuthorizedUserException, NotPositiveNumberException, ApartmentNotInTheSystemException {
        editApartment.editApartmentSize(apartment,new_size,this);
    }

}

