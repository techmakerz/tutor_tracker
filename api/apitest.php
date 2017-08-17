<?php

   $servername = "localhost";
   $username = "root";
   $password = "";
   $dbname = "api";
   // $con=mysqli_connect("$servername","$username","$password","$dbname");

	// Create connection
	$conn = mysqli_connect($servername, $username, $password, $dbname);
	// Check connection
	if (!$conn) {
	    die("Connection failed: " . mysqli_connect_error());
	}

	$adhaar = $_GET['adhaar'];
	$name = $_GET['name'];
	$email = $_GET['email'];
	$u_latitude = $_GET['u_latitude'];
	$u_longitude = $_GET['u_longitude'];
	$timestamp = time();
	$sql = "INSERT INTO attendance (adhaar, name, email, timestamp, u_latitude, u_longitude)
	VALUES ('$adhaar', '$name', '$email', '$timestamp', '$u_latitude', '$u_longitude')";

	if (mysqli_query($conn, $sql)) {
	    echo "New record created successfully";
	} else {
	    echo "Error: " . $sql . "<br>" . mysqli_error($conn);
	}

	mysqli_close($conn);
?>