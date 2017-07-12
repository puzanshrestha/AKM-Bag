<?php
require("dbconnect.php");


$bidd=$_POST['bid'];
$bid=(int)$bidd;
$bagName = $_POST['bag_name'];
$bagCategory = $_POST['bag_category'];
$bagPrice = $_POST['bag_price'];
$bagCompany = $_POST['bag_company'];
$source=$_POST['source'];
$ext=$_POST['ext'];
$vendor_id=$_POST['vendor_id'];

if($source=='update')
{
$update='b_'.$bid.".".$ext;
$sql_query= "UPDATE bagDetails SET bag_name = '$bagName',bag_category='$bagCategory',bag_company='$bagCompany',bag_price='$bagPrice',bag_photo='$update' WHERE bag_id = $bid";

if(sqlsrv_query($conn,$sql_query))
{
echo "Updated";
}


else
echo "Error in Updating Information";

}



else if($source=='delete'){

$sql_query="DELETE FROM bagDetails WHERE bag_id=$bid";


if(sqlsrv_query($conn,$sql_query))
echo "Deleted";


else
echo "Error has been occured while deleting a bag";


}


else if($source=='insert')
{

$sql_query = "INSERT INTO bagDetails (bag_name,bag_category,bag_price,bag_company,vendor_id) OUTPUT INSERTED.bag_id VALUES('$bagName','$bagCategory',$bagPrice,'$bagCompany',$vendor_id)";


$result=sqlsrv_query($conn,$sql_query);

if($row = sqlsrv_fetch_array($result))
{
$tempid=$row['bag_id'];
$id="b_".$tempid."."."$ext";
$sql_query = "UPDATE bagDetails SET bag_photo='$id' where bag_id=$tempid";
sqlsrv_query($conn,$sql_query);
echo "Inserted"."id=$tempid";
}

else
	echo "Error Cannot Insert Bag";

}


?>