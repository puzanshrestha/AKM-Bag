<?php

require ("dbconnect.php");



$bag_id_code = $_POST['bag_ids'];
$customer_id =$_POST['customer_id'];
$sql_query = "SELECT customer_name FROM customerDetails where customer_id=$customer_id";
$res=sqlsrv_query($conn,$sql_query);
if($r=sqlsrv_fetch_array($res))
{
$customer_name=$r[0];

}




$bag_ids = Array();


$bag_name=Array();
$bag_price=Array();
	
$bag_ids=explode('#',$bag_id_code);

$jsonValue=Array();

for($i=0;$i<sizeof($bag_ids)-1;$i++)
{

if(!($bag_ids[$i]==0))
{
	$sql_query = "SELECT bagDetails.bag_name,bagDetails.bag_price from bagDetails where bag_id=$bag_ids[$i]";
	
	if($result=sqlsrv_query($conn,$sql_query))
	{
			
		while($row=sqlsrv_fetch_array($result))
		{
			array_push($jsonValue,array("bag_name"=>$row[0],"bag_price"=>$row[1]));
			
		}

	}
	
	else
	{
		echo "failed";
		break;
	}


}

else
{
			array_push($jsonValue,array("bag_name"=>"0","bag_price"=>"0"));
}

}




echo json_encode(array("result"=>$jsonValue,"customer_name"=>$customer_name));


?>