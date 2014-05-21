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
	
	/**
	 * Skapar en databas med information om alla cykelägare och PIN-koder.
	 * 
	 * @param capacity
	 *            cykelgaragets kapacitet
	 */
	public BicycleGarageDatabase(int cap){
		barcodeMap = new HashMap<String, User>();
		pinMap = new HashMap<String, LinkedList<User>>();
		orders = new LinkedList<RetrievalOrder>();
		savedir = "garagedata";
		capacity = cap;
		bikesInside = 0;
	}
	
	/**
	 * Kollar om PIN-koden är registrerad i databasen.
	 * 
	 * @param pin
	 *            PIN-koden som ska kollas
	 * @return true om PIN-koden är registrerad
	 */
	public boolean checkBarcodeRegistered(String barcode){
		return barcodeMap.containsKey(barcode);
	}
	
	/**
	 * Kollar om PIN-koden är registrerad i databasen.
	 * 
	 * @param pin
	 *            PIN-koden som ska kollas
	 * @return true om PIN-koden är registrerad
	 */
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
	
	/**
	 * Kollar om cykelägarens cykel är hämtbar.
	 * 
	 * @param barcode
	 *            cykelägarens streckkod
	 * @return true om cykeln/cyklarna är hämtbar/hämtbara
	 */
	public boolean checkBikeRetrievable(String barcode){
		
		String thePin = barcodeMap.get(barcode).getPin();
		RetrievalOrder oldest = null;
		for(RetrievalOrder o : orders){
			//Testers: watch out for NoSuchElement.
			if(o.getExpMillis()<System.currentTimeMillis()){
				orders.remove(o);
			}
			else if(o.getPin().equals(thePin)){
				if(oldest==null){
					oldest = o;
				}
				else if(o.getExpMillis()<oldest.getExpMillis()){
					oldest = o;
				}
			}
		}
		if(oldest != null){
			orders.remove(oldest);
			return true;
		}
		else{
			return false;			
		}
	}
	
	/**
	 * Sätter samtliga cyklar med denna PIN-koden som hämtbar.
	 * 
	 * @param pin
	 *            cyklar med denna pin görs hämtbara
	 */
	public void setBikesRetrievable(String pin){
		orders.add(new RetrievalOrder(pin));
	}
	
	/**
	 * Returnerar cykelägaren vars sträckkod matchar parametern ‘barcode’.
	 * 
	 * @param barcode
	 *            cykelägarens sträckkod
	 * @return cykelägaren vars sträckkod matchar ‘barcode’
	 */
	public User getUserByBarcode(String barcode){
		return barcodeMap.get(barcode);
	}
	
	/**
	 * Returnerar cykelägaren vars persnonnummer matchar parametern personNr.
	 * 
	 * @param personNr
	 *            cykelägarens personnummer
	 * @return cykelägaren vars personnummer matchar personnummer
	 */
	public User getUserByPersonnumber(String personNr) {
		Iterator<User> usersIterator = barcodeMap.values().iterator();
		while (usersIterator.hasNext()) {
			User u = usersIterator.next();
			if (u.getPersonNr().equals(personNr)) {
				return getUserByBarcode(u.getBarcode());
			}
		}
		return null;
	}
	
	/**
	 * Sparar all data till filen specifierad i setDirectory.
	 */
	public void save(){
		for(String bc : barcodeMap.keySet()){
			save(bc);
		}
//		File dir = new File(savedir);
//		if(!dir.exists()){
//			dir.mkdir();
//		}
		try {
			FileWriter fw = new FileWriter(savedir + "/cap");
			fw.write(capacity);
			fw.close();
			
		}catch(IOException e){
			System.out.println("ERROR: couldn't save garage capacity");
		}
	}
	
	/**
	 * Sparar bara datan för usern med streckkoden barcode.
	 * 
	 * @param barcode
	 *            streckkoden refererar till den usern som ska sparas
	 */
	public void save(String barcode){
		if (!savedir.isEmpty()) {
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
						u.getPersonNr() + "\n" +
						u.getBikesInGarage());
				
				//FELVARNING! KAN MÖJLIGTVIS STÄNGA STREAMEN PERMANENT SÅ ATT DET INTE GÅR ATT SKRIVA TILL FILEN IGEN FÖRENS PROGRAMMET STARTAS OM.
				fw.close();
				
			} catch (IOException e) {
				// Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * Laddar den sparade datan från filen specifierad i setDirectory.
	 */
	public void load(){
		File userdir = new File(savedir);
		if(userdir.exists()){
//			System.out.println("save directory hittades!");
			
			Scanner capScan = new Scanner(savedir + "/cap");
			if (capScan.hasNextInt()){
				capacity = capScan.nextInt();				
			}
			capScan.close();
			File[] files = userdir.listFiles();
			for (File f : files){
				try {
					if (f.getName().charAt(0)=='.'){		//Buggfix för Mac-datorer
						continue;
					} else if (f.getName().equals("cap")){
						continue;
					}
					
					Scanner s = new Scanner(new File(userdir + "/" + f.getName()));
					
					String bc = s.nextLine();
					String n = s.nextLine();
					String tel = s.nextLine();
					String pin = s.nextLine();
					String pnr = s.nextLine();
					int big = Integer.parseInt(s.nextLine());
					
					addUser(pin,bc,n,tel, pnr);
					modifyBikesInGarage(bc, big);
					
					s.close();
				} catch (FileNotFoundException e) {
					//nåt ble fel me filläsandet.
					System.out.println("filenotfound");
				} catch (NumberFormatException e) {
					//hur fan blir bikesInGarage fel?
					System.out.println("något är fel med bikesInGarage");
				}
				
				
			}
		}
	}
	
	
	/**
	 * Specificerar var databasen sparas.
	 * 
	 * @param dir
	 *            plats som filen sparas på (t.e.x "C:/Users/Database/save.txt")
	 */
	public String getDirectory(){
		return savedir;
	}
	
	public void setDirectory(String dir){
		savedir = dir;
	}
	
	/**
	 * Lägger till en ny cykelägare i databasen.
	 * 
	 * @param pin
	 *            cykelägarens PIN-kod
	 * @param barcode
	 *            cykelägarens streckkod
	 * @param name
	 *            cykelägarens namn
	 * @param telNr
	 *            cykelägarens telefonnummer
	 * @param personNr
	 *           cykelägarens personnummer
	 */
	public int addUser(String pin, String barcode, String name, String telNr, String personNr){
		if (pin.length() != 4) {
			return PIN_LENGTH_ERROR;
		} else if (barcode.length() != 5) {
			return BARCODE_LENGTH_ERROR;
		}
		User usr = new User(name, telNr, barcode, pin, personNr);
		barcodeMap.put(barcode, usr);
		if(!pinMap.containsKey(pin)){
			pinMap.put(pin, new LinkedList<User>());
		}
		pinMap.get(pin).add(usr);
		save(barcode);
		return NO_ADDUSER_ERROR;
		
	}
	
	/**
	 * Tar bort en cykelägare. Ifall cykelägaren har cyklar i garaget så kommer en
	 * MessageBox upp där operatören kan välja att radera cykelägaren ändå.
	 * 
	 * @param barcode
	 *            cykelägarens streckkod
	 */
	public void removeUser(String barcode){
		User u = barcodeMap.get(barcode);
		modifyBikesInGarage(u.getBarcode(), -u.getBikesInGarage());
		pinMap.get(u.getPin()).remove(u);
		barcodeMap.remove(barcode);
		
		File f = new File(savedir + "/" + barcode);
		if(f.exists()){
			f.delete();				
		}
		
	}
	
	/**
	 * Ändrar antalet cyklar som en cykelägare har ställt i garaget.
	 * 
	 * @param barcode
	 *            cykelägarens streckkod
	 *            
	 * @param modifier
	 *            antalet cyklar som ska läggas till/tas bort
	 */
	public void modifyBikesInGarage(String barcode, int modifier){
		barcodeMap.get(barcode).modBikesInGarage(modifier);
		bikesInside += modifier;
		save(barcode);
	}
	
	/**
	 * Kollar om cykelgaraget är fullt eller ej.
	 * 
	 * @return true om cykelgaraget är fullt
	 */
	public boolean isGarageFull(){
		return bikesInside>=capacity;
	}
	
	public int getCapacity(){
		return capacity;
	}
	
	/**
	 * Ändrar cykelgaragets kapacitet.
	 * 
	 * @param capacity
	 * 		den nya kapaciteten
	 * 
	 * @return true om ändringen gick igenom
	 * 
	 * @return false om capacity än antalet cyklar i garaget just nu
	 */
	public boolean setCapacity(int newCapacity) {
		if (newCapacity < bikesInside) {
			return false;
		}
		capacity = newCapacity;
		return true;
	}
}
