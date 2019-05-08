/**
 * Mandelbrot - a solution to creating the Mandelbrot set.<p>
 * Involves complex numbers z = x + iy such that z*z + c<br>
 * does not diverge when iterated from z = 0<br>
 * where c = Xp + iYp, where Xp and Yp are the scaled values for of x and y for the pixel<br>
 * Implemented using an escape time algorithm<br>
 * Iterate Z*Z + c until it is greater than 4 or the number of iterations is greater than escape value<br>
 * The following algorithm is used <p>
 * <pre>
 * For each pixel (Px, Py) on the screen, do: {
 *  x0 = scaled x coordinate of pixel  	-2.5 <= x < = 1
 *  y0 = scaled y coordinate of pixel                -1 <= y < = 1
 *  x = 0.0
 *  y = 0.0
 *  iteration = 0
 *  max_iteration = 1000
 *  while (x*x + y*y < 2*2 AND iteration < max_iteration) {
 *     xtemp = x*x - y*y + x0
 *     y = 2*x*y + y0
 *     x = xtemp
 *     iteration = iteration + 1
 *   }
 *   color[Px, Py] = iteration < max_iteration ? white : black
 * }
 * </pre><p>
 * Approaches that can be taken<br>
 * Process per pixel<br>
 * Process per line<br>
 * Consider the space required<br>
 *   -2.5 <= x <= 1.0<br>
 *   -1.0 <= y <= 1.0<br>
 * Yields 200 by 350 pixels, 70000 processes<br>
 * 200 lines each of 350 pixels, 200 processes<br>
 * Processing time is determined by the number of iterations<br><p>
 * Visualisation<br>
 * GPP Library provides a simple process based Canvas environment to display such images<br>
 * It is itself a parallel of Manager and Interface processes but the user is not concerned with this detail<br>
 * Visualisation influences performance<br>
 * Two versions are provided, with and without visualisation. The versions are identical apart from the Collect process<br>
 *
 *<pre>
 * Author, Licence and Copyright statement
 * author  Jon Kerridge
 * 		   School of Computing
 * 		   Edinburgh Napier University
 * 		   Merchiston Campus,
 * 		   Colinton Road
 * 		   Edinburgh EH10 5DT
 *
 * Author contact: j.kerridge (at) napier.ac.uk
 *
 * Copyright  Jon Kerridge Edinburgh Napier University *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *</pre>
 *
*/

package GPP_Demos.mandelbrot.scripts;
