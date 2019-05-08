/**
 * A solution to the N-body problem that ustilses a List of Planet objects based representation of the planets using the
 * MultiCoreEngine process, which itself comprises a MultiCoreRoot process and a number of MultiCoreNode processes
 * over which the Matrix is partitioned for processing.  The solution undertakes a fixed number of iterations.
 * The architecture used is the same as the nbody demonstration and demonstrrates that the primary programming
 * content is that produced by the user of the library in the code they provide..
 * <p>
 *<pre>
 * Author, Licence and Copyright statement
 * author  Jon Kerridge
 *         School of Computing
 *         Edinburgh Napier University
 *         Merchiston Campus,
 *         Colinton Road
 *         Edinburgh EH10 5DT
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
 */

package GPP_Demos.solarSystem;