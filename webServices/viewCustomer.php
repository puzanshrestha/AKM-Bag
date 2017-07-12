<?php

require "dbconnect.php";

$query = "SELECT * FROM customerDetails";

$result = sqlsrv_query($conn,$query);

$response = array();

while($row = sqlsrv_fetch_array($result))
{
	array_push($response,array("customer_id"=>$row['customer_id'],"customer_name"=>$row['customer_name'],"customer_address"=>$row['customer_address'],"customer_phone"=>$row['customer_phone']));
}

echo json_encode(array("result"=>$response));

?>