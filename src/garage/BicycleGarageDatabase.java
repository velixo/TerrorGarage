package garage;

import java.util.HashMap;
import java.util.LinkedList;

public class BicycleGarageDatabase {
	
	HashMap<String,User> whosBikeIs;
	HashMap<String,LinkedList<User>> whosPinIs;
	LinkedList<RetrievalOrder> retrievalOrders;
	
	
	
	
	public BicycleGarageDatabase(){
		//TODO;
	}
	public boolean checkBarcodeRegistered(String barcode){
		//TODO;
		return false;
	}
	public boolean checkPinRegistered(String pin){
		//TODO;
		return false;
	}
	public boolean checkBikeRetrievable(String barcode){
		//TODO;
		return false;
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
