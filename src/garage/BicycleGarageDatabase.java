package garage;

import java.util.HashMap;
import java.util.LinkedList;


public class BicycleGarageDatabase {
	
	HashMap<String,User> barcodeMap;
	HashMap<String,LinkedList<User>> pinMap;
	LinkedList<RetrievalOrder> orders;
	
	String savefile;
	
	
	public BicycleGarageDatabase(){
		//TODO;
		barcodeMap = new HashMap<String, User>();
		pinMap = new HashMap<String, LinkedList<User>>();
		orders = new LinkedList<RetrievalOrder>();
		savefile = "garagedata";
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
			//TODO check for nulls, perhaps trim statement.;
			if(o.getPin().equals(thePin)){
				orders.remove(o);
				return true;
			}
			else if(o.getExpMillis()>System.currentTimeMillis()){
				orders.remove(o);
			}
		}
		return false;
	}
	public User getUserByBarcode(String barcode){
		return barcodeMap.get(barcode);
	}
	public User getUserByName(String name){
		return null;
	}
	public LinkedList<User> getUsers(String pin){
		return pinMap.get(pin);
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
	public void addUser(/*TODO: add parameters*/){
		//TODO;
	}
	
	
}
