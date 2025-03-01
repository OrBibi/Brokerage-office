package Theoffice.Tests;

import Theoffice.Menegment.*;
import Theoffice.Exception.*;
import Theoffice.Menegment.Address;
import Theoffice.Menegment.Apartment;
import Theoffice.Menegment.Services.ServiceKind;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.nio.file.*;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

class RealEstateTests {

    private RealEstateAgency agency;
    private static final String FILE_NAME = "apartmentsInfoFile.csv";
    private static final String BACKUP_FILE_NAME = "apartmentsInfoFile_backup.csv";
    private Seller testseller;
    private Buyer testbuyer;
    private Broker testbroker;

    @BeforeEach
    void setUp() throws IOException, NullValueException {
        agency = RealEstateAgency.getInstance();

        // Create a backup of the original file
        Path originalFilePath = Paths.get(FILE_NAME);
        Path backupFilePath = Paths.get(BACKUP_FILE_NAME);

        if (Files.exists(originalFilePath)) {
            Files.copy(originalFilePath, backupFilePath, StandardCopyOption.REPLACE_EXISTING);
        } else {
            // If the file doesn't exist, create an empty backup
            Files.createFile(backupFilePath);
        }
        testbroker = (Broker) agency.CreateUser("brokertest",UserType.Broker);
        testseller = (Seller) agency.CreateUser("sellertest",UserType.Seller);
        testbuyer = (Buyer) agency.CreateUser("buyertest", UserType.Buyer);
    }

    @AfterEach
    void tearDown() throws IOException {
        // Restore the original file from backup after each test
        Path originalFilePath = Paths.get(FILE_NAME);
        Path backupFilePath = Paths.get(BACKUP_FILE_NAME);

        if (Files.exists(backupFilePath)) {
            Files.copy(backupFilePath, originalFilePath, StandardCopyOption.REPLACE_EXISTING);
        } else {
            // If the backup file is not found, create an empty file as a fallback
            Files.createFile(originalFilePath);
        }
        if (Files.exists(backupFilePath)) {
            Files.delete(backupFilePath);
        }
        agency.ResetInstance();
    }

    // Test creation of good users
    @Test
    void testCreateUser() throws NullValueException {
        User seller = agency.CreateUser("OrSeller", UserType.Seller);
        User buyer = agency.CreateUser("KerenBuyer", UserType.Buyer);
        User broker = agency.CreateUser("BibiBroker", UserType.Broker);

        assertNotNull(seller);
        assertNotNull(buyer);
        assertNotNull(broker);
        assertEquals("OrSeller", seller.getName());
        assertEquals("KerenBuyer", buyer.getName());
        assertEquals("BibiBroker", broker.getName());
    }

    ///////////////////////////////////test creating users///////////////////////////////////////////////////////
    // Test creation of user with null or empty name
    @Test
    void testCreateUserWithNullValues() {
        // Test for null name
        NullValueException exception = assertThrows(NullValueException.class, () -> {
            agency.CreateUser(null, UserType.Seller);
        });
        assertEquals("Error: name can't be null or empty.", exception.getMessage());

        // Test for empty string
        exception = assertThrows(NullValueException.class, () -> {
            agency.CreateUser("", UserType.Seller);
        });
        assertEquals("Error: name can't be null or empty.", exception.getMessage());

        // Test for name with only spaces
        exception = assertThrows(NullValueException.class, () -> {
            agency.CreateUser("     ", UserType.Seller);
        });
        assertEquals("Error: name can't be null or empty.", exception.getMessage());
    }

    // Test creation of user with invalid characters in name (non-English letters)
    @Test
    void testCreateUserWithInvalidName() {
        // Test for name with digits
        NullValueException exception = assertThrows(NullValueException.class, () -> {
            agency.CreateUser("John123", UserType.Seller);
        });
        assertEquals("Error: name must contain only English letters and spaces.", exception.getMessage());

        // Test for name with special characters
        exception = assertThrows(NullValueException.class, () -> {
            agency.CreateUser("John@Doe", UserType.Buyer);
        });
        assertEquals("Error: name must contain only English letters and spaces.", exception.getMessage());

        // Test for name with non-English letters
        exception = assertThrows(NullValueException.class, () -> {
            agency.CreateUser("אור", UserType.Broker);
        });
        assertEquals("Error: name must contain only English letters and spaces.", exception.getMessage());
    }

    // Test valid user creation with proper name format
    @Test
    void testCreateUserWithValidName() throws NullValueException {
        User seller = agency.CreateUser("Aa", UserType.Seller);
        User buyer = agency.CreateUser("bb", UserType.Buyer);
        User broker = agency.CreateUser("b b", UserType.Broker);

        assertNotNull(seller);
        assertNotNull(buyer);
        assertNotNull(broker);
        assertEquals("Aa", seller.getName());
        assertEquals("bb", buyer.getName());
        assertEquals("b b", broker.getName());
    }

    // Test creating two users with the same name (should throw exception)
    @Test
    void testCreateUserWithDuplicateName() throws NullValueException {
        // First user creation
        User firstUser = agency.CreateUser("jk", UserType.Seller);
        assertNotNull(firstUser);

        // Trying to create another user with the same name
        NullValueException exception = assertThrows(NullValueException.class, () -> {
            agency.CreateUser("jk", UserType.Buyer);
        });
        assertEquals("Error: name already exists.", exception.getMessage());
    }
    ///////////////////////////////////test creating apartments///////////////////////////////////////////////////////
    // Test creation of good apartment
    @Test
    void GoodApartment() throws NullValueException, NotPositiveNumberException, ApartmentNotForSaleException, ApartmentAlreadyInTheSystemException, NotAuthorizedUserException {
        Apartment a = testbroker.addApartment(10, 100, new Address(8, 8, asList(4, 4)), false, testseller);
        assertEquals(10,a.getSizeInM2());
        assertEquals(100,a.getPrice());
        assertEquals(a.getAddress(),new Address(8,8,asList(4,4)));
        assertFalse(a.isSold());
        assertEquals(testseller,a.getSeller());
        assertEquals(testbroker,a.getBroker());
        assertNull(a.getBuyer());
    }
    //test apartment with negative values (different entries)
    @Test
    void ApartmentWithNegativeVal() {
        assertThrows(NotPositiveNumberException.class, () -> {
            testbroker.addApartment(-5, 100, new Address(8, 8, asList(4, 4)), false, testseller);
        });
        assertThrows(NotPositiveNumberException.class, () -> {
            testbroker.addApartment(5, -100, new Address(8, 8, asList(4, 4)), false, testseller);
        });
        assertThrows(NotPositiveNumberException.class, () -> {
            testbroker.addApartment(5, 100, new Address(-8, 8, asList(4, 4)), false, testseller);
        });
        assertThrows(NotPositiveNumberException.class, () -> {
            testbroker.addApartment(5, 100, new Address(8, 8, asList(4, -4)), false, testseller);
        });
        assertThrows(NotPositiveNumberException.class, () -> {
            testbroker.addApartment(0, 100, new Address(8, 8, asList(4, 4)), false, testseller);
        });
        assertThrows(NotPositiveNumberException.class, () -> {
            testbroker.addApartment(5, 100, new Address(8, 8, asList(4, 0)), false, testseller);
        });
        assertThrows(NotPositiveNumberException.class, () -> {
            testbroker.addApartment(0, -100, new Address(-8, 0, asList(0, -4)), false, testseller);
        });
    }
    //test apartment with negative values
    @Test
    void ApartmentWithNullVal() {
        assertThrows(NullValueException.class, () -> {
            testbroker.addApartment(5, 100, new Address(8, 8, asList(4, 4)), false, null);
        });
        assertThrows(NullValueException.class, () -> {
            testbroker.addApartment(5, 100, null, false, testseller);
        });
    }
    ///////////////////////////////////test broker function///////////////////////////////////////////////////////
    //test adding apartment
    @Test
    void AddApartment() throws NotPositiveNumberException, NullValueException, ApartmentNotForSaleException, ApartmentAlreadyInTheSystemException, NotAuthorizedUserException, ApartmentNotInTheSystemException {
        Address address = new Address(80,80,asList(4,4));
        assertThrows(ApartmentNotInTheSystemException.class, () -> {
            agency.findApartment(address);
        });
        Apartment a = testbroker.addApartment(10,100,address,false,testseller);
        Apartment b = agency.findApartment(address);
        assertEquals(a,b);
    }
    //test adding apartment not for sale-need to fail
    @Test
    void AddApartmentNotForSale() throws NotPositiveNumberException, NullValueException {
        Address address = new Address(80,80,asList(4,4));
        assertThrows(ApartmentNotForSaleException.class, () -> {
            testbroker.addApartment(10,100,address,true,testseller);
        });
    }
    //test adding apartment twice - need to fail
    @Test
    void AddApartmentDuplicate() throws NotPositiveNumberException, NullValueException, ApartmentNotForSaleException, ApartmentAlreadyInTheSystemException, NotAuthorizedUserException {
        Address address = new Address(80,80,asList(4,4));
        testbroker.addApartment(10,100,address,false,testseller);
        assertThrows(ApartmentAlreadyInTheSystemException.class, () -> {
            testbroker.addApartment(10,100,address,false,testseller);
        });
    }
    //test adding apartment sub of another apartment
    @Test
    void AddApartmentSubOfExist() throws NotPositiveNumberException, NullValueException, ApartmentNotForSaleException, ApartmentAlreadyInTheSystemException, NotAuthorizedUserException {
        Address address1 = new Address(80,80,asList(4,4));
        Address address2 = new Address(80,80,asList(4));
        Address address3 = new Address(80,80,null);
        testbroker.addApartment(10,100,address1,false,testseller);
        assertThrows(ApartmentAlreadyInTheSystemException.class, () -> {
            testbroker.addApartment(10,100,address2,false,testseller);
        });
        assertThrows(ApartmentAlreadyInTheSystemException.class, () -> {
            testbroker.addApartment(10,100,address3,false,testseller);
        });
    }
    //test adding apartment that exist sub of it in the agency
    @Test
    void AddApartmentAfterSubExist() throws NotPositiveNumberException, NullValueException, ApartmentNotForSaleException, ApartmentAlreadyInTheSystemException, NotAuthorizedUserException {
        Address address1 = new Address(80,80,asList(4,4));
        Address address2 = new Address(80,80,asList(4));
        Address address3 = new Address(80,80,null);
        testbroker.addApartment(10,100,address3,false,testseller);
        assertThrows(ApartmentAlreadyInTheSystemException.class, () -> {
            testbroker.addApartment(10,100,address2,false,testseller);
        });
        assertThrows(ApartmentAlreadyInTheSystemException.class, () -> {
            testbroker.addApartment(10,100,address1,false,testseller);
        });
        Address address4 = new Address(80,81,asList(1,2,3));
        Address address5 = new Address(80,81,asList(1,2,3,5));
        testbroker.addApartment(10,100,address4,false,testseller);
        assertThrows(ApartmentAlreadyInTheSystemException.class, () -> {
            testbroker.addApartment(10,100,address5,false,testseller);
        });
    }
    //test edit apartment price
    @Test
    void EditApartmentPrice() throws NotPositiveNumberException, NullValueException, ApartmentNotForSaleException, ApartmentAlreadyInTheSystemException, NotAuthorizedUserException, ApartmentNotInTheSystemException {
        Address address = new Address(80,80,asList(4,4));
        Apartment a = testbroker.addApartment(10,100,address,false,testseller);
        testbroker.editApartmentPrice(a,200);
        assertEquals(200,a.getPrice());
        testbroker.editApartmentPrice(a,300);
        assertEquals(300,agency.findApartment(address).getPrice());
        assertThrows(NotPositiveNumberException.class, () -> {
            testbroker.editApartmentPrice(a,-1);
        });
        Broker broker = (Broker) agency.CreateUser("waefrewaf",UserType.Broker);
        assertThrows(NotAuthorizedUserException.class, () -> {
            broker.editApartmentPrice(a,1);
        });
        testbuyer.buyApartment(a);
        assertThrows(NotAuthorizedUserException.class, () -> {
            testbroker.editApartmentPrice(a,200);
        });
    }
    //test edit apartment size
    @Test
    void EditApartmentSize() throws NotPositiveNumberException, NullValueException, ApartmentNotForSaleException, ApartmentAlreadyInTheSystemException, NotAuthorizedUserException, ApartmentNotInTheSystemException {
        Address address = new Address(80,80,asList(4,4));
        Apartment a = testbroker.addApartment(10,100,address,false,testseller);
        testbroker.editApartmentSize(a,200);
        assertEquals(200,a.getSizeInM2());
        testbroker.editApartmentSize(a,300);
        assertEquals(300,agency.findApartment(address).getSizeInM2());
        assertThrows(NotPositiveNumberException.class, () -> {
            testbroker.editApartmentSize(a,-1);
        });
        Broker broker = (Broker) agency.CreateUser("waefrewaf",UserType.Broker);
        assertThrows(NotAuthorizedUserException.class, () -> {
            broker.editApartmentSize(a,1);
        });
    }
    ///////////////////////////////////test seller function///////////////////////////////////////////////////////
    //test delete apartment
    @Test
    void DeleteApartment() throws NotPositiveNumberException, NullValueException, ApartmentNotForSaleException, ApartmentAlreadyInTheSystemException, NotAuthorizedUserException, ApartmentNotInTheSystemException {
        Address address = new Address(80,80,asList(4,4));
        Apartment a = testbroker.addApartment(10,100,address,false,testseller);
        testseller.deleteApartment(a);
        assertThrows(ApartmentNotInTheSystemException.class, () -> {
            agency.findApartment(address);
        });
    }
    //test delete apartment - fails
    @Test
    void DeleteApartmentFails() throws NotPositiveNumberException, NullValueException, ApartmentNotForSaleException, ApartmentAlreadyInTheSystemException, NotAuthorizedUserException, ApartmentNotInTheSystemException {
        Address address = new Address(80,80,asList(4,4));
        Apartment a = testbroker.addApartment(10,100,address,false,testseller);
        Seller seller = (Seller) agency.CreateUser("fhdhdf", UserType.Seller);
        assertThrows(NotAuthorizedUserException.class, () -> {
            seller.deleteApartment(a);
        });// fail because this is not the right seller
        testbuyer.buyApartment(a);
        assertThrows(NotAuthorizedUserException.class, () -> {
            seller.deleteApartment(a);
        });// fail because someone buy the apartment
    }
    ///////////////////////////////////test buyer function///////////////////////////////////////////////////////
    //test buy apartment
    @Test
    void BuyApartment() throws NotPositiveNumberException, NullValueException, ApartmentNotForSaleException, ApartmentAlreadyInTheSystemException, NotAuthorizedUserException, ApartmentNotInTheSystemException {
        Address address = new Address(80,80,asList(4,4));
        Apartment a = testbroker.addApartment(10,100,address,false,testseller);
        testbuyer.buyApartment(a);
        assertEquals(testbuyer,a.getBuyer());
        assertTrue(a.isSold());
    }
    //test buy apartment twice - fail
    @Test
    void BuyApartmentTwice() throws NotPositiveNumberException, NullValueException, ApartmentNotForSaleException, ApartmentAlreadyInTheSystemException, NotAuthorizedUserException, ApartmentNotInTheSystemException {
        Address address = new Address(80,80,asList(4,4));
        Apartment a = testbroker.addApartment(10,100,address,false,testseller);
        testbuyer.buyApartment(a);
        assertThrows(ApartmentNotForSaleException.class, () -> {
            testbuyer.buyApartment(a);
        });// fail because someone buy the apartment
    }
    //test buy apartment with services
    @Test
    void BuyApartmentWitheServices() throws NotPositiveNumberException, NullValueException, ApartmentNotForSaleException, ApartmentAlreadyInTheSystemException, NotAuthorizedUserException, ApartmentNotInTheSystemException {
        Address address = new Address(80,80,asList(4,4));
        Apartment a = testbroker.addApartment(10,100,address,false,testseller);
        testbuyer.buyApartment(a, asList(ServiceKind.Design,ServiceKind.Cleaning));
    }
    //test buy apartment with services
    @Test
    void BuyApartmentWithDuplicateServices() throws NotPositiveNumberException, NullValueException, ApartmentNotForSaleException, ApartmentAlreadyInTheSystemException, NotAuthorizedUserException, ApartmentNotInTheSystemException {
        Address address = new Address(80,80,asList(4,4));
        Apartment a = testbroker.addApartment(10,100,address,false,testseller);
        testbuyer.buyApartment(a, asList(ServiceKind.Design,ServiceKind.Cleaning,ServiceKind.Cleaning));
    }
    ///////////////////////////////////test users function///////////////////////////////////////////////////////
    //test average price
    @Test
    void AveragePrice() throws NotPositiveNumberException, NullValueException, ApartmentNotForSaleException, ApartmentAlreadyInTheSystemException, NotAuthorizedUserException, ApartmentNotInTheSystemException, NotApartmentsInTheRediusException {
        Address address = new Address(80,80,asList(4,4));
        Apartment a = testbroker.addApartment(10,100,address,false,testseller);
        assertEquals(testbuyer.averagePricePerM2InRadius(a,1), (double) 100 /10);
        Address address2 = new Address(81,81,asList(4,4));
        Address address3 = new Address(80,80,asList(1,4));
        Apartment c = testbroker.addApartment(11,300,address3,false,testseller);
        Apartment b = testbroker.addApartment(10,201,address2,false,testseller);
        assertEquals(testbuyer.averagePricePerM2InRadius(a,0.1),(double)(10+(double)300/11)/2);
        assertEquals(testbuyer.averagePricePerM2InRadius(a,1.5),(double)(10+(double)300/11+20.1)/3);
    }
    //test apartment sold in radius
    @Test
    void ApartmentsSold() throws NotPositiveNumberException, NullValueException, ApartmentNotForSaleException, ApartmentAlreadyInTheSystemException, NotAuthorizedUserException, ApartmentNotInTheSystemException, NotApartmentsInTheRediusException {
        Address address = new Address(80,80,asList(4,4));
        Apartment a = testbroker.addApartment(10,100,address,false,testseller);
        Address address2 = new Address(81,81,asList(4,4));
        Apartment b = testbroker.addApartment(10,200,address2,false,testseller);
        Address address3 = new Address(80,80,asList(1,4));
        Apartment c = testbroker.addApartment(10,300,address3,false,testseller);
        double radius=Math.sqrt(2);

        assertTrue(testbroker.apartmentsSoldInRadius(a,radius).isEmpty());
        testbuyer.buyApartment(a);
        assertEquals(1, testbroker.apartmentsSoldInRadius(a, radius).size());
        assertTrue(testbroker.apartmentsSoldInRadius(a,radius).contains(a));
        testbuyer.buyApartment(b);
        assertEquals(2, testbroker.apartmentsSoldInRadius(a, radius).size());
        assertTrue(testbroker.apartmentsSoldInRadius(a,radius).contains(b));
        testbuyer.buyApartment(c);
        assertEquals(3, testbroker.apartmentsSoldInRadius(a, radius).size());
        assertTrue(testbroker.apartmentsSoldInRadius(a,radius).contains(c));
        assertEquals(0, testbroker.apartmentsSoldInRadius(a, -1).size());
        assertEquals(2, testbroker.apartmentsSoldInRadius(a, 0).size());
    }
    //test apartment for sale in radius
    @Test
    void ApartmentsForSale() throws NotPositiveNumberException, NullValueException, ApartmentNotForSaleException, ApartmentAlreadyInTheSystemException, NotAuthorizedUserException, ApartmentNotInTheSystemException, NotApartmentsInTheRediusException {
        Address address = new Address(80,80,asList(4,4));
        Address address2 = new Address(81,81,asList(4,4));
        Address address3 = new Address(80,80,asList(1,4));
        double radius=Math.sqrt(2);

        Apartment a = testbroker.addApartment(10,100,address,false,testseller);
        assertEquals(1, testbroker.apartmentsForSaleInRadius(a, radius).size());
        assertTrue(testbroker.apartmentsForSaleInRadius(a,radius).contains(a));
        Apartment b = testbroker.addApartment(10,200,address2,false,testseller);
        assertEquals(2, testbroker.apartmentsForSaleInRadius(a, radius).size());
        assertTrue(testbroker.apartmentsForSaleInRadius(a,radius).contains(b));
        Apartment c = testbroker.addApartment(10,300,address3,false,testseller);
        assertEquals(3, testbroker.apartmentsForSaleInRadius(a, radius).size());
        assertTrue(testbroker.apartmentsForSaleInRadius(a,radius).contains(c));
        assertEquals(0, testbroker.apartmentsForSaleInRadius(a, -1).size());
        assertEquals(2, testbroker.apartmentsForSaleInRadius(a, 0).size());
        testbuyer.buyApartment(a);
        testbuyer.buyApartment(b);
        assertEquals(1, testbroker.apartmentsForSaleInRadius(a, radius).size());
        assertTrue(testbroker.apartmentsForSaleInRadius(a,radius).contains(c));
    }
    //test apartments by price in the radius
    @Test
    void ApartmentsByPricePerM2InRadius() throws NotPositiveNumberException, NullValueException, ApartmentNotForSaleException, ApartmentAlreadyInTheSystemException, NotAuthorizedUserException, ApartmentNotInTheSystemException, NotApartmentsInTheRediusException {
        Address address = new Address(80,80,asList(4,4));
        Apartment a = testbroker.addApartment(10,100,address,false,testseller);
        Address address2 = new Address(81,81,asList(4,4));
        Apartment b = testbroker.addApartment(10,200,address2,false,testseller);
        Address address3 = new Address(80,80,asList(1,4));
        Apartment c = testbroker.addApartment(10,300,address3,false,testseller);
        double radius=Math.sqrt(2);
        testbuyer.buyApartment(a);

        assertEquals(2, testbroker.apartmentsByPricePerM2InRadius(c,radius, ApartmentSearch.PriceComparison.LESS_THAN).size());
        assertFalse(testbroker.apartmentsByPricePerM2InRadius(c,radius, ApartmentSearch.PriceComparison.LESS_THAN).contains(c));
        assertTrue(testbroker.apartmentsByPricePerM2InRadius(c,radius, ApartmentSearch.PriceComparison.LESS_THAN).contains(a));
        assertEquals(1, testbroker.apartmentsByPricePerM2InRadius(a,radius, ApartmentSearch.PriceComparison.EQUAL).size());
        assertTrue(testbroker.apartmentsByPricePerM2InRadius(a,radius, ApartmentSearch.PriceComparison.EQUAL).contains(a));
        assertEquals(1, testbroker.apartmentsByPricePerM2InRadius(b,radius, ApartmentSearch.PriceComparison.GREATER_THAN).size());
        assertTrue(testbroker.apartmentsByPricePerM2InRadius(b,radius, ApartmentSearch.PriceComparison.GREATER_THAN).contains(c));
    }
    //test the notification
    @Test
    void Notifies() throws NotPositiveNumberException, NullValueException, ApartmentNotForSaleException, ApartmentAlreadyInTheSystemException, NotAuthorizedUserException, ApartmentNotInTheSystemException {
        Address address = new Address(80,80,asList(4,4));
        Apartment a = testbroker.addApartment(10,100,address,false,testseller);
        assertEquals(testseller.getNotification().getFirst(),testbroker.getName()+ " add your apartment to the agency: "+a);
        testbroker.editApartmentPrice(a,200);
        assertEquals(testseller.getNotification().size(),2);
        testbroker.editApartmentPrice(a,100);
        assertEquals(testseller.getNotification().size(),3);
        assertEquals(testseller.getNotification().getFirst(),testbroker.getName()+ " add your apartment to the agency: "+a);
        testbuyer.buyApartment(a,asList(ServiceKind.Cleaning,ServiceKind.Design,ServiceKind.Cleaning));
        assertEquals(testbroker.getNotification().getLast(),testbuyer.getName()+ " buy this apartment: "+a+"\nServices:\nCleaning services\nDesign services\ntotal price: "+(a.getPrice()+5500)+"\n");
        assertEquals(testseller.getNotification().getLast(),testbuyer.getName()+ " buy this apartment: "+a+"\nServices:\nCleaning services\nDesign services\ntotal price: "+(a.getPrice()+5500)+"\n");
    }

}
