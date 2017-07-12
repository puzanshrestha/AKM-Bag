<?php


	require("dbconnect.php");

	$vendorName = $_POST['vendor_name'];
	$vendorAddress = $_POST['vendor_address'];
	$source=$_POST['source'];
	$id=$_POST['id'];



	if($source=="insert")
	{
		$sql_query = "INSERT INTO vendorDetails(vendor_name,vendor_address) VALUES('$vendorName','$vendorAddress')";

		if(sqlsrv_query($conn,$sql_query))
		echo "Inserted";
	}	
	if($source=="update")
	{
		$sql_query = "UPDATE vendorDetails SET vendor_name='$vendorName',vendor_address='$vendorAddress' WHERE vendor_id=$id";
		if(sqlsrv_query($conn,$sql_query))
		echo "Updated";
	}

	if($source=="delete")
	{
		$sql_query = "DELETE FROM vendorDetails WHERE vendor_id=$id";
		if(sqlsrv_query($conn,$sql_query))		
		echo "Deleted";
	}
	


	




?>