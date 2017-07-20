<?php

error_reporting(E_ERROR | E_PARSE);
	$server = "SAAJAN";
	
	$conInfo = array("Database"=>"Bags");
	$conn = sqlsrv_connect($server,$conInfo);

	//print_r (sqlsrv_errors());

	if($conn)
	{
		//echo "Succeeded";
	}
	

	else
	{
		echo "Failed";

	}




?>