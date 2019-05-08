package GPP_Demos.concordance

import GPP_Library.DataClass

class ConcordanceWords extends DataClass {
  def punctuatedWords = []
  def unPunctuatedWords = []
  def intValues = []
  String fileName = null
  static int maxBufferSize = -1
  def static fileHandle = null
  def static fileReader = null
  static final int errorState = -1
  static final String processBuffer = "createWordsAndIntValues"

  static final String valueList = "createIntValueList"
  static final String indicesMap = "createValueIndicesMap"
  static final String wordsMap = "createWordMap"

  static final String init = "initClass"
  static final String create = "createInstance"
  static final String finalise = "finalise"
  int bufferInstance
  static int bufferId = 100


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
   * Reads the file a line at a time using the fileReader and returns
   * at least maxWords in buffer, except for the last buffer.  Once the end of file
   * is reached the method returns a null value.  Words in the buffer may
   * include punctuation.
   *
   * @param fileReader The file fileName's associated fileReader
   * @param maxWords The minimum number of words in a returned buffer
   * @return a buffer of words or null when the end of file is reached
   */
  def static createBuffer (FileReader fileReader, int maxWords){
    def buffer = []
    def line
    int wordCount = 0
    boolean notFull = true
    while ((line = fileReader.readLine())!=null && notFull) {
         def words = 	processLine(line)
         buffer << words
         wordCount = wordCount + words.size()
         if (wordCount >= maxWords) notFull = false
    }
    //		println "CW: ${buffer.flatten()}"
    if (line == null) return null
    else return buffer.flatten()
  }

  /* (non-Javadoc)
   * @see jcsp.GPP_Library.DataClass#createInstance(java.lang.Object)
   */
  public int createInstance (List d) {
    punctuatedWords = createBuffer(fileReader, maxBufferSize)
    if (punctuatedWords == null)
      return normalTermination
    else {
      bufferInstance = bufferId
      bufferId = bufferId + 1 // simply to identify each block
      return normalContinuation
    }
  }

  /**
   *
   */
  int createWordsAndIntValues (List d) {
    def word = null
    for (w in punctuatedWords){
      word = removePunctuation(w)
      unPunctuatedWords << word
      intValues << charSum(word)
    }
//		println "CW: ${unPunctuatedWords.size()} = $unPunctuatedWords"
    return completedOK
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

  public int initClass( List d) {
    fileName = d[0]
    maxBufferSize = d[1]
    fileHandle = new File (fileName)
    fileReader = new FileReader(fileHandle)
    return completedOK
  }
}
