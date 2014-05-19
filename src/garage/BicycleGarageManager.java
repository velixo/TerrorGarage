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
	 * Konstruktorn f�r en BicycleGarageManager.
	 *
	 * @param database
	 *           cykelgaragets databas
	 */
	public BicycleGarageManager(BicycleGarageDatabase database) {
		this.database = database;
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
	public void registerHardwareDrivers(BarcodePrinter printer,ElectronicLock entryLock, ElectronicLock exitLock,PinCodeTerminal terminal, PinCharCollector charCollecter) {
		this.printer = printer;
		this.entryLock = entryLock;
		this.exitLock = exitLock;
		this.terminal = terminal;
		this.charCollecter = charCollecter;
//		charCollecter = new PinCharCollector(database, this.terminal, this.entryLock);

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
		if (database.checkBarcodeRegistered(barcode) && !database.isGarageFull()) {
			database.modifyBikesInGarage(barcode, 1);
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
			database.modifyBikesInGarage(barcode, -1);
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

//				Kom p� att denna aldrig anv�nds d� man alltid beh�ver en database i konstruktorn p� managern
//	/**
//	 * Registrerar databasen som skall anv�ndas.
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