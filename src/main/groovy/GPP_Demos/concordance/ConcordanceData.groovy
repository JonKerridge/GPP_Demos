package GPP_Demos.concordance

class ConcordanceData extends GPP_Library.DataClass {

  static int N = 0				// the maximum string length
  int strLen = 0					// the string length created by this instance
  static String fileName = ""		// name of file containing source text
  static String outFileName = ""
  //static final int errorState = -1
  static final String valueList = "createIntValueList"
  static final String indicesMap = "createValueIndicesMap"
  static final String wordsMap = "createWordMap"

  static final String init = "initClass"
  static final String create = "createInstance"
  static final String finalise = "finalise"
  static final String indexer = "indexer"
  
  // for extended concordance and test versions
  static final String initLocal = "initLocal"
  static final String collector = "collector"
  static final String finaliseCollect = "finaliseCollect"
  static final String initTest = "initTest"
  


  def static wordBuffer = new ArrayList(10000)	// the words, punctuation removed, in the text
  def static intValueList = new ArrayList(10000)	// the integer value associated with each word
  static int wordCount = 0						// number of words in text
  def sequenceList = new ArrayList(10000)	// integer value for each sequence of length strLen
  def valueIndicesMap = [:]		// for each distinct value (key) in sequenceList
                      // the indices as a list of its location (value)
  def wordMap = [:]		// map of words (key) and list of indices (value)


  /**
   * endPunctuationList the list of punctuation symbols
   * that can be removed from the ends of words
   */
  def static endPunctuationList =[',','.',';',':','?','!', '\'', '"', '_', '}', ')']

  /**
   * startPunctuationList the list of punctuation symbols
   * that can be removed from the start of words
   */
  def static startPunctuationList = ['\'' ,'"', '_', '\t', '{', '(']

  /**
   * processLine takes a line of text
   * which is split into words using tokenize(' ')
   *
   * @param line a line that has been read from a file
   * @return list of words containing each word in the line
   */
  def static processLine (line){
    def words = []
    words = line.tokenize(' ')
    return words
  }

  /**
   *
   * removePunctuation removes any punctuation characters
   * from the start and end of a word
   * @param w String containing the word to be processed
   * @return String rw containing the input word less any punctuation symbols
   *
   */
  def static removePunctuation(w) {
    def ew = w
    def rw
    def len = w.size()
    if ( len == 1 )
      rw = w
    else {
      def lastCh = w.substring(len-1, len)
      while (endPunctuationList.contains(lastCh)){
        ew = w.substring(0, len-1)
        len = len - 1
        lastCh = ew.substring(len-1, len)
      }
      def firstCh = ew.substring(0, 1)
      if ( startPunctuationList.contains(firstCh) ) {
        rw = w.substring(1, len)
      }
      else {
        rw = ew
      }
    }
    return rw
  }

  /**
   * charSum transforms a word into a single integer based upon the
   * sum of the ASCII characters that make up the word
   *
   * @param w String containing word to be transformed into an integer
   * @return int containing the integer equivalent of word
   */
  def static charSum(w) {
    def sum = 0
    def wbuff = new StringBuffer(w)
    def len = wbuff.length()
    for ( i in 0..< len) {
      sum = sum + (int) wbuff[i]
    }
    return sum
  }

  /* (non-Javadoc)
   * @see jcsp.GPP_Library.DataClass#initClass(java.lang.Object)
   */
  public int initClass( List d) {
    if ( d == null )return completedOK
    N = d[0]
    fileName = d[1]
    outFileName = d[2]
    def fileHandle = new File (fileName)
    def fileReader = new FileReader(fileHandle)
    fileReader.eachLine { line ->
      def words = processLine(line)
      for ( w in words) {
        wordBuffer << removePunctuation(w)
        intValueList << charSum (wordBuffer[wordCount])
        wordCount = wordCount + 1
      }
    }
//		println "Emit has processed $fileName"
    return completedOK
  }

  static int currentInstance = 1

  int createInstance(List d) {
    if (currentInstance > N) return normalTermination
    else {
      strLen = currentInstance
      currentInstance = currentInstance + 1
//			println "created Instance with strLen = $strLen and N = $N"
      return normalContinuation
    }
  }

  int createIntValueList (List p) {
    sequenceList = []
    int partSum = 0
    for (w in 0..wordCount-strLen) {
      for ( i in 0..< strLen) partSum = partSum + intValueList[w + i]
      sequenceList << partSum
      partSum = 0
    }
//		println "\tcreated sequenceList with strLen = $strLen"
    return completedOK
  }

  int createValueIndicesMap (List p) {
    def index = 0
    def indexList = []
    valueIndicesMap = [:]
    for ( v in sequenceList){
      indexList = valueIndicesMap.get (v, [])
      indexList << index
      valueIndicesMap.put (v, indexList)
      index = index + 1
    }
//		println "\t\tcreated valueIndicesMap with strLen = $strLen"
    return completedOK
  }

  int createWordMap (List p) {
    def sequenceValues = valueIndicesMap.keySet()
    def wordKeyList = []
    def indexList = []
    def wordMapEntry = []
    wordMap = [:]
    for ( sv in sequenceValues){
      indexList = valueIndicesMap.get(sv)
      wordMapEntry = []
      for ( il in indexList){
        wordKeyList = []
        for ( w in 0..(strLen-1)) wordKeyList << wordBuffer[il + w]
        wordMapEntry = wordMap.get (wordKeyList, [])
        wordMapEntry << il
        wordMap.put (wordKeyList, wordMapEntry)
      }
    }
//		println "\t\t\tcreated wordMap with strLen = $strLen"
    return completedOK
  }

  public int finalise(List l){
    wordBuffer << l[0].wordBufferAll
    intValueList << l[0].intValuesAll
    wordBuffer = wordBuffer.flatten()
    intValueList = intValueList.flatten()
    N = l[0].maxN
    outFileName = l[0].outFileName
    wordCount = wordBuffer.size()
    return completedOK
  }

  int indexer (List p){
      if ( p == null) return N-1
      else return strLen % p[1]
  }
  // methods used for the extended version 
  int initLocal( List d) {
	  if ( d == null )return completedOK
	  N = d[0]
	  fileName = d[1]
	  outFileName = d[2]
	  return completedOK
  }
  
  int initTest( List d) {
	  if ( d == null )return completedOK
	  N = d[0]
	  fileName = d[1]
	  outFileName = d[2]
	  return completedOK
  }
  
  int collector(def o) {
	  // assume no output to file
	  return completedOK
  }
  
  int finaliseCollect(List p) {
	  //print "${wordBuffer.size()},  "
	  return completedOK
  }
}
