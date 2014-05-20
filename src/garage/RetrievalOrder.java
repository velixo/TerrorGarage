package garage;
import java.sql.Timestamp;

class RetrievalOrder {
	String pin;
	Timestamp expiration;
	
	/** 
	 * OBS! DENNA KLASSEN SKA ENDAST ANVÄNDAS GENOM/I DATABASEN!
	 * Skapar en retrieval order. Dessa orders (beställningar) håller koll på om cyklar är hämtbara
	 * 
	 * @param p
	 * 		pin koden vars cyklar ska göras hämtbara
	 * */
	public RetrievalOrder(String p){
		pin = p;
		expiration = new Timestamp(System.currentTimeMillis()+(long)1800000);	//1800000 millisekunder på 30 minuter.
	}
	
	/**
	 *  Returnerar pin-koden på denna ordern
	 * */
	public String getPin(){
		return pin;
	}
	
	/**
	 *  Returnerar expirationstiden på ordern
	 * */
	public long getExpMillis(){
		return expiration.getTime();
	}
}
