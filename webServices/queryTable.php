<?php
require "dbconnect.php";

$bag_quantity=$_POST['quantity'];
$sql_query="SELECT bag_name,bag_quantity FROM bagDetails WHERE bag_quantity<=$bag_quantity";

if($result=sqlsrv_query($conn,$sql_query))
{

	$response=Array();


	while($row=sqlsrv_fetch_array($result))
	{

		array_push($response,array("bag_name"=>$row[0],"bag_quantity"=>$row[1]));
	
	}

echo json_encode(array("response"=>$response));
}

else
echo "failed";




?>