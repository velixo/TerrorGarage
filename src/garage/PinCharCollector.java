package garage;

import java.lang.StringBuilder;
import drivers.PinCodeTerminal;
import drivers.ElectronicLock;

public class PinCharCollector {
	private StringBuilder pinCharList;
	private BicycleGarageDatabase database;
	private PinCodeTerminal terminal;
	private ElectronicLock entryLock;
	private final int BLINKING_OFF = 0;
	private final int BLINKING_SINGLE = 1;
	private final int BLINKING_DOUBLE = 2;
	private final int BLINKING_TRIPLE = 3;
	private BlinkingTask task;
	private Thread blinkThread;
	private boolean blinkingStarted;
	private long timeLastCharClick;
	private boolean firstTimeStarted;
	
	/**
	* Konstruktorn f�r en PinCharCollector.
	*/
	public PinCharCollector(BicycleGarageDatabase database, PinCodeTerminal terminal, ElectronicLock entryLock) {
		this.database = database;
		this.terminal = terminal;
		this.entryLock = entryLock;
		pinCharList = new StringBuilder();
		task = new BlinkingTask();
		task.setBlinkMode(BLINKING_OFF);
		blinkingStarted = false;
		firstTimeStarted = true;
	}

	/**
	* L�gger till ett tecken i den nuvarande 4-siffriga PIN-koden som h�ller p�
	* att matas in. Om f�rsta tecknet i listan �r en stj�rna, �*�, s� kommer
	* den att anropa en metod som beter sig enligt "byte-av-PIN"
	* anv�ndarfallet. Om det inte �r en stj�rna som f�rsta tecken, v�nta tills
	* 4 tecken �r inmatade.
	* 
	* @param c
	*            tecknet som l�ggs till
	*/
	public void add(char c) {
		if (firstTimeStarted) {
			firstTimeStarted = false;
			timeLastCharClick = System.currentTimeMillis();
		} else if (System.currentTimeMillis() > timeLastCharClick + 10 * 1000) {
			clear();
		}
		timeLastCharClick = System.currentTimeMillis();
		
		pinCharList.append(c);
		if (pinCharList.charAt(0) == '*') {
			
			if (pinCharList.length() < 5) {
				if (task.getBlinkMode() < BLINKING_SINGLE) {
					blinkingStarted = true;
					blinkThread = new Thread(task);
					task.setBlinkMode(BLINKING_SINGLE);
					blinkThread.start();
				}
			}
			
			else if (pinCharList.length() >= 5 && pinCharList.length() < 10) {
				if (task.getBlinkMode() < BLINKING_DOUBLE) {
					blinkThread.interrupt();
					blinkThread = new Thread(task);
					task.setBlinkMode(BLINKING_DOUBLE);
					blinkThread.start();
				}
			}
			
			else if (pinCharList.length() >= 10 && pinCharList.length() < 14) {
				if (task.getBlinkMode() < BLINKING_TRIPLE) {
					blinkThread.interrupt();
					blinkThread = new Thread(task);
					task.setBlinkMode(BLINKING_TRIPLE);
					blinkThread.start();
				}
			}
			
			else {				// *, oldPin, barcode, samt newPin har matats in. �ndra cykel�garens pin, och l�s sedan upp d�rren.
				blinkingStarted = false;
				task.setBlinkMode(BLINKING_OFF);
				blinkThread.interrupt();
				String oldPin = pinCharList.substring(1,5);
				String barcode = pinCharList.substring(5,10);
				String newPin = pinCharList.substring(10,14);
				User u = database.getUserByBarcode(barcode);
				
				if (u.getPin().equals(oldPin)) {
					database.changeUserPin(barcode, newPin);
					database.setBikesRetrievable(newPin);
					entryLock.open(10);
					terminal.lightLED(terminal.GREEN_LED, 3);
					clear();
				} else {
					terminal.lightLED(terminal.RED_LED, 3);
					clear();
				}
			}
			
			
		} else if (pinCharList.length() >= 4) {			// pinkod har matats in, kolla om pinen �r registrerad. �r den det, l�s upp d�rren.
			String pin = pinCharList.substring(0, 4);
			clear();
			if (database.checkPinRegistered(pin)) {				
				database.setBikesRetrievable(pin);
				entryLock.open(10);
				//kommer koden forts�tta? eller stannar den upp i 3 sekunder?
				terminal.lightLED(terminal.GREEN_LED, 3);
				clear();
			} else {
				terminal.lightLED(terminal.RED_LED, 3);
				clear();
			}
		}
		
	}
	
	/**
	* Rensar ut kod-bufferten.
	*/
	private void clear() {
		int length = pinCharList.length();
		pinCharList.delete(0, length);
	}
	
	/** OBS! F�R ENDAST ANV�NDAS GENOM/I BICYCLEGARAGEMANAGER
	 * Returnar om pin-terminalen har n�gra inmatningar sparade eller inte (om listan med inmatningar/chars
	 * �r tom eller ej)
	 */
	public boolean isPinCharListEmpty() {
		return pinCharList.toString().isEmpty();
//		return (pinCharList.length() == 0);
	}
	
	private class BlinkingTask implements Runnable {
		private volatile int blinkMode;
		
		public BlinkingTask () {
			blinkMode = BLINKING_OFF;
		}
		
		public void run() {
			try {
				long interval = 1;	//SKA EGENTLIGEN VARA 0.3 SEKUNDER. S�TTS TILL 1 I V�NTAN P� KONFIRMATION ATT �NDRA PINCODETERMINAL OCH TESTDRIVER
				long wait = 4;		//SKA EGENTLIGEN VARA 1.5 SEKUNDER. S�TTS TILL 3 I V�NTAN P� KONFIRMATION ATT �NDRA PINCODETERMINAL OCH TESTDRIVER
				if (blinkMode == BLINKING_SINGLE) {
					while (!Thread.currentThread().isInterrupted()){
						System.out.println("BLINKING SINGLE");
						terminal.lightLED(terminal.GREEN_LED, (int)interval);
						Thread.sleep(wait * 1000);
					}
				} else if (blinkMode == BLINKING_DOUBLE) {
					while (!Thread.currentThread().isInterrupted()){
						System.out.println("BLINKING DOUBLE");
						terminal.lightLED(terminal.GREEN_LED, (int)interval);
						Thread.sleep(interval * 1000);
						terminal.lightLED(terminal.GREEN_LED, (int)interval);
						Thread.sleep(wait * 1000);
					}
				} else if (blinkMode == BLINKING_TRIPLE) {
					while (!Thread.currentThread().isInterrupted()){
						System.out.println("BLINKING TRIPLE");
						terminal.lightLED(terminal.GREEN_LED, (int)interval);
						Thread.sleep(interval * 1000);
						terminal.lightLED(terminal.GREEN_LED, (int)interval);
						Thread.sleep(interval * 1000);
						terminal.lightLED(terminal.GREEN_LED, (int)interval);
						Thread.sleep(wait * 1000);
					}
				}
			} catch (InterruptedException iex) {
				blinkMode = BLINKING_OFF;
				
				Thread.currentThread().interrupt();
				return;
			}
			
		}
		
		
		public void setBlinkMode(int bMode) {
			blinkMode = bMode;
		}
		
		public int getBlinkMode() {
			return blinkMode;
		}
		
	}
			
}