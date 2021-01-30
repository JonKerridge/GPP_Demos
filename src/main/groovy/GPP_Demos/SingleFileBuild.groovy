package GPP_Demos

import gppBuilder.GPPbuilder

def build = new GPPbuilder()
String rootPath = "./"  // as required for use in Intellij

build.runBuilder(rootPath + "imageProcessing\\RunEdge")
build.runBuilder(rootPath + "imageProcessing\\RunEdge2")



