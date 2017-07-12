<?php

require ("dbconnect.php");



$bag_id_code=$_POST['bag_id'];
$customer_id =$_POST['customer_id'];
$sql_query = "SELECT customer_name FROM customerDetails where customer_id=$customer_id";

$source=$_POST['source'];

$customer_name="manual";
if($source!="manual")
{
$res=sqlsrv_query($conn,$sql_query);
if($r=sqlsrv_fetch_array($res))
{
	$customer_name=$r[0];

}

}
$bag_ids=json_decode($bag_id_code);



$jsonValue=Array();

foreach ($bag_ids as $i)
{


	$sql_query = "SELECT bagDetails.bag_name,bagDetails.bag_price from bagDetails where bag_id=".$i;

	
	if($result=sqlsrv_query($conn,$sql_query))
	{
			
		while($row=sqlsrv_fetch_array($result))
		{
			array_push($jsonValue,array("bag_name"=>$row[0],"bag_price"=>$row[1]));
			
		}

	}
	
	else
	{
		echo "failed";
		break;
	}


}


echo json_encode(array("result"=>$jsonValue,"customer_name"=>$customer_name));



?>