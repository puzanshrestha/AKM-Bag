<?php

require "dbconnect.php";

$pId=$_POST['pId'];

$flag=1;

$sql_query="DELETE FROM pendingBill WHERE pId=$pId";
if(sqlsrv_query($conn,$sql_query))
	{
	}
else
	$flag=0;

$sql_query="DELETE FROM pendingBillDetails WHERE p_details_id=$pId";
if(sqlsrv_query($conn,$sql_query))
	{

	}
	else
	$flag=0;



if($flag==1)
echo "Canceled Bill";

else
echo "Failed";
?>