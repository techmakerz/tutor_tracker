<?php
   $servername = "localhost";
   $username = "root";
   $password = "";
   $dbname = "api";
   $con=mysqli_connect("$servername","$username","$password","$dbname");

   if (mysqli_connect_errno($con)) {
      echo "Failed to connect to MySQL: " . mysqli_connect_error();
   }

   $username = $_GET['username'];
   $password = $_GET['password'];
   $name = $_GET['name'];
   $adhaar = $_GET['adhaar'];
   $timestamp = $_GET['timestamp'];
   $user_gps = $_GET['user_gps'];

   $result = mysqli_query($con,"SELECT Role FROM table1 where Username='$username' 
      and Password='$password'");
   $row = mysqli_fetch_array($result);
   $data = $row[0];

   if($data){
      echo $data;
   }
   mysqli_close($con);
?>