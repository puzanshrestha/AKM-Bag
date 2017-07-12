<?php

error_reporting(E_ERROR | E_PARSE);
	$server = "PCPCPC";
	
	$conInfo = array("Database"=>"Bags","UID"=>"pujan", "PWD"=>"pujan");
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