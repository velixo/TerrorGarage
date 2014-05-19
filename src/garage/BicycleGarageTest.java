package garage;

import drivers.*;
import testDrivers.*;

public class BicycleGarageTest {
	
	/**
	 *Används för att starta igång hela systemet.
	 */
	public static void main(String[]args) {
		BicycleGarageDatabase database = new BicycleGarageDatabase(100);
		BicycleGarageManager manager = new BicycleGarageManager(database);
		new Operator(database, manager);
		
        ElectronicLock entryLock = new ElectronicLockTestDriver("Entry lock");
        ElectronicLock exitLock = new ElectronicLockTestDriver("Exit lock");
        BarcodePrinter printer = new BarcodePrinterTestDriver();
        PinCodeTerminal terminal = new PinCodeTerminalTestDriver();
//		PinCharCollector charCollecter = new PinCharCollector(database, terminal, entryLock);
        manager.registerHardwareDrivers(printer, entryLock, exitLock, terminal);
        terminal.register(manager);
        BarcodeReader readerEntry = new BarcodeReaderEntryTestDriver();
        BarcodeReader readerExit = new BarcodeReaderExitTestDriver();
        readerEntry.register(manager);
        readerExit.register(manager);
	}
}
