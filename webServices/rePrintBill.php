
<?php

require "dbconnect.php";


$orderId = $_POST['order_id'];


$sql_query = "SELECT customer_name,date,discount,bag_name,bag_price,bag_color,quantity FROM orders,orderDetails WHERE order_id=orderId and order_id=$orderId ORDER BY bag_name";
$result = sqlsrv_query($conn,$sql_query);


$response = array();

while($row = sqlsrv_fetch_array($result)){

	$date= date_format($row['date'], 'Y-m-d');
array_push($response,array("customer_name"=>$row['customer_name'],"discount"=>$row['discount'],"bag_name"=>$row['bag_name'],"bag_price"=>$row['bag_price'],"bag_color"=>$row['bag_color'],"quantity"=>$row['quantity'],"date"=>$date));
}

echo json_encode(array("result"=>$response));
?>