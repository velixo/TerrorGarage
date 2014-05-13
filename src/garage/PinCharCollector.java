package garage;

import java.lang.StringBuilder;
import drivers.PinCodeTerminal;

public class PinCharCollector {
	private StringBuilder pinCharList;
	private BicycleGarageDatabase database;
	private PinCodeTerminal terminal;
	private int lightMode;
	private final int BLINKING_OFF = 0;
	private final int BLINKING_SINGLE = 1;
	private final int BLINKING_DOUBLE = 2;
	private final int BLINKING_TRIPLE = 3;
	private MyTask task;
	
	/**
	* Konstruktorn f�r en PinCharCollector.
	*/
	public PinCharCollector(BicycleGarageDatabase database, PinCodeTerminal terminal) {
		this.database = database;
		this.terminal = terminal;
		pinCharList = new StringBuilder();
		lightMode = BLINKING_OFF;
		task = new MyTask();
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
		pinCharList.append(c);
		if (pinCharList.charAt(0) == '*') {
			if (pinCharList.length() < 5) {
				//b�rja blinka BLINKING_SINGLE
				if (!task.hasBlinkingStarted()) {
					task.setBlinkMode(BLINKING_SINGLE);
					task.run();
				}
			} else if (pinCharList.length() >= 5 && pinCharList.length() <= 18) {
				//b�rja blinka BLINKING_DOUBLE
				if (task.hasBlinkingStarted()) {
					task.stop();
				}
				if (!task.hasBlinkingStarted()) {					
					task.setBlinkMode(BLINKING_DOUBLE);
					task.run();
				}
			} else if (pinCharList.length() >= 18 && pinCharList.length() <= 23) {
				//b�rja blinka BLINKING_TRIPLE
				if (task.hasBlinkingStarted()) {
					task.stop();
				}
				if (!task.hasBlinkingStarted()) {					
					task.setBlinkMode(BLINKING_TRIPLE);
					task.run();
				}
			} else {
				String oldPin = pinCharList.substring(1,6);
				String barcode = pinCharList.substring(6,19);
				String newPin = pinCharList.substring(19,23);
				
				User u = database.getUserByBarcode(barcode);
				
				if (u.getPin() == oldPin) {
					u.setPin(newPin);
					//lysa gr�nt? l�s upp? kolla kravspecen
					clear();
				} else {
					//lysa r�tt? kolla kravspecen
				}
			}
			
			
		} else if (pinCharList.length() >= 4) {
			String pin = pinCharList.substring(0, 4);
			database.setBikesRetrievable(pin);
			clear();
		}
		
	}
	
	/**
	* Rensar ut kod-bufferten.
	*/
	private void clear() {
		
	}
	
	private class MyTask implements Runnable {
		private volatile int blinkMode;
		private volatile boolean permissionToRun;
		private volatile boolean blinkingStarted;
		
		public MyTask () {
			blinkMode = BLINKING_OFF;
			permissionToRun = false;
			blinkingStarted = false;
		}
		
		public void run() {
			blinkingStarted = true;
			permissionToRun = true;
			try {
				long interval = 1;	//SKA EGENTLIGEN VARA 0.3 SEKUNDER. S�TTS TILL 1 I V�NTAN P� KONFIRMATION ATT �NDRA PINCODETERMINAL OCH TESTDRIVER
				long wait = 3;		//SKA EGENTLIGEN VARA 1.5 SEKUNDER. S�TTS TILL 3 I V�NTAN P� KONFIRMATION ATT �NDRA PINCODETERMINAL OCH TESTDRIVER
				if (blinkMode == BLINKING_SINGLE) {
					while (permissionToRun){
						terminal.lightLED(terminal.GREEN_LED, (int)interval);
						Thread.sleep(wait);
					}
				} else if (blinkMode == BLINKING_DOUBLE) {
					while (true){
						terminal.lightLED(terminal.GREEN_LED, (int)interval);
						Thread.sleep(interval);
						terminal.lightLED(terminal.GREEN_LED, (int)interval);
						Thread.sleep(wait);
					}
				} else {
					while (true){
						terminal.lightLED(terminal.GREEN_LED, (int)interval);
						Thread.sleep(interval);
						terminal.lightLED(terminal.GREEN_LED, (int)interval);
						Thread.sleep(interval);
						terminal.lightLED(terminal.GREEN_LED, (int)interval);
						Thread.sleep(wait);
					}
				}
			} catch (InterruptedException iex) {}
			
		}
		
		public void stop() {
			blinkingStarted = false;
			permissionToRun = false;
		}
		
		public void setBlinkMode(int bMode) {
			blinkMode = bMode;
		}
		
		public boolean hasBlinkingStarted() {
			return blinkingStarted;
		}
	}
			
}