<?php

require "dbconnect.php";

$offset=$_POST['offset'];
$difference=$_POST['difference'];

/* for Stock Information*/
$query = "SELECT bag_id FROM bagDetails ORDER BY bag_id
OFFSET  $offset ROWS 
FETCH NEXT $difference ROWS ONLY ";

$result = sqlsrv_query($conn,$query);


$idArray= array();

$i=0;
while($row = sqlsrv_fetch_array($result))
{
	$idArray[$i]=$row['bag_id'];
	$i++;


}

$bagColorQty = array();
for($i=0;$i<sizeof($idArray);$i++)
{
	
$sql_query = "SELECT * FROM bagStock WHERE bag_id=$idArray[$i]";
$result = sqlsrv_query($conn,$sql_query);




while($row = sqlsrv_fetch_array($result)){

array_push($bagColorQty,array("bag_id"=>$idArray[$i],"color"=>$row['bag_color'],"quantityColor"=>$row['quantity_color']));


}



}


/* for BagList Information*/

$query = "SELECT * FROM bagDetails ORDER BY bag_id
OFFSET  $offset ROWS 
FETCH NEXT $difference ROWS ONLY ";

$result = sqlsrv_query($conn,$query);

$response = array();

while($row = sqlsrv_fetch_array($result))
{
	array_push($response,array("bag_id"=>$row['bag_id'],"bag_name"=>$row['bag_name'],"bag_category"=>$row['bag_category'],"bag_price"=>$row['bag_price'],"bag_company"=>$row['bag_company'],"bag_photo"=>$row['bag_photo'],"vendor_id"=>$row['vendor_id']));
}

echo json_encode(array("result"=>$response,"stockData"=>$bagColorQty));

?>