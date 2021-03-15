This repository contains a set of demonstrations of the 
Groovy Parallel Patterns (GPP) library.

Contact J.Kerridge @ napier.ac.uk

It is work in progress and not all demos will work.

The build assumes you are using Java-11 and Groovy-3.
Java 11 is required because openjavafx version 11 is used.

It is known that the demos do work with the current release of Groovy 3.0.7.

The gradle build file for the project does load the required components.

The required libraries are:    

jcsp: cspforjava.jcsp version 1.1.9 
groovyJCSP: jonkerridge.groovy_jcsp version 1.1.9 
groovyParallelPatterns: jonkerridge.groovy_parallel_patterns version 1.1.12  
GPP_Builder: jonkerridge.gpp_builder version1.1.12  

The build.gradle uses the repositories and dependencies: 

<pre>
repositories {
  mavenCentral()
  maven { // to download the jonkerridge.groovy_jcsp library
    name = "GitHub"
    url = "https://maven.pkg.github.com/JonKerridge/groovyJCSP"
    credentials {
      username = project.findProperty("gpr.user")
      password = project.findProperty("gpr.key")
    }
  }
  maven { // to download the cspforjava.jcsp library
    name = "GitHub"
    url = "https://maven.pkg.github.com/CSPforJAVA/jcsp"
    credentials {
      username = project.findProperty("gpr.user")
      password = project.findProperty("gpr.key")
    }
  }
  maven { // to download the jonkerridge.groovy_parallel_patterns library
    name = "GitHub"
    url = "https://maven.pkg.github.com/JonKerridge/GPP_Library"
    credentials {
      username = project.findProperty("gpr.user")
      password = project.findProperty("gpr.key")
    }
  }
  maven { // to download the jonkerridge.gpp_builder library
    name = "GitHub"
    url = "https://maven.pkg.github.com/JonKerridge/GPP_Builder"
    credentials {
      username = project.findProperty("gpr.user")
      password = project.findProperty("gpr.key")
    }
  }

}

dependencies {
  implementation 'org.codehaus.groovy:groovy-all:3.0.7'
  implementation 'cspforjava:jcsp:1.1.9'
  implementation 'jonkerridge:groovy_jcsp:1.1.9'
  implementation "jonkerridge:groovy_parallel_patterns:1.1.12"
  implementation "jonkerridge:gpp_builder:1.1.12"
// to include javafx  into  compile required for groovy_parallel_patterns
  implementation "org.openjfx:javafx-base:11:win"
  implementation "org.openjfx:javafx-graphics:11:win"
  implementation "org.openjfx:javafx-controls:11:win"
}

</pre>

The script AllDemoBuilds will build all the xxx.groovy codes 
from the corresponding xxx.gpp files, that are known to be functional.

**Please note**

In order to download Github Packages a user requires to have a Github Personal Access Token.  
See https://docs.github.com/en/github/authenticating-to-github/creating-a-personal-access-token

A gradle.properties file is required at the same directory level as the build.gradle file that contains

<pre>
gpr.user=userName
gpr.key=userPersonalAccessToken
</pre>



