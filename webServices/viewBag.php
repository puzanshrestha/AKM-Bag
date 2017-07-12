<?php

require "dbconnect.php";

$query = "SELECT * FROM bagDetails";

$result = sqlsrv_query($conn,$query);

$response = array();

while($row = sqlsrv_fetch_array($result))
{
	array_push($response,array("bag_id"=>$row['bag_id'],"bag_name"=>$row['bag_name'],"bag_category"=>$row['bag_category'],"bag_price"=>$row['bag_price'],"bag_company"=>$row['bag_company'],"bag_photo"=>$row['bag_photo'],"vendor_id"=>$row['vendor_id']));
}

echo json_encode(array("result"=>$response));

?>