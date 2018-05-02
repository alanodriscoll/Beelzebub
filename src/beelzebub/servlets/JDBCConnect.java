package beelzebub.servlets;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
	 
	public class JDBCConnect {
		
		// Private fields for DB Connection
		private static final String DB_DRIVER = "org.postgresql.Driver";
		private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
		private static final String DB_USER = "postgres";
		private static final String DB_PASSWORD = "";
	 
		public Connection connect() {
			Connection conn = null;
			
			try {
				//Classpath for driver
				Class.forName(DB_DRIVER);
				
			} catch (ClassNotFoundException e) {
				System.out.println(e.getMessage());
			}
			return conn;
		}
		
		
	    public static void main(String[] args) {
	        Connection conn = null;

	        try {
	            String dbURL = "jdbc:postgresql://localhost:5433/Beelzebub_DB";
	            String user = "postgres";
	            String pass = "siemens4";
	 
	            conn = DriverManager.getConnection(dbURL, user, pass);
	            if (conn != null) {
	                System.out.println("Connected to database");
	            }

	 } catch (SQLException ex) {
	            ex.printStackTrace();
	        } finally {

	            try {
	                if (conn != null && !conn.isClosed()) {
	                    conn.close();
	                }
	                
	            } catch (SQLException ex) {
	                ex.printStackTrace();
	            }
	        }
	    }
	}

