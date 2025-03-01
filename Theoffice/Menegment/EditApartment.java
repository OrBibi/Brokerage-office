package Theoffice.Menegment;

import Theoffice.Exception.ApartmentNotInTheSystemException;
import Theoffice.Exception.NotAuthorizedUserException;
import Theoffice.Exception.NotPositiveNumberException;
//this class exist to edit apartments in the system

public class EditApartment {
    private static EditApartment instance;
    private EditApartment(){}

    //Static method to return the EditApartment instance (Singleton pattern)

    protected static EditApartment getInstance(){
        if (instance == null){
            instance = new EditApartment();
        }
        return instance;
    }
    //edit the price
    protected void editApartmentPrice(Apartment apartment, int new_price, Broker broker) throws NotAuthorizedUserException, NotPositiveNumberException, ApartmentNotInTheSystemException {
        this.CheckBeforeEdit(apartment,new_price,broker);//check the broker, and the new price
        apartment.getSeller().update(broker.getName()+ " edit your apartment price from "+apartment.getPrice()+" to "+new_price);
        apartment.setPrice(new_price);
        RealEstateAgency.getInstance().saveApartmentsToFile();
    }
    //edit the size
    protected void editApartmentSize(Apartment apartment, int new_size, Broker broker) throws NotAuthorizedUserException, NotPositiveNumberException, ApartmentNotInTheSystemException {
        this.CheckBeforeEdit(apartment,new_size,broker);//check the broker, and the new size
        apartment.getSeller().update(broker.getName()+ " edit your apartment size from "+apartment.getSizeInM2()+" to "+new_size);
        apartment.setSizeInM2(new_size);
        RealEstateAgency.getInstance().saveApartmentsToFile();
    }
    //checks if this is the broker of the apartment, and if the change is legal
    private void CheckBeforeEdit(Apartment apartment, int change, Broker broker) throws NotAuthorizedUserException, NotPositiveNumberException, ApartmentNotInTheSystemException {
        RealEstateAgency agency = RealEstateAgency.getInstance();
        agency.findApartment(apartment.getAddress());//check if the apartment in the system
        if (apartment.getBroker()!=broker){
            throw new NotAuthorizedUserException("Error: you can't edit another broker apartments.");
        }
        if(change<=0){
            throw new NotPositiveNumberException("Error: the size and price must be positive.");
        }
        agency.findApartment(apartment.getAddress());
        if(apartment.isSold())throw new NotAuthorizedUserException("Error: the apartment sold, you can't edit it.");
    }
}
