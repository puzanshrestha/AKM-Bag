<?php


require ("dbconnect.php");


$jsonOrder =$_POST['AddOrderJson'];
$customer_id=$_POST['customer_id'];
$customer_name=$_POST['customer_name'];
$discount=$_POST['discount'];

$source=$_POST['source'];
$shop_number=$_POST['shop_number'];

$test =array();
$test= json_decode($jsonOrder,true);

$status=1;

if($source=="Pending")
{


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

}

$orderId=-1;
$date = date("Y-m-d");
$sql_query="INSERT INTO orders(customer_id,customer_name,discount,date,shop_number) OUTPUT INSERTED.order_id VALUES($customer_id,'$customer_name',$discount,'$date','$shop_number')";
$result=sqlsrv_query($conn,$sql_query);
if($row=sqlsrv_fetch_array($result))
{	
	$orderId=$row['order_id'];

foreach($test as $k)
{

	$bag_id=$k['bag_id'];
	$bag_name=$k['product'];
	$bag_price=$k['price'];
	$quantitycolor=$k['colorQuantity'];
	foreach($quantitycolor as $k=>$val)
	{
		$sql_query="INSERT INTO orderDetails
           ([orderId]
	   ,[bag_id]
	   ,[bag_name]
           ,[bag_color]
           ,[bag_price]
           ,[quantity])
		VALUES($orderId,$bag_id,'$bag_name','$k',$bag_price,$val)";

		if(sqlsrv_query($conn,$sql_query))
		{
				

				$sql_query="SELECT quantity_color FROM bagStock where bag_id= $bag_id and bag_color='$k'";
				$res=sqlsrv_query($conn,$sql_query);
				$quantity_color=0;
				if($r=sqlsrv_fetch_array($res))
					{
						$quantity_color=$r[0];

					}

				$quantity=$quantity_color-$val;
				$sql_query= "UPDATE bagStock SET quantity_color =$quantity WHERE bag_id = $bag_id and bag_color='$k'";



				if(sqlsrv_query($conn,$sql_query))
					{
/*
					session_start();
					$_SESSION['bag_id']=$bag_id;
					include "queryQuantity.php";
					//echo "updated";

*/

					}



	
		}
		else
		{
			$status=0;
		//echo "failed";
		}


	}

}
}
else
$status=0;

if (status==1)
echo "failed";
else
echo "updated";






?>