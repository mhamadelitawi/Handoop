package twitter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;

import twitter.Selection.SelectionMapper;

public class GetMyFollowingOnly {

	public static class MapperMyFollowingOnly extends Mapper<LongWritable, Text, Text, NullWritable> {
		@Override
		public void map(LongWritable key, Text value, Context context) {
			String user = context.getConfiguration().get("user").toLowerCase();
			String fol = context.getConfiguration().get("following");
			try {
				String Relation[] = value.toString().split(",");
				
				
				if(fol.equals("yes"))
				{
					if (Relation[0].equals(user))
						context.write(new Text(Relation[1]), NullWritable.get());
				}
				else
				{
					if (Relation[1].equals(user))
						context.write(new Text(Relation[0]), NullWritable.get());
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	
	public static int runGetMyFollowingOnly(String input , String output , String user , String following ) throws Exception {
		

		Configuration conf = new Configuration();
		conf.set("user", user);
		conf.set("following", following);
		Job job = Job.getInstance(conf, "GetMyFollowingOnly");
		job.setJarByClass(GetMyFollowingOnly.class);
	    job.setMapperClass(MapperMyFollowingOnly.class);

	    
	    job.setNumReduceTasks(0);
	    
	    FileInputFormat.setInputDirRecursive(job, true);
        FileInputFormat.setInputPaths(job, new Path(input));
        FileOutputFormat.setOutputPath(job, new Path(output));
		
        return job.waitForCompletion(true) ? 0 : 1;
		 
	}
	
	
	
}
