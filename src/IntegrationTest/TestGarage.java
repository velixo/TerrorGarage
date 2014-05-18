package IntegrationTest;

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
	private PinCharCollector pinChar;
	private BarcodePrinter printer;
	private User user;
	private String namn, telNr, pin, barcode, personNumber, dir;

	@Before
	public void make() {
		namn = "Rasha";
		telNr = "0720457387";
		pin = "1234";
		barcode = "12345";
		personNumber = "951120-0001";
		dir = "/Users/rashaelmanzalawy/Desktop/Hej";
		entryLock = new ElectronicLockTestDriver("Entry lock");
		exitLock = new ElectronicLockTestDriver("Exit lock");
		printer = new BarcodePrinterTestDriver();
		terminal = new PinCodeTerminalTestDriver();
		database = new BicycleGarageDatabase(300);
		manager = new BicycleGarageManager(database);
		manager.registerHardwareDrivers(printer, entryLock, exitLock, terminal);
		pinChar = new PinCharCollector(database, terminal, entryLock);
		database.addUser(pin, barcode, namn, telNr, personNumber);
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
		assertEquals("User's name is not Rasha", namn, user.getName());
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
		// assertNotNull("User is null", user);
		manager.entryBarcode(barcode);
		user = database.getUserByBarcode(barcode);
		for (int i = 0; i < 4; i++) {
			manager.entryCharacter(pin.charAt(i));
		}
		manager.exitBarcode(barcode);
		user = database.getUserByBarcode(barcode);
		assertEquals("User hasn't taken bike out of garage", 0,
				user.getBikesInGarage());

		// System.out.println(user.getBikesInGarage());
		// lägg till cykel och ta ut den, assertEquals user.bikesinGarage() = 0?
	}

	@Test
	public void testRemoveUser() {
		assertNotNull("User is null", user);
		database.removeUser(barcode);
		user = database.getUserByBarcode(barcode);
		assertNull("User is not null", user);
	}

	@Test
	public void testSaveAndLoad() {
		database.setDirectory(dir);
		database.save();
		database = new BicycleGarageDatabase(300);
		manager = new BicycleGarageManager(database);
		manager.registerHardwareDrivers(printer, entryLock, exitLock, terminal);
		database.setDirectory(dir);
		database.load();
		assertEquals("User's barcode is not the same after database restart ",
				barcode, database.getUserByBarcode(barcode).getBarcode());

	}

	@Test
	public void testOperatorPinChange() {
		String newPin = "2345";
		String currentPin;
		if (database.checkPinRegistered(pin)) {
			database.changeUserPin(barcode, newPin);
			currentPin = user.getPin();
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
	public void testprintBarcodes() {
		if (database.checkBarcodeRegistered(barcode)) {
			manager.print(barcode);
			manager.print(barcode);

			// inte klar än
		}
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
		database.addUser(pin, invalidBarcode, namn, telNr, "560605-3456");
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
	public void testInvalidPin() {
		String invalidPin = "12345";
		database.addUser(invalidPin, "34567", namn, telNr, "560605-3456");
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
		// TODO;
		if (database.checkPinRegistered(pin)) {
			for (int i = 0; i < 4; i++) {
				manager.entryCharacter(pin.charAt(i));
			}
		}
		if (database.checkBikeRetrievable(barcode)) {
			try {
				Thread.sleep(5);
			} catch (InterruptedException iex) {

			}

		}
	}

	@Test
	public void testEnterGarageAfterTimeout() {
		// TODO;
		if (database.checkPinRegistered(barcode)) {
			for (int i = 0; i < 4; i++) {
				manager.entryCharacter(pin.charAt(i));
			}
		}
	}

	@Test
	public void testExitGarageAfterTimeout() {
		if (!database.checkBikeRetrievable(barcode)) {

		}
	}

	@Test
	public void testPinTerminalTimeoutCharlistClear() {
		assertTrue("Pin-terminalen är inte clearad innan inmatning",
				pinChar.isPinCharListEmpty());
		assertNotNull("Terminal is null", terminal);
		manager.entryCharacter('1');
		assertFalse("Pin-terminalen är clearad", pinChar.isPinCharListEmpty());
		try {
			wait(11 * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// try {
		// Thread.sleep(11 * 1000);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		assertTrue(
				"Pin-terminalen är inte clearad 10 sekunder efter inmatning",
				pinChar.isPinCharListEmpty());
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
		if (database.getUserByPersonnumber(personNumber) != null) {
			assertEquals("", database.getUserByPersonnumber(personNumber)
					.getPersonNr(), personNumber);
		}
	}

	@Test
	public void testExitWithoutEnteringPin() {
		manager.entryBarcode(barcode);
		manager.exitBarcode(barcode);
		assertEquals("", 1, database.getUserByBarcode(barcode)
				.getBikesInGarage());
	}

}
