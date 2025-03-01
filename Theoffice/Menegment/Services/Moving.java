package Theoffice.Menegment.Services;
/**
 * This class implements ApartmentService represent a Moving services.
 */

public class Moving implements ApartmentService {
    @Override
    public double calculateServiceCost() {
        return 4000;
    }

    @Override
    public String getDescription() {
        return "Moving services";
    }
}
