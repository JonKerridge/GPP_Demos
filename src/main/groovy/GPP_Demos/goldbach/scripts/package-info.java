/**
 * Primes and the Goldbach Conjecture presents a solution to the problem of creating prime numbers
 * and then using those primes to validate the Goldbach conjecture.  <p>
 * Initially evaluate the primes up to N<br>
 * Then evaluate the Goldbach Conjecture using the primes previously calculated<br>
 * Goldbach's conjecture is one of the oldest and best-known unsolved problems in
 * number theory and in all of mathematics. It states: Every even integer greater
 * than 2 can be expressed as the sum of two primes.<p>
 * Let N be the integer up to which we test<br>
 * We require the primes up to maxP = sqroot(N)+1<br>
 * Use a sieve to find primes up to maxP<br>
 * Partition the range up to N to find all the primes in that range, in parallel<br>
 * Create a single list of primes which is broadcast in parallel to Goldbach workers<br>
 * Each worker is allocated an (overlapping) range of primes<br>
 * Each worker then finds all the sequential even numbers it can calculate from its range of primes<br>
 * Collect the results from each worker and ensure that ranges overlap, except for the last<br>
 * The maximum Goldbach number will be less than 2*N<br>
 *
 * Two versions of the source of each example are provided. The version ending 'Chans' has the network defined using
 * user defined channel definitions and each process declares its input and output channels.  The version
 * ending '_gpp' does not define the channels nor the channel declarations in the process definitions.  The processes
 * have to be defined in the order in which they occur in the dataflow through the application solution.  The processes are preceded
 * by the annotation //NETWORK and terminated by //END NETWORK.  The program {@link jcsp.GPP_Library.build.GPPbuild} can then be used
 * to create the channel definitions, channel declarations and the required parallel constructor required to invoke the process network.<p>
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
 */

package GPP_Demos.goldbach.scripts;
