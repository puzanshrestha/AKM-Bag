<?php
	
	$ipString = "192.168.1.";
	$count=0;

	{

		exec("ping -w 1 -n 1 ".$ipString."7",$output,$status);
	

		$data = $output[2];

		if(strpos($data,'TTL',1))
		echo "ok";
	
		else
		echo "No";
	
	
        }


?>