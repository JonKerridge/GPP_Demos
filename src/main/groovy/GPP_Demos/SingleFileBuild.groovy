package GPP_Demos

import GPP_Builder.GPPbuilder

def build = new GPPbuilder()
String rootPath = "./"  // as required for use in Intellij

build.runBuilder(rootPath + "QuickSortRecords\\FileRunTime")
build.runBuilder(rootPath + "QuickSortRecords\\RunFileSort")


