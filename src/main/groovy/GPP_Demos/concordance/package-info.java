/**
 * Concordance provides a typical problem scenario that justifies the use of a parallel solution,
 * both a sequential and parallel version are provided that use the same data classes.<p>
 *
 * A number of source files are provided and the resultant output files will be placed in the
 * same folder.<p>
 *
 * A concordance is a means of determining the places where the same string of words is
 * repeated in a text. <p>
 * Usually the concordance is constructed for sequences of words for length 1 up to some defined value N. <p>
 * Usually used for large texts. <p>
 * Output comprises the strings of words and where they were found in the text.<p>
 * Processing is carried out in a number of distinct phases.<p>
 * Phase 1<br>
 * Read file line-by-line and extract words removing extraneous punctuation<br>
 * Calculate an integer value based upon the letters that make up the word including hyphens and apostrophes.<br>
 * It is easier to compare using integers<br>
 * Just use the ASCII coding for the letter values.<br>
 * Save values in a list called word-List<br>
 * This also provides the required data for N = 1<p>
 * Phase 2 <br>
 * Each value of N will have its own data structures<br>
 * For strings of length 2 to N<br>
 * Sum sequences of values depending on the length<br>
 * Save these values in a list called a sequence-List<p>
 * Phase 3<br>
 * <pre>
 * For each of the sequence-Lists 1 up to N
 *   Find the index of each element that has the same value
 *      Store this in a map, called equal-Key-Map comprising:
 *        Key : value
 *        Entry: list of index values where that value was found
 * </pre><p>
 * Phase 4 <br>
 * <pre>
 * For each of the N equal-Key-Maps
 *   Process each entry in turn
 *     Problem is that the same key value may result from different word strings
 *     Build a map comprising
 *       Key: String of words
 *       Entry: index of places where that string was found
 *     This map is the concordance for that value of N.
 * </pre> <p>
 * Commentary<br>
 * Each data structure is indexed by N<br>
 * Each data structure is only written to by one of the phases.<br>
 * The original word list is referred to in phase 4 but is only read.<br>
 * Hence we can do the processing in parallel for each value of N<p>
 *
 * Several versions are provided:<br>
 * RunCollectConcordance:
 * RunConcordancePoG:
 * RunExtendedConcordance:
 * RunGoPConcordance:
 * RunGoPConcordanceLog:
 * RunGroupCollectConcordance:
 * RunSkeletonConcordance:
 *
 * The versions ending '_gpp' do not define the channels nor the channel declarations in the process definitions.  The processes
 * have to be defined in the order in which they occur in the dataflow through the application solution.  The processes are preceded
 * by the annotation //NETWORK and terminated by //END NETWORK.  The program {@link jcsp.groovyParallelPatterns.build.GPPbuild} can then be used
 * to create the channel definitions, channel declarations and the required parallel constructor required to invoke the process network.<p>
 *
 * Some versions ending in Chans have user defined channel names and parallel constructs rather than using the GPPbuilder script.<p>
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

 */

package GPP_Demos.concordance;
