package twitter;
import java.io.IOException;
import java.io.StringReader;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import au.com.bytecode.opencsv.CSVReader;


public class WordCount {

	  public static class TokenizerMapper
	       extends Mapper<Object, Text, Text, IntWritable>{

	    private final static IntWritable one = new IntWritable(1);
	    private Text word = new Text();

	    public void map(Object key, Text value, Context context
	                    ) throws IOException, InterruptedException {
	    	CSVReader r = new CSVReader(new StringReader(value.toString()));
	    	try
	    	{
	    		word.set(r.readNext()[5]);
		        context.write(word, one);
		        r.close();
	    	}
	    	
	    	catch(Exception ex){}
	    }
	  }

	  public static class IntSumReducer
	       extends Reducer<Text,Text,Text,IntWritable> {
	    private IntWritable result = new IntWritable();

	    public void reduce(Text key, Iterable<IntWritable> values,
	                       Context context
	                       ) throws IOException, InterruptedException {
	      int sum = 0;
	      
	     
	      
	      result.set(sum);
	      context.write(key, result);
	    }
	  }

	  
	  
	  static void activateWordCount (String inputPath , String outputPath)
	    {
			try 
			{
				Configuration conf = new Configuration();
			    Job job = Job.getInstance(conf, "word count");
			    job.setJarByClass(WordCount.class);
			    job.setMapperClass(TokenizerMapper.class);
			   // job.setCombinerClass(IntSumReducer.class);
			    job.setReducerClass(IntSumReducer.class);
			    job.setOutputKeyClass(Text.class);
			    job.setOutputValueClass(IntWritable.class);
			    FileInputFormat.addInputPath(job, new Path(inputPath));
			    FileOutputFormat.setOutputPath(job, new Path(outputPath));
			    System.exit(job.waitForCompletion(true) ? 0 : 1);
			}
	     catch (IOException | ClassNotFoundException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    }  
	  
	  
	  
	  
	  
	  public static void main(String[] args) throws Exception {
	    Configuration conf = new Configuration();
	    Job job = Job.getInstance(conf, "word count");
	    job.setJarByClass(WordCount.class);
	    job.setMapperClass(TokenizerMapper.class);
	    job.setCombinerClass(IntSumReducer.class);
	    job.setReducerClass(IntSumReducer.class);
	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(IntWritable.class);
	    FileInputFormat.addInputPath(job, new Path(args[0]));
	    FileOutputFormat.setOutputPath(job, new Path(args[1]));
	    System.exit(job.waitForCompletion(true) ? 0 : 1);
	  }
	  
	  
	  
	  
	}