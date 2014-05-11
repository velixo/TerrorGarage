package garage;

public class User implements Comparable<User>{
	String name;
	String telNr;
	String barcode;
	String pin;
	int bikesInGarage;
	
	public String getBarcode(){
		return barcode;
	}
	
	public String getPin(){
		return pin;
	}

	@Override
	public int compareTo(User other) {
		return barcode.compareTo(other.getBarcode());
	}
	
	public void setPin (String newPin) {
		
	}
}