// Author: Christopher Davis
import java.io.*;
import java.net.*;
import java.util.*;

public class TCPServer
{
	private static final int PORT = 5000;
	private static ServerSocket server = null;
	private static boolean isListening = true;
	
	public static void traceOut( String message )
	{
		System.out.println( new Date().toString() + "\t" + message );
	}
	
	public static void main(String[] args)
	{
		TCPServer.traceOut( "Server Startup Initiated" );
		
		try
		{
			server = new ServerSocket( PORT );
			TCPServer.traceOut( "Server listening on port " + PORT );
			
			while( isListening )
			{
				TCPServer.traceOut( "Waiting for a connection.");
				Socket client = server.accept();
				
				TCPServer.traceOut( "Accepted connection from: " + client.getInetAddress().toString() );
				
				ServerSession session = new ServerSession( client );
				session.start();
			}
			
			server.close();
			server = null;
			TCPServer.traceOut( "Server shutdown complete." );
			
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
	}
}
