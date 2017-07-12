<?php
require("dbconnect.php");

$customerName = $_POST['customer_name'];
$customerAddress = $_POST['customer_address'];
$customerPhone = $_POST['customer_phone'];
$customerSource=$_POST['customer_source'];
$customer_idd=$_POST['customer_id'];
$customer_id=(int)$customer_idd;

if($customerSource=='update')
{
$sql_query= "UPDATE customerDetails SET customer_name = '$customerName',customer_address='$customerAddress',customer_phone='$customerPhone' WHERE customer_id = $customer_id";
if(sqlsrv_query($conn,$sql_query))
echo "Update";
else
echo "error has been occured in update";
}
else if($customerSource=='delete'){
$sql_query="DELETE FROM customerDetails WHERE customer_id=$customer_id";
if(sqlsrv_query($conn,$sql_query))
echo "Delete";
else
echo "error in delete";
}
else
{
$sql_query = "INSERT INTO customerDetails(customer_name,customer_address,customer_phone) VALUES('$customerName','$customerAddress','$customerPhone')";
if(sqlsrv_query($conn,$sql_query))
echo "Inserted";
else
echo "error has been occured";
}


?>