<?php

require "dbconnect.php";


$pId = $_POST['pId'];


$sql_query = "SELECT bag_id,bag_name,bag_color,quantity,bag_price FROM pendingBill,pendingBillDetails WHERE p_details_id=pId and pId=$pId";
$result = sqlsrv_query($conn,$sql_query);


$response = array();

while($row = sqlsrv_fetch_array($result)){

array_push($response,array("bag_id"=>$row['bag_id'],"bag_name"=>$row['bag_name'],"bag_price"=>$row['bag_price'],"color"=>$row['bag_color'],"quantityColor"=>$row['quantity']));
}

echo json_encode(array("result"=>$response));
?>