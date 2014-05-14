package garage;

import java.util.HashMap;
import java.util.LinkedList;


public class BicycleGarageDatabase {
	
	private	HashMap<String,User> barcodeMap;
	private HashMap<String,LinkedList<User>> pinMap;
	private LinkedList<RetrievalOrder> orders;
	
	private String savedir;
	private int capacity;
	
	public BicycleGarageDatabase(int cap){
		//TODO;
		barcodeMap = new HashMap<String, User>();
		pinMap = new HashMap<String, LinkedList<User>>();
		orders = new LinkedList<RetrievalOrder>();
		savedir = "garagedata";
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
		//TODO
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
	//TODO snygga till.
	public void removeUser(String barcode){
		String pin = barcodeMap.get(barcode).getPin();
		pinMap.get(pin).remove(barcodeMap.get(barcode));
		barcodeMap.remove(barcode);
	}
	
	//TODO*****************ADD FOLLOWING TO DESIGNDOC************************************
	
	public void modifyBikesInGarage(String barcode, int modifier){
		//TODO
		barcodeMap.get(barcode).modBikesInGarage(modifier);
	}
	
	
}
