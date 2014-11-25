(ns satmod-lambda.construct.common)

(def wgs84 
  "Parameters in the 1984 World Geodetic System (WGS84) defining the
   measurements of the Earth's reference ellipsoid. Available 
   parameters are: <br>
   `:semi-major-axis` - Earth's equatorial radius (in kilometers) <br>
   `:semi-minor-axis` - Earth's polar radius (in kilometers) <br>
   `:ecc-squared` - squared eccentricity of the reference ellipsoid"
  (let [a 6378137
        f (/ 1 298.257223563)]
    {:semi-major-axis a
     :semi-minor-axis (* a (- 1 f))
     :ecc-squared (- (* 2 f) (* f f))}))

(defn deg->rad 
  "Converts argument `deg` from degrees to radians."
  [deg]
  (* deg (/ Math/PI 180)))

(defn rad->deg
  "Converts argument `rad` from radians to degrees."
  [rad]
  (* rad (/ 180 Math/PI)))

(defn geo-radius
  "Calculate Earth's geocentric radius (in meters) for a map containing the key
   `{:latitude}` in degrees, respectively."
  [{:keys [latitude]}]
  (let [lat (deg->rad latitude)
        a (:semi-major-axis wgs84)
        b (:semi-minor-axis wgs84)]
    (-> (/ (+ (* (Math/pow (* a a (Math/cos lat)) 2))
              (* (Math/pow (* b b (Math/sin lat)) 2)))
           (+ (Math/pow (* a (Math/cos lat)) 2)
              (Math/pow (* b (Math/sin lat)) 2)))
      (Math/sqrt))))