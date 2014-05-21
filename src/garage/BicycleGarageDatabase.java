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
	 * Skapar en databas med information om alla cykel�gare och PIN-koder.
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
	 * Kollar om PIN-koden �r registrerad i databasen.
	 * 
	 * @param pin
	 *            PIN-koden som ska kollas
	 * @return true om PIN-koden �r registrerad
	 */
	public boolean checkBarcodeRegistered(String barcode){
		return barcodeMap.containsKey(barcode);
	}
	
	/**
	 * Kollar om PIN-koden �r registrerad i databasen.
	 * 
	 * @param pin
	 *            PIN-koden som ska kollas
	 * @return true om PIN-koden �r registrerad
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
	 * Kollar om cykel�garens cykel �r h�mtbar.
	 * 
	 * @param barcode
	 *            cykel�garens streckkod
	 * @return true om cykeln/cyklarna �r h�mtbar/h�mtbara
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
	 * S�tter samtliga cyklar med denna PIN-koden som h�mtbar.
	 * 
	 * @param pin
	 *            cyklar med denna pin g�rs h�mtbara
	 */
	public void setBikesRetrievable(String pin){
		orders.add(new RetrievalOrder(pin));
	}
	
	/**
	 * Returnerar cykel�garen vars str�ckkod matchar parametern �barcode�.
	 * 
	 * @param barcode
	 *            cykel�garens str�ckkod
	 * @return cykel�garen vars str�ckkod matchar �barcode�
	 */
	public User getUserByBarcode(String barcode){
		return barcodeMap.get(barcode);
	}
	
	/**
	 * Returnerar cykel�garen vars persnonnummer matchar parametern personNr.
	 * 
	 * @param personNr
	 *            cykel�garens personnummer
	 * @return cykel�garen vars personnummer matchar personnummer
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
	 * Sparar bara datan f�r usern med streckkoden barcode.
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
				
				//FELVARNING! KAN M�JLIGTVIS ST�NGA STREAMEN PERMANENT S� ATT DET INTE G�R ATT SKRIVA TILL FILEN IGEN F�RENS PROGRAMMET STARTAS OM.
				fw.close();
				
			} catch (IOException e) {
				// Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * Laddar den sparade datan fr�n filen specifierad i setDirectory.
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
					if (f.getName().charAt(0)=='.'){		//Buggfix f�r Mac-datorer
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
					//n�t ble fel me fill�sandet.
					System.out.println("filenotfound");
				} catch (NumberFormatException e) {
					//hur fan blir bikesInGarage fel?
					System.out.println("n�got �r fel med bikesInGarage");
				}
				
				
			}
		}
	}
	
	
	/**
	 * Specificerar var databasen sparas.
	 * 
	 * @param dir
	 *            plats som filen sparas p� (t.e.x "C:/Users/Database/save.txt")
	 */
	public String getDirectory(){
		return savedir;
	}
	
	public void setDirectory(String dir){
		savedir = dir;
	}
	
	/**
	 * L�gger till en ny cykel�gare i databasen.
	 * 
	 * @param pin
	 *            cykel�garens PIN-kod
	 * @param barcode
	 *            cykel�garens streckkod
	 * @param name
	 *            cykel�garens namn
	 * @param telNr
	 *            cykel�garens telefonnummer
	 * @param personNr
	 *           cykel�garens personnummer
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
	 * Tar bort en cykel�gare. Ifall cykel�garen har cyklar i garaget s� kommer en
	 * MessageBox upp d�r operat�ren kan v�lja att radera cykel�garen �nd�.
	 * 
	 * @param barcode
	 *            cykel�garens streckkod
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
	 * �ndrar antalet cyklar som en cykel�gare har st�llt i garaget.
	 * 
	 * @param barcode
	 *            cykel�garens streckkod
	 *            
	 * @param modifier
	 *            antalet cyklar som ska l�ggas till/tas bort
	 */
	public void modifyBikesInGarage(String barcode, int modifier){
		barcodeMap.get(barcode).modBikesInGarage(modifier);
		bikesInside += modifier;
		save(barcode);
	}
	
	/**
	 * Kollar om cykelgaraget �r fullt eller ej.
	 * 
	 * @return true om cykelgaraget �r fullt
	 */
	public boolean isGarageFull(){
		return bikesInside>=capacity;
	}
	
	public int getCapacity(){
		return capacity;
	}
	
	/**
	 * �ndrar cykelgaragets kapacitet.
	 * 
	 * @param capacity
	 * 		den nya kapaciteten
	 * 
	 * @return true om �ndringen gick igenom
	 * 
	 * @return false om capacity �n antalet cyklar i garaget just nu
	 */
	public boolean setCapacity(int newCapacity) {
		if (newCapacity < bikesInside) {
			return false;
		}
		capacity = newCapacity;
		return true;
	}
}
