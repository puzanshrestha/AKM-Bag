<?php

session_start();

$bag_id=$_SESSION['bag_id'];

	$sql_query = "SELECT bag_color,quantity_color FROM bagStock WHERE bag_id=$bag_id";
	$result = sqlsrv_query($conn,$sql_query);


$qtyTotal=0;
while($row=sqlsrv_fetch_array($result))
{
	
$qtyTotal+=$row['quantity_color'];

}

$sql_query="UPDATE bagDetails SET bag_quantity=$qtyTotal WHERE bag_id=$bag_id";
sqlsrv_query($conn,$sql_query);


?>