This repository contains a set of demonstrations of the Groovy Parallel Patterns (GPP) library.

Contact J.Kerridge@napier.ac.uk

It is a work in progress and not all demos will work.


It is known that the demos do work with the current release of groovy 3.0.2.

The gradle build file for the project does load the required components.

The build assumes that:<br>
library groovyParallelPatterns-1.0.6 <br>
code GPP_Builder-1.0.6<br>
have been built and the associated jar files placed in C:\jcspLib<p>

The build.gradle files for each of these downloads contains a **copyLib** task in the 
**other** task group in the Gradle interface.

The script AllDemoBuilds will build all the xxx.groovy codes from the corresponding xx.gpp files


