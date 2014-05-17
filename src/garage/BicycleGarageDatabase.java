package garage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.io.*;


public class BicycleGarageDatabase {
	
	private	HashMap<String,User> barcodeMap;
	private HashMap<String,LinkedList<User>> pinMap;
	private LinkedList<RetrievalOrder> orders;
	
	//Error ints for when incorrect addUser
	public static final int PIN_LENGTH_ERROR = 1;
	public static final int BARCODE_LENGTH_ERROR = 2;
	public static final int NO_ADDUSER_ERROR = 3;
	
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
		if (pin.length() != 4) {
			return false;
		} else {
			if(pinMap.containsKey(pin)){
				return !pinMap.get(pin).isEmpty();
			}
			else{
				return false;
			}
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
		
	}
	
	public LinkedList<User> getUsers(String pin){
		return pinMap.get(pin);
	}
	
	public void save(){
		for(String bc : barcodeMap.keySet()){
			save(bc);
		}
	}
	
	public void save(String barcode){
		File userdir = new File(savedir);
		if(!userdir.exists()){
			userdir.mkdir();
		}
		try {
			FileWriter fw = new FileWriter(savedir + "/" + barcode);
			User u = barcodeMap.get(barcode);
			fw.write("" +
					u.getBarcode() + "\n" +
					u.getName() + "\n" + 
					u.getTelNr() + "\n" + 
					u.getPin() + "\n" + 
					u.getBikesInGarage());
			
			//FELVARNING! KAN MÖJLIGTVIS STÄNGA STREAMEN PERMANENT SÅ ATT DET INTE GÅR ATT SKRIVA TILL FILEN IGEN FÖRENS PROGRAMMET STARTAS OM.
			fw.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void load(){
		File userdir = new File(savedir);
		if(userdir.exists()){
			for(File f : userdir.listFiles()){
				try {
					Scanner s = new Scanner(f);
					
					String bc = s.nextLine();
					String n = s.nextLine();
					String tel = s.nextLine();
					String pin = s.nextLine();
					int big = Integer.parseInt(s.nextLine());
					
					addUser(pin,bc,n,tel);
					modifyBikesInGarage(bc, big);
					
					s.close();
				} catch (FileNotFoundException e) {
					//nåt ble fel me filläsandet.
				} catch (NumberFormatException e) {
					//hur fan blir bikesInGarage fel?
				}
				
				
			}
		}
	}
	
	public void setDirectory(String dir){
		savedir = dir;
	}
	
	public int addUser(String pin, String barcode, String name, String telNr){
		if (pin.length() != 4) {
			return PIN_LENGTH_ERROR;
		} else if (barcode.length() != 5) {
			return BARCODE_LENGTH_ERROR;
		}
		User usr = new User(name, telNr, barcode, pin);
		barcodeMap.put(barcode, usr);
		if(!pinMap.containsKey(pin)){
			pinMap.put(pin, new LinkedList<User>());
		}
		pinMap.get(pin).add(usr);
		return NO_ADDUSER_ERROR;
		
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
		
		//pinbyte för BarcodeMap
		//u = barcodeMap.get(barcode);	//inte säker om detta behövs? |I: nä, det behövs inte.
		u.setPin(newPin);
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
	public boolean isGarageFull(){
		return bikesInside>=capacity;
	}
	
	private class TimeoutTask implements Runnable {
		
		public TimeoutTask(String pin) {
			
		}
		
		public void run () {
			try
		}
	}
	
}
