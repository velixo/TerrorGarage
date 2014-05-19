package garage;

public class User{
	private String name;
	private String telNr;
	private String personNr;
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
	public User(String n, String t, String b, String p, String pNr){
		name = n;
		telNr = t;
		barcode = b;
		pin = p;
		personNr = pNr;
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
	 * Returnerar userns personnummer.
	 */
	public String getPersonNr(){
		return personNr;
	}
	
	/**
	 * Returnerar antalet cyklar usern har i garaget.
	 */
	public int getBikesInGarage(){
		return bikesInGarage;
	}
	
	/**
	 * OBS! FÅR ENDAST ANVÄNDAS I/GENOM DATABASEN! Metod som 
	 * ändrar en users antal cyklar i garaget
	 *
	 * @param m
	 * 	mängden cyklar som läggs/tas bort hos 
	 */
	public void modBikesInGarage(int m){
		bikesInGarage+=m;
	}
	
	/** 
	 * Skuggning av metoden equals
	 * 
	 * @param u
	 * 		usern som detta objekt ska jämföras med
	 * 
	 * @return true om de har identiska attribut
	 * 
	 * @return false om de inte har det
	 * 
	 * */
	public boolean equals(User u) {
		if(
			this.name.equals(u.getName()) &&
			this.barcode.equals(u.getBarcode()) &&
			this.telNr.equals(u.getTelNr()) &&
			this.bikesInGarage == u.getBikesInGarage() &&
			this.pin.equals(u.getPin()) &&
			this.personNr.equals(u.getPersonNr())
		) {
			return true;
		}
		return false;
	}
}