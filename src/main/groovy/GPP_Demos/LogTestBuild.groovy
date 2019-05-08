package GPP_Demos

import GPP_Builder.*


def build = new GPPbuilder()
String rootPath = "./VisLogDemos/"  // as required for use in Intellij
build.runBuilder(rootPath + "Test20Log")
build.runBuilder(rootPath + "Test21Log")
build.runBuilder(rootPath + "Test23Log")

