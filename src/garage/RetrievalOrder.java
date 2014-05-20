package garage;
import java.sql.Timestamp;

class RetrievalOrder {
	String pin;
	Timestamp expiration;
	
	/** 
	 * OBS! DENNA KLASSEN SKA ENDAST ANV�NDAS GENOM/I DATABASEN!
	 * Skapar en retrieval order. Dessa orders (best�llningar) h�ller koll p� om cyklar �r h�mtbara
	 * 
	 * @param p
	 * 		pin koden vars cyklar ska g�ras h�mtbara
	 * */
	public RetrievalOrder(String p){
		pin = p;
		expiration = new Timestamp(System.currentTimeMillis()+(long)1800000);	//1800000 millisekunder p� 30 minuter.
	}
	
	/**
	 *  Returnerar pin-koden p� denna ordern
	 * */
	public String getPin(){
		return pin;
	}
	
	/**
	 *  Returnerar expirationstiden p� ordern
	 * */
	public long getExpMillis(){
		return expiration.getTime();
	}
}
