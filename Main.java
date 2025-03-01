import Theoffice.Exception.*;
import Theoffice.Menegment.*;
import Theoffice.Menegment.Apartment;
import Theoffice.Menegment.Services.ServiceKind;

import static java.util.Arrays.asList;

public class Main {
    public static void main(String[] args) throws NullValueException, NotPositiveNumberException, NotAuthorizedUserException, ApartmentNotInTheSystemException, ApartmentNotForSaleException, ApartmentAlreadyInTheSystemException {
        RealEstateAgency agency = RealEstateAgency.getInstance();
        //user from the file and new ones
        Buyer adam = (Buyer) agency.CreateUser("adam",UserType.Buyer);//create new user
        Buyer admond = agency.findBuyer("admond");//old seller from the csv
        Seller Roei = agency.findSeller("Roei");//old seller from the csv
        Broker adi = (Broker) agency.CreateUser("adi",UserType.Broker);//create new user
        Broker Amit = agency.findBroker("Amit");//old Broker from the csv

        Apartment apartment1 = agency.findApartment(new Address(13,26,asList(13,14)));//apartment from the list

        try{
            adi.editApartmentPrice(apartment1,100000);
        } catch (NotAuthorizedUserException | ApartmentNotInTheSystemException | NotPositiveNumberException e) {
            System.out.println(e);//not authorized broker
        }
        try{
            Amit.editApartmentPrice(apartment1,-100000);
        } catch (NotAuthorizedUserException | ApartmentNotInTheSystemException | NotPositiveNumberException e) {
            System.out.println(e+"\n");//not positive price
        }
        try{
            Amit.editApartmentPrice(apartment1,300000);



        } catch (NotAuthorizedUserException | ApartmentNotInTheSystemException | NotPositiveNumberException e) {
            System.out.println(e+"\n");//for the 2 run - after the sell
        }

        System.out.println("The average price per m2 in all the agency: "+adi.averagePricePerM2InRadius(apartment1,1000)+"\n");
        System.out.println("apartment for sale by radius 10 from (12,13):");
        System.out.println(Roei.apartmentsForSaleInRadius(apartment1,10));
        Apartment a2 = adi.addApartment(100,10000,new Address(12,20,null),false,Roei);
        System.out.println("Same radius from same place but after adding 1 apartment");
        System.out.println("apartment for sale by radius 10 from (12,13): ");
        System.out.println(Roei.apartmentsForSaleInRadius(apartment1,10));
        Roei.deleteApartment(a2);
        try{
            admond.buyApartment(a2);
        } catch (ApartmentNotForSaleException | ApartmentNotInTheSystemException | NotPositiveNumberException |
                 NullValueException e) {
            System.out.println("\n"+e+"\n");//apartment not in the agency
        }
        try{
            admond.buyApartment(apartment1,asList(ServiceKind.Cleaning,ServiceKind.Design));
        } catch (ApartmentNotForSaleException | ApartmentNotInTheSystemException | NotPositiveNumberException |
                 NullValueException e) {
            System.out.println("\n"+e+"\n");//apartment not for sale - in run 2
        }



        Roei.displayNotification();
        adi.displayNotification();
        //if you run it twice the second time will be with more exception


    }
}

