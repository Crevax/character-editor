// Author: Christopher Davis
import java.io.*;
import java.net.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.dom4j.*;

public class ServerSession extends Thread
{
	private Socket client;
	private BufferedReader remoteIn;
	private PrintWriter remoteOut;
	private boolean isListening;
	public static CharacterDB cDB = new CharacterDB();
	
	public ServerSession( Socket socket )
	{
		try {
			client = socket;
			remoteIn = new BufferedReader( new InputStreamReader( client.getInputStream() ) );
			remoteOut = new PrintWriter( client.getOutputStream(), true );
			isListening = true;
			
			TCPServer.traceOut( "Session #" + getId() + " Started." );
			TCPServer.traceOut( "Session #" + getId() + " is " + client.getInetAddress().toString() );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getAction( String request )
	{
		String action = null;
		Document xmlDoc = null;
		XPath xpath = null;
		Node n = null;
		
		try
		{
			xmlDoc = DocumentHelper.parseText( request );
			xpath = DocumentHelper.createXPath( "//request/action" );
			
			n = xpath.selectSingleNode(xmlDoc);
			
			if( n != null)
			{
				action = n.getText();
			}
			
		}
		catch ( Exception ex )
		{
			System.out.println( ex.getMessage() );
		}
		
		return action;
	}
	
	
	public String getData( String request, String xPath )
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
	
	public synchronized void run()
	{
		String s = null;
		isListening = true;

		while (isListening)
		{
			try {
				s = remoteIn.readLine();
				
				if (s != null) 
				{	
					String action = getAction(s);
					
					TCPServer.traceOut("Session #" + getId() + " Request: " + action);
					
					if( action == null )
					{
						Document xmlDocument = DocumentHelper.createDocument();
						
						Element root = xmlDocument.addElement("response");
						root.addElement("ErrorCode").addText("0");
						root.addElement("ErrorMessage").addText("NO REQUEST DETECTED");
						
						String response = root.asXML();
						
						remoteOut.println(response);
						remoteOut.flush();
					}
					else if ( action.equals("add") )
					{
						String char_name = getData(s, "//request/Name");
						String char_title = getData(s, "//request/Title");
						int role_id = Integer.parseInt(getData(s, "//request/RoleID"));
						int health = Integer.parseInt(getData(s, "//request/BaseHealth"));
						int attack = Integer.parseInt(getData(s, "//request/BaseAttack"));
						int defense = Integer.parseInt(getData(s, "//request/BaseDefense"));
						int mana = Integer.parseInt(getData(s, "//request/BaseMana"));
						int healthGain = Integer.parseInt(getData(s, "//request/HealthGainPerLvl"));
						int attackGain = Integer.parseInt(getData(s, "//request/AttackGainPerLvl"));
						int defenseGain = Integer.parseInt(getData(s, "//request/DefenseGainPerLvl"));
						int manaGain = Integer.parseInt(getData(s, "//request/ManaGainPerLvl"));
						
						int newID = cDB.Add(char_name, char_title, role_id, health, attack, defense, mana, healthGain, attackGain, defenseGain, manaGain);
						
						if( newID != 0)
						{
							Document xmlDocument = DocumentHelper.createDocument();
							
							Element root = xmlDocument.addElement("response");
							root.addElement("ErrorCode").addText("1");
							root.addElement("ErrorMessage").addText("Success!");
							root.addElement("CHAR_ID").addText( String.valueOf(newID) );
							
							String response = root.asXML();
							
							remoteOut.println(response);
							remoteOut.flush();
						}
						else
						{
							Document xmlDocument = DocumentHelper.createDocument();
							
							Element root = xmlDocument.addElement("response");
							root.addElement("ErrorCode").addText("0");
							root.addElement("ErrorMessage").addText("Could not add character to the database.");
							
							String response = root.asXML();
							
							remoteOut.println(response);
							remoteOut.flush();
						}
					}
					else if ( action.equals("FetchAllRecords") )
					{
						Document xmlDocument = DocumentHelper.createDocument();
						
						Element root = xmlDocument.addElement("response");
						root.addElement("ErrorCode").addText("1");
						root.addElement("ErrorMessage");
						
						String response = root.asXML();
						
						remoteOut.println(response);
						remoteOut.flush();
					}
					else if ( action.equals("getCharacter") )
					{
						int char_id = Integer.parseInt(getData(s, "//request/CharID"));
						ResultSet rs = cDB.getCharacter(char_id);
						
						try
						{
							Document xmlDocument = DocumentHelper.createDocument();
							
							Element root = xmlDocument.addElement("response");
							root.addElement("ErrorCode").addText("1");
							Element Character = root.addElement("Character");
							
							while( rs.next() )
							{					
								Character.addElement("CharID").addText( rs.getString("CHAR_ID") );
								Character.addElement("CharName").addText( rs.getString("CHAR_NAME") );
								Character.addElement("CharTitle").addText( rs.getString("CHAR_TITLE") );
								Character.addElement("RoleID").addText( rs.getString("ROLE_ID") );
								Character.addElement("BaseHealth").addText( rs.getString("CHAR_BASE_HP") );
								Character.addElement("BaseAttack").addText( rs.getString("CHAR_BASE_ATK") );
								Character.addElement("BaseDefense").addText( rs.getString("CHAR_BASE_DEF") );
								Character.addElement("BaseMana").addText( rs.getString("CHAR_BASE_MP") );
								Character.addElement("HealthGainPerLvl").addText( rs.getString("CHAR_HP_LVL") );
								Character.addElement("AttackGainPerLvl").addText( rs.getString("CHAR_ATK_LVL") );
								Character.addElement("DefenseGainPerLvl").addText( rs.getString("CHAR_DEF_LVL") );
								Character.addElement("ManaGainPerLvl").addText( rs.getString("CHAR_MP_LVL") );
							}
							String response = root.asXML();
							
							remoteOut.println(response);
							remoteOut.flush();
			
						} catch (SQLException ex) {
							ex.printStackTrace();
						}
					}
					else if ( action.equals("inquire") )
					{
						String searchTxt = getData(s, "//request/searchTxt");
						
						ResultSet rs = cDB.Inquire(searchTxt);
						
						try
						{
							Document xmlDocument = DocumentHelper.createDocument();
							
							Element root = xmlDocument.addElement("response");
							
							if (rs.next()) {  
									root.addElement("ErrorCode").addText("1");
								do {  
									Element Characters = root.addElement("Characters");
									Element Character = Characters.addElement("Character");
									
									Character.addElement("CHAR_ID").addText( rs.getString("CHAR_ID") );
									Character.addElement("CHAR_NAME").addText( rs.getString("CHAR_NAME") );
									Character.addElement("CHAR_TITLE").addText( rs.getString("CHAR_TITLE") );
								} while (rs.next());  
							} 
							else 
							{  
								root.addElement("ErrorCode").addText("0");
								root.addElement("ErrorMessage").addText("No records found!");
							}
							
							String response = root.asXML();
							
							remoteOut.println(response);
							remoteOut.flush();
			
						} catch (SQLException ex) {
							ex.printStackTrace();
						}
					}
					else if ( action.toLowerCase().equals("update") )
					{
						int char_id = Integer.parseInt(getData(s, "//request/CharID"));
						String char_name = getData(s, "//request/CharName");
						String char_title = getData(s, "//request/CharTitle");
						int role_id = Integer.parseInt(getData(s, "//request/RoleID"));
						int health = Integer.parseInt(getData(s, "//request/BaseHealth"));
						int attack = Integer.parseInt(getData(s, "//request/BaseAttack"));
						int defense = Integer.parseInt(getData(s, "//request/BaseDefense"));
						int mana = Integer.parseInt(getData(s, "//request/BaseMana"));
						int healthGain = Integer.parseInt(getData(s, "//request/HealthGainPerLvl"));
						int attackGain = Integer.parseInt(getData(s, "//request/AttackGainPerLvl"));
						int defenseGain = Integer.parseInt(getData(s, "//request/DefenseGainPerLvl"));
						int manaGain = Integer.parseInt(getData(s, "//request/ManaGainPerLvl"));
						
						int ID = cDB.Update( char_id, char_name, char_title, role_id, health, attack, defense, mana, healthGain, attackGain, defenseGain, manaGain);
						
						if( ID != 0)
						{
							Document xmlDocument = DocumentHelper.createDocument();
							
							Element root = xmlDocument.addElement("response");
							root.addElement("ErrorCode").addText("1");
							root.addElement("ErrorMessage").addText("Success!");
							root.addElement("CHAR_ID").addText( String.valueOf(ID) );
							
							String response = root.asXML();
							
							remoteOut.println(response);
							remoteOut.flush();
						}
						else
						{
							Document xmlDocument = DocumentHelper.createDocument();
							
							Element root = xmlDocument.addElement("response");
							root.addElement("ErrorCode").addText("0");
							root.addElement("ErrorMessage").addText("Could not add character to the database.");
							
							String response = root.asXML();
							
							remoteOut.println(response);
							remoteOut.flush();
						}
					}
					else if ( action.equals("delete") )
					{
						int id = Integer.parseInt(getData(s, "//request/ID"));
						int errorCode = cDB.Delete(id);
						
						if( errorCode != -1)
						{
							Document xmlDocument = DocumentHelper.createDocument();
							
							Element root = xmlDocument.addElement("response");
							root.addElement("ErrorCode").addText("1");
							root.addElement("ErrorMessage").addText("Success!");
							root.addElement("CHAR_ID").addText( String.valueOf(errorCode) );
							
							String response = root.asXML();
							
							remoteOut.println(response);
							remoteOut.flush();
						}
						else
						{
							Document xmlDocument = DocumentHelper.createDocument();
							
							Element root = xmlDocument.addElement("response");
							root.addElement("ErrorCode").addText("0");
							root.addElement("ErrorMessage").addText("Invalid Character ID");
							
							String response = root.asXML();
							
							remoteOut.println(response);
							remoteOut.flush();
						}
					}
					else if ( action.equals("purge") )
					{
						int id = Integer.parseInt(getData(s, "//request/ID"));
						int errorCode = cDB.Purge(id);
						
						if( errorCode != -1)
						{
							Document xmlDocument = DocumentHelper.createDocument();
							
							Element root = xmlDocument.addElement("response");
							root.addElement("ErrorCode").addText("1");
							root.addElement("ErrorMessage").addText("Success!");
							root.addElement("CHAR_ID").addText( String.valueOf(errorCode) );
							
							String response = root.asXML();
							
							remoteOut.println(response);
							remoteOut.flush();
						}
						else
						{
							Document xmlDocument = DocumentHelper.createDocument();
							
							Element root = xmlDocument.addElement("response");
							root.addElement("ErrorCode").addText("0");
							root.addElement("ErrorMessage").addText("Invalid Character ID");
							
							String response = root.asXML();
							
							remoteOut.println(response);
							remoteOut.flush();
						}
					}
					else if ( action.equals("undelete") )
					{
						int id = Integer.parseInt(getData(s, "//request/ID"));
						int errorCode = cDB.Undelete(id);
						
						if( errorCode != -1)
						{
							Document xmlDocument = DocumentHelper.createDocument();
							
							Element root = xmlDocument.addElement("response");
							root.addElement("ErrorCode").addText("1");
							root.addElement("ErrorMessage").addText("Success!");
							root.addElement("CHAR_ID").addText( String.valueOf(errorCode) );
							
							String response = root.asXML();
							
							remoteOut.println(response);
							remoteOut.flush();
						}
						else
						{
							Document xmlDocument = DocumentHelper.createDocument();
							
							Element root = xmlDocument.addElement("response");
							root.addElement("ErrorCode").addText("0");
							root.addElement("ErrorMessage").addText("Invalid Character ID");
							
							String response = root.asXML();
							
							remoteOut.println(response);
							remoteOut.flush();
						}
					}
					else if (action.equals("getRoleCodes"))
					{
						ResultSet rs = cDB.ListRoleCodes();
						
						try
						{
							Document xmlDocument = DocumentHelper.createDocument();
							
							Element root = xmlDocument.addElement("response");
							Element RoleCodes = root.addElement("RoleCodes");
							
							while( rs.next() )
							{
								Element RoleCode = RoleCodes.addElement("RoleCode");
								
								RoleCode.addElement("ROLE_ID").addText( rs.getString("ROLE_ID") );
								RoleCode.addElement("ROLE_NAME").addText( rs.getString("ROLE_NAME") );
							}
							String response = root.asXML();
							
							remoteOut.println(response);
							remoteOut.flush();
			
						} catch (SQLException ex) {
							ex.printStackTrace();
						}
					}
					else if (action.equals("getDeletedChars"))
					{
						ResultSet rs = cDB.ListDeletedChars();
						
						try
						{
							Document xmlDocument = DocumentHelper.createDocument();
							
							Element root = xmlDocument.addElement("response");
							Element DeletedChars = root.addElement("DeletedChars");
							
							while( rs.next() )
							{
								Element DeletedChar = DeletedChars.addElement("DeletedChar");
								
								DeletedChar.addElement("CHAR_ID").addText( rs.getString("CHAR_ID") );
								DeletedChar.addElement("CHAR_NAME").addText( rs.getString("CHAR_NAME") );
								DeletedChar.addElement("CHAR_TITLE").addText( rs.getString("CHAR_TITLE") );
								
							}
							String response = root.asXML();
							
							remoteOut.println(response);
							remoteOut.flush();
			
						} catch (SQLException ex) {
							ex.printStackTrace();
						}
					}
					else if ( action.toLowerCase().equals("disconnect") )
					{			
						isListening = false;
					}
					else
					{
						Document xmlDocument = DocumentHelper.createDocument();
						
						Element root = xmlDocument.addElement("response");
						root.addElement("ErrorCode").addText("0");
						root.addElement("ErrorMessage").addText("INVALID REQUEST");
						
						String response = root.asXML();
						
						remoteOut.println(response);
						remoteOut.flush();
					}
				}
				else 
				{
					isListening = false;
				}
			}
			catch (IOException e)
			{
				TCPServer.traceOut(e.toString());
				isListening = false;
			} 
			catch (NullPointerException e)
			{
				TCPServer.traceOut("Session #" + getId() + " closed without saying 'disconnect.'");
			}
		}

	}
	
	static String PrintCharacter( ResultSet rs )
	{
		String output = "No Record Found";
		try
		{
			if( rs.next() )
			{
				output = "----- Begin Character Record -----\n"
				+ "ID: " + rs.getInt("ID") + "\n"
				+ "Character Name: " + rs.getString("CHAR_NAME") + "\n"
				+ "Character Title: " + rs.getString("CHAR_TITLE") + "\n"
				+ "Base Health: " + rs.getString("CHAR_BASE_HP") + "\n"
				+ "Base Attack: " + rs.getFloat("CHAR_BASE_ATK") + "\n"
				+ "Base Defense: " + rs.getString("CHAR_BASE_DEF") + "\n"
				+ "Base Mana: " + rs.getString("CHAR_BASE_MP") + "\n"
				+ "Health Gain Per Level: " + rs.getString("CHAR_HP_LVL") + "\n"
				+ "Attack Gain Per Level: " + rs.getFloat("CHAR_ATK_LVL") + "\n"
				+ "Defense Gain Per Level: " + rs.getString("CHAR_DEF_LVL") + "\n"
				+ "Mana Gain Per Level: " + rs.getString("CHAR_MP_LVL") + "\n"
				+ "----- End Character Record -----";
			}			
		}
		catch( SQLException ex )
		{
			ex.printStackTrace();
		}
		
		return output;
	}
}
