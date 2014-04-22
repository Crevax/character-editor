import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.dom4j.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class Client extends JFrame implements ActionListener
{
	private static final int WIDTH = 924;
	private static final int HEIGHT = 668;
	
	private Socket client;
	private PrintWriter writer;
	private BufferedReader reader;
	
	private JMenuBar mnuMain = new JMenuBar();
	private JMenu mnuFile, mnuHelp;
	private JMenuItem mnuFileExit;
	private JMenuItem mnuHelpAbout;
	
	private JTabbedPane tabbedPane;
	
	private JTable charTable, trashTable;
	
	private DefaultTableModel charTableModel, trashTableModel; 
	
	private JPanel 	topPanel, addCharacter, editorContainer, charSearch, 
					searchResults, editCharacter, trashBin;
	
	// JLabels for addCharacter
	private JLabel 	lblAddCharName, lblAddCharTitle, lblAddCharRole, lblAddCharBaseHP,
					lblAddCharBaseAtk, lblAddCharBaseDef, lblAddCharBaseMP, lblAddCharHPGain,
					lblAddCharAtkGain, lblAddCharDefGain, lblAddCharMPGain;
	
	// JLabels for editCharacter
	private JLabel 	lblEditCharID, lblEditCharName, lblEditCharTitle, lblEditCharRole, lblEditCharBaseHP,
					lblEditCharBaseAtk, lblEditCharBaseDef, lblEditCharBaseMP, lblEditCharHPGain,
					lblEditCharAtkGain, lblEditCharDefGain, lblEditCharMPGain, lblSearchName;
	
	// JTextFields for addCharacter
	private JTextField 	txtAddCharName, txtAddCharTitle, txtAddCharBaseHP, txtAddCharBaseAtk,
						txtAddCharBaseDef, txtAddCharBaseMP, txtAddCharHPGain,
						txtAddCharAtkGain, txtAddCharDefGain, txtAddCharMPGain;
	
	// JTextFields for editCharacter
	private JTextField 	txtEditCharID, txtEditCharName, txtEditCharTitle, txtEditCharBaseHP,
						txtEditCharBaseAtk, txtEditCharBaseDef, txtEditCharBaseMP, txtEditCharHPGain,
						txtEditCharAtkGain, txtEditCharDefGain, txtEditCharMPGain, txtSearchName;
	
	// JComboBox for addCharacter
	private JComboBox cbAddCharRole;
	
	// JComboBox for editCharacter
	private JComboBox cbEditCharRole;
	
	private JButton btnAdd, btnEdit, btnSearch,
					btnSelect, btnDelete, btnUndelete;
	
	public Client()
	{
		setTitle("Character Editor");
		setSize(WIDTH,HEIGHT);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		int x =  ( d.width - this.getWidth() ) / 2 ;
		int y = ( d.height - this.getHeight() ) / 2;
		this.setLocation(x, y);
		
		setJMenuBar(mnuMain);
		setFileMenu();
		setHelpMenu();
		
		topPanel = new JPanel();
		topPanel.setLayout( new BorderLayout() );
		getContentPane().add(topPanel);
		
		addCharacterScreen();
		editCharacterScreen();
		trashBinScreen();
		
		tabbedPane = new JTabbedPane();
		tabbedPane.addTab("New Character", addCharacter);
		tabbedPane.addTab("Edit Character", editorContainer);
		tabbedPane.addTab("Trash Bin", trashBin);
		
		topPanel.add(tabbedPane, BorderLayout.CENTER);
		
	}
	
	private void addCharacterScreen()
	{	
	
		addCharacter = new JPanel();
		addCharacter.setLayout( new GridLayout(12,2) );
		
		lblAddCharName = new JLabel("Name: ", SwingConstants.RIGHT);
		lblAddCharTitle = new JLabel("Title: ", SwingConstants.RIGHT);
		lblAddCharRole = new JLabel("Role: ", SwingConstants.RIGHT);
		lblAddCharBaseHP = new JLabel("Base Health: ", SwingConstants.RIGHT);
		lblAddCharBaseAtk = new JLabel("Base Attack Damage: ", SwingConstants.RIGHT);
		lblAddCharBaseDef = new JLabel("Base Defense: ", SwingConstants.RIGHT);
		lblAddCharBaseMP = new JLabel("Base Mana: ", SwingConstants.RIGHT);
		lblAddCharHPGain = new JLabel("Health Gain Per Level: ", SwingConstants.RIGHT);
		lblAddCharAtkGain = new JLabel("Attack Gain Per Level: ", SwingConstants.RIGHT);
		lblAddCharDefGain = new JLabel("Defense Gain Per Level: ", SwingConstants.RIGHT);
		lblAddCharMPGain = new JLabel("Mana Gain Per Level: ", SwingConstants.RIGHT);

		txtAddCharName = new JTextField(10);
		txtAddCharTitle = new JTextField(10); 
		txtAddCharBaseHP = new JTextField(10);
		txtAddCharBaseAtk = new JTextField(10);
		txtAddCharBaseDef = new JTextField(10);
		txtAddCharBaseMP = new JTextField(10);
		txtAddCharHPGain = new JTextField(10);
		txtAddCharAtkGain = new JTextField(10);
		txtAddCharDefGain = new JTextField(10);
		txtAddCharMPGain = new JTextField(10);
		
		cbAddCharRole = loadCharacterRoles();
		
		btnAdd = new JButton("Add");
		btnAdd.setActionCommand("Add");
		btnAdd.addActionListener(this);
		
		addCharacter.add(lblAddCharName);
		addCharacter.add(txtAddCharName);
		
		addCharacter.add(lblAddCharTitle);
		addCharacter.add(txtAddCharTitle);
		
		addCharacter.add(lblAddCharRole);
		addCharacter.add(cbAddCharRole);
		
		addCharacter.add(lblAddCharBaseHP);
		addCharacter.add(txtAddCharBaseHP);
		
		addCharacter.add(lblAddCharBaseAtk);
		addCharacter.add(txtAddCharBaseAtk);
		
		addCharacter.add(lblAddCharBaseDef);
		addCharacter.add(txtAddCharBaseDef);
		
		addCharacter.add(lblAddCharBaseMP);
		addCharacter.add(txtAddCharBaseMP);
		
		addCharacter.add(lblAddCharHPGain);
		addCharacter.add(txtAddCharHPGain);
		
		addCharacter.add(lblAddCharAtkGain);
		addCharacter.add(txtAddCharAtkGain);
		
		addCharacter.add(lblAddCharDefGain);
		addCharacter.add(txtAddCharDefGain);
		
		addCharacter.add(lblAddCharMPGain);
		addCharacter.add(txtAddCharMPGain);
		
		addCharacter.add(btnAdd);
	}

	private void editCharacterScreen()
	{	
		editorContainer = new JPanel();
		editorContainer.setLayout( new BorderLayout() );
		
		//Start Search Panel
		charSearch = new JPanel();
		charSearch.setLayout( new GridLayout(1,3) );
		
		lblSearchName = new JLabel("Search by name: ", SwingConstants.RIGHT);
		txtSearchName = new JTextField(10);
		
		charSearch.add(lblSearchName);
		charSearch.add(txtSearchName);
		
		btnSearch =  new JButton("Search");
		btnSearch.setActionCommand("Search");
		btnSearch.addActionListener(this);
		
		charSearch.add(btnSearch);
		//End Search Panel
		
		//Start Results Area
		searchResults = new JPanel();
		searchResults.setLayout( new GridLayout(2,1) );
		
		charTableModel = new DefaultTableModel();
		charTableModel.addColumn("ID");
		charTableModel.addColumn("Name");
		charTableModel.addColumn("Title");		
		
		charTable = new JTable(charTableModel);
		
		JScrollPane scrollPane = new JScrollPane(charTable);
		
		searchResults.add(scrollPane);
		
		btnSelect =  new JButton("Select");
		btnSelect.setActionCommand("Select");
		btnSelect.addActionListener(this);
		
		searchResults.add(btnSelect);
		//End Results Area
		
		//Begin Editing Area
		editCharacter = new JPanel();
		editCharacter.setLayout( new GridLayout(13,2) );
		
		lblEditCharID = new JLabel("ID: ", SwingConstants.RIGHT);
		lblEditCharName = new JLabel("Name: ", SwingConstants.RIGHT);
		lblEditCharTitle = new JLabel("Title: ", SwingConstants.RIGHT);
		lblEditCharRole = new JLabel("Role: ", SwingConstants.RIGHT);
		lblEditCharBaseHP = new JLabel("Base Health: ", SwingConstants.RIGHT);
		lblEditCharBaseAtk = new JLabel("Base Attack Damage: ", SwingConstants.RIGHT);
		lblEditCharBaseDef = new JLabel("Base Defense: ", SwingConstants.RIGHT);
		lblEditCharBaseMP = new JLabel("Base Mana: ", SwingConstants.RIGHT);
		lblEditCharHPGain = new JLabel("Health Gain Per Level: ", SwingConstants.RIGHT);
		lblEditCharAtkGain = new JLabel("Attack Gain Per Level: ", SwingConstants.RIGHT);
		lblEditCharDefGain = new JLabel("Defense Gain Per Level: ", SwingConstants.RIGHT);
		lblEditCharMPGain = new JLabel("Mana Gain Per Level: ", SwingConstants.RIGHT);

		txtEditCharID = new JTextField(10);
		txtEditCharName = new JTextField(10);
		txtEditCharTitle = new JTextField(10); 
		txtEditCharBaseHP = new JTextField(10);
		txtEditCharBaseAtk = new JTextField(10);
		txtEditCharBaseDef = new JTextField(10);
		txtEditCharBaseMP = new JTextField(10);
		txtEditCharHPGain = new JTextField(10);
		txtEditCharAtkGain = new JTextField(10);
		txtEditCharDefGain = new JTextField(10);
		txtEditCharMPGain = new JTextField(10);
		
		cbEditCharRole = loadCharacterRoles();
		
		btnEdit = new JButton("Update");
		btnEdit.setActionCommand("Update");
		btnEdit.addActionListener(this);
		
		btnDelete = new JButton("Delete");
		btnDelete.setActionCommand("Delete");
		btnDelete.addActionListener(this);
		
		editCharacter.add(lblEditCharID);
		editCharacter.add(txtEditCharID);
		
		editCharacter.add(lblEditCharName);
		editCharacter.add(txtEditCharName);
		
		editCharacter.add(lblEditCharTitle);
		editCharacter.add(txtEditCharTitle);
		
		editCharacter.add(lblEditCharRole);
		editCharacter.add(cbEditCharRole);
		
		editCharacter.add(lblEditCharBaseHP);
		editCharacter.add(txtEditCharBaseHP);
		
		editCharacter.add(lblEditCharBaseAtk);
		editCharacter.add(txtEditCharBaseAtk);
		
		editCharacter.add(lblEditCharBaseDef);
		editCharacter.add(txtEditCharBaseDef);
		
		editCharacter.add(lblEditCharBaseMP);
		editCharacter.add(txtEditCharBaseMP);
		
		editCharacter.add(lblEditCharHPGain);
		editCharacter.add(txtEditCharHPGain);
		
		editCharacter.add(lblEditCharAtkGain);
		editCharacter.add(txtEditCharAtkGain);
		
		editCharacter.add(lblEditCharDefGain);
		editCharacter.add(txtEditCharDefGain);
		
		editCharacter.add(lblEditCharMPGain);
		editCharacter.add(txtEditCharMPGain);
		
		editCharacter.add(btnEdit);
		editCharacter.add(btnDelete);
		//End Editing Area
		
		editorContainer.add(charSearch, BorderLayout.NORTH);
		editorContainer.add(searchResults, BorderLayout.CENTER);
		editorContainer.add(editCharacter, BorderLayout.SOUTH);
	}
	
	private void trashBinScreen()
	{
		trashBin = new JPanel();
		trashBin.setLayout( new GridLayout(3,1) );
		
		trashTableModel = new DefaultTableModel();
		trashTableModel.addColumn("ID");
		trashTableModel.addColumn("Name");
		trashTableModel.addColumn("Title");	
		
		loadDeletedCharacters();
		
		trashTable = new JTable(trashTableModel);
		
		JScrollPane scrollPane = new JScrollPane(trashTable);
		
		trashBin.add(scrollPane);
		
		btnDelete = new JButton("Delete");
		btnDelete.setActionCommand("Purge");
		btnDelete.addActionListener(this);
		
		btnUndelete = new JButton("Undelete");
		btnUndelete.setActionCommand("Undelete");
		btnUndelete.addActionListener(this);
		
		trashBin.add(btnDelete);
		trashBin.add(btnUndelete);
	}
	
	private void setFileMenu()
	{
		mnuFile = new JMenu("File");
		mnuMain.add(mnuFile);
		mnuFileExit = new JMenuItem("Exit");
		mnuFile.add(mnuFileExit);
		mnuFileExit.addActionListener(this);
	}
	
	private void setHelpMenu()
	{
		mnuHelp = new JMenu("Help");
		mnuMain.add(mnuHelp);
		mnuHelpAbout = new JMenuItem("About");
		mnuHelp.add(mnuHelpAbout);
		mnuHelpAbout.addActionListener(this);
	}
	
	private void showAboutScreen()
	{
		JOptionPane.showMessageDialog(null, "Version 0.1 Beta \n Made by Christopher Davis");
	}
	
	private JComboBox loadCharacterRoles()
	{
		JComboBox cbo = new JComboBox();
		
		try {
			client = new Socket("localhost", 5000);
			writer = new PrintWriter( client.getOutputStream(), true );
			reader = new BufferedReader( new InputStreamReader( client.getInputStream() ) );
	
			writer.println("<request><action>getRoleCodes</action></request>");
			writer.flush();
			
			String response = reader.readLine();
			
			Document xmlDoc = DocumentHelper.parseText(response);
			XPath xPath = DocumentHelper.createXPath("//response/RoleCodes/RoleCode");
			
			java.util.List l = xPath.selectNodes(xmlDoc);
			
			if( l != null)
			{
				for( int lcv = 0; lcv < l.size(); lcv++)
				{
					Node n = (Node)l.get(lcv);
					System.out.println(n.asXML());
					cbo.addItem( getData( n.asXML(), "//RoleCode/ROLE_NAME" ) );
				}
			}
			
			writer.println("<request><action>disconnect</action></request>");
			writer.flush();
			
			writer.close();
			reader.close();
			client.close();
			
			writer = null;
			reader = null;
			client = null;
			
		} catch (UnknownHostException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (DocumentException ex) {
			ex.printStackTrace();
		}
		
		return cbo;
	}

	private void loadDeletedCharacters()
	{
		
		try {
			client = new Socket("localhost", 5000);
			writer = new PrintWriter( client.getOutputStream(), true );
			reader = new BufferedReader( new InputStreamReader( client.getInputStream() ) );
	
			writer.println("<request><action>getDeletedChars</action></request>");
			writer.flush();
			
			String response = reader.readLine();
			
			Document xmlDoc = DocumentHelper.parseText(response);
			XPath xPath = DocumentHelper.createXPath("//response/DeletedChars/DeletedChar");
			
			java.util.List l = xPath.selectNodes(xmlDoc);
			
			if( l != null)
			{
				for( int lcv = 0; lcv < l.size(); lcv++)
				{
					Node n = (Node)l.get(lcv);
					System.out.println(n.asXML());
					trashTableModel.addRow( new Object[]{getData( n.asXML(), "//DeletedChar/CHAR_ID" ),
								getData( n.asXML(), "//DeletedChar/CHAR_NAME" ),
								getData( n.asXML(), "//DeletedChar/CHAR_TITLE" )} );
				}
			}
			
			writer.println("<request><action>disconnect</action></request>");
			writer.flush();
			
			writer.close();
			reader.close();
			client.close();
			
			writer = null;
			reader = null;
			client = null;
			
		} catch (UnknownHostException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (DocumentException ex) {
			ex.printStackTrace();
		}
	}
	
	private void addCharacter()
	{
		Document xmlDocument = DocumentHelper.createDocument();
		
		Element root = xmlDocument.addElement("request");
		root.addElement("action").addText("add");
		
		if( txtAddCharName.getText().equals("") )
		{
			txtAddCharName.setText("Unnamed Hero");
		}
		root.addElement("Name").addText( txtAddCharName.getText() );
		
		if( txtAddCharTitle.getText().equals("") )
		{
			txtAddCharTitle.setText("Untitled Hero");
		}
		root.addElement("Title").addText( txtAddCharTitle.getText() );
		
		root.addElement("RoleID").addText( String.valueOf( (cbAddCharRole.getSelectedIndex() + 1) ) );
		
		if( txtAddCharBaseHP.getText().equals("") )
		{
			txtAddCharBaseHP.setText("0");
		}
		root.addElement("BaseHealth").addText( String.valueOf( txtAddCharBaseHP.getText() ) );	
		
		if( txtAddCharBaseAtk.getText().equals("") )
		{
			txtAddCharBaseAtk.setText("0");
		}
		root.addElement("BaseAttack").addText( String.valueOf( txtAddCharBaseAtk.getText() ) );
		
		if( txtAddCharBaseDef.getText().equals("") )
		{
			txtAddCharBaseDef.setText("0");
		}
		root.addElement("BaseDefense").addText( String.valueOf( txtAddCharBaseDef.getText() ) );
		
		if( txtAddCharBaseMP.getText().equals("") )
		{
			txtAddCharBaseMP.setText("0");
		}
		root.addElement("BaseMana").addText( String.valueOf( txtAddCharBaseMP.getText() ) );
		
		if( txtAddCharHPGain.getText().equals("") )
		{
			txtAddCharHPGain.setText("0");
		}
		root.addElement("HealthGainPerLvl").addText( String.valueOf( txtAddCharHPGain.getText() ) );	
		
		if( txtAddCharAtkGain.getText().equals("") )
		{
			txtAddCharAtkGain.setText("0");
		}
		root.addElement("AttackGainPerLvl").addText( String.valueOf( txtAddCharAtkGain.getText() ) );
		
		if( txtAddCharDefGain.getText().equals("") )
		{
			txtAddCharDefGain.setText("0");
		}
		root.addElement("DefenseGainPerLvl").addText( String.valueOf( txtAddCharDefGain.getText() ) );
		
		if( txtAddCharMPGain.getText().equals("") )
		{
			txtAddCharMPGain.setText("0");
		}
		root.addElement("ManaGainPerLvl").addText( String.valueOf( txtAddCharMPGain.getText() ) );
			
		
		// Server connection setup
		try {
			client = new Socket("localhost", 5000);
			writer = new PrintWriter( client.getOutputStream(), true );
			reader = new BufferedReader( new InputStreamReader( client.getInputStream() ) );
	
			
			writer.println(root.asXML());
			//writer.flush();
			
			String response = reader.readLine();
			
			String errorCode = getData(response, "//response/ErrorCode");
			
			if( errorCode.equals("0") )
			{
				JOptionPane.showMessageDialog(null, getData(response, "//response/ErrorMessage"));
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Character added with ID " + getData(response, "//response/CHAR_ID") + ".");
			}
	
			writer.println("<request><action>disconnect</action></request>");
			writer.flush();
			
			writer.close();
			reader.close();
			client.close();
			
			writer = null;
			reader = null;
			client = null;
			
		} catch (UnknownHostException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	private void perfromSearch()
	{
		Document xmlDocument = DocumentHelper.createDocument();
		
		Element root = xmlDocument.addElement("request");
		root.addElement("action").addText("inquire");
		root.addElement("searchTxt").addText( txtSearchName.getText() );
		
		// Server connection setup
		try {
			client = new Socket("localhost", 5000);
			writer = new PrintWriter( client.getOutputStream(), true );
			reader = new BufferedReader( new InputStreamReader( client.getInputStream() ) );
	
			
			writer.println(root.asXML());
			//writer.flush();
			
			String response = reader.readLine();
			
			String errorCode = getData(response, "//response/ErrorCode");
			
			if( errorCode.equals("0") )
			{
				JOptionPane.showMessageDialog(null, getData(response, "//response/ErrorMessage"));
			}
			else
			{
				Document xmlDoc = DocumentHelper.parseText(response);
				XPath xPath = DocumentHelper.createXPath("//response/Characters/Character");
				
				java.util.List l = xPath.selectNodes(xmlDoc);
				
				if( charTableModel.getColumnCount() > 0)
				{
					clearTable(charTableModel);
				}
				
				if( l != null)
				{
					for( int lcv = 0; lcv < l.size(); lcv++)
					{
						Node n = (Node)l.get(lcv);
						System.out.println(n.asXML());
						charTableModel.addRow( new Object[]{getData( n.asXML(), "//Character/CHAR_ID" ),
									getData( n.asXML(), "//Character/CHAR_NAME" ),
									getData( n.asXML(), "//Character/CHAR_TITLE" )} );
					}
				}
			}
	
			writer.println("<request><action>disconnect</action></request>");
			writer.flush();
			
			writer.close();
			reader.close();
			client.close();
			
			writer = null;
			reader = null;
			client = null;
			
		} catch (UnknownHostException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	private void getCharacter(int ID)
	{
		Document xmlDocument = DocumentHelper.createDocument();
		
		Element root = xmlDocument.addElement("request");
		root.addElement("action").addText("getCharacter");
		root.addElement("CharID").addText( String.valueOf(ID) );
		
		// Server connection setup
		try {
			client = new Socket("localhost", 5000);
			writer = new PrintWriter( client.getOutputStream(), true );
			reader = new BufferedReader( new InputStreamReader( client.getInputStream() ) );
	
			
			writer.println(root.asXML());
			//writer.flush();
			
			String response = reader.readLine();
			
			String errorCode = getData(response, "//response/ErrorCode");
			
			if( errorCode.equals("0") )
			{
				JOptionPane.showMessageDialog(null, getData(response, "//response/ErrorMessage"));
			}
			else
			{
				txtEditCharID.setText( getData(response, "//Character/CharID") );
				txtEditCharName.setText( getData(response, "//Character/CharName") );
				txtEditCharTitle.setText( getData(response, "//Character/CharTitle") );
				txtEditCharBaseHP.setText( getData(response, "//Character/BaseHealth") );
				txtEditCharBaseAtk.setText( getData(response, "//Character/BaseAttack") );
				txtEditCharBaseDef.setText( getData(response, "//Character/BaseDefense") );
				txtEditCharBaseMP.setText( getData(response, "//Character/BaseMana") );
				txtEditCharHPGain.setText( getData(response, "//Character/HealthGainPerLvl") );
				txtEditCharAtkGain.setText( getData(response, "//Character/AttackGainPerLvl") );
				txtEditCharDefGain.setText( getData(response, "//Character/DefenseGainPerLvl") );
				txtEditCharMPGain.setText( getData(response, "//Character/ManaGainPerLvl") );
			}
			
			clearTable(charTableModel);
	
			writer.println("<request><action>disconnect</action></request>");
			writer.flush();
			
			writer.close();
			reader.close();
			client.close();
			
			writer = null;
			reader = null;
			client = null;
			
		} catch (UnknownHostException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	private void updateCharacter()
	{
		Document xmlDocument = DocumentHelper.createDocument();
		
		Element root = xmlDocument.addElement("request");
		root.addElement("action").addText("update");
		
		root.addElement("CharID").addText( String.valueOf( txtEditCharID.getText() ) );
		
		if( txtEditCharName.getText().equals("") )
		{
			txtEditCharName.setText("Unnamed Hero");
		}
		root.addElement("CharName").addText( txtEditCharName.getText() );
		
		if( txtEditCharTitle.getText().equals("") )
		{
			txtEditCharTitle.setText("Untitled Hero");
		}
		root.addElement("CharTitle").addText( txtEditCharTitle.getText() );
		
		root.addElement("RoleID").addText( String.valueOf( (cbEditCharRole.getSelectedIndex() + 1) ) );
		
		if( txtEditCharBaseHP.getText().equals("") )
		{
			txtEditCharBaseHP.setText("0");
		}
		root.addElement("BaseHealth").addText( String.valueOf( txtEditCharBaseHP.getText() ) );	
		
		if( txtEditCharBaseAtk.getText().equals("") )
		{
			txtEditCharBaseAtk.setText("0");
		}
		root.addElement("BaseAttack").addText( String.valueOf( txtEditCharBaseAtk.getText() ) );
		
		if( txtEditCharBaseDef.getText().equals("") )
		{
			txtEditCharBaseDef.setText("0");
		}
		root.addElement("BaseDefense").addText( String.valueOf( txtEditCharBaseDef.getText() ) );
		
		if( txtEditCharBaseMP.getText().equals("") )
		{
			txtEditCharBaseMP.setText("0");
		}
		root.addElement("BaseMana").addText( String.valueOf( txtEditCharBaseMP.getText() ) );
		
		if( txtEditCharHPGain.getText().equals("") )
		{
			txtEditCharHPGain.setText("0");
		}
		root.addElement("HealthGainPerLvl").addText( String.valueOf( txtEditCharHPGain.getText() ) );	
		
		if( txtEditCharAtkGain.getText().equals("") )
		{
			txtEditCharAtkGain.setText("0");
		}
		root.addElement("AttackGainPerLvl").addText( String.valueOf( txtEditCharAtkGain.getText() ) );
		
		if( txtEditCharDefGain.getText().equals("") )
		{
			txtEditCharDefGain.setText("0");
		}
		root.addElement("DefenseGainPerLvl").addText( String.valueOf( txtEditCharDefGain.getText() ) );
		
		if( txtEditCharMPGain.getText().equals("") )
		{
			txtEditCharMPGain.setText("0");
		}
		root.addElement("ManaGainPerLvl").addText( String.valueOf( txtEditCharMPGain.getText() ) );
			
		
		// Server connection setup
		try {
			client = new Socket("localhost", 5000);
			writer = new PrintWriter( client.getOutputStream(), true );
			reader = new BufferedReader( new InputStreamReader( client.getInputStream() ) );
	
			
			writer.println(root.asXML());
			//writer.flush();
			
			String response = reader.readLine();
			
			String errorCode = getData(response, "//response/ErrorCode");
			
			if( errorCode.equals("0") )
			{
				JOptionPane.showMessageDialog(null, getData(response, "//response/ErrorMessage"));
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Character edited with ID " + String.valueOf( txtEditCharID.getText() )+ ".");
			}
	
			writer.println("<request><action>disconnect</action></request>");
			writer.flush();
			
			writer.close();
			reader.close();
			client.close();
			
			writer = null;
			reader = null;
			client = null;
			
		} catch (UnknownHostException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	private void deleteCharacter(int ID)
	{
		try {
			client = new Socket("localhost", 5000);
			writer = new PrintWriter( client.getOutputStream(), true );
			reader = new BufferedReader( new InputStreamReader( client.getInputStream() ) );
	
			writer.println("<request>" +
								"<action>delete</action>" +
								"<ID>" + ID + "</ID>" +
							"</request>");
			writer.flush();
			
			String response = reader.readLine();
			
			String errorCode = getData(response, "//response/ErrorCode");
			
			if( errorCode.equals("0") )
			{
				JOptionPane.showMessageDialog(null, getData(response, "//response/ErrorMessage"));
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Character with ID " + ID + " has been added to the trash bin.");
			}
			
			writer.println("<request><action>disconnect</action></request>");
			writer.flush();
			
			writer.close();
			reader.close();
			client.close();
			
			writer = null;
			reader = null;
			client = null;
			
		} catch (UnknownHostException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}
	
	private void undeleteCharacter(int ID)
	{
		try {
			client = new Socket("localhost", 5000);
			writer = new PrintWriter( client.getOutputStream(), true );
			reader = new BufferedReader( new InputStreamReader( client.getInputStream() ) );
	
			writer.println("<request>" +
								"<action>undelete</action>" +
								"<ID>" + ID + "</ID>" +
							"</request>");
			writer.flush();
			
			String response = reader.readLine();
			
			String errorCode = getData(response, "//response/ErrorCode");
			
			if( errorCode.equals("0") )
			{
				JOptionPane.showMessageDialog(null, getData(response, "//response/ErrorMessage"));
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Character with ID " + getData(response, "//response/CHAR_ID") + " has been removed from the trash bin.");
			}
			
			writer.println("<request><action>disconnect</action></request>");
			writer.flush();
			
			writer.close();
			reader.close();
			client.close();
			
			writer = null;
			reader = null;
			client = null;
			
		} catch (UnknownHostException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}
		
	private void purgeCharacter(int ID)
	{
		try {
			client = new Socket("localhost", 5000);
			writer = new PrintWriter( client.getOutputStream(), true );
			reader = new BufferedReader( new InputStreamReader( client.getInputStream() ) );
	
			writer.println("<request>" +
								"<action>purge</action>" +
								"<ID>" + ID + "</ID>" +
							"</request>");
			writer.flush();
			
			String response = reader.readLine();
			
			String errorCode = getData(response, "//response/ErrorCode");
			
			if( errorCode.equals("0") )
			{
				JOptionPane.showMessageDialog(null, getData(response, "//response/ErrorMessage"));
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Character with ID " + getData(response, "//response/CHAR_ID") + " has been removed from the database.");
			}
			
			writer.println("<request><action>disconnect</action></request>");
			writer.flush();
			
			writer.close();
			reader.close();
			client.close();
			
			writer = null;
			reader = null;
			client = null;
			
		} catch (UnknownHostException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}
	
	private String getData( String request, String xPath )
	{
		String data = null;
		Document xmlDoc = null;
		XPath xpath = null;
		Node n = null;
		
		try
		{
			xmlDoc = DocumentHelper.parseText( request );
			xpath = DocumentHelper.createXPath( xPath );
			
			n = xpath.selectSingleNode(xmlDoc);
			
			if( n != null)
			{
				data = n.getText();
			}
			
		}
		catch ( Exception ex )
		{
			System.out.println( ex.getMessage() );
		}
		
		return data;
	}
	
	private void clearTable(DefaultTableModel dtm)
	{
		dtm.setRowCount(0);
	}
	
	public static void main(String[] arg)
	{
		Client mainProg = new Client();
		mainProg.setVisible(true);
		mainProg.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if( e.getSource() instanceof JButton || e.getSource() instanceof JMenuItem)
		{
			if( e.getActionCommand().equals("Exit") )
			{
				System.exit(0);
			}
			if( e.getActionCommand().equals("About") )
			{
				showAboutScreen();
			}
			else if( e.getActionCommand().equals("Add") )
			{
				addCharacter();
			}
			else if( e.getActionCommand().equals("Update") )
			{
				if( !txtEditCharID.getText().equals("") )
				{
					updateCharacter();
				}
			}
			else if( e.getActionCommand().equals("Delete") )
			{
				if( !txtEditCharID.getText().equals("") )
				{
					int ID = Integer.parseInt( txtEditCharID.getText() );
					deleteCharacter(ID);
					clearTable(trashTableModel);
					loadDeletedCharacters();
				}
			}
			else if( e.getActionCommand().equals("Undelete") )
			{
					if( trashTable.getSelectedRowCount() == 1 )
					{
						int r = trashTable.getSelectedRow();
						String id = (String)trashTable.getValueAt(r, 0);
						
						int ID = Integer.parseInt( id );
						undeleteCharacter(ID);
						clearTable(trashTableModel);
						loadDeletedCharacters();
					}
			}
			else if( e.getActionCommand().equals("Purge") )
			{
					if( trashTable.getSelectedRowCount() == 1 )
					{
						int r = trashTable.getSelectedRow();
						String id = (String)trashTable.getValueAt(r, 0);
						
						int ID = Integer.parseInt( id );
						purgeCharacter(ID);
						clearTable(trashTableModel);
						loadDeletedCharacters();
					}
			}
			else if( e.getActionCommand().equals("Search") )
			{
				perfromSearch();
			}
			else if( e.getActionCommand().equals("Select") )
			{
				if( charTable.getSelectedRowCount() == 1 )
				{
					int r = charTable.getSelectedRow();
					String id = (String)charTable.getValueAt(r, 0);
					
					int ID = Integer.parseInt( id );
					getCharacter(ID);
				}
			}
		}
		
	}
}
