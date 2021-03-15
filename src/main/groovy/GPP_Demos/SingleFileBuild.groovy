package GPP_Demos

import gpp_builder.GPPbuilder

def build = new GPPbuilder()
String rootPath = "./"  // as required for use in Intellij

build.runBuilder(rootPath + "imageProcessing\\RunEdge")
build.runBuilder(rootPath + "imageProcessing\\RunEdge2")



