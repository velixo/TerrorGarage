package garage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;

import springUtilities.SpringUtilities;

public class Operator {

	private BicycleGarageDatabase database;
	private JPanel buttonPanel, textPanel;
	private JTextArea mainTextField;
	private JFrame frame;
	private JButton add, edit, remove;
	private JMenu settings, view, about;
	private JMenuBar menuBar;
	private JMenuItem options, showBarcodeReader, showBarcodeWrite, showPIN,
			showLock, showAbout;

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
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
		showBarcodeReader.getAccessibleContext().setAccessibleDescription("");
		view.add(showBarcodeReader);

		showBarcodeWrite = new JCheckBoxMenuItem("Visa streckkodsskrivare");
		showBarcodeWrite.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2,
				ActionEvent.CTRL_MASK));
		showBarcodeWrite.getAccessibleContext().setAccessibleDescription("");
		view.add(showBarcodeWrite);

		showPIN = new JCheckBoxMenuItem("Visa PIN-kodsterminal");
		showPIN.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3,
				ActionEvent.CTRL_MASK));
		showPIN.getAccessibleContext().setAccessibleDescription("");
		view.add(showPIN);

		showLock = new JCheckBoxMenuItem("Visa dörrlås");
		showLock.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4,
				ActionEvent.CTRL_MASK));
		showLock.getAccessibleContext().setAccessibleDescription("");
		view.add(showLock);

		showAbout = new JMenuItem("Alternativ", KeyEvent.VK_T);
		showAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1,
				ActionEvent.SHIFT_MASK));
		showAbout.getAccessibleContext().setAccessibleDescription("");
		about.add(showAbout);

		add = new JButton("Lägg till cykelägare");
		add.addActionListener(new AddBikeOwner());
		edit = new JButton("Redigera cykelägare");
		remove = new JButton("Ta bort cykelägare");

		mainTextField = new JTextArea();
		mainTextField.setSize(600, 500);
		mainTextField.setEditable(false);
		mainTextField.setBackground(new Color(255, 255, 255));

		menuBar = new JMenuBar();

		menuBar.add(settings);
		menuBar.add(view);
		menuBar.add(about);

		frame.setJMenuBar(menuBar);

		textPanel.add(mainTextField, BorderLayout.CENTER);
		textPanel.setBackground(new Color(124, 230, 242));

		buttonPanel.add(add);
		buttonPanel.add(edit);
		buttonPanel.add(remove);

		frame.add(buttonPanel, BorderLayout.SOUTH);
		frame.add(textPanel, BorderLayout.CENTER);
		frame.pack();
	}

	class AddBikeOwner implements ActionListener {

		private JFrame addFrame;
		private JTextField[] textFields;
		private String[] labels = { "PIN: ", "Streckkod: ", "Namn: ",
				"Telefonnummer: ", "Antal Streckkodskopior: " };

		public void actionPerformed(ActionEvent e) {
			int numPairs = labels.length;

			textFields = new JTextField[labels.length];
			JPanel p = new JPanel(new SpringLayout());
			for (int i = 0; i < numPairs; i++) {
				JLabel l = new JLabel(labels[i], JLabel.TRAILING);
				p.add(l);
				JTextField textField = new JTextField(10);
				textFields[i] = textField;
				l.setLabelFor(textField);
				p.add(textField);
			}

			SpringUtilities.makeCompactGrid(p, numPairs, 2, 6, 6, 6, 6);

			addFrame = new JFrame("SpringForm");
			addFrame.setLayout(new BorderLayout());
			addFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

			p.setOpaque(true);
			addFrame.add(p, BorderLayout.CENTER);

			JPanel buttons = new JPanel();
			buttons.setLayout(new BorderLayout());

			JButton cancel = new JButton("Avbryt");
			cancel.addActionListener(new Cancel());
			
			JButton apply = new JButton("Verkställ");
			apply.addActionListener(new Apply());
			
			JButton generate = new JButton("Generera");
			generate.addActionListener(new Generate());
			
			buttons.add(cancel, BorderLayout.LINE_START);
			buttons.add(apply, BorderLayout.LINE_END);
			buttons.add(generate, BorderLayout.SOUTH);

			addFrame.add(buttons, BorderLayout.SOUTH);
			addFrame.pack();
			addFrame.setVisible(true);
		}

		class Cancel implements ActionListener {

			public void actionPerformed(ActionEvent arg0) {
				addFrame.setVisible(false);
			}
		}

		class Apply implements ActionListener {

			public void actionPerformed(ActionEvent arg0) {
				mainTextField.setText("");
				if(database.checkBarcodeRegistered(textFields[1].getText())){
					JOptionPane.showMessageDialog (null, "Streckkoden är upptagen", "Felmeddelande", JOptionPane.ERROR_MESSAGE);
				} else {
				database.addUser(textFields[0].getText(),
						textFields[1].getText(), textFields[2].getText(),
						textFields[3].getText());
				
				StringBuilder sb = new StringBuilder();
				
				sb.append("Cykelägaren har lagts till\n");
				
				for(int i = 0; i < textFields.length; i++){
					sb.append(labels[i]);
					sb.append(textFields[i].getText() + "\n");
				}
				
				mainTextField.setText(sb.toString());
				
				addFrame.setVisible(false);
				}
			}
		}
		
		class Generate implements ActionListener {
			
			private Random rand = new Random();
			private String newPin;
			private String newBarcode;
			
			public void actionPerformed(ActionEvent arg0){
				
				StringBuilder pinBuilder = new StringBuilder();
				
				for(int i = 0; i < 4; i++){
					int a = rand.nextInt(10);
					pinBuilder.append(String.valueOf(a));
				}
				
				newPin = pinBuilder.toString();
				textFields[0].setText(newPin);
				StringBuilder barcodeBuilder = new StringBuilder();
				
				for(int i = 0; i < 5; i++){
					int b = rand.nextInt(10);
					barcodeBuilder.append(String.valueOf(b));
				}
				
				newBarcode = barcodeBuilder.toString();
				
				while(database.checkBarcodeRegistered(newBarcode)){
					barcodeBuilder.delete(0, 5);
					
					for(int i = 0; i < 5; i++){
						int b = rand.nextInt(10);
						barcodeBuilder.append(String.valueOf(b));
					}
					
					newBarcode = barcodeBuilder.toString();
				}
				
				textFields[1].setText(newBarcode);
//				newPin = String.valueOf(rand.nextInt(9000) + 1000);
//				newBarcode = String.valueOf(rand.nextInt(90000) + 10000);
//				while(database.checkPinRegistered(newPin)){
//					newPin = String.valueOf(rand.nextInt(9000) + 1000);
//				}
//				
//				while(database.checkBarcodeRegistered(newBarcode)){
//					newBarcode = String.valueOf(rand.nextInt(90000) + 10000);
//				}
//				
			}
		}

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
