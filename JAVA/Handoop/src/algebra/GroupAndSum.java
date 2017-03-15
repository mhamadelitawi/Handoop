package algebra;


import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;


//Grouping all words together and then use a sum (WordCount)
public class GroupAndSum extends Configured implements Tool{

	  public static class MapperGroupAndSum
	       extends Mapper<Object, Text, Text, IntWritable>{

	    private final static IntWritable one = new IntWritable(1);
	    private Text word = new Text();

	    public void map(Object key, Text value, Context context
	                    ) throws IOException, InterruptedException {
	    	
	    	try
	    	{	
	    		
	    			word.set(value.toString());
			        context.write(word, one);
	    		
	    	}
	    	
	    	catch(Exception ex){}
	    }
	  }

	  public static class ReducerGroupAndSum
	       extends Reducer<Text,Text,Text,IntWritable> {

	    public void reduce(Text key, Iterable<IntWritable> values,
	                       Context context
	                       ) throws IOException, InterruptedException {
	    	int sum = 0;
			for (IntWritable val : values) {
				sum += val.get();
			}
			context.write(key, new IntWritable(sum));
	    }
	  }

	 
	@Override
	public int run(String[] arg0) throws Exception {
		 Configuration conf = new Configuration();
		    Job job = Job.getInstance(conf, "GroupAndSum");
		    job.setJarByClass(GroupAndSum.class);
		    job.setMapperClass(MapperGroupAndSum.class);
		    job.setCombinerClass(ReducerGroupAndSum.class);
		    job.setReducerClass(ReducerGroupAndSum.class);
		    job.setOutputKeyClass(Text.class);
		    job.setOutputValueClass(IntWritable.class);
		    FileInputFormat.addInputPath(job, new Path(arg0[0]));
		    FileOutputFormat.setOutputPath(job, new Path(arg0[1]));
		    return (job.waitForCompletion(true) ? 0 : 1);
	
	}
	  
	  
	  
	  
	}