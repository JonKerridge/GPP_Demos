package GPP_Demos

import GPP_Builder.GPPbuilder

def build = new GPPbuilder()
String rootPath = "./"  // as required for use in Intellij

build.runBuilder(rootPath + "nbody\\ParNbody")
build.runBuilder(rootPath + "nbody\\ParNbodyVis")
