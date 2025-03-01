package Theoffice.Menegment;

import Theoffice.Exception.NotPositiveNumberException;
import Theoffice.Exception.NullValueException;

import java.util.ArrayList;
import java.util.List;
/**
 * This class represents a Address.
 */

public class Address {
    private int street;
    private int avenue;
    private List<Integer> apartmentHierarchy;

    public Address(int street, int avenue, List<Integer> apartmentHierarchy) throws NullValueException, NotPositiveNumberException {
        this.chaking(street,avenue,apartmentHierarchy);
        this.street = street;
        this.avenue = avenue;
        this.apartmentHierarchy= apartmentHierarchy==null? new ArrayList<Integer>() : apartmentHierarchy;
    }
    //checks that this address is legal
    private void chaking(int street, int avenue, List<Integer> apartmentHierarchy) throws NotPositiveNumberException {
        boolean problem= street <= 0 || avenue <= 0;
        if(apartmentHierarchy!=null&&!apartmentHierarchy.isEmpty()){
            for (Integer inti : apartmentHierarchy){
                if(inti<=0){
                    problem=true;
                }
            }
        }
        if (problem){
            throw new NotPositiveNumberException("Error: address only with positive numbers");
        }
    }
    @Override
    public String toString() {
        return "Street " + street + ", Avenue " + avenue + (apartmentHierarchy.isEmpty() ? "" : " -> " + apartmentHierarchy.toString());
    }

    // Getter methods
    public int getStreet() {
        return street;
    }

    public int getAvenue() {
        return avenue;
    }

    public List<Integer> getApartmentHierarchy() {
        return apartmentHierarchy;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Address address = (Address) obj;

        return avenue == address.avenue && street == address.street &&
                (apartmentHierarchy == null ? address.apartmentHierarchy == null : apartmentHierarchy.equals(address.apartmentHierarchy));
    }
    public boolean isSubAddress(Address address) {
        if(address.getAvenue() != avenue || address.getStreet() != street) return false;
        //if we here street and avenue are equals.
        List<Integer> list = address.getApartmentHierarchy();
        if (list.isEmpty())return true;//null always sub.
        if (list.size()>this.getApartmentHierarchy().size())return false;//if its bigger it cant be sub.

        // check all the list.
        for (int i = 0; i < list.size(); i++) {
            if(!list.get(i).equals(this.getApartmentHierarchy().get(i)))return false;
        }

        return true;
    }
}
