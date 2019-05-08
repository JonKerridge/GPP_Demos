package GPP_Demos.MCpi

import jcsp.lang.ChannelOutput

class MCpiFeedback extends GPP_Library.DataClass {
    static int previousIterations = 0
    static int previousWithin = 0
    static double errorMargin = 0.1
    static double previousPi = 0.0
    static boolean sentFeedback = false
    static final String initClass = "initClass"
    static final String feedbackBool = "feedbackBool"

    int initClass(List p) {
        errorMargin = p[0]
        return completedOK
    }

    int feedbackBool(List p) {
        MCpiData val = p[0]
        previousIterations = previousIterations + val.iterations
        previousWithin = previousWithin + val.within
        double currentPi = 4.0 * ((double) previousWithin / (double) previousIterations)
        if (Math.abs(currentPi - previousPi) < errorMargin) {
//            println "MCpiFbck: $previousPi: $currentPi = ${Math.abs(currentPi - previousPi)}"
            return normalTermination
        }
        else {
//            println "MCpiFbck: $previousPi: $currentPi \t\t\t= ${Math.abs(currentPi - previousPi)}"
            previousPi = currentPi
            return normalContinuation
        }

    }
}
