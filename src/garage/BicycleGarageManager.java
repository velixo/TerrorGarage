package garage;

import drivers.BarcodePrinter;
import drivers.ElectronicLock;
import drivers.PinCodeTerminal;

//import drivers;

public class BicycleGarageManager {
	private PinCharCollector charCollecter;
	private ElectronicLock entryLock;
	private ElectronicLock exitLock;
	private BarcodePrinter printer;
	private PinCodeTerminal terminal;
	private BicycleGarageDatabase database;

	
	/**
	 * Konstruktorn för en BicycleGarageManager.
	 *
	 * @param database
	 *           cykelgaragets databas
	 */
	public BicycleGarageManager(BicycleGarageDatabase database) {
		this.database = database;
	}
	
	
	/** 
	 * Registrerar hårdvara så att BicycleGarageManager vet vilka drivrutiner
	 * som ska hämtas.
	 * 
	 * @param printer
	 *            hårdvarudrivrutinen för streckkodsskrivaren
	 * @param entryLock
	 *            hårdvarudrivrutinen för ingångslåset
	 * @param exitLock
	 *            hårdvarudrivrutinen för utgångslåset
	 * @param terminal
	 *            hårdvarudrivrutinen för PIN-kodsterminalen
	 */
	public void registerHardwareDrivers(BarcodePrinter printer,ElectronicLock entryLock, ElectronicLock exitLock,PinCodeTerminal terminal, PinCharCollector charCollecter) {
		this.printer = printer;
		this.entryLock = entryLock;
		this.exitLock = exitLock;
		this.terminal = terminal;
		this.charCollecter = charCollecter;
//		charCollecter = new PinCharCollector(database, this.terminal, this.entryLock);

	}

	/**
	 * Kallas när en cykelägare har använt streckkodsläsaren vid ingångsdörren.
	 * Cykel-ID ska vara en sträng av 5 tecken där varje tecken kan vara '0',
	 * '1',... '9'.
	 * 
	 * @param barcode
	 *            cykelns streckkod
	 */
	public void entryBarcode(String barcode) {
		if (database.checkBarcodeRegistered(barcode) && !database.isGarageFull()) {
			database.modifyBikesInGarage(barcode, 1);
			entryLock.open(10);
		}
	}

	/**
	 * Kallas när en cykelägare har använt streckkodsläsaren vid utgångsdörren.
	 * Cykel-ID ska vara en sträng av 5 tecken där varje tecken kan vara '0',
	 * '1',... '9'.
	 * 
	 * @param barcode
	 *            cykelns streckkod
	 */
	public void exitBarcode(String barcode) {
		if (database.checkBarcodeRegistered(barcode) && database.checkBikeRetrievable(barcode)) {
			database.modifyBikesInGarage(barcode, -1);
			exitLock.open(10);
		}
	}

	/**
	 * Kallas när en cykelägare har tryckt på en knapp vid PIN-kodsterminalen
	 * vid ingångsdörren. Följande tecken kan tryckas: '0', '1',... '9','*','#'.
	 * 
	 * @param c
	 *            tecknet som har tryckts
	 */
	public void entryCharacter(char c) {
		charCollecter.add(c);
	}

//				Kom på att denna aldrig används då man alltid behöver en database i konstruktorn på managern
//	/**
//	 * Registrerar databasen som skall användas.
//	 * 
//	 * @param database
//	 *			databasen som ska registreras
//	 */
//	public void registerDatabase(BicycleGarageDatabase database) {
//		this.database = database;
//	}
	
	/** 
	 * Skriver ut streckkoden barcode.
	 * 
	 * @param barcode
	 * 		streckkoden som ska skrivas ut
	 */
	public void print(String barcode) {
		printer.printBarcode(barcode);
	}
}