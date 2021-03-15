package GPP_Demos.imageProcessing

import java.awt.image.BufferedImage
import javax.imageio.ImageIO

import groovy_parallel_patterns.DataClassInterface as constants

import groovy.transform.CompileStatic

@CompileStatic
class CompositeGSResult {

    static String initMethod = "init"
    static String collectMethod = "collect"
    static String finaliseMethod = "finalise"
    List FinaliseData = null


    int init(List d) {  // d is null]
        return constants.completedOK
    } // init

    BufferedImage resultantImage

    int collect(CompositeGSImage result){
        // this only works if there is one file being processed
//        println "Collect called"
        result.compressImage()
        File imageOutFile = new File(result.outFileName)
        ImageIO.write(result.image, "jpg", imageOutFile)
        return constants.completedOK
    }

    int finalise(List d){ // d is [outFileName]
//        println "Finalise called"
        return constants.completedOK
    }

}
