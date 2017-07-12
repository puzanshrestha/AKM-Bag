<?php

require "dbconnect.php";


$stockEditJson = $_POST['stockEditJson'];
$bag_id=$_POST['bag_id'];


$test =array();
$test= json_decode($stockEditJson,true);
$flag=1;

foreach($test as $k)
{


	$sql_query="IF EXISTS(SELECT * FROM bagStock WHERE bag_id=$bag_id AND bag_color='".$k['color']."')  
			UPDATE bagStock SET quantity_color=".$k['cquantity'] ." WHERE bag_id=$bag_id AND bag_color='".$k['color']."'".
			"ELSE
			INSERT INTO bagStock(bag_id,quantity_color,bag_color) VALUES ($bag_id,".$k['cquantity'].",'".$k['color']."')";
		
	if(!sqlsrv_query($conn,$sql_query))
		$flag=0;
$qtyTotal+=$k['cquantity'];	
	
}

if($flag==1)
echo "Succeeded";
else
echo "Failed";
	

?>