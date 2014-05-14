package garage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;


public class BicycleGarageDatabase {
	
	private	HashMap<String,User> barcodeMap;
	private HashMap<String,LinkedList<User>> pinMap;
	private LinkedList<RetrievalOrder> orders;
	
	private String savedir;
	private int bikesInside;
	private int capacity;
	
	public BicycleGarageDatabase(int cap){
		//TODO;
		barcodeMap = new HashMap<String, User>();
		pinMap = new HashMap<String, LinkedList<User>>();
		orders = new LinkedList<RetrievalOrder>();
		savedir = "garagedata";
		capacity = cap;
		bikesInside = 0;
	}
	
	public boolean checkBarcodeRegistered(String barcode){
		return barcodeMap.containsKey(barcode);
	}
	
	public boolean checkPinRegistered(String pin){
		if(pinMap.containsKey(pin)){
			return !pinMap.get(pin).isEmpty();
		}
		else{
			return false;
		}
	}
	
	public boolean checkBikeRetrievable(String barcode){
		
		String thePin = barcodeMap.get(barcode).getPin();
		for(RetrievalOrder o : orders){
			//Testers: watch out for NoSuchElement.
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
	
	public boolean setBikesRetrievable(String pin){
		orders.add(new RetrievalOrder(pin));
		return false;
	}
	
	public User getUserByBarcode(String barcode){
		return barcodeMap.get(barcode);
	}
	
	public User getUserByName(String name){
		//Vilhelms förslag
	Iterator<User> usersIterator = barcodeMap.values().iterator();
	while (usersIterator.hasNext()) {
		User u = usersIterator.next();
		if (u.getName() == name) {	//DETTA KOMMER GE ATT FÖRSTA USERN SOM HAR SAMMA NAMN RETURNERAS.
			return getUserByBarcode(u.getBarcode());
		}
	}
	return null;
	//End of Vilhelms förslag
		
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
		savedir = dir;
	}
	
	public void addUser(String pin, String barcode, String name, String telNr){
		User usr = new User(name, telNr, barcode, pin);
		barcodeMap.put(barcode, usr);
		if(!pinMap.containsKey(pin)){
			pinMap.put(pin, new LinkedList<User>());
		}
		pinMap.get(pin).add(usr);
		
	}
	
	public void changeUserPin(String barcode, String newPin){
		User u = barcodeMap.get(barcode);
		String oldPin = u.getPin();
		//remove from old pin list
		pinMap.get(oldPin).remove(u);
		//insert in new pin list
		if(!pinMap.containsKey(newPin)){
			pinMap.put(newPin, new LinkedList<User>());
		}
		pinMap.get(newPin).add(u);
	}
	
	public void removeUser(String barcode){
		User u = barcodeMap.get(barcode);
		pinMap.get(u.getPin()).remove(u);
		barcodeMap.remove(barcode);
	}
	
	public void modifyBikesInGarage(String barcode, int modifier){
		barcodeMap.get(barcode).modBikesInGarage(modifier);
		bikesInside += modifier;
	}
	
	
}
