package Theoffice.Menegment.Services;
/**
 * This class implements ApartmentService represent a Design services.
 */

public class Design implements ApartmentService {
    @Override
    public double calculateServiceCost() {
        return 4000;
    }

    @Override
    public String getDescription() {
        return "Design services";
    }
}
