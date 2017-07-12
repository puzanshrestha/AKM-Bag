<?php

require "dbconnect.php";
$dateTo= $_POST['dateTo'];
$dateFrom=$_POST['dateFrom'];
$shop_number =$_POST['shop_number'];

$response = array();


$sql_query = "SELECT order_id,date,bag_name,customer_name,quantity,bag_color,bag_price,discount FROM orderDetails,orders where order_id=orderId and date>='$dateFrom' and date<='$dateTo' AND shop_number='$shop_number' ORDER BY date,order_id,bag_name";

$result = sqlsrv_query($conn,$sql_query);
while($row = sqlsrv_fetch_array($result))
{

	$date= date_format($row['date'], 'Y-m-d');

	array_push($response,array("order_id"=>$row['order_id'],"date"=>$date,"bag_name"=>$row['bag_name'],"customer_name"=>$row['customer_name'],"quantity"=>$row['quantity'],"bag_color"=>$row['bag_color'],"bag_price"=>$row['bag_price'],"discount"=>$row['discount']));
}



echo json_encode(array("result"=>$response));




?>