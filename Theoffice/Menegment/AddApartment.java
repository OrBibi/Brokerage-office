package Theoffice.Menegment;

import Theoffice.Exception.*;

//this class exist to add new apartments to the system.

public class AddApartment {
    private static AddApartment instance;
    private AddApartment(){}
    //Static method to return the AddApartment instance (Singleton pattern)
    protected static AddApartment getInstance(){
        if (instance == null){
            instance = new AddApartment();
        }
        return instance;
    }
    //add new apartment to the agency
    protected Apartment addApartment(double sizeInM2, double price, Address address, boolean isSold, Seller seller,Broker broker) throws ApartmentAlreadyInTheSystemException, ApartmentNotForSaleException, NotAuthorizedUserException, NotPositiveNumberException, NullValueException {
        RealEstateAgency agency = RealEstateAgency.getInstance();
        this.CheckBeforeAdd(sizeInM2,price,address,isSold,seller);
        Apartment apartment=new Apartment(sizeInM2, price,address, isSold,seller);

        if (agency.PartOrContainOfAnotherApartment(apartment.getAddress())){//check fot duplication or sub addresses
            throw new ApartmentAlreadyInTheSystemException("Error: not legal address to add, duplication");
        }
        else{
            apartment.setBroker(broker); //we set the broker to be the adding broker
            agency.get_apartments().add(apartment); // add the apartment to the system
            agency.saveApartmentsToFile();//save the system to the file
            apartment.getSeller().update(broker.getName()+ " add your apartment to the agency: "+apartment);
            return apartment;
        }

    }
    private void CheckBeforeAdd(double sizeInM2, double price, Address address, boolean isSold, Seller seller) throws NullValueException, NotPositiveNumberException, ApartmentNotForSaleException {
        if(sizeInM2<=0||price<=0){ // check positive price and size
            throw new NotPositiveNumberException("Error: size and price must be positive.");
        }
        if(address==null||seller==null){ // check seller and address not nul
            throw new NullValueException("Error: address,seller and broker cant be null.");
        }
        if(isSold){  //allow adding only apartments for sale
            throw new ApartmentNotForSaleException("Error: you can add only apartments for sale.");
        }
    }

}
