package IntegrationTest;

import java.io.File;

import garage.*;
import drivers.*;
import testDrivers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TestGarage {
	private BicycleGarageDatabase database;
	private BicycleGarageManager manager;
	private ElectronicLock entryLock;
	private ElectronicLock exitLock;
	private PinCodeTerminal terminal;
	private BarcodePrinter printer;
	private User user;
	private String name, telNr, pin, barcode, personNumber, dir, nonExistingDir;

	@Before
	public void make() {
		name = "Rasha";
		telNr = "0720457387";
		pin = "1234";
		barcode = "12345";
		personNumber = "951120-0001";
		dir = "testDirectory";
		nonExistingDir = "testRemovedDirectory";
		entryLock = new ElectronicLockTestDriver("Entry lock");
		exitLock = new ElectronicLockTestDriver("Exit lock");
		printer = new BarcodePrinterTestDriver();
		terminal = new PinCodeTerminalTestDriver();
		database = new BicycleGarageDatabase(300);
		database.setDirectory(dir);
		manager = new BicycleGarageManager(database);
		manager.registerHardwareDrivers(printer, entryLock, exitLock, terminal);
		database.addUser(pin, barcode, name, telNr, personNumber);
		user = database.getUserByBarcode(barcode);
		terminal.register(manager);

	}

	@Test
	public void testAddingUser() {
		assertNotNull("User is null", user);
		assertEquals("There are bikes in garage", 0, user.getBikesInGarage());
		assertEquals("User's PIN-code is not 1234", pin, user.getPin());
		assertEquals("User's phonenumber is not 0720457387", telNr,
				user.getTelNr());
		assertEquals("User's name is not Rasha", name, user.getName());
		assertEquals("User's barcode is not 12345", barcode, user.getBarcode());
		assertEquals("User's personnumber is not 951120-0001", personNumber,
				user.getPersonNr());
	}

	@Test
	public void testBikeEntry() {
		assertNotNull("User is null", user);
		manager.entryBarcode(barcode);
		user = database.getUserByBarcode(barcode);
		assertEquals("There is more or less than one bike in garage", 1,
				user.getBikesInGarage());
	}

	@Test
	public void testBikeExit() {
		assertNotNull("User is null", user);
		manager.entryBarcode(barcode);
		user = database.getUserByBarcode(barcode);
		for (int i = 0; i < 4; i++) {
			manager.entryCharacter(pin.charAt(i));
		}
		manager.exitBarcode(barcode);
		user = database.getUserByBarcode(barcode);
		assertEquals("User hasn't taken bike out of garage", 0,
				user.getBikesInGarage());
	}

	@Test
	public void testRemoveUser() {
		assertNotNull("User is null", user);
		database.removeUser(barcode);
		user = database.getUserByBarcode(barcode);
		assertNull("User is not null", user);
		File f = new File(dir + "/" + barcode);
		assertFalse("User file has not been deleted", f.exists());
	}

	@Test
	public void testSaveAndLoad() {
		database.setDirectory(dir);
		database.save();
		user = database.getUserByBarcode(barcode);
		database = new BicycleGarageDatabase(300);
		manager = new BicycleGarageManager(database);
		manager.registerHardwareDrivers(printer, entryLock, exitLock, terminal);
		database.setDirectory(dir);
		database.load();
		User tempUser = database.getUserByBarcode(barcode);
		assertTrue("User is not the same/saved", user.equals(tempUser));

	}

	@Test
	public void testOperatorPinChange() {
		String newPin = "2345";
		String currentPin;
		if (database.checkPinRegistered(pin)) {
			int bikesInGarage = database.getUserByBarcode(barcode).getBikesInGarage();
			database.removeUser(barcode);
			database.addUser(newPin, barcode, name, telNr, personNumber);
			database.modifyBikesInGarage(barcode, bikesInGarage);
			user = database.getUserByBarcode(barcode);
			currentPin = user.getPin();
			assertEquals("Name has changed", name, user.getName());
			assertEquals("Barcode has changed", barcode, user.getBarcode());
			assertEquals("Telnr has changed", telNr, user.getTelNr());
			assertEquals("Personnumber has changed", personNumber, user.getPersonNr());
			assertEquals("BikesInGarage has changed", bikesInGarage, user.getBikesInGarage());
			assertEquals("Pin has not changed", newPin, currentPin);
		}
	}

	@Test
	public void testTerminalPinChange() {
		String newPin = "9876";
		String currentPin;
		manager.entryCharacter('*');
		for (int i = 0; i < 4; i++) {
			manager.entryCharacter(pin.charAt(i));
		}
		for (int i = 0; i < 5; i++) {
			manager.entryCharacter(barcode.charAt(i));
		}
		for (int i = 0; i < 4; i++) {
			manager.entryCharacter(newPin.charAt(i));
		}
		currentPin = database.getUserByBarcode(barcode).getPin();

		assertEquals("Pin has not changed", newPin, currentPin);
	}
	
	@Test
	public void testIncorrectTerminalPinChange() {
		String newPin = "9876";
		String currentPin;
		manager.entryCharacter('*');
		manager.entryCharacter('1');
		manager.entryCharacter('1');
		manager.entryCharacter('1');
		manager.entryCharacter('1');
		for (int i = 0; i < 5; i++) {
			manager.entryCharacter(barcode.charAt(i));
		}
		for (int i = 0; i < 4; i++) {
			manager.entryCharacter(newPin.charAt(i));
		}
		currentPin = database.getUserByBarcode(barcode).getPin();

		assertEquals("Pin has changed", pin, currentPin);
	}
	
	@Test
	public void testNonExistingUserTerminalPinChange() {
		String nonExistingPin = "1111";
		String nonExistingBarcode = "11111";
		String newPin = "9876";
		assertNull("User exists", database.getUserByBarcode(nonExistingBarcode));
		manager.entryCharacter('*');
		for (int i = 0; i < 4; i++) {
			manager.entryCharacter(nonExistingPin.charAt(i));
		}
		for (int i = 0; i < 5; i++) {
			manager.entryCharacter(nonExistingBarcode.charAt(i));
		}
		for (int i = 0; i < 4; i++) {
			manager.entryCharacter(newPin.charAt(i));
		}
		assertTrue("PinCharList is not empty", manager.isPinCharListEmpty());
	}

	@Test
	public void testEditUsersPhoneNbr() { // barcode skall bytas ut mot
											// personnummer senare
		if (database.checkBarcodeRegistered(barcode)) {
			String newPhoneNbr = "0734567890";
			user = database.getUserByBarcode(barcode);
			newPhoneNbr = user.getTelNr();
			assertEquals("Phonenumber hasn't changed", newPhoneNbr,
					user.getTelNr());
		}
	}

	@Test
	public void testInvalidBarcode() {
		String invalidBarcode = "123456";
		database.addUser(pin, invalidBarcode, name, telNr, "560605-3456");
		user = database.getUserByPersonnumber("560605-3456");
		assertFalse("Barcode is registered",
				database.checkBarcodeRegistered(invalidBarcode));
	}

	@Test
	public void testNonExistingPin() {
		String noPin = "3456";
		assertFalse("Pin exists", database.checkPinRegistered(noPin));

	}
	
	@Test
	public void testNonExistingPinTerminal() {
		assertFalse("Bike with barcode 12345 is retrievable", database.checkBikeRetrievable(barcode));
		assertFalse("Pin 1111 is registered", database.checkPinRegistered("1111"));
		for (int i = 0; i < 4; i++) {
			manager.entryCharacter('1');
		}
		assertFalse("Bike with barcode 12345 is retrievable", database.checkBikeRetrievable(barcode));
	}

	@Test
	public void testInvalidPin() {
		String invalidPin = "12345";
		database.addUser(invalidPin, "34567", name, telNr, "560605-3456");
		user = database.getUserByBarcode("34567");
		assertFalse("Pin is registered",
				database.checkPinRegistered(invalidPin));
	}

	@Test
	public void testValidPin() {
		assertTrue("Pin is not registered", database.checkPinRegistered(pin));
	}

	@Test
	public void testEnterWithPin() {
		for (int i = 0; i < 4; i++) {			
			manager.entryCharacter(pin.charAt(i));
		}
		assertTrue("Bike is not retrievable", database.checkBikeRetrievable(barcode));
	}

	@Test
	public void testEnterGarageAfterTimeout() {
		manager.entryCharacter('1');
		try {
			Thread.sleep(11 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertTrue("PinCharList is not empty", manager.isPinCharListEmpty());
		manager.entryCharacter('2');
		try {
			Thread.sleep(11 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertTrue("PinCharList is not empty", manager.isPinCharListEmpty());
	}

	@Test
	public void testExitGarageAfterTimeout() {
		manager.entryBarcode(barcode);
		for (int i = 0; i < 4; i++) {
			manager.entryCharacter(pin.charAt(i));
		}
		assertTrue("Bike is not retrievable", database.checkBikeRetrievable(barcode));
		try {
			Thread.sleep(31 * 60 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertFalse("Bike is retrievable", database.checkBikeRetrievable(barcode));
	}

	@Test
	public void testPinTerminalTimeoutCharlistClear() {
		assertTrue("Pin-terminalen är inte clearad innan inmatning",
				manager.isPinCharListEmpty());
		assertNotNull("Terminal is null", terminal);
		manager.entryCharacter('1');
		assertFalse("Pin-terminalen är clearad", manager.isPinCharListEmpty());
		
//		try {
//			wait(11 * 1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		
		try {
			Thread.sleep(11 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertTrue(
				"Pin-terminalen är inte clearad 10 sekunder efter inmatning",
				manager.isPinCharListEmpty());
	}

	@Test
	public void testCheckBikesRetrievableAfterExit() {
		if (database.checkBarcodeRegistered(barcode)) {
			manager.entryBarcode(barcode);
			for (int i = 0; i < 4; i++) {
				manager.entryCharacter(pin.charAt(i));
			}
			manager.exitBarcode(barcode);
		}
		assertFalse("Bike is retrievable",
				database.checkBikeRetrievable(barcode));
	}

	@Test
	public void testSettingSamePinForTwoUsers() {
		database.addUser(pin, "56789", "Vilhelm", "0701234567", "940506-0001");
		user = database.getUserByBarcode("56789");
		assertEquals("Pins are not the same", pin, user.getPin());
	}

	@Test
	public void testGetUserByPersonnumber() {
		assertEquals("", database.getUserByPersonnumber(personNumber)
					.getPersonNr(), personNumber);
	}

	@Test
	public void testExitWithoutEnteringPin() {
		manager.entryBarcode(barcode);
		manager.exitBarcode(barcode);
		assertEquals("", 1, database.getUserByBarcode(barcode)
				.getBikesInGarage());
	}
	
	@Test
	public void testSaveWithNonExistingDirectory() {
		File f = new File(nonExistingDir);
		if (f.exists()) {
			File[] files = f.listFiles();
			for (File fSub : files) {
				fSub.delete();
			}
			f.delete();
		}
		
		f = new File(nonExistingDir);
		assertFalse("nonExistingDir exists", f.exists());
		database.setDirectory(nonExistingDir);
		database.save();
		
		database = new BicycleGarageDatabase(300);
		manager = new BicycleGarageManager(database);
		manager.registerHardwareDrivers(printer, entryLock, exitLock, terminal);
		database.setDirectory(nonExistingDir);
		database.load();
		User tempUser = database.getUserByBarcode(barcode);
		assertTrue("User is not the same/saved", user.equals(tempUser));
	}
	
	@Test
	public void testUserEquals() {
		String secondPin = "4321";
		String secondBarcode = "54321";
		String secondName = "Tjalle";
		String secondTelNr = "0739000000";
		String secondPersonNr = "99999999";
		
		user = database.getUserByBarcode(barcode);
		User secondUser = new User(secondName, secondTelNr, secondBarcode, secondPin,secondPersonNr);
		User thirdUser = new User(name, telNr, barcode, pin,personNumber);
		assertFalse("Users are the same", user.equals(secondUser));
		assertTrue("Users are not the same", user.equals(thirdUser));
	}
	
	@Test
	public void testGetDirectory() {
		database.setDirectory(dir);
		assertEquals("The directory is not testDirectory", dir, database.getDirectory());
	}
	
	@Test
	public void testThreadsInPinChar() {
		String newPin = "9876";
		String currentPin;
		manager.entryCharacter('*');
		for (int i = 0; i < 4; i++) {
			manager.entryCharacter(pin.charAt(i));
			try {
				Thread.sleep(9 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for (int i = 0; i < 5; i++) {
			manager.entryCharacter(barcode.charAt(i));
			try {
				Thread.sleep(9 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for (int i = 0; i < 4; i++) {
			manager.entryCharacter(newPin.charAt(i));
			try {
				Thread.sleep(9 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		currentPin = database.getUserByBarcode(barcode).getPin();

		assertEquals("Pin has not changed", newPin, currentPin);
	}
	
	@Test
	public void testLoadCapacity() {
		database.load();
		int oldCap = database.getCapacity();
		database = new BicycleGarageDatabase(oldCap);
		database.setCapacity(oldCap + 100);
		database.setDirectory(dir);
		database.save();
		
		database = new BicycleGarageDatabase(1);
		database.load();
		int currentCap = database.getCapacity();
		assertNotEquals("Capacity is not the same", oldCap, currentCap);
		
	}
	
	@Test
	public void testSetCapacityLessThanBikesInGarage() {
		manager.entryBarcode(barcode);
		manager.entryBarcode(barcode);
		manager.entryBarcode(barcode);
		assertFalse("Capacity was changed", database.setCapacity(2));
	}

}
