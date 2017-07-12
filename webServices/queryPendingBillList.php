<?php

require "dbconnect.php";

$shop_number=$_POST['shop_number'];

$sql_query="SELECT pId,date,customer_name,customer_id,address,total FROM pendingBill WHERE shop_number='$shop_number'";

$result=sqlsrv_query($conn,$sql_query);

$response = array();
while($row=sqlsrv_fetch_array($result))
{
$date= date_format($row['date'], 'Y-m-d');

	array_push($response,array("pId"=>$row['pId'],"date"=>$date,"customer_name"=>$row['customer_name'],"customer_id"=>$row['customer_id'],"address"=>$row['address'],"total"=>$row['total']));

	
}

echo json_encode(array("result"=>$response));



?>