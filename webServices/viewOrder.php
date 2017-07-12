<?php

require "dbconnect.php";

$order_id =1;// $_POST['order_id'];
$customer_id=1;
$bag_id=1;

$query = "SELECT bag_name, customer_name,bag_price, quantity FROM orderDetails,bagDetails,customerDetails WHERE orderDetails.order_id=1 AND bagDetails.bag_id=1 AND customerDetails.customer_id=1";

$result = sqlsrv_query($conn,$query);

$response = array();

while($row = sqlsrv_fetch_array($result))
{

	array_push($response,array("bag_name"=>$row['bag_name'],"customer_name"=>$row['customer_name'],"bag_price"=>$row['bag_price'],"bag_quantity"=>$row['quantity']));
}

print_r($row);

echo json_encode(array("result"=>$response));

?>