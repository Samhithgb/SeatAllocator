<?php

     include 'config.inc.php';
	 
	 // Check whether username or password is set from android	
     if(isset($_POST['username']))
     {
		  // Innitialize Variable
		  $result='';
	   	  $username = $_POST['username'];
         
		  // Query database for row exist or not
          $sql = 'SELECT * FROM alloted WHERE  username = :username';
		 $result= mysql_query($sql);
		 $response=array();
		 while($row=mysql_fetch_array($result)){
			array_push($response, array(
			'usn'=>$row[1],
			'subject'=>$row[5],
			'testdept'=>$row[7],
			'room_no'=>$row[8],
			'seat_no'=>$row[9],
			'time'=>$row[11],
			'date'=>$row[12]
			));			
		}
          echo json_encode(array("response"=>$response));
	}
	else echo 'No usn received';
	
?>
