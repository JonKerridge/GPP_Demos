/**
 * This package implements the QuickSort algorithm.  The approach adopted is to split the data
 * to be sorted into a number of different partitions, the parallel owrkers.  The workers each then undertake
 * a QuickSort on their partition.  The workers then out put their sorted order to a merge process that merges
 * all the input streams into a final sorted order. <p>
 *
 * This is achieved by using
 * the spreader OneDirectedList, the reducer N_WayMerge and a group of ThreePhaseWorkers.
 * The emit process creates a large number of QSData objects; such that one property, index,
 * will be the key value used to sort that objects.  Another property, batch, indicates to
 * which of the ThreePhaseWorkers the objects should be directed by the OneDirectedList process.<p>
 *
 * The ThreePhaseWorkers each read a number of the QSData objects into a local worker object called
 * QSWorker.  This object implements the required inFunction, workFunction and outFunction methods.
 * Internally, once all the input objects have been read, the workFunction is called, which carries
 * out the QuickSort.  Once the sort finishes the outFunction method is repeatedly called to outout the
 * data held in the local data store.  The output data objects are also QSData objects.<p>
 *
 * The output data objects are read by a N_WayMerge process that  a method called mergeChoice that determines
 * which of the available data objects that have been read will be chosen and written to the output channel
 * of the process, where they can be Collected.
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
 *
 */

package GPP_Demos.QuickSortIntegers;
