<?php

$servername = "sql6.freemysqlhosting.net:3306";
$username = "sql6137908";
$password = "mgGGVanpCB";
$dbname = "sql6137908";

try {
    	$conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);
    	$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    }
catch(PDOException $e)
    {
    	die("OOPs something went wrong");
    }

?>

