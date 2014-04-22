import java.sql.*;
import java.io.File;

public class CharacterDB
{
	private static String strConn = "jdbc:odbc:DRIVER={Microsoft Access Driver (*.mdb, *.accdb)};DBQ=";
	private static final String DB_NAME = "Game_Master.accdb";
	
	private String path;
	
	public CharacterDB()
	{
		path = System.getProperty("user.dir") + File.separatorChar + "data" + File.separatorChar + DB_NAME;		
		strConn += path;
	}
	
	public int Add( String CHAR_NAME, String CHAR_TITLE, int ROLE_ID, int CHAR_BASE_HP, int CHAR_BASE_ATK, int CHAR_BASE_DEF,
					int CHAR_BASE_MP, int CHAR_HP_LVL, int CHAR_ATK_LVL, int CHAR_DEF_LVL, int CHAR_MP_LVL)
	{
		int newID = 0;
		
		try
		{
			// Causes the class named 'sun.jdbc.odbc.JdbcOdbcDriver' to be initialized
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			
			// Obtain a connection to the database
			Connection cn = DriverManager.getConnection(strConn);

			// Create the INSERT SQL statement 
			String strSQL = "INSERT INTO Character ";
			strSQL += "(";
			strSQL += " CHAR_NAME,";
			strSQL += " CHAR_TITLE,";
			strSQL += " ROLE_ID,";
			strSQL += " CHAR_BASE_HP,";
			strSQL += " CHAR_BASE_ATK,";
			strSQL += " CHAR_BASE_DEF,";
			strSQL += " CHAR_BASE_MP,";
			strSQL += " CHAR_HP_LVL,";
			strSQL += " CHAR_ATK_LVL,";
			strSQL += " CHAR_DEF_LVL,";
			strSQL += " CHAR_MP_LVL,";
			strSQL += " CHAR_ACTIVE";
			strSQL += ") ";
			strSQL += "VALUES ";
			strSQL += "(?, ?, ?, ?, ? , ?, ?, ?, ?, ?, ?, 'A')";
			
			// Create a prepared statement based upon the SQL
			PreparedStatement ps = cn.prepareStatement(strSQL);
			
			// Set the parameters for the prepared statement
			ps.setString(1, CHAR_NAME);
			ps.setString(2, CHAR_TITLE);
			ps.setInt(3, ROLE_ID);
			ps.setInt(4, CHAR_BASE_HP);
			ps.setInt(5, CHAR_BASE_ATK);
			ps.setInt(6, CHAR_BASE_DEF);
			ps.setInt(7, CHAR_BASE_MP);
			ps.setInt(8, CHAR_HP_LVL);
			ps.setInt(9, CHAR_ATK_LVL);
			ps.setInt(10, CHAR_DEF_LVL);
			ps.setInt(11, CHAR_MP_LVL);
			
			// Send the SQL to the database engine for execution
			ps.execute();
			
			// Create a SQL statement to retrieve the AUTONUMBER field value as NEW_ID
			ps = cn.prepareStatement("SELECT @@IDENTITY AS NEW_ID");
			
			// Execute the SQL
			ResultSet rs = ps.executeQuery();
			
			// If we have a result 
			if( rs.next() )
				newID = rs.getInt("NEW_ID");

			// Close the prepared statement 
			ps.close();
			ps = null;
			
			// Close the connection 
			cn.close();
			cn = null;
		}
		catch( ClassNotFoundException ex )
		{
			ex.printStackTrace();
		}
		catch( SQLException ex )
		{
			ex.printStackTrace();
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
		// return the new id value or 0 if error
		return newID;
	}
	
	public ResultSet getCharacter(int CHAR_ID)
	{
		ResultSet rs = null;

		try
		{
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			
			Connection cn = DriverManager.getConnection(strConn);
			
			// Ask for all fields for the specific student record
			String strSQL = "SELECT * FROM Character WHERE CHAR_ID = ? ";
			
			PreparedStatement ps = cn.prepareStatement(strSQL);
		
			ps.setInt(1, CHAR_ID);
			
			// Use executeQuery when results are expected
			rs = ps.executeQuery();
			
			// Do not close the prepared statement or the connection if returning a ResultSet from a method
		}
		catch( ClassNotFoundException ex )
		{
			ex.printStackTrace();
		}
		catch( SQLException ex )
		{
			ex.printStackTrace();
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
			
		return rs;
	}
	
	public ResultSet Inquire( String searchTxt )
	{
		ResultSet rs = null;

		try
		{
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			
			Connection cn = DriverManager.getConnection(strConn);
			
			// Ask for all fields for the specific student record
			String strSQL = "SELECT CHAR_ID, CHAR_NAME, CHAR_TITLE FROM Character ";
			strSQL += "WHERE CHAR_NAME LIKE ? AND CHAR_ACTIVE = 'A'";
			
			PreparedStatement ps = cn.prepareStatement(strSQL);
			
			ps.setString(1, "%" + searchTxt + "%");
		
			// Use executeQuery when results are expected
			rs = ps.executeQuery();
			
			// Do not close the prepared statement or the connection if returning a ResultSet from a method
		}
		catch( ClassNotFoundException ex )
		{
			ex.printStackTrace();
		}
		catch( SQLException ex )
		{
			ex.printStackTrace();
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
			
		return rs;
	}
	
	public ResultSet ListRoleCodes()
	{
		ResultSet rs = null;

		try
		{
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			
			Connection cn = DriverManager.getConnection(strConn);
			
			// Ask for all fields for the specific student record
			String strSQL = "SELECT ROLE_ID, ROLE_NAME, ROLE_DESC FROM Role ORDER BY ROLE_ID ";
			
			PreparedStatement ps = cn.prepareStatement(strSQL);
		
			// Use executeQuery when results are expected
			rs = ps.executeQuery();
			
			// Do not close the prepared statement or the connection if returning a ResultSet from a method
		}
		catch( ClassNotFoundException ex )
		{
			ex.printStackTrace();
		}
		catch( SQLException ex )
		{
			ex.printStackTrace();
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
			
		return rs;
	}
	
	public ResultSet ListDeletedChars()
	{
		ResultSet rs = null;

		try
		{
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			
			Connection cn = DriverManager.getConnection(strConn);
			
			// Ask for all fields for the specific student record
			String strSQL = "SELECT CHAR_ID, CHAR_NAME, CHAR_TITLE FROM Character WHERE CHAR_ACTIVE = 'I' ORDER BY CHAR_ID ";
			
			PreparedStatement ps = cn.prepareStatement(strSQL);
		
			// Use executeQuery when results are expected
			rs = ps.executeQuery();
			
			// Do not close the prepared statement or the connection if returning a ResultSet from a method
		}
		catch( ClassNotFoundException ex )
		{
			ex.printStackTrace();
		}
		catch( SQLException ex )
		{
			ex.printStackTrace();
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
			
		return rs;
	}
	
	public int Update( int ID, String CHAR_NAME, String CHAR_TITLE, int ROLE_ID, int CHAR_BASE_HP, int CHAR_BASE_ATK, int CHAR_BASE_DEF,
			int CHAR_BASE_MP, int CHAR_HP_LVL, int CHAR_ATK_LVL, int CHAR_DEF_LVL, int CHAR_MP_LVL)
	{
		int rowcount = -1;
		
		try
		{
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			
			Connection cn = DriverManager.getConnection(strConn);

			// Create the UPDATE SQL statement 
			String strSQL = "UPDATE Character";
			strSQL += " SET CHAR_NAME = ?,";
			strSQL += " CHAR_TITLE = ?,";
			strSQL += " ROLE_ID = ?,";
			strSQL += " CHAR_BASE_HP = ?,";
			strSQL += " CHAR_BASE_ATK = ?,";
			strSQL += " CHAR_BASE_DEF = ?,";
			strSQL += " CHAR_BASE_MP = ?,";
			strSQL += " CHAR_HP_LVL = ?,";
			strSQL += " CHAR_ATK_LVL = ?,";
			strSQL += " CHAR_DEF_LVL = ?,";
			strSQL += " CHAR_MP_LVL = ?";
			strSQL += " WHERE CHAR_ID = ?";
			
			// Create a prepared statement based upon the SQL
			PreparedStatement ps = cn.prepareStatement(strSQL);
			
			// Set the parameters for the prepared statement
			ps.setString(1, CHAR_NAME);
			ps.setString(2, CHAR_TITLE);
			ps.setInt(3, ROLE_ID);
			ps.setInt(4, CHAR_BASE_HP);
			ps.setInt(5, CHAR_BASE_ATK);
			ps.setInt(6, CHAR_BASE_DEF);
			ps.setInt(7, CHAR_BASE_MP);
			ps.setInt(8, CHAR_HP_LVL);
			ps.setInt(9, CHAR_ATK_LVL);
			ps.setInt(10, CHAR_DEF_LVL);
			ps.setInt(11, CHAR_MP_LVL);
			ps.setInt(12, ID);
		
			rowcount = ps.executeUpdate();
			
			ps.close();
			ps = null;
			
			cn.close();
			cn = null;
		}
		catch( ClassNotFoundException ex )
		{
			ex.printStackTrace();
		}
		catch( SQLException ex )
		{
			ex.printStackTrace();
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
		
		return rowcount;
	}

	public int Delete( int ID )
	{
		int rowcount = -1;
		
		try
		{
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			
			Connection cn = DriverManager.getConnection(strConn);

			// Create the UPDATE SQL statement 
			String strSQL = "UPDATE Character";
			strSQL += " SET CHAR_ACTIVE = 'I'";
			strSQL += " WHERE CHAR_ID = ?";
			
			// Create a prepared statement based upon the SQL
			PreparedStatement ps = cn.prepareStatement(strSQL);
			
			// Set the parameters for the prepared statement
			ps.setInt(1, ID);
		
			rowcount = ps.executeUpdate();
			
			ps.close();
			ps = null;
			
			cn.close();
			cn = null;
		}
		catch( ClassNotFoundException ex )
		{
			ex.printStackTrace();
		}
		catch( SQLException ex )
		{
			ex.printStackTrace();
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
		
		return rowcount;
	}
	
	public int Undelete( int ID )
	{
		int rowcount = -1;
		
		try
		{
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			
			Connection cn = DriverManager.getConnection(strConn);

			// Create the UPDATE SQL statement 
			String strSQL = "UPDATE Character";
			strSQL += " SET CHAR_ACTIVE = 'A'";
			strSQL += " WHERE CHAR_ID = ?";
			
			// Create a prepared statement based upon the SQL
			PreparedStatement ps = cn.prepareStatement(strSQL);
			
			// Set the parameters for the prepared statement
			ps.setInt(1, ID);
		
			rowcount = ps.executeUpdate();
			
			ps.close();
			ps = null;
			
			cn.close();
			cn = null;
		}
		catch( ClassNotFoundException ex )
		{
			ex.printStackTrace();
		}
		catch( SQLException ex )
		{
			ex.printStackTrace();
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
		
		return rowcount;
	}
	
	public int Purge( int ID )
	{
		int rowcount = -1;
		
		try
		{
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			
			Connection cn = DriverManager.getConnection(strConn);

			// Create the UPDATE SQL statement 
			String strSQL = "DELETE FROM Character";
			strSQL += " WHERE CHAR_ID = ? AND CHAR_ACTIVE = 'I'" ;
			
			// Create a prepared statement based upon the SQL
			PreparedStatement ps = cn.prepareStatement(strSQL);
			
			// Set the parameters for the prepared statement
			ps.setInt(1, ID);
		
			rowcount = ps.executeUpdate();
			
			ps.close();
			ps = null;
			
			cn.close();
			cn = null;
		}
		catch( ClassNotFoundException ex )
		{
			ex.printStackTrace();
		}
		catch( SQLException ex )
		{
			ex.printStackTrace();
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
		
		return rowcount;
	}
}
