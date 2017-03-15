package twitter;


import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.json.*;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;




public class GetHashtags {

	
	public static class Map extends Mapper<LongWritable, Text, Text, IntWritable>
	{
		 private final static IntWritable one = new IntWritable(1);
		     Text word = new Text();
		   
		    @Override
		 public void map(LongWritable key, Text value, Context context) {

	          
	            String line = value.toString();	   
	            try{   
	            	
	                  JSONObject obj = new JSONObject(line);
	                 String tweet = obj.getString( "text");
	                 ArrayList <String> hashtags =  Utils.testAndGetHashtags(tweet);
	                if (hashtags!= null)
	                {
	                	for (String hash : hashtags)
	                	{
	                		 word.set(hash.toLowerCase());
			                 context.write(word, one);
	                	}
	                	
	                }         
	                
	            }catch(JSONException | IOException | InterruptedException e){
	                e.printStackTrace();
	            }
	        }
		  
	}
	
	
	public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable>
	{
		
		@Override
		public void reduce(Text key, Iterable<IntWritable> values, Context context) 
				throws IOException, InterruptedException {
int sum = 0;

for (IntWritable val : values) {
    sum += val.get();
  }

context.write(key, new IntWritable(sum));
}
	}
	
	
	
	
	
	  static int activateGetHashtags (String inputPath , String outputPath)
	    {
			try 
			{
				Configuration conf = new Configuration();
			    Job job2 = Job.getInstance(conf, "sonParser");
			    
			    FileInputFormat.setInputPaths(job2, new Path(inputPath));
		        FileOutputFormat.setOutputPath(job2, new Path(outputPath));
		       
		        job2.setMapperClass(Map.class);
		        job2.setReducerClass(Reduce.class);
		        
		        job2.setInputFormatClass(TextInputFormat.class);
		        job2.setMapOutputKeyClass(LongWritable.class);
		        job2.setMapOutputValueClass(Text.class);
		        job2.setSortComparatorClass(LongWritable.DecreasingComparator.class);
		        job2.setOutputFormatClass(TextOutputFormat.class);
		        
		        
			    //System.exit(job.waitForCompletion(true) ? 0 : 1);
				return (job2.waitForCompletion(true) ? 0 : 1);
			} catch (IOException | ClassNotFoundException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return -1;
			}
	    }
	
	
	
	
	

}
