This repository contains a set of demonstrations of the 
Groovy Parallel Patterns (GPP) library.

Contact J.Kerridge@napier.ac.uk

It is work in progress and not all demos will work.

The build assumes you are using Java-11 and Groovy-3

It is known that the demos do work with the current release of Groovy 3.0.7.

The gradle build file for the project does load the required components.

The build assumes that the required libraries have been downloaded into
a local Maven Repository.  The required libraries are:    

jcsp: https://github.com/CSPforJAVA/jcsp/releases/tag/1.1.8  
groovyJCSP: https://github.com/JonKerridge/groovyJCSP/releases/tag/1.1.8  
groovyParallelPatterns: https://github.com/JonKerridge/GPP_Library/releases/tag/1.1.11  
GPP_Builder: https://github.com/JonKerridge/GPP_Builder/releases/tag/1.1.11  

The build.gradle uses the repositories and dependencies: 

<pre>
repositories {
  mavenCentral()
  maven {
    url "https://mvnrepository.com/artifact/org.codehaus.groovy/groovy-all"
  }
  mavenLocal()
}

dependencies {
  implementation 'org.codehaus.groovy:groovy-all:3.0.7'
  implementation 'cspforjava:jcsp:1.1.8'
  implementation 'groovyJCSP:groovyJCSP:1.1.8'
  implementation "groovyParallelPatterns:groovyParallelPatterns:1.1.11"
  implementation "gppBuilder:gppBuilder:1.1.11"
// to include javafx  into  compile required for groovyParallelPatterns
  implementation "org.openjfx:javafx-base:11:win"
  implementation "org.openjfx:javafx-graphics:11:win"
  implementation "org.openjfx:javafx-controls:11:win"
}

</pre>

The script AllDemoBuilds will build all the xxx.groovy codes 
from the corresponding xxx.gpp files, that are known to be functional.


