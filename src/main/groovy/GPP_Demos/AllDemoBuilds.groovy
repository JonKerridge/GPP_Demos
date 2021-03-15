package GPP_Demos

import gpp_builder.GPPbuilder

def build = new GPPbuilder()
String rootPath = "./"  // as required for use in Intellij
//// concordance
build.runBuilder(rootPath + "concordance\\EPC")
build.runBuilder(rootPath + "concordance\\TPP")
build.runBuilder(rootPath + "concordance\\EPCLog")
build.runBuilder(rootPath + "concordance\\XtndEPC")
build.runBuilder(rootPath + "concordance\\XtndEPCLog")
build.runBuilder(rootPath + "concordance\\PoG")
build.runBuilder(rootPath + "concordance\\PoGC")
build.runBuilder(rootPath + "concordance\\GoPC")
build.runBuilder(rootPath + "concordance\\GoP")
// goldbach_Paper
build.runBuilder(rootPath + "goldbach_Paper\\scripts\\RunParGoldbach")
build.runBuilder(rootPath + "goldbach_Paper\\scripts\\RunMultiPrimesParGoldbach")
build.runBuilder(rootPath + "goldbach_Paper\\scripts\\RunCombiningPrimes")
build.runBuilder(rootPath + "goldbach_Paper\\scripts\\RunPrimes")
build.runBuilder(rootPath + "goldbach_Paper\\scripts\\RunSeqGoldbach")
build.runBuilder(rootPath + "goldbach_Paper\\scripts\\RunSimplePrimes")
// goldbach_Book
build.runBuilder(rootPath + "goldbach_Book\\scripts\\ParGoldbach")
build.runBuilder(rootPath + "goldbach_Book\\scripts\\ParPrimes")
// Jacobi
build.runBuilder(rootPath + "jacobi\\RunJacobiMC")
build.runBuilder(rootPath + "jacobi\\RunJacobiMCVis")
//mandelbrot
build.runBuilder(rootPath + "mandelbrot\\scripts\\RunMandelbrot")
build.runBuilder(rootPath + "mandelbrot\\scripts\\RunMandelbrotLine")
build.runBuilder(rootPath + "mandelbrot\\scripts\\RunMandelbrotNoGui")
build.runBuilder(rootPath + "mandelbrot\\scripts\\RunMandelbrotLineNoGui")
//nbody  and solar system
build.runBuilder(rootPath + "nbody\\ParNbody")
build.runBuilder(rootPath + "nbody\\ParNbodyVis")
build.runBuilder(rootPath + "solarSystem\\RunPlanets")
//image processing
build.runBuilder(rootPath + "imageProcessing\\RunGSImage")
build.runBuilder(rootPath + "imageProcessing\\RunRGBImage")
build.runBuilder(rootPath + "imageProcessing\\RunEdge")
build.runBuilder(rootPath + "imageProcessing\\RunEdge2")
//mcpi
build.runBuilder(rootPath + "MCpi\\RunSkelMCpi")
build.runBuilder(rootPath + "MCpi\\RunSkelMCpiVisLog")
build.runBuilder(rootPath + "MCpi\\RunMCpiDataParallel")
build.runBuilder(rootPath + "MCpi\\RunMCpiWorkerDataParallel")
//mceSort
build.runBuilder(rootPath + "mceSort\\RunMCEsort")
// quick sorts
build.runBuilder(rootPath + "QuickSortRecords\\RunFileSort")
build.runBuilder(rootPath + "QuickSortRecords\\RunFileSortVis")


