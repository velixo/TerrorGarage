package garage;

import java.lang.StringBuilder;

public class PinCharCollector {
	private StringBuilder pinCharList;
	private BicycleGarageDatabase database;
	private int lightMode;
	private final int BLINKING_OFF = 0;
	private final int BLINKING_SINGLE = 1;
	private final int BLINKING_DOUBLE = 2;
	private final int BLINKING_TRIPLE = 3;
	
	/**
	* Konstruktorn f�r en PinCharCollector.
	*/
	public PinCharCollector(BicycleGarageDatabase database) {
		this.database = database;
		pinCharList = new StringBuilder();
		lightMode = BLINKING_OFF;
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
				//b�rja blinka BLINKING_ONE
			} else if (pinCharList.length() >= 5 && pinCharList.length() <= 18) {
				//b�rja blinka BLINKING_DOUBLE
			} else if (pinCharList.length() >= 18 && pinCharList.length() <= 23) {
				//b�rja blinka BLINKING_TRIPLE
			} else {
				String oldPin = pinCharList.substring(1,6);
				String barcode = pinCharList.substring(6,19);
				String newPin = pinCharList.substring(19,23);
				
				User u = database.getUser(barcode);
				
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
			database.setRetrievable(pin);
			clear();
		}
		
	}
	
	/**
	* Rensar ut kod-bufferten.
	*/
	private void clear() {
		
	}
			
}