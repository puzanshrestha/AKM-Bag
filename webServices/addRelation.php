<?php
require("dbconnect.php");

$bag_name=$_POST['bag_name'];
$vendor_idd = $_POST['vendor_id'];
$vendor_id=(int)$vendor_idd;


$sql_query = "SELECT bag_id FROM bagDetails where bag_name='$bag_name'";
$res=sqlsrv_query($conn,$sql_query);
if($r=sqlsrv_fetch_array($res))
{
$bag_id=$r[0];

$sql_query= "INSERT INTO relation (bag_id,vendor_id) VALUES('$bag_id','$vendor_id')";

if(sqlsrv_query($conn,$sql_query))
{
echo "Updated";

}
else
echo "ERROR";


}







?>