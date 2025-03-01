package Theoffice.Menegment.Services;
/**
 * This class implements ApartmentService represent a Guarantee services.
 */

public class Guarantee implements ApartmentService {
    @Override
    public double calculateServiceCost() {
        return 1000;
    }
    @Override
    public String getDescription() {return "Guarantee services";}
}
