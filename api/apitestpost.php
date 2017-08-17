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

	$adhaar = $_POST['adhaar'];
	$name = $_POST['name'];
	$email = $_POST['email'];
	$mobile = $_POST['mobile'];
	$u_latitude = $_POST['u_latitude'];
	$u_longitude = $_POST['u_longitude'];
	$s_latitude = $_POST['s_latitude'];
	$s_longitude = $_POST['s_longitude'];
	$timestamp = time();
	$sql = "INSERT INTO attendance (adhaar, name, email, mobile, timestamp, u_latitude, u_longitude, s_latitude, s_longitude)
	VALUES ('$adhaar', '$name', '$email', '$mobile', '$timestamp', '$u_latitude', '$u_longitude', '$s_latitude', '$s_longitude')";

	if (mysqli_query($conn, $sql)) {
	    echo "New record created successfully";
	} else {
	    echo "Error: " . $sql . "<br>" . mysqli_error($conn);
	}

	//retrieving school location
	$sql1 = "SELECT s_latitude, s_longitude FROM attendance WHERE email= '$email'";
	$result1 = mysqli_query($conn, $sql1);
	
	if (mysqli_num_rows($result1) > 0) {
	    // output data of each row
	    while($data = mysqli_fetch_assoc($result1)) {
	    	$latitudeFrom = $data["s_latitude"];
	    	$longitudeFrom = $data["s_longitude"];
	        echo "School Latitude " . $data["s_latitude"]. " - School longitude " . $data["s_longitude"]. "<br>";
	    }
	} else {
	    echo "0 results";
	}

	$latitudeTo = $u_latitude;
	$latitudeFrom = $u_longitude;
	//distance function
	function haversineGreatCircleDistance($latitudeFrom, $longitudeFrom, $latitudeTo, $longitudeTo, $earthRadius)
	  {
	    // convert from degrees to radians
	    $latFrom = deg2rad($latitudeFrom);
	    $lonFrom = deg2rad($longitudeFrom);
	    $latTo = deg2rad($latitudeTo);
	    $lonTo = deg2rad($longitudeTo);

	    $latDelta = $latTo - $latFrom;
	    $lonDelta = $lonTo - $lonFrom;

	    $angle = 2 * asin(sqrt(pow(sin($latDelta / 2), 2) +
	      cos($latFrom) * cos($latTo) * pow(sin($lonDelta / 2), 2)));
	    return $angle * $earthRadius;
	  }	
	$answer = haversineGreatCircleDistance($latitudeFrom, $longitudeFrom, $latitudeTo, $longitudeTo, $earthRadius = 6371);
	echo($answer);

	mysqli_close($conn);
?>