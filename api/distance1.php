<?php
	$center_lat = 28.6415494;
	$center_lng = 77.22081230000003;
	$lat = 28.6317868;
	$lng = 77.11648209999998;

	// test with your arccosine formula
	$distance =( 6371 * acos((cos(deg2rad($center_lat)) ) * (cos(deg2rad($lat))) * (cos(deg2rad($lng) - deg2rad($center_lng)) )+ ((sin(deg2rad($center_lat))) * (sin(deg2rad($lat))))) );
	print($distance); // prints 9.662174538188

	// test with my haversine formula
	$distance = haversineGreatCircleDistance($center_lat, $center_lng, $lat, $lng, 6371);
	print($distance); // prints 9.6621745381693
?>