package garage;

public class User implements Comparable<User>{
	String name;
	String telNr;
	String barcode;
	String pin;
	int bikesInGarage;
	
	public User(String n, String t, String b, String p){
		name = n;
		telNr = t;
		barcode = b;
		pin = p;
	}
	
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