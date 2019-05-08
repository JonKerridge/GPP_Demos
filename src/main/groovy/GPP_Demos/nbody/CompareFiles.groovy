package GPP_Demos.nbody


String fileName1 = "src/org/jcsp/gpp/demos/nbody/result_100_100_planets_Seq.txt"
String fileName2 = "src/org/jcsp/gpp/demos/nbody/result_100_100_planets_2_Par.txt"

File file1 = new File(fileName1)
File file2 = new File(fileName2)

double margin = 1e-15

BufferedReader reader1 = file1.newReader()
BufferedReader reader2 = file2.newReader()

String line1 = reader1.readLine()
String line2 = reader2.readLine()

int line = 1
boolean ok = true

while (( line1 != null) && ok){
    def tokens1 = line1.split (" ")
    def tokens2 = line2.split (" ")
    int token = 0
    while ((token < 4) && ok){
        double d1 = Double.parseDouble(tokens1[token])
        double d2 = Double.parseDouble(tokens2[token])
        if (Math.abs(d1 - d2) < margin) {
            token = token + 1
            line = line + 1
            line1 = reader1.readLine()
            line2 = reader2.readLine()
        }
        else ok = false
    }
}
if (ok) println "files match"
else println "error found on $line"

