package garage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JButton;
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
import testDrivers.BarcodeReaderEntryTestDriver;
import testDrivers.BarcodeReaderExitTestDriver;

public class Operator {

	private BicycleGarageDatabase database;
	private BicycleGarageManager manager;
	private JPanel buttonPanel, textPanel;
	private JTextArea mainTextField;
	private JFrame frame;
	private JButton add, edit, remove;
	private JMenu settings, view, about;
	private JMenuBar menuBar;
	private JMenuItem options, showBarcodeReaderEntry, showBarcodeReaderExit,
			showBarcodeWrite, showPIN, showLock, showAbout;
	private BarcodeReaderEntryTestDriver entryReader;
	private BarcodeReaderExitTestDriver exitReader;
	private boolean brexit = false;
	private boolean brentry = false;

	/**
	 * Skapar ett GUI med alla knappar, fönster och dylikt som ska finnas med i
	 * operatörprogrammet.
	 * 
	 * @param database
	 *            cykelgaragets databas som skall användas
	 */
	public Operator(BicycleGarageDatabase database, BicycleGarageManager manager) {
		this.database = database;
		this.manager = manager;
		database.load();

		// Autosavingthreaden start
		AutosaveTask task = new AutosaveTask(4);
		Thread autoSaveThread = new Thread(task);
		autoSaveThread.start();
		// Autosavingthreaden slut

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
		// view = new JMenu("Visa");
		// view.setMnemonic(KeyEvent.VK_A);
		// view.getAccessibleContext().setAccessibleDescription("");
		about = new JMenu("Om");
		about.setMnemonic(KeyEvent.VK_A);
		about.getAccessibleContext().setAccessibleDescription("");

		options = new JMenuItem("Alternativ", KeyEvent.VK_T);
		options.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1,
				ActionEvent.ALT_MASK));
		options.getAccessibleContext().setAccessibleDescription(
				"This doesn't really do anything");
		settings.add(options);

		// showBarcodeReaderEntry = new
		// JCheckBoxMenuItem("Visa streckkodsläsare (ingång)");
		// showBarcodeReaderEntry.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1,
		// ActionEvent.CTRL_MASK));
		// showBarcodeReaderEntry.getAccessibleContext().setAccessibleDescription("");
		// // showBarcodeReaderEntry.addActionListener(new
		// ShowBarcodeReaderEntry());
		// view.add(showBarcodeReaderEntry);
		//
		// showBarcodeReaderExit = new
		// JCheckBoxMenuItem("Visa streckkodsläsare (utgång)");
		// showBarcodeReaderExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1,
		// ActionEvent.CTRL_MASK));
		// showBarcodeReaderExit.getAccessibleContext().setAccessibleDescription("");
		// // showBarcodeReaderExit.addActionListener(new
		// ShowBarcodeReaderExit());
		// view.add(showBarcodeReaderExit);
		//
		// showBarcodeWrite = new JCheckBoxMenuItem("Visa streckkodsskrivare");
		// showBarcodeWrite.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2,
		// ActionEvent.CTRL_MASK));
		// showBarcodeWrite.getAccessibleContext().setAccessibleDescription("");
		// view.add(showBarcodeWrite);
		//
		// showPIN = new JCheckBoxMenuItem("Visa PIN-kodsterminal");
		// showPIN.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3,
		// ActionEvent.CTRL_MASK));
		// showPIN.getAccessibleContext().setAccessibleDescription("");
		// view.add(showPIN);
		//
		// showLock = new JCheckBoxMenuItem("Visa dörrlås");
		// showLock.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4,
		// ActionEvent.CTRL_MASK));
		// showLock.getAccessibleContext().setAccessibleDescription("");
		// view.add(showLock);

		showAbout = new JMenuItem("Om", KeyEvent.VK_T);
		showAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1,
				ActionEvent.SHIFT_MASK));
		showAbout.getAccessibleContext().setAccessibleDescription("");
		showAbout.addActionListener(new ShowAbout());
		about.add(showAbout);

		add = new JButton("Lägg till ny cykelägare");
		add.addActionListener(new AddUser());
		edit = new JButton("Redigera cykelägare");
		edit.addActionListener(new EditUser());
		remove = new JButton("Ta bort cykelägare");
		remove.addActionListener(new RemoveUser());

		mainTextField = new JTextArea();
		mainTextField.setSize(600, 500);
		mainTextField.setEditable(false);
		mainTextField.setBackground(new Color(255, 255, 255));

		menuBar = new JMenuBar();

		menuBar.add(settings);
		// menuBar.add(view);
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
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// KANSKE HA MED DETTA: FET EJ
	//
	// class ShowBarcodeReaderEntry implements ActionListener {
	//
	//
	//
	// @Override
	// public void actionPerformed(ActionEvent arg0) {
	// brentry =! brentry;
	// if (!brentry){
	// entryReader = null;
	// } else {
	// entryReader = new BarcodeReaderEntryTestDriver();
	// }
	// }
	//
	// }
	//
	// class ShowBarcodeReaderExit implements ActionListener {
	//
	//
	//
	//
	// @Override
	// public void actionPerformed(ActionEvent arg0) {
	// brexit =! brexit;
	// if (!brexit){
	// exitReader = null;
	// } else {
	// exitReader = new BarcodeReaderExitTestDriver();
	// }
	// }
	//
	// }
	//
	class ShowAbout implements ActionListener {

		private JFrame aboutFrame;
		private String labels = 
				"\n                Bicycle Garage Manager\n\n   Koordinatör:\t\t\tVilhelm Lundqvist \n"
						+ "   Allt-i-allo:\t\t\tAxel Wihlborg-Rasmusen \n"
						+ "   Kodarassistent/whitebox-testare:\tRasha El Manzalawy \n"
						+ "   Blackbox-testare/textskrivarassistent:\tMaja Scherman \n"
						+ "   Kodare:\t\t\tShan Langlais \n"
						+ "   Kodare:\t\t\tIsak Lindhé \n"
						+ "   Projekthandledare:\t\tSandra Nilsson " + "\n\n                ETSA01, Datavetenskap, LTH, Lund, 2013 - 2014";

		// private JTextField[] textFields;

		public void actionPerformed(ActionEvent e) {
			// int numPairs = labels.length;

			// textFields = new JTextField[labels.length];
			// JPanel p = new JPanel(new SpringLayout());
			// for (int i = 0; i < numPairs; i++) {
			// JLabel l = new JLabel(labels[i], JLabel.TRAILING);
			// p.add(l);
			// JTextField textField = new JTextField(10);
			// textField.setEditable(false);
			// textFields[i] = textField;
			// l.setLabelFor(textField);
			// p.add(textField);
			// }

			// SpringUtilities.makeCompactGrid(p, numPairs, 2, 6, 6, 6, 6);

			JTextArea text = new JTextArea(labels);
			text.setEditable(false);
			text.setBackground(new Color(225, 225, 225));
			text.setFont(new Font("Serif", Font.PLAIN, 15));
			aboutFrame = new JFrame("SpringForm");
			aboutFrame.setLayout(new BorderLayout());
			aboutFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			aboutFrame.setPreferredSize(new Dimension(450, 300));
			aboutFrame.setResizable(false);
			aboutFrame.add(text);

			// p.setOpaque(true);
			// aboutFrame.add(p, BorderLayout.CENTER);

			aboutFrame.pack();
			aboutFrame.setVisible(true);
		}

	}

	class AddUser implements ActionListener {

		private JFrame addFrame;
		private JTextField[] textFields;
		private String[] labels = { "PIN: ", "Streckkod: ", "Namn: ",
				"Telefonnummer: ", "Personnummer", "Antal Streckkodskopior: " };

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

			JButton apply = new JButton(
					"Lägg till användare och skriv ut streckkod(er)");
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
				if (database.checkBarcodeRegistered(textFields[1].getText())) {
					JOptionPane.showMessageDialog(null,
							"Streckkoden är upptagen", "Felmeddelande",
							JOptionPane.ERROR_MESSAGE);
				} else if (textFields[0].getText().equals("")
						|| textFields[1].getText().equals("")
						|| textFields[2].getText().equals("")
						|| textFields[3].getText().equals("")
						|| textFields[4].getText().equals("")
						|| textFields[5].getText().equals("")) {

					JOptionPane.showMessageDialog(null,
							"Var vänlig fyll i alla uppgifter",
							"Felmeddelande", JOptionPane.ERROR_MESSAGE);

				} else {
					int addUserErrorFeedback = database.addUser(
							textFields[0].getText(), textFields[1].getText(),
							textFields[2].getText(), textFields[3].getText(),
							textFields[4].getText());
					if (addUserErrorFeedback == database.PIN_LENGTH_ERROR) {
						JOptionPane.showMessageDialog(null,
								"PIN-koden är inte 4 siffror lång",
								"Felmeddelande", JOptionPane.ERROR_MESSAGE);
					} else if (addUserErrorFeedback == database.BARCODE_LENGTH_ERROR) {
						JOptionPane.showMessageDialog(null,
								"Streckkoden är inte 5 siffror lång",
								"Felmeddelande", JOptionPane.ERROR_MESSAGE);
					} else if (addUserErrorFeedback == database.NO_ADDUSER_ERROR) {
						StringBuilder sb = new StringBuilder();

						sb.append("Cykelägaren har lagts till\n");

						for (int i = 0; i < textFields.length; i++) {
							sb.append(labels[i]);
							sb.append(textFields[i].getText() + "\n");
						}

						mainTextField.setText(sb.toString());

						if (!textFields[5].getText().isEmpty()
								&& !textFields[5].getText().contains("")) {
							int barcodeCopies = Integer.valueOf(textFields[5]
									.getText());
							String barcode = textFields[1].getText();
							print(barcode, barcodeCopies);
						}

						addFrame.setVisible(false);
					}
				}
			}
		}

		class Generate implements ActionListener {

			private Random rand = new Random();
			private String newPin;
			private String newBarcode;

			public void actionPerformed(ActionEvent arg0) {

				StringBuilder pinBuilder = new StringBuilder();

				for (int i = 0; i < 4; i++) {
					int a = rand.nextInt(10);
					pinBuilder.append(String.valueOf(a));
				}

				newPin = pinBuilder.toString();
				textFields[0].setText(newPin);
				StringBuilder barcodeBuilder = new StringBuilder();

				for (int i = 0; i < 5; i++) {
					int b = rand.nextInt(10);
					barcodeBuilder.append(String.valueOf(b));
				}

				while (database.checkPinRegistered(newPin)) {
					pinBuilder.delete(0, 4);

					for (int i = 0; i < 4; i++) {
						int a = rand.nextInt(10);
						pinBuilder.append(String.valueOf(a));
					}

					newPin = pinBuilder.toString();
				}

				newBarcode = barcodeBuilder.toString();

				while (database.checkBarcodeRegistered(newBarcode)) {
					barcodeBuilder.delete(0, 5);

					for (int i = 0; i < 5; i++) {
						int b = rand.nextInt(10);
						barcodeBuilder.append(String.valueOf(b));
					}

					newBarcode = barcodeBuilder.toString();
				}

				textFields[1].setText(newBarcode);
			}
		}
	}

	class RemoveUser implements ActionListener {
		private JFrame removeFrame;
		private JTextField[] textFields;
		private String[] labels = { "Cykelägarens streckkodsnummer: ", "PIN: " };

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

			removeFrame = new JFrame("SpringForm");
			removeFrame.setLayout(new BorderLayout());
			removeFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

			p.setOpaque(true);
			removeFrame.add(p, BorderLayout.CENTER);

			JPanel buttons = new JPanel();
			buttons.setLayout(new BorderLayout());

			JButton cancel = new JButton("Avbryt");
			cancel.addActionListener(new Cancel());
			//
			JButton apply = new JButton("Ta bort");
			apply.addActionListener(new Apply());

			buttons.add(cancel, BorderLayout.LINE_START);
			buttons.add(apply, BorderLayout.LINE_END);

			removeFrame.add(buttons, BorderLayout.SOUTH);
			removeFrame.pack();
			removeFrame.setVisible(true);
		}

		class Cancel implements ActionListener {

			public void actionPerformed(ActionEvent arg0) {
				removeFrame.setVisible(false);
			}
		}

		class Apply implements ActionListener {

			public void actionPerformed(ActionEvent arg0) {
				mainTextField.setText("");
				if (!database.checkBarcodeRegistered(textFields[0].getText())) {
					JOptionPane.showMessageDialog(null,
							"Det finns ingen cykelägare med denna streckkod",
							"Felmeddelande", JOptionPane.ERROR_MESSAGE);
				} else if (textFields[0].getText().equals("")
						|| textFields[1].getText().equals("")) {

					JOptionPane.showMessageDialog(null,
							"Var vänlig fyll i alla uppgifter",
							"Felmeddelande", JOptionPane.ERROR_MESSAGE);

				} else if (textFields[1].getText().equals(
						database.getUserByBarcode(textFields[0].getText())
								.getPin())) {
					JOptionPane
							.showMessageDialog(
									null,
									"Den inmatade PIN-koden matcher inte PIN-koden som  är associerad med den inmatade streckkoden",
									"Felmeddelande", JOptionPane.ERROR_MESSAGE);
				} else if (database.getUserByBarcode(textFields[0].getText())
						.getBikesInGarage() > 0) {
					if (JOptionPane
							.showConfirmDialog(
									null,
									"Cykelägaren har cyklar i garaget.\nÄr du säker på att du vill ta bort cykelägaren?",
									"Felmeddelande",
									JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
						delete();
					}

				} else {
					delete();
				}
			}

			public void delete() {
				User u = database.getUserByBarcode(textFields[0].getText());
				database.removeUser(textFields[0].getText());

				StringBuilder sb = new StringBuilder();

				sb.append("Cykelägaren har tagits bort \n");
				sb.append("Namn: ");
				sb.append(u.getName() + "\n");

				for (int i = 0; i < textFields.length; i++) {
					sb.append(labels[i]);
					sb.append(textFields[i].getText() + "\n");
				}

				mainTextField.setText(sb.toString());

				removeFrame.setVisible(false);
			}
		}
	}

	class EditUser implements ActionListener {

		private JFrame editFrame;
		private JTextField[] textFields;
		private String[] labels = { "Cykelägarens streckkodsnummer: ",
				"Personnummer: " };
		private User u;
		private JTextField[] textSubFields;
		private JFrame editSubFrame;

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

			editFrame = new JFrame("SpringForm");
			editFrame.setLayout(new BorderLayout());
			editFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

			p.setOpaque(true);
			editFrame.add(p, BorderLayout.CENTER);

			JPanel buttons = new JPanel();
			buttons.setLayout(new BorderLayout());

			JButton cancel = new JButton("Avbryt");
			cancel.addActionListener(new Cancel());
			//
			JButton apply = new JButton("Sök");
			apply.addActionListener(new Apply());

			buttons.add(cancel, BorderLayout.LINE_START);
			buttons.add(apply, BorderLayout.LINE_END);

			editFrame.add(buttons, BorderLayout.SOUTH);
			editFrame.pack();
			editFrame.setVisible(true);
		}

		class Cancel implements ActionListener {

			public void actionPerformed(ActionEvent arg0) {
				editFrame.setVisible(false);
			}
		}

		class Apply implements ActionListener {

			private String[] labels = { "PIN: ", "Streckkod: ", "Namn: ",
					"Telefonnummer: ", "Personnummer",
					"Antal Streckkodskopior: ", "Antal cyklar i garaget: " };

			public void actionPerformed(ActionEvent e) {
				editFrame.setVisible(false);
				if (!textFields[0].getText().isEmpty()) {
					u = database.getUserByBarcode(textFields[0].getText());
					if (u == null) {
						JOptionPane.showMessageDialog(null,
								"Streckkodsnumret existerar ej",
								"Felmeddelande", JOptionPane.ERROR_MESSAGE);
					} else {
						findUser(u);
					}
				} else if (!textFields[1].getText().isEmpty()) {
					u = database.getUserByPersonnumber(textFields[1].getText());
					if (u == null) {
						JOptionPane.showMessageDialog(null,
								"Personnumret existerar ej", "Felmeddelande",
								JOptionPane.ERROR_MESSAGE);
					} else {
						findUser(u);
					}
				} else {
					JOptionPane.showMessageDialog(null,
							"Var vänlig fyll i alla uppgifter",
							"Felmeddelande", JOptionPane.ERROR_MESSAGE);
				}

			}

			private void findUser(User u) {
				int numPairs = labels.length;

				textSubFields = new JTextField[labels.length];
				JPanel p = new JPanel(new SpringLayout());
				for (int i = 0; i < numPairs; i++) {
					JLabel l = new JLabel(labels[i], JLabel.TRAILING);
					p.add(l);
					JTextField textField = new JTextField(10);
					textSubFields[i] = textField;
					l.setLabelFor(textField);
					p.add(textField);
				}

				textSubFields[0].setText(u.getPin());
				textSubFields[1].setText(u.getBarcode());
				textSubFields[1].setEditable(false);
				textSubFields[2].setText(u.getName());
				textSubFields[3].setText(u.getTelNr());
				// textSubFields[4].setText("0"); //kan vara bra att det står så
				// från början
				textSubFields[5].setText(u.getPersonNr());
				textSubFields[6].setText(String.valueOf(u.getBikesInGarage()));

				SpringUtilities.makeCompactGrid(p, numPairs, 2, 6, 6, 6, 6);

				editSubFrame = new JFrame("SpringForm");
				editSubFrame.setLayout(new BorderLayout());
				editSubFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

				p.setOpaque(true);
				editSubFrame.add(p, BorderLayout.CENTER);

				JPanel buttons = new JPanel();
				buttons.setLayout(new BorderLayout());

				JButton cancel = new JButton("Avbryt");
				cancel.addActionListener(new SubCancel());

				JButton apply = new JButton("Godkänn");
				apply.addActionListener(new SubApply());

				JButton generate = new JButton("Generera");
				generate.addActionListener(new Generate());

				buttons.add(cancel, BorderLayout.LINE_START);
				buttons.add(apply, BorderLayout.LINE_END);
				buttons.add(generate, BorderLayout.SOUTH);

				editSubFrame.add(buttons, BorderLayout.SOUTH);
				editSubFrame.pack();
				editSubFrame.setVisible(true);
			}

		}

		class Generate implements ActionListener {

			private Random rand = new Random();
			private String newPin;

			public void actionPerformed(ActionEvent arg0) {

				StringBuilder pinBuilder = new StringBuilder();

				for (int i = 0; i < 4; i++) {
					int a = rand.nextInt(10);
					pinBuilder.append(String.valueOf(a));
				}

				newPin = pinBuilder.toString();
				textSubFields[0].setText(newPin);

				while (database.checkPinRegistered(newPin)) {
					pinBuilder.delete(0, 4);

					for (int i = 0; i < 4; i++) {
						int a = rand.nextInt(10);
						pinBuilder.append(String.valueOf(a));
					}

					newPin = pinBuilder.toString();
				}
			}
		}

		class SubApply implements ActionListener {

			public void actionPerformed(ActionEvent e) {

				database.removeUser(u.getBarcode());
				database.addUser(textSubFields[0].getText(),
						textSubFields[1].getText(), textSubFields[2].getText(),
						textSubFields[3].getText(), textSubFields[4].getText());
				// database.modifyBikesInGarage();

				StringBuilder sb = new StringBuilder();
				sb.append("Cykelägaren har redigerats\nPIN: " + u.getPin()
						+ " -> " + textSubFields[0].getText() + "\nStreckkod: "
						+ u.getBarcode() + " -> " + textSubFields[1].getText()
						+ "\nNamn: " + u.getName() + " -> "
						+ textSubFields[2].getText() + "\nTelNr: "
						+ u.getTelNr() + " -> " + textSubFields[3].getText()
						+ "\nPersonNr: " + u.getPersonNr()
						+ "\nAntal cyklar i garaget: " + u.getBikesInGarage()
						+ " -> " + textSubFields[5].getText());
				mainTextField.setText("");
				mainTextField.setText(sb.toString());

				if (!textSubFields[4].getText().isEmpty()
						&& !textSubFields[4].getText().contains(" ")) { // fixade
																		// bugg
																		// #4
					int barcodeCopies = Integer.valueOf(textSubFields[4]
							.getText());
					String barcode = textSubFields[1].getText();
					print(barcode, barcodeCopies);
				}

				editSubFrame.setVisible(false);
			}

		}

		class SubCancel implements ActionListener {

			public void actionPerformed(ActionEvent arg0) {
				editSubFrame.setVisible(false);
			}
		}

	}

	// /**
	// * Returnerar en specifik cykelägare.
	// *
	// * @param barcode
	// * cykelägarens streckkod
	// * @return cykelägaren
	// */
	// public User getUser(String barcode) {
	// return null;
	// }

	public boolean running() {
		if (frame.isVisible()) {
			return true;
		}
		return false;
	}

	/**
	 * Söker efter streckkoden för en viss cykelägare.
	 * 
	 * @param name
	 *            cykelägarens namn
	 * @return cykelägarens streckkod
	 */
	// public String findBarcodeWithName(String name) {
	// return null;
	// }

	/**
	 * Genererar en ny 4-siffrig PIN-kod.
	 * 
	 * @return ny PIN-kod
	 */
	// public String GeneratePin() {
	// return null;
	// }

	public static void main(String[] args) {
		BicycleGarageDatabase database = new BicycleGarageDatabase(10000);
		BicycleGarageManager manager = new BicycleGarageManager(database);
		database.load();
		Operator main = new Operator(database, manager);
		while (main.running()) {
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			database.save();
		}
		System.exit(0);
	}

	private void print(String barcode, int barcodeCopies) {
		if (barcodeCopies > 0) {
			for (int i = 0; i < barcodeCopies; i++) {
				manager.print(barcode);
				// System.out.println("Streckkodskopia nr: " + i);
			}
		}
	}

	private class AutosaveTask implements Runnable {
		private volatile long saveFrequency;

		/**
		 * @param saveFrequency
		 *            hur många sekunder det är mellan varje gång databasen
		 *            autosparas
		 * */
		public AutosaveTask(long saveFrequency) {
			this.saveFrequency = saveFrequency;
		}

		public void run() {

			try {
				Thread.sleep(saveFrequency * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			database.save();
			// continue;

		}

		public void editSaveFrequency(long saveFrequency) {
			this.saveFrequency = saveFrequency;
		}
	}
}
