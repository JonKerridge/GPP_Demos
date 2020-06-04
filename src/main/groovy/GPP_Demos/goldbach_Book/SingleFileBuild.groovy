package GPP_Demos.goldbach_Book

import gppBuilder.GPPbuilder

def build = new GPPbuilder()
String rootPath = "./"  // as required for use in Intellij

build.runBuilder(rootPath + "scripts/ParGoldbach")
build.runBuilder(rootPath + "scripts/ParPrimes")

