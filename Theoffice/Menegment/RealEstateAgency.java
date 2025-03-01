package Theoffice.Menegment;

import Theoffice.Exception.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class represents the RealEstateAgency class.
 * All the information of the RealEstateAgency is save here by the users action
 * Singleton pattern
 */

public class RealEstateAgency {
    private static RealEstateAgency instance;
    private UsersFactory _usersFactory;
    private Set<Apartment> _apartments;
    private Set<Broker> _brokers;
    private Set<Seller> _sellers;
    private Set<Buyer> _buyers;
    private final String file_name = "apartmentsInfoFile.csv";

    public static RealEstateAgency getInstance() {
        if (instance == null) {
            instance = new RealEstateAgency();
        }
        return instance;
    }

    private RealEstateAgency() {
        _usersFactory=UsersFactory.getInstance();
        _apartments = new HashSet<>();
        _brokers = new HashSet<>();
        _sellers = new HashSet<>();
        _buyers = new HashSet<>();
        loadApartmentsFromFile();  // load the data from the file
    }
    // Reading the data from the file
    private void loadApartmentsFromFile() {
        UsersFactory usersFactory = UsersFactory.getInstance();// Creating an instance of UsersFactory to manage users
        File file = new File(file_name); // Creating a File object with the specified file name
        if (!file.exists()) {// Check if the file does not exist
            try {
                file.createNewFile(); // Attempt to create the file if it doesn't exist
                System.out.println("File not found, creating a new empty file for apartments...");
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file_name))) { // Writing the header line to the new file
                    writer.write("SizeInM2,Price,Street,Avenue,ApartmentHierarchy,Sold,Seller,Broker,Buyer\n");
                } catch (IOException e) {
                    e.printStackTrace();// Print stack trace if there's an error during file writing
                }
            } catch (IOException e) {
                e.printStackTrace();// Print stack trace if there's an error creating the new file
            }
        } else {// the file exist
            try (BufferedReader reader = new BufferedReader(new FileReader(file_name))) {
                String headerLine = reader.readLine();// Read the first line (header) of the file
                String line;
                while ((line = reader.readLine()) != null) {// Read each line from the file and create objects from the line
                    String[] values = line.split(",");// Split the line by commas to get the individual values
                    if (values.length == 9) {// Check if the line has exactly 9 values (valid data for an apartment)
                        double sizeInM2 = Double.parseDouble(values[0]);// Parse the values into appropriate data types
                        double price = Double.parseDouble(values[1]);
                        int street = Integer.parseInt(values[2]);
                        int avenue = Integer.parseInt(values[3]);
                        // If the hierarchy is not empty or "<null>", split it into a list of integers
                        List<Integer> hierarchy = (values[4].equals("<null>") || values[4].isEmpty()) ?
                                null : Arrays.asList(values[4].split(";"))//create the list
                                //convert from strings to integers and back to list
                                .stream().map(Integer::parseInt).collect(Collectors.toList());
                        boolean isSold = values[5].equals("Yes");

                        // Create an Address object using the parsed street, avenue, and hierarchy
                        Address address = new Address(street, avenue, hierarchy);

                        // Find the Seller in the sellers list, or create a new one if not found
                        Seller seller = this.findSeller(values[6]);
                        if (seller == null) {
                            seller = (Seller) this.CreateUser(values[6], UserType.Seller);
                        }

                        // Find the Broker in the brokers list, or create a new one if not found
                        Broker broker = this.findBroker(values[7]);
                        if (broker == null) {
                            broker = (Broker) this.CreateUser(values[7], UserType.Broker);
                        }

                        // Find the Buyer in the buyers list, or create a new one if not found (null if not applicable)
                        Buyer buyer = this.findBuyer(values[8]);
                        if (buyer == null) {
                            buyer = (values[8].equals("<null>") || values[8].isEmpty()) ? null : (Buyer) this.CreateUser(values[8], UserType.Buyer);
                        }
                        // Create a new Apartment object using the parsed values
                        Apartment apartment = new Apartment(sizeInM2, price, address, isSold, seller);
                        apartment.setBroker(broker);
                        // Set the buyer for the apartment if he exists
                        apartment.setBuyer(buyer);

                        // Add the apartment to the apartments list
                        _apartments.add(apartment);
                    }
                }
            } catch (IOException | NullValueException e) {
                // Print stack trace if an exception occurs during file reading or value parsing
                e.printStackTrace();
            } catch (NotPositiveNumberException e) {
                // Throw a runtime exception if the parsed value is not positive (for size or price)
                throw new RuntimeException(e);
            }
        }
    }
    //Save the data to the file
    protected void saveApartmentsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file_name))) {
            // Write headers for CSV
            writer.write("SizeInM2,Price,Street,Avenue,ApartmentHierarchy,Sold,Seller,Broker,Buyer\n");
            // Write each apartment's data to the file
            for (Apartment apartment : _apartments) {
                writer.write(apartment.toCSV() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Create users with the UserFactory
    public User CreateUser(String name, UserType type) throws NullValueException {
        User user = _usersFactory.CreateUser(name,type,this.getUsers()); // create the user
        switch (type) { // add him to the list
            case Seller -> _sellers.add((Seller) user);
            case Broker -> _brokers.add((Broker) user);
            case Buyer -> _buyers.add((Buyer) user);
        };
        return user;
    }
    // reset the instance, not delete the data about the apartments, only force to reed again from the file - good for the tests
    public void ResetInstance(){
        _buyers=null;
        _brokers=null;
        _sellers=null;
        _apartments=null;
        instance=null;
    }
    // find seller in the sellers list
    public Seller findSeller(String name) {
        if (name == null) return null;
        for (Seller seller : _sellers) {
            if (name.equals(seller.getName())) return seller;
        }
        return null;
    }
    // find broker in the brokers list
    public Broker findBroker(String name) {
        if (name == null) return null;
        for (Broker broker : _brokers) {
            if (name.equals(broker.getName())) return broker;
        }
        return null;
    }
    // find buyer in the buyers list
    public Buyer findBuyer(String name) {
        if (name.equals("N/A")) return null;
        for (Buyer buyer : _buyers) {
            if (name.equals(buyer.getName())) return buyer;
        }
        return null;
    }
    // find apartment in the apartment list
    public Apartment findApartment(Address address) throws ApartmentNotInTheSystemException {
        for (Apartment apartment : _apartments) {
            if (apartment.getAddress().equals(address)) {
                return apartment;
            }
        }
        throw new ApartmentNotInTheSystemException("Error: this apartment is not in the agency system.");
    }
    protected boolean PartOrContainOfAnotherApartment(Address address1){
        for (Apartment apartment : _apartments) {
            Address address2 = apartment.getAddress();
            if(address2.isSubAddress(address1)||address1.isSubAddress(address2))return true;
        }
        return false;
    }
    // find apartments in the radius from the apartments list
    public List<Apartment> getApartmentsByRadius(Address centerAddress, double radius) {
        List<Apartment> apartmentsInRadius = new ArrayList<>();
        for (Apartment apartment : _apartments) {
            Address apartmentAddress = apartment.getAddress();
            double distance = calculateDistance(centerAddress, apartmentAddress);
            if (distance <= radius) {
                apartmentsInRadius.add(apartment);
            }
        }
        return apartmentsInRadius;
    }
    // calculate distance between 2 apartment
    private double calculateDistance(Address address1, Address address2) {
        int street1 = address1.getStreet();
        int avenue1 = address1.getAvenue();
        int street2 = address2.getStreet();
        int avenue2 = address2.getAvenue();
        return Math.sqrt(Math.pow(street2 - street1, 2) + Math.pow(avenue2 - avenue1, 2));
    }
    // unite all the users to 1 set, it's for the usersFactory
    private Set<User> getUsers(){
        Set<User> users = new HashSet<>();
        users.addAll(_brokers);
        users.addAll(_buyers);
        users.addAll(_sellers);
        return users;
    }
    // send update to all the sellers
    protected void notifySellers(String message) {
        for (Seller seller : _sellers) {
            seller.update(message);
        }
        System.out.println("Message sent to all the sellers in the agency: " + message);
    }
    // send update to all the brokers
    protected void notifyBrokers(String message) {
        for (Broker broker : _brokers) {
            broker.update(message);
        }
        System.out.println("Message sent to all the brokers in the agency: " + message);
    }
    // send update to all the buyers
    protected void notifyBuyers(String message) {
        for (Buyer buyer : _buyers) {
            buyer.update(message);
        }
        System.out.println("Message sent to all the buyers in the agency: " + message);
    }
    protected Set<Apartment> get_apartments() {
        return _apartments;
    }

}
