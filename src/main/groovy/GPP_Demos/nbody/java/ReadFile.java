package GPP_Demos.nbody.java;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReadFile {
	private String path;
	BufferedReader br = null;
	FileReader fr = null;
	private String sCurrentLine;


	public ReadFile(String filename)
	{
		path = filename;
	}

	public void readFromFile()
	{
		try {
			fr = new FileReader(path);
			br = new BufferedReader(fr);

			br = new BufferedReader(new FileReader(path));

			while ((sCurrentLine = br.readLine())!= null)
			{
				System.out.println(sCurrentLine);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			try{
				if(br != null)
					br.close();
				if(fr != null)
					fr.close();
			}
			catch(IOException ex){
				ex.printStackTrace();
			}
		}

	}

}
