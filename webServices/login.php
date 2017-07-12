<?php
	require("dbconnect.php");

	$username = $_POST['username'];
	$password = $_POST['password'];
	$shop_number=$_POST['shop_number'];


	$sql_query = "SELECT * FROM users WHERE username ='$username' AND password = '$password' AND shop_number='$shop_number'";  
	$result = sqlsrv_query($conn,$sql_query);
 
	$row_count = sqlsrv_has_rows($result ); 
	if($row_count==false)
		echo $sql_query;
	
	else 
	{
		if($row=sqlsrv_fetch_array($result))
			echo "userType-".$row['userType'];
	}



?>