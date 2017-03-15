package twitter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.json.JSONObject;


public class GetRelated {

	public static class MapperGetRelated extends Mapper<LongWritable, Text, Text, NullWritable> {
		@Override
		public void map(LongWritable key, Text value, Context context) {
			
			try {
			String search = context.getConfiguration().get("key").toLowerCase();
			String tweet = new JSONObject(value.toString()).getString("text").toLowerCase().replace("\n", " ");	
			
			if (tweet.contains(search) )
			context.write(new Text(tweet), NullWritable.get());
			}catch (Exception e){}
			
		}
	}
	
	
	
	
	public static int runGetRelated(String input , String output , String key ) throws Exception {
		

		Configuration conf = new Configuration();
		conf.set("key", key);
		Job job = Job.getInstance(conf, "GetMyFollowingOnly");
		job.setJarByClass(GetRelated.class);
	    job.setMapperClass(MapperGetRelated.class);

	    
	    job.setNumReduceTasks(0);
	    
	    FileInputFormat.setInputDirRecursive(job, true);
        FileInputFormat.setInputPaths(job, new Path(input));
        FileOutputFormat.setOutputPath(job, new Path(output));
		
        return job.waitForCompletion(true) ? 0 : 1;
		 
	}
}
