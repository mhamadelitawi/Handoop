package Handoop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class Tools {

	static void callThePush(String title , String guid , String status)
	{
		try {
		    // open a connection to the site
		    URL url = new URL("http://127.0.0.1/Handoop/javaphp.php");
		    URLConnection con = url.openConnection();
		    // activate the output
		    con.setDoOutput(true);
		    PrintStream ps = new PrintStream(con.getOutputStream());
		    // send your parameters to your site
		    ps.print("title="+title);
		    ps.print("&guid="+guid);
		    ps.print("&status="+status);
		   
		    
		    
		    // we have to get the input stream in order to actually send the request
		    con.getInputStream();
		 
		    // close the print stream
		    ps.close();
		    
		
		    } catch (MalformedURLException e) {
		        e.printStackTrace();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }

	}
	
	
	
	public static void copyFromHDFSToLocal(String pathSource , String pathDestination)
	{
		try{
		FileSystem fs = FileSystem.get(new Configuration());
		Path source = new Path(pathSource);
		Path destination = new Path(pathDestination);
		System.out.println("source = "+pathSource);
		if (fs.exists(source))
		{
			System.out.println("fet");
			fs.copyToLocalFile(true, source, destination);
			System.out.println("5alas");
		}
		
		}catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	
	
}
