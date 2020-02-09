package GPP_Demos.QuickSortRecords

def fileOutPath = "D:\\QuickSortOutputs\\"

String inFileName
if (args.size() == 0) {
  inFileName = fileOutPath + "16m-output.txt"
} else {
//    String folder = args[0] not used
  inFileName = fileOutPath + args[1]
}

def keyCompare (String s1, String s2){
  int i = 0
  while ((s1[i].equals(s2[i])) && (i < 10) ) i = i + 1
  return (s1[i] < s2[i])
}
int count = 1
File file = new File(inFileName)
reader = file.newReader()
String line1 = reader.readLine()
String line2 = reader.readLine()
while (line2 != null){
  if (keyCompare(line1, line2)){
    line1 = line2
    line2 = reader.readLine()
    count = count + 1
  }
  else {
    println "PROBLEM: \n$line1, \n$line2"
    break
  }
}
if (line2 == null) println "File is sorted correctly $count records"