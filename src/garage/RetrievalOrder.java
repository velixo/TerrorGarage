package garage;
import java.sql.Timestamp;

public class RetrievalOrder {
	String pin;
	Timestamp expiration;
	
	public RetrievalOrder(String p){
		pin = p;
		expiration = new Timestamp(System.currentTimeMillis()+(long)1800000);	//1800000 millisekunder på 30 minuter.
	}
	public String getPin(){
		return pin;
	}
	public long getExpMillis(){
		return expiration.getTime();
	}
}
