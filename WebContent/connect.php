<?php

function connect(){
	$fp = fsockopen("http://localhost:8080/ransombot.candc.net/html/connect.php", 8081, $errno, $errstr, 30);
	
	$command = "mkdir test";
	
	fwrite($fp,$command);
	
	fclose;
}

function test(){
	
}
?>