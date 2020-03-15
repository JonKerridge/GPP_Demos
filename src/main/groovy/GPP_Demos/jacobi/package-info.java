/**
 * The Jacobi demo finds the solution to the simultaneous equations represented by Ax = b,
 * where A is the nxn  matrix of coefficients, x represents the n unkowns for which the value is required
 * and b is n values that result from the vector sum of Ax.<p>
 *
 * The Jacobi method is used due to its ease of parallelisation.  A Divide and Conquer approach is adopted.
 * Initially each of the values of x are given a value; in this case 0.<br>
 * A new value for each x is then determined by evaluating each equation.<br>
 * The new estimates are then plugged into equations and the process repeated until the difference between
 * x-value estimations is sufficiently small.<p>
 * Test files are provided, which are known to have a solution that converges for values of n 512, 1024, 2048 and 4096.<p>
 * Another test file is provided that contains 4 different data sets ranging in size from 128 to 1024. (Jacobi.txt)<p>
 *
 * The parallel architecture uses the Root and Node processes provided in the package jcsp.groovyParallelPatterns.divideAndConquer
 *
 * An Emit process reads in the matrix(ces) from a file and constructs an object that is sent to
 * the Root node.  The Root node then send copies of the base object to each of the Node processes.
 * The Root process then send the initial guess such that each Node gets a sub-range of the complete matric to calculate.
 * The Node process then returns its subrange of recalcuated x values back to Root, where they are reconmbined into a single matrix.
 * This is repeated until the estimates between subsequent iterations are sufficiently small.
 * The final result is then output to a Collect process where the answer is checked.
 * A OneDirectedList spreader process is used to direct the correct subrange to each Node process.
 * The Root process can output at each stage of the iteration so that a system that involves visualisation can
 * output the intermediate stages.<p>
 *
 * The system terminates in the same manner as other processes in the library.
 *
 * The Root process has the ability to provide logging information should that be needed.
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

package GPP_Demos.jacobi;
