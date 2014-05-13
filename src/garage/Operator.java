package garage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;

import springUtilities.SpringUtilities;

public class Operator {

	private BicycleGarageDatabase database;
	private JPanel buttonPanel, textPanel;
	private JTextField textField;
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

		textField = new JTextField(2);
		textField.setSize(600, 500);
		textField.setEditable(false);
		textField.setBackground(new Color(255, 255, 255));

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

	class AddBikeOwner implements ActionListener {
		
		private JFrame frame;
		
		public void actionPerformed(ActionEvent e) {
			String[] labels = { "Name: ", "Fax: ", "Email: ", "Address: " };
			int numPairs = labels.length;

			// Create and populate the panel.
			JPanel p = new JPanel(new SpringLayout());
			for (int i = 0; i < numPairs; i++) {
				JLabel l = new JLabel(labels[i], JLabel.TRAILING);
				p.add(l);
				JTextField textField = new JTextField(10);
				l.setLabelFor(textField);
				p.add(textField);
			}

			// Lay out the panel.
			SpringUtilities.makeCompactGrid(p, numPairs, 2, // rows, cols
					6, 6, // initX, initY
					6, 6); // xPad, yPad

			// Create and set up the window.
			frame = new JFrame("SpringForm");
			frame.setLayout(new BorderLayout());
			frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

			// Set up the content pane.
			p.setOpaque(true); // content panes must be opaque
			frame.add(p, BorderLayout.CENTER);

			JPanel buttons = new JPanel();
			buttons.setLayout(new BorderLayout());
			
			JButton cancel = new JButton("Avbryt");
			cancel.addActionListener(new Cancel());
			
			buttons.add(cancel, BorderLayout.LINE_START);
			buttons.add(new JButton("Verkställ"), BorderLayout.LINE_END);
			buttons.add(new JButton("Generera"), BorderLayout.SOUTH);

			// Display the window.
			frame.add(buttons, BorderLayout.SOUTH);
			frame.pack();
			frame.setVisible(true);
		}
		
		class Cancel implements ActionListener{

			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
			}
		}
		
		class 

			
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
