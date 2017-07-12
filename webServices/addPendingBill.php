<?php


require ("dbconnect.php");


$jsonOrder =$_POST['AddOrderJson'];
$customer_id=$_POST['customer_id'];
$customer_name=$_POST['customer_name'];
$total=$_POST['total'];
$address=$_POST['address'];
$shop_number=$_POST['shop_number'];



$test =array();
$test= json_decode($jsonOrder,true);

$status=1;


if($customer_id!=0)
$sql_query="SELECT * FROM pendingBill WHERE customer_id=$customer_id";

else
$sql_query="SELECT * FROM pendingBill WHERE customer_name='$customer_name'";

$result=sqlsrv_query($conn,$sql_query);


if(sqlsrv_has_rows($result))
{
   if($customer_id!=0)
	$sql_query="DELETE FROM pendingBill OUTPUT DELETED.pId WHERE customer_id=$customer_id";
   else
	$sql_query="DELETE FROM pendingBill OUTPUT DELETED.pId WHERE customer_name='$customer_name'";

	$result=sqlsrv_query($conn,$sql_query);
	if($row=sqlsrv_fetch_array($result))
	{

		$sql_query="DELETE FROM pendingBillDetails WHERE p_details_id=".$row['pId'];
		sqlsrv_query($conn,$sql_query);	
	}

	
}


$date = date("Y-m-d");

$sql_query="INSERT INTO pendingBill(date,customer_name,customer_id,total,address,shop_number) OUTPUT INSERTED.pId VALUES('$date','$customer_name',$customer_id,$total,'$address','$shop_number')";

$result=sqlsrv_query($conn,$sql_query);
$ID=-1;

if($row = sqlsrv_fetch_array($result))
{
	$ID=$row['pId'];

}
if($ID!=-1)
{
foreach($test as $k)
{

	$bag_id=$k['bag_id'];
	$bag_name=$k['product'];
	$bag_price=$k['price'];
	$quantitycolor=$k['colorQuantity'];
	foreach($quantitycolor as $k=>$val)
	{


		$sql_query="INSERT INTO pendingBillDetails
           ([p_details_id]
           ,[bag_id]
	   ,[bag_name]
           ,[bag_color]
           ,[bag_price]
           ,[quantity])
		VALUES($ID,$bag_id,'$bag_name','$k',$bag_price,$val)";

		

		if(sqlsrv_query($conn,$sql_query))
		{
			
	
		}
		else
		{
			$status=0;
		}


	}

}


if($status==1)
echo "addedPendingBill";
else
echo "Error has been Occured";


}
else
echo "Insert Error has been Occured";


?>