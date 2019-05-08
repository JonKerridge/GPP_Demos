package GPP_Demos

import GPP_Builder.GPPbuilder

def build = new GPPbuilder()
String rootPath = "./"  // as required for use in Intellij
// concordance
build.runBuilder(rootPath + "concordance\\RunCollectConcordance")
build.runBuilder(rootPath + "concordance\\RunConcordancePoG")
build.runBuilder(rootPath + "concordance\\RunExtendedConcordance")
build.runBuilder(rootPath + "concordance\\RunExtendedConcordanceVisLog")
build.runBuilder(rootPath + "concordance\\RunGoPConcordance")
build.runBuilder(rootPath + "concordance\\RunGoPConcordanceLog")
build.runBuilder(rootPath + "concordance\\RunGroupCollectConcordance")
build.runBuilder(rootPath + "concordance\\RunManyCollectConcordance")
// goldbach
build.runBuilder(rootPath + "goldbach\\scripts\\RunParGoldbach")
build.runBuilder(rootPath + "goldbach\\scripts\\RunMultiPrimesParGoldbach")
build.runBuilder(rootPath + "goldbach\\scripts\\RunCombiningPrimes")
build.runBuilder(rootPath + "goldbach\\scripts\\RunPrimes")
build.runBuilder(rootPath + "goldbach\\scripts\\RunSeqGoldbach")
build.runBuilder(rootPath + "goldbach\\scripts\\RunSimplePrimes")
// Jacobi
build.runBuilder(rootPath + "jacobi\\RunJacobiMC")
//mandelbrot
build.runBuilder(rootPath + "mandelbrot\\scripts\\RunMandelbrot")
build.runBuilder(rootPath + "mandelbrot\\scripts\\RunMandelbrotLine")
build.runBuilder(rootPath + "mandelbrot\\scripts\\RunMandelbrotNoGui")
build.runBuilder(rootPath + "mandelbrot\\scripts\\RunMandelbrotLineNoGui")
//nbody
build.runBuilder(rootPath + "nbody\\ParNbody")
build.runBuilder(rootPath + "solarSystem\\RunPlanets")
//image processing
build.runBuilder(rootPath + "imageProcessing\\RunGSImage")
build.runBuilder(rootPath + "imageProcessing\\RunRGBImage")
//mcpi
build.runBuilder(rootPath + "MCpi\\RunSkelMCpi")
build.runBuilder(rootPath + "MCpi\\RunSkelMCpiVisLog")
build.runBuilder(rootPath + "MCpi\\RunMCpiDPCPattern")
build.runBuilder(rootPath + "MCpi\\RunMCpiWorkerDPCPattern")



