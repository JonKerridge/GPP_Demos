package GPP_Demos

import gppBuilder.GPPbuilder

def build = new GPPbuilder()
String rootPath = "./"  // as required for use in Intellij

build.runBuilder(rootPath + "QuickSortRecords\\RunFileSortVis")



