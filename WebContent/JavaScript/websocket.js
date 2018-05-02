		var webSocket = new WebSocket("ws:http://127.0.0.1:8080/Beelzebub/cmdclient.html");
		webSocket.onopen = function(message){ wsOpen(message);};
		webSocket.onmessage = function(message){ wsGetMessage(message);};
		webSocket.onclose = function(message){ wsClose(message);};
		webSocket.onerror = function(message){ wsError(message);};
     
		//var graphNumber;
		var attack;
		var incomingIP;
		
        			function wsOpen(message){
        				//echoText.value += "Connected ... \n";
        				echoText.value += "Command here";
        			}
        			function wsSendMessage(){
        				webSocket.send(message.value);
        				echoText.value += message.value;
        				//graphNumber += message;
        				message.value = "";
        			}
        			function wsCloseConnection(){
        				webSocket.close();
        			}
        			function wsGetMessage(message){
        				echoText.value += message.data;
        				//graphNumber = message.data;
        				//runGraph();
        				
        			}
        			function wsClose(message){
        				echoText.value += "Disconnect ... \n";
        			}

        			function wserror(message){
        				echoText.value += "Error ... \n";
        			}
        		
