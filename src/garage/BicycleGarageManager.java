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
	 * Konstruktorn f�r en BicycleGarageManager.
	 *
	 * @param capacity
	 *           maximala m�ngden cyklar som f�r plats i garaget
	 */
	public BicycleGarageManager(int capacity, BicycleGarageDatabase database) {
		charCollecter = new PinCharCollector(database, null);
		this.database = database;
		capacity = 0;
		bikesInGarage = 0;
	}
	
	
	/** 
	 * Registrerar h�rdvara s� att BicycleGarageManager vet vilka drivrutiner
	 * som ska h�mtas.
	 * 
	 * @param printer
	 *            h�rdvarudrivrutinen f�r streckkodsskrivaren
	 * @param entryLock
	 *            h�rdvarudrivrutinen f�r ing�ngsl�set
	 * @param exitLock
	 *            h�rdvarudrivrutinen f�r utg�ngsl�set
	 * @param terminal
	 *            h�rdvarudrivrutinen f�r PIN-kodsterminalen
	 */
	public void registerHardwareDrivers(BarcodePrinter printer,ElectronicLock entryLock, ElectronicLock exitLock,PinCodeTerminal terminal) {
		this.printer = printer;
		this.entryLock = entryLock;
		this.exitLock = exitLock;
		this.terminal = terminal;
		charCollecter = new PinCharCollector(database, terminal);

	}

	/**
	 * Kallas n�r en cykel�gare har anv�nt streckkodsl�saren vid ing�ngsd�rren.
	 * Cykel-ID ska vara en str�ng av 5 tecken d�r varje tecken kan vara '0',
	 * '1',... '9'.
	 * 
	 * @param barcode
	 *            cykelns streckkod
	 */
	public void entryBarcode(String barcode) {
		if (bikesInGarage < capacity && database.checkBarcodeRegistered(barcode)) {
			bikesInGarage++;
			entryLock.open(10);
		}
	}

	/**
	 * Kallas n�r en cykel�gare har anv�nt streckkodsl�saren vid utg�ngsd�rren.
	 * Cykel-ID ska vara en str�ng av 5 tecken d�r varje tecken kan vara '0',
	 * '1',... '9'.
	 * 
	 * @param barcode
	 *            cykelns streckkod
	 */
	public void exitBarcode(String barcode) {
		if (database.checkBarcodeRegistered(barcode) && database.checkBikeRetrievable(barcode)) {
			bikesInGarage--;
			exitLock.open(10);
		}
	}

	/**
	 * Kallas n�r en cykel�gare har tryckt p� en knapp vid PIN-kodsterminalen
	 * vid ing�ngsd�rren. F�ljande tecken kan tryckas: '0', '1',... '9','*','#'.
	 * 
	 * @param c
	 *            tecknet som har tryckts
	 */
	public void entryCharacter(char c) {
		charCollecter.add(c);
	}

	/**
	 * Registrerar databasen som skall anv�ndas.
	 * 
	 * @param database
	 *            databasen som ska registreras
	 */
	public void registerDatabase(BicycleGarageDatabase database) {
		this.database = database;
	}
}