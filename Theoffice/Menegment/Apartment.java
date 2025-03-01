package Theoffice.Menegment;

/**
 * This class represents a Apartment, not have to be in the system.
 */
public class Apartment {
    private double sizeInM2;
    private double price;
    private Address address;
    private boolean isSold;
    private Seller seller;
    private Broker broker;
    private Buyer buyer;


    protected Apartment(double sizeInM2, double price, Address address, boolean isSold, Seller seller) {
        this.sizeInM2 = sizeInM2;
        this.price = price;
        this.address = address;
        this.isSold = isSold;
        this.seller = seller;
        this.broker = null;
        this.buyer = null;
    }

    @Override
    public String toString(){
        return ("Address: "+address+ " | Size: "+sizeInM2+" | Price: "+price+" | Sold: "+isSold);
    }
    // Getters and Setters
    public double getSizeInM2() {
        return sizeInM2;
    }

    public void setSizeInM2(double sizeInM2) {
        this.sizeInM2 = sizeInM2;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Address getAddress() {
        return address;
    }

    public boolean isSold() {
        return isSold;
    }

    public void setSold(boolean sold) {
        isSold = sold;
    }

    public String toCSV() {
        // Return the data in CSV format
        String buyerName = (buyer != null) ? buyer.getName() : "<null>";

        String apartmentHierarchy = (this.getAddress().getApartmentHierarchy() != null && !this.getAddress().getApartmentHierarchy().isEmpty()) ?
                String.join(";", this.getAddress().getApartmentHierarchy().stream().map(String::valueOf).toArray(String[]::new)) : "<null>";

        return this.getSizeInM2() + "," + this.getPrice() + "," +
                this.getAddress().getStreet() + "," + this.getAddress().getAvenue() + "," +
                apartmentHierarchy + "," +
                (this.isSold() ? "Yes" : "No") + "," +
                seller.getName() + "," + broker.getName() + "," + buyerName;
    }

    public void notifySellerAndBroker(String message){
        seller.update(message);
        broker.update(message);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj == null || getClass() != obj.getClass()) return false;

        Apartment apartment = (Apartment) obj;

        return address.equals(apartment.getAddress());
    }
    // getters and setters
    public Seller getSeller() {
        return seller;
    }
    public Broker getBroker() {
        return broker;
    }
    public void setBroker(Broker broker) {
        this.broker = broker;
    }
    public Buyer getBuyer() {
        return buyer;
    }
    public void setBuyer(Buyer buyer) {
        this.buyer = buyer;
    }
}
