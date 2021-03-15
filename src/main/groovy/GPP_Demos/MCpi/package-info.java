/**
 * The demos in this package use some of the Farm type patterns in the package {@link groovy_parallel_patterns.patterns} to implement a
 * solution to the calculation of pi using a MonteCarlo method.
 * <p>
 * Consider a unit positive quadrant and use a random number generator to create points [x, y] such that both
 * x and y are in the range 0 to 1.<br>
 * Determine whether the random point is within the unit circle x*x + y*y <= 1 and keep a count of the points within this circle.<br>
 * The ratio of points within to total number of points created in pi/4<p>
 * The architecture generates a number of instances of a data object that contains a property that indicates the
 * number of iterations to undertaken for that object.  These objects are then sent to a Farm where the calculations are undertaken and
 * subsequently collected. <p>
 * Various versions have been created, first, where each data object is passed to the Collector process with the counts updated
 * within the object and secondly where a local worker class is used in each of the Workers in the Farm and they record the sums, which
 * are then passed once to the Collector process for final processing.<pP
 * A final version that uses a feedback loop to stop the emitting of new objects is also provided.
 * <p>
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
 * Copyright  Jon Kerridge Edinburgh Napier University
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
 *
 * @see groovy_parallel_patterns.patterns#FarmPattern.groovy
 */

package GPP_Demos.MCpi;
