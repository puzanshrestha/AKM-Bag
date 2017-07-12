<?php

require "dbconnect.php";


$bag_id = $_POST['bag_id'];


$sql_query = "SELECT * FROM bagStock WHERE bag_id=$bag_id";
$result = sqlsrv_query($conn,$sql_query);


$response = array();

while($row = sqlsrv_fetch_array($result)){

array_push($response,array("color"=>$row['bag_color'],"quantityColor"=>$row['quantity_color']));
}


$sql_query = "SELECT * FROM bagDetails WHERE bag_id=$bag_id";
$sqlresult = sqlsrv_query($conn,$sql_query);


$bagInfo= array();

while($row = sqlsrv_fetch_array($sqlresult)){

array_push($bagInfo,array("bag_id"=>$row['bag_id'],"bag_name"=>$row['bag_name'],"bag_category"=>$row['bag_category'],"bag_price"=>$row['bag_price'],"bag_company"=>$row['bag_company'],"bag_photo"=>$row['bag_photo'],"bag_quantity"=>$row['bag_quantity']));
}

echo json_encode(array("result"=>$response,"bagInfo"=>$bagInfo));



?>