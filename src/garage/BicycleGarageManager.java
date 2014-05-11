import drivers.BarcodePrinter;
import drivers.ElectronicLock;
import drivers.PinCodeTerminal;

//import drivers;

public class BicycleGarageManager {
	private int capacity;
	private	int bikesInGarage;
	private PinCharCollector charCollecter;
	private ElectronicLock entryLock;
	private ElectronicLock exitLock;
	private BarcodePrinter printer;
	private PinCodeTerminal terminal;
	private BicycleGarageDatabase database;

	
	/**
	 * Konstruktorn för en BicycleGarageManager.
	 *
	 * @param capacity
	 *           maximala mängden cyklar som får plats i garaget
	 */
	public BicycleGarageManager(int capacity, BicycleGarageDatabase database) {
		charCollecter = new PinCharCollector;
		this.database = database;
		capacity = 0;
		bikesInGarage = 0;
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
	public void registerHardwareDrivers(BarcodePrinter printer,ElectronicLock entryLock, ElectronicLock exitLock,PinCodeTerminal terminal) {
		this.printer = printer;
		this.entryLock = entryLock;
		this.exitLock = exitLock;
		this.terminal = terminal;
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
		if (bikesInGarage < capacity && checkBarcodeRegistered(barcode)) {
			bikesInGarage++;
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
		if (checkBarcodeRegistered(barcode) && checkBikeRetrievable(barcode)) {
			bikesInGarage--;
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

	/**
	 * Registrerar databasen som skall användas.
	 * 
	 * @param database
	 *            databasen som ska registreras
	 */
	public void registerDatabase(BicycleGarageDatabase database) {
		this.database = database;
	}
}