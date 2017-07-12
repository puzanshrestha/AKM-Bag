<?php

require "dbconnect.php";


$bag_id = $_POST['bag_id'];
$color= $_POST['color'];
$quantity= $_POST['quantity'];



	$sql_query = "SELECT * FROM bagStock WHERE bag_id=$bag_id and bag_color='$color'";
	$result = sqlsrv_query($conn,$sql_query);


	$row_count = sqlsrv_has_rows( $result ); 
	if($row_count==false)
	{
		$sql_query="INSERT INTO bagStock(bag_id,bag_color,quantity_color) VALUES($bag_id,'$color',$quantity)";
		
	}

		
	else 
	{
		if($row = sqlsrv_fetch_array($result))
		$newQty=$quantity+$row['quantity_color'];
		$sql_query="UPDATE bagStock SET quantity_color=$newQty WHERE bag_id=$bag_id AND bag_color='$color'";
	}


if(sqlsrv_query($conn,$sql_query))
{

	session_start();
	$_SESSION['bag_id']=$bag_id;
	include "queryQuantity.php";


}
else
echo "failed";
		

?>