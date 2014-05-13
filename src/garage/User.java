package garage;

public class User{
	private String name;
	private String telNr;
	private String barcode;
	private String pin;
	private int bikesInGarage;
	
	/**
	 * Konstruktorn för en User.
	 * 
	 * @param n
	 * 	userns name
	 * @param t
	 * 	userns telefonnummer
	 * @param b
	 * 	userns streckkod
	 * @param p
	 * 	userns PIN-kod
	 */
	public User(String n, String t, String b, String p){
		name = n;
		telNr = t;
		barcode = b;
		pin = p;
	}
	
	/**
	 * Returnerar userns streckkod.
	 */
	public String getBarcode(){
		return barcode;
	}
	
	/**
	 * Returnerar userns PIN-kod.
	 */
	public String getPin(){
		return pin;
	}
	
	/**
	 * Returnerar userns namn.
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Returnerar userns telefonnummer.
	 */
	public String getTelNr(){
		return telNr;
	}
	
	/**
	 * Returnerar antalet cyklar usern har i garaget.
	 */
	public int getBikesInGarage(){
		return bikesInGarage;
	}
	

//	@Override
//	public int compareTo(String otherbc) {
//		return barcode.compareTo(otherbc);
//	}
	
	//public void setPin måste diskuteras!
	
	/**
	 * Tilldelar usern ett nytt namn.
	 * 
	 * @param n
	 * 	userns nya namn
	 */
	public void setName(String n){
		name = n;
	}
}