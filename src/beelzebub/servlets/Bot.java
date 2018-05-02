package beelzebub.servlets;

import java.io.IOException;
import java.io.PrintWriter;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.jni.Time;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 * Servlet implementation class ServletRandomGenerator
 */
public class Bot {
	
	private String username ;
	private String IP ;
	private String machine ;
	private Timestamp time ;
	private String player ;
	private Boolean status ;
	
	long difference = -1;
	
	DatabaseManager dbm = new DatabaseManager();
	Connection dbConn = dbm.connect();
	
	public Bot() {
		this(null,null,null,null,null,null);
	}
	
	public Bot(String username, String IP, String machine, Timestamp time, String player, Boolean status) {
		this.username = username;
		this.IP = IP;
		this.machine = machine;
		this.time = time;
		this.player = player;
		this.status = status;
		this.difference = difference;
		
		getStatusUpdate();
		
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getIP() {
		return IP;
	}
	
	public void setIP(String IP) {
		this.IP = IP;
	}
	
	public String getMachine() {
		return machine;
	}
	
	public void setMachine(String machine) {
		this.machine = machine;
	}
	
	public Timestamp getTime() {
		return time;
	}
	
	public void setTime(Timestamp time) {
		this.time = time;
	}
	
	public String getPlayer() {
		return player;
	}
	
	public void setPlayer(String player) {
		this.player = player;
	}
	
	public Boolean getStatus() {
		return this.status;
	}
	
	public void getStatusUpdate() {
		Boolean active = false;
		
		Statement statementStatus = null;
		ResultSet rsStatus = null;
		Timestamp last_access = null;
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		
		String dbGetStatus = "Select last_access FROM botnet WHERE ip= '"+this.getIP()+"' ;";
		
		//Access database for last_access...
		try {
			statementStatus = dbConn.createStatement();
			statementStatus.execute(dbGetStatus);
			rsStatus = statementStatus.getResultSet();
			while (rsStatus.next()) {
				last_access = (rsStatus.getTimestamp("last_access"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
    		// Cleanup
			dbConn.close();
			statementStatus.close();
			rsStatus.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// Create two Timestamps for comparison
		Timestamp t1 = last_access;
		Timestamp t2 = new Timestamp(System.currentTimeMillis());
		difference = timeCompare(t2,t1);
		this.setDifference(difference);
		
		if(difference>2) {
			setStatus(false);
		}
		else if(difference<=2) {
			setStatus(true);
		}
	}
	
	public void setStatus(Boolean status) {
		this.status = status;
	}
	
	/*
	 * Compare the value of two timestamps and return the difference in minutes
	 */
	private long timeCompare(java.sql.Timestamp currentTime, java.sql.Timestamp oldTime){
		
	  long milliseconds1 = oldTime.getTime();
	  long milliseconds2 = currentTime.getTime();

	  long diff = milliseconds2 - milliseconds1;
	  long diffSeconds = diff / 1000;
	  long diffMinutes = diff / (60 * 1000);
	  long diffHours = diff / (60 * 60 * 1000);
	  long diffDays = diff / (24 * 60 * 60 * 1000);

	  this.setDifference(diffMinutes);
	  return diffMinutes;
	}
	
	
	public void setDifference(long difference) {
			this.difference = difference;
	}
	
	public long getDifference() {
		
		if (difference>0) {
			return this.difference;
		}
		else {
			return 0;
		}
	}
	
}
