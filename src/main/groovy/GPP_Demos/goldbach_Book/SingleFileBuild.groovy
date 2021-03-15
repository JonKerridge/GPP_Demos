package GPP_Demos.goldbach_Book

import gpp_builder.GPPbuilder

def build = new GPPbuilder()
String rootPath = "./"  // as required for use in Intellij

build.runBuilder(rootPath + "scripts/ParGoldbach")
build.runBuilder(rootPath + "scripts/ParPrimes")

