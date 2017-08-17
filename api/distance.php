<?php
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
 $answer = haversineGreatCircleDistance($latitudeFrom = 28.6317868, $longitudeFrom = 77.11648209999998, $latitudeTo = 28.6305571, $longitudeTo = 77.11570649999999, $earthRadius = 6371);
 echo($answer);

 if ($answer<0.2) {
   echo "TRUE";
 } else {
   echo "FALSE";
 }
 
?>