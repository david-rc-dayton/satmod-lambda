# Supporting Mathematics

## Haversine Formula

A critical formula in determining earth coverage, the Haversine Formula provides
the accurate great-circle distance between two points on a sphere given their 
latitude and longitude:

$$
d=2r\sin^{-1}\left(
\sqrt{\sin^2
\left(\frac{\phi_2-\phi_1}{2}\right)
+\cos(\phi_1)\cos(\phi_2)\sin^2
\left(\frac{\lambda_2-\lambda_1}{2}\right)
}\right)
$$

where:

* $d$ is the distance between the two points along a great circle of the sphere
* $r$ is the radius of the sphere
* $\phi_1,\phi_2$ are the latitude of point 1 and latitude of point 2
* $\lambda_1,\lambda_2$ are the longitude of point 1 and longitude of point 2

By omitting $r$ in the above equation, the angular distance can be
returned in radians, and can be converted to degrees via multiplying
by $\frac{180}{\pi}$; this can be visualized easily on an Equidistant
Rectangular Map projection. By treating the map as a matrix of equally
spaced points (in degrees), coverage maps can be created as a sparse
matrix of points-in-view which are then applied to the world map.

## Law of Cosines

The Law of Cosines can be used to get a reasonable approximation of the great
circle distance between two points, given their latitude and longitude.
Computationally, this is faster than the Haversine Formula.

$$
d=\cos^{-1}\left(
\sin(\phi_1)\sin(\phi_2)+\cos(\phi_1)\cos(\phi_2)\cos(\lambda_2-\lambda_1)
\right)\cdot{r}
$$

where:

* $d$ is the distance between the two points along a great circle of the sphere
* $r$ is the radius of the sphere
* $\phi_1,\phi_2$ are the latitude of point 1 and latitude of point 2
* $\lambda_1,\lambda_2$ are the longitude of point 1 and longitude of point 2

Again, by omitting $r$ in the above equation, the angular distance can be
returned in radians, and can be converted to degrees via multiplying by 
$\frac{180}{\pi}$ for representation on an Equidistant Rectangular Map 
projection.

## Equirectangular Approximation

Computationally faster than both the Haversine Formula and the Law of Cosines,
and significantly less accurate over large distances, the Equirectangular
Approximation can be used to determine the distance between two points with
relatively small error for Low Earth Orbiting (LEO) satellites. Error is
proportional to the difference in latitude of the measured points; higher 
altitudes result in a larger footprint, outputting very noticeable errors as the
view-horizon approaches the Earth's poles.

$$
d=r\cdot\sqrt{
\left(\Delta\lambda\cdot\cos(\phi_m)\right)^2
+\left(\Delta\phi\right)^2}
$$

where:

* $d$ is the distance between the two points along a great circle of the sphere
* $r$ is the radius of the sphere
* $\phi_m$ is the mean value of the latitude of point 1 and latitude of point 2
* $\Delta\phi$ is the difference between the latitude of point 1 and latitude of point 2
* $\Delta\lambda$ is the difference between the longitude of point 1 and longitude of point 2

Similar to the previous examples, by removing $r$ from the calculation and
multiplying $d$ by $\frac{180}{\pi}$, the distance can be output in degrees
for use with an Equidistant Rectangular map projection. Care must be taken to
handle coordinate wrapping around the 180th meridian, as the formula relies on 
Pythagoras' Theorem. This can be accomplished with a bit of logic:

$$
f(\lambda_1,\lambda_2) = \left\{ \begin{array}{rl}
\lambda_2 - 360 &\mbox{ if $\Delta\lambda>180$} \\
\lambda_2 + 360 &\mbox{ if $\Delta\lambda<-180$} \\
\lambda_2 &\mbox{ otherwise}
\end{array} \right.
$$

By replacing the destination point's longitude in the Equirectangular
Approximation with the output from the above function, the shortest radial
distance will be used to  effectively wrap the coordinate path around the 180th
meridian.

## Distance to Horizon

The Distance to Horizon Formula is useful in determining which points on
the Earth are within a satellite's Field-of-View:

$$ s=R\cos^{-1}\left(\frac{R}{R+h}\right) $$

where:

* $s$ is the distance along the curved surface of the Earth to the horizon
* $R$ is the mean radius of the Earth (6371.0 km)
* $h$ is the satellite's altitude above sea level

Similar to the Haversine Formula, the first $R$ (outside the $\cos^{-1}$
function) can be omitted to get the angular distance in radians.
Multiplying the radian output by $\frac{180}{\pi}$ results in the
angular distance in degrees. Again, this provides a useful measure of
distance on the easily displayed Equidistant Rectangular Map projection.

\pagebreak

