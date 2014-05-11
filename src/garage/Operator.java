package garage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

public class Operator {

	private BicycleGarageDatabase database;
	private JPanel buttonPanel, textPanel;
	private JTextField textField;
	private JFrame frame;
	private JButton add, edit, remove;
	private JMenu settings, view, about;
	private JMenuBar menuBar;
	private JMenuItem options, showBarcodeReader, showBarcodeWrite, showPIN, showLock, showAbout;

	/**
	 * Skapar ett GUI med alla knappar, fönster och dylikt som ska finnas med i
	 * operatörprogrammet.
	 * 
	 * @param database
	 *            cykelgaragets databas som skall användas
	 */
	public Operator(BicycleGarageDatabase database) {
		this.database = database;

		buttonPanel = new JPanel();
		buttonPanel.setVisible(true);

		textPanel = new JPanel();
		textPanel.setVisible(true);
		textPanel.setLayout(new BorderLayout());

		frame = new JFrame();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setPreferredSize(new Dimension(500, 200));
		frame.setMinimumSize(new Dimension(470, 200));
		frame.setResizable(true);
		frame.setTitle("Bicycle Garage Manager");

		settings = new JMenu("Inställningar");
		settings.setMnemonic(KeyEvent.VK_A);
		settings.getAccessibleContext().setAccessibleDescription("");
		view = new JMenu("Visa");
		view.setMnemonic(KeyEvent.VK_A);
		view.getAccessibleContext().setAccessibleDescription("");
		about = new JMenu("Om");
		about.setMnemonic(KeyEvent.VK_A);
		about.getAccessibleContext().setAccessibleDescription("");

		options = new JMenuItem("Alternativ", KeyEvent.VK_T);
		options.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1,
				ActionEvent.ALT_MASK));
		options.getAccessibleContext().setAccessibleDescription(
				"This doesn't really do anything");
		settings.add(options);
		
		showBarcodeReader = new JCheckBoxMenuItem("Visa streckkodsläsare");
		showBarcodeReader.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1,
				ActionEvent.CTRL_MASK));
		showBarcodeReader.getAccessibleContext().setAccessibleDescription(
				"");
		view.add(showBarcodeReader);
		
		showBarcodeWrite = new JCheckBoxMenuItem("Visa streckkodsskrivare");
		showBarcodeWrite.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2,
				ActionEvent.CTRL_MASK));
		showBarcodeWrite.getAccessibleContext().setAccessibleDescription(
				"");
		view.add(showBarcodeWrite);
		
		showPIN = new JCheckBoxMenuItem("Visa PIN-kodsterminal");
		showPIN.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3,
				ActionEvent.CTRL_MASK));
		showPIN.getAccessibleContext().setAccessibleDescription(
				"");
		view.add(showPIN);
		
		showLock = new JCheckBoxMenuItem("Visa dörrlås");
		showLock.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4,
				ActionEvent.CTRL_MASK));
		showLock.getAccessibleContext().setAccessibleDescription(
				"");
		view.add(showLock);
		
		showAbout = new JMenuItem("Alternativ", KeyEvent.VK_T);
		showAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1,
				ActionEvent.SHIFT_MASK));
		showAbout.getAccessibleContext().setAccessibleDescription(
				"");
		about.add(showAbout);
		
		add = new JButton("Lägg till cykelägare");
		edit = new JButton("Redigera cykelägare");
		remove = new JButton("Ta bort cykelägare");

		textField = new JTextField(2);
		textField.setSize(600, 500);
		textField.setEditable(false);
		textField.setBackground(new Color(255,255,255));

		menuBar = new JMenuBar();

		menuBar.add(settings);
		menuBar.add(view);
		menuBar.add(about);

		frame.setJMenuBar(menuBar);

		textPanel.add(textField, BorderLayout.CENTER);
		textPanel.setBackground(new Color(124, 230, 242));

		buttonPanel.add(add);
		buttonPanel.add(edit);
		buttonPanel.add(remove);

		frame.add(buttonPanel, BorderLayout.SOUTH);
		frame.add(textPanel, BorderLayout.CENTER);
		frame.pack();
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
	 * @param copiesOfBarcode
	 *            antal cyklar som registreras hos cykelägaren dvs. hur många
	 *            streckkoder som ska skrivas ut
	 */
	public void AddBikeOwner(String pin, String barcode, String name,
			String telNr, int copiesOfBarcode) {

	}

	/**
	 * Redigerar en cykelägare.
	 * 
	 * @param pin
	 *            cykelägarens PIN-kod
	 * @param barcode
	 *            cykelägarens streckkod
	 * @param name
	 *            cykelägarens namn
	 * @param telNr
	 *            cykelägarens telefonnummer
	 */
	public void EditBikeOwner(String pin, String barcode, String name,
			String telNr) {

	}

	/**
	 * Tar bort en cykelägare. Ifall cykelägaren har cyklar i garaget så kommer
	 * en MessageBox upp där operatören kan välja att radera cykelägaren ändå.
	 * 
	 * @param barcode
	 *            cykelägarens streckkod
	 * @param pin
	 *            cykelägarens PIN-kod
	 */
	public void removeUser(String barcode, String pin) {

	}

	/**
	 * Returnerar en specifik cykelägare.
	 * 
	 * @param barcode
	 *            cykelägarens streckkod
	 * @return cykelägaren
	 */
	public User getUser(String barcode) {
		return null;
	}

	/**
	 * Söker efter streckkoden för en viss cykelägare.
	 * 
	 * @param name
	 *            cykelägarens namn
	 * @return cykelägarens streckkod
	 */
	public String findBarcodeWithName(String name) {
		return null;
	}

	/**
	 * Genererar en ny 4-siffrig PIN-kod.
	 * 
	 * @return ny PIN-kod
	 */
	public String GeneratePin() {
		return null;
	}

	public static void main(String[] args) {
		BicycleGarageDatabase database = new BicycleGarageDatabase();
		new Operator(database);

	}

}
