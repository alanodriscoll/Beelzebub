/**
 * Servlet handling
 * Call the servlet
 * 
 * Display html output
 */

function reloadBotnet() {
	alert("Relaoding Botnet");
}

function encryptBot(){
	alert("Encrypting this bot");
}

function loadBots(){
	$.get('database', function(data) {
        alert(data);
    });
}