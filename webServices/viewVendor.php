<?php

require "dbconnect.php";

$query = "SELECT * FROM vendorDetails";

$result = sqlsrv_query($conn,$query);

$response = array();

while($row = sqlsrv_fetch_array($result))
{
	array_push($response,array("vendor_id"=>$row['vendor_id'],"vendor_name"=>$row['vendor_name'],"vendor_address"=>$row['vendor_address']));
}

echo json_encode(array("result"=>$response));

?>