package GPP_Demos

import GPP_Builder.GPPbuilder

def build = new GPPbuilder()
String rootPath = "./"  // as required for use in Intellij
// concordance
build.runBuilder(rootPath + "concordance\\EPC")
build.runBuilder(rootPath + "concordance\\EPCLog")
build.runBuilder(rootPath + "concordance\\XtndEPC")
build.runBuilder(rootPath + "concordance\\XtndEPCLog")
build.runBuilder(rootPath + "concordance\\PoG")
build.runBuilder(rootPath + "concordance\\PoGC")
build.runBuilder(rootPath + "concordance\\GoPC")
build.runBuilder(rootPath + "concordance\\GoP")
//// goldbach
//build.runBuilder(rootPath + "goldbach\\scripts\\RunParGoldbach")
//build.runBuilder(rootPath + "goldbach\\scripts\\RunMultiPrimesParGoldbach")
//build.runBuilder(rootPath + "goldbach\\scripts\\RunCombiningPrimes")
//build.runBuilder(rootPath + "goldbach\\scripts\\RunPrimes")
//build.runBuilder(rootPath + "goldbach\\scripts\\RunSeqGoldbach")
//build.runBuilder(rootPath + "goldbach\\scripts\\RunSimplePrimes")
//// Jacobi
//build.runBuilder(rootPath + "jacobi\\RunJacobiMC")
////mandelbrot
//build.runBuilder(rootPath + "mandelbrot\\scripts\\RunMandelbrot")
//build.runBuilder(rootPath + "mandelbrot\\scripts\\RunMandelbrotLine")
//build.runBuilder(rootPath + "mandelbrot\\scripts\\RunMandelbrotNoGui")
//build.runBuilder(rootPath + "mandelbrot\\scripts\\RunMandelbrotLineNoGui")
////nbody
//build.runBuilder(rootPath + "nbody\\ParNbody")
//build.runBuilder(rootPath + "solarSystem\\RunPlanets")
////image processing
//build.runBuilder(rootPath + "imageProcessing\\RunGSImage")
//build.runBuilder(rootPath + "imageProcessing\\RunRGBImage")
////mcpi
//build.runBuilder(rootPath + "MCpi\\RunSkelMCpi")
//build.runBuilder(rootPath + "MCpi\\RunSkelMCpiVisLog")
//build.runBuilder(rootPath + "MCpi\\RunMCpiDataParallel")
//build.runBuilder(rootPath + "MCpi\\RunMCpiWorkerDataParallel")



