package Theoffice.Menegment.Services;
/**
 * This class implements ApartmentService represent a Cleaning services.
 */

public class Cleaning implements ApartmentService {
    @Override
    public double calculateServiceCost() {
        return 1500;
    }

    @Override
    public String getDescription() {
        return "Cleaning services";
    }
}
