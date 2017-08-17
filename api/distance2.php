<?php
    function getDistance($latitude1, $longitude1, $latitude2, $longitude2) {
        
        $earth_radius = 6371;
     
        $dLat = deg2rad($latitude2 - $latitude1);
        $dLon = deg2rad($longitude2 - $longitude1);
     
        $a = sin($dLat/2) * sin($dLat/2) + cos(deg2rad($latitude1)) * cos(deg2rad($latitude2)) * sin($dLon/2) * sin($dLon/2);
        $c = 2 * asin(sqrt($a));
        $d = $earth_radius * $c;
     
        return $d;
        
    }
    echo(getDistance(28.6415494, 77.22081230000003, 28.6317868, 77.11648209999998));
?>