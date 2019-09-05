package GPP_Demos

import GPP_Builder.GPPbuilder

def build = new GPPbuilder()
String rootPath = "./"  // as required for use in Intellij

build.runBuilder(rootPath + "concordance\\GoP")
build.runBuilder(rootPath + "concordance\\GoPC")
build.runBuilder(rootPath + "concordance\\PoG")
build.runBuilder(rootPath + "concordance\\PoGC")

