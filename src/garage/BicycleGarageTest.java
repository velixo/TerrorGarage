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
		Operator op = new Operator(database, manager);
		
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
        
<<<<<<< HEAD
        while(true) {
        	if (!op.running()) {
        		entryLock = null;
        		exitLock = null;
        		printer = null;
        		terminal = null;
        		readerEntry = null;
        		readerExit = null;
        		
				System.exit(0);
			}
        }
=======
//        while(true) {
//        	if (!op.running()) {
//				System.exit(0);
//			}
//        }
>>>>>>> daa84e46c1c76bc77d86cb5f5f1a068bbc469e14
	}
}
