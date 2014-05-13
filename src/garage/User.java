package garage;

public class User implements Comparable<String>{
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
	
	public String getName(){
		return name;
	}
	
	public String getTelNr(){
		return telNr;
	}
	
	public int getBikesInGarage(){
		return bikesInGarage;
	}
	

	@Override
	public int compareTo(String otherbc) {
		return barcode.compareTo(otherbc);
	}
	
	//public void setPin måste diskuteras!
	
	public void setName(String n){
		name = n;
	}
}