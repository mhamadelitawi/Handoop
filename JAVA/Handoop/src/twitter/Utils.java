package twitter;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class Utils {

	public static void deleteDestinationHDFS(String pathDestination) {
		try {
			Path destination = new Path(pathDestination);
			FileSystem fs = FileSystem.get(new Configuration());
			if (fs.exists(destination))
				fs.delete(destination, true);
		} catch (IOException e) {
		}
	}

	public static void readFileHDFS(String pathDestination)
	{
		try{
		
		FileSystem fs = FileSystem.get(new Configuration());
		Path destination = new Path(pathDestination);
		if (fs.exists(destination))
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(destination)));
			String line;
			line = br.readLine();
			while (line != null) 
			{
			System.out.println(line);
			line = br.readLine();
			}
			br.close();
		}
		}catch(Exception e){}
	}
	
	public static boolean isAlpha(String name) {
	        return name.matches("[_a-zA-Z0-9]+");
	    }
	
	
	public static boolean isSpecial(String name) {
        return name.matches("[a-zA-Z0-9]+");
    }
	
	

    public static ArrayList <String> getHashtags (String line)
    {
        String l = "";
        ArrayList <String> ar = new ArrayList<>();
        String t[] = line.split("\n");
        for (int i = 0 ; i < t.length ;i++)
        {
            String [] h = t[i].split("#");
            for (int j = 1 ; j < h.length ; j++)
            {
            	l = h[j].split(" ")[0];
            	if(!l.equals("")) ar.add(l );
            }
               
        } 
    return ar;
    } 
    
	
    public static ArrayList <String>  testAndGetHashtags(String tweet)
    {
    	if(tweet.contains("#")) return getHashtags(tweet);
    	return null;
    }
    
    
    
    public static String firstNumber(String a)
	{
		String n="" ;
		for (int  i = 0 ; i < a.length() ; i++)
		if (Character.isDigit(a.charAt(i)))
			n+=a.charAt(i);
		else
			break;
		return n;
	}
    
    
}
