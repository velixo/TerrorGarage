package garage;

import java.util.HashMap;
import java.util.LinkedList;


public class BicycleGarageDatabase {
	
	HashMap<String,User> barcodeMap;
	HashMap<String,LinkedList<User>> pinMap;
	LinkedList<RetrievalOrder> orders;
	
	
	
	
	public BicycleGarageDatabase(){
		//TODO;
		barcodeMap = new HashMap<String, User>();
		pinMap = new HashMap<String, LinkedList<User>>();
		orders = new LinkedList<RetrievalOrder>();
	}
	public boolean checkBarcodeRegistered(String barcode){
		return barcodeMap.containsKey(barcode);
	}
	public boolean checkPinRegistered(String pin){
		return pinMap.containsKey(pin);
	}
	public boolean checkBikeRetrievable(String barcode){
		
		String thePin = barcodeMap.get(barcode).getPin();
		for(RetrievalOrder o : orders){
			//TODO;
			if()
		}
		return false;
	}
	public User getUser(String barcode){
		
		return barcodeMap.get(barcode);
	}
	public void save(){
		//TODO;
	}
	public void load(){
		//TODO;
	}
	public void setDirectory(String dir){
		//TODO;
	}
	
	
	
}
