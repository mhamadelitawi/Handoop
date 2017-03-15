package twitter;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;


import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import org.json.JSONObject;
import org.apache.hadoop.mapreduce.Job;

import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;


public class Selection extends Configured implements Tool {
	
	private static final String inputPath = "js";
	private static final String outputPath ="outputselection";
	
	
	public static class SelectionMapper extends Mapper<LongWritable, Text, Text, NullWritable>
	{
		@Override
		public void map(LongWritable key, Text value, Context context)  {
			
			
			
					
						try {
							//Configuration conf = context.getConfiguration();
							//String param = conf.get("selection");
							String tweet = new JSONObject(value.toString()).getString("text");
							if (tweet.contains(context.getConfiguration().get("selection")))
							     context.write(new Text(tweet), NullWritable.get());
						} catch (Exception  e) {
							e.printStackTrace();
						}
				}
	}

	@Override
	public int run(String[] arg0) throws Exception {
		

		Configuration conf = new Configuration();
		conf.set("selection", "iphone");
		Job job = Job.getInstance(conf, "Selection");
		job.setJarByClass(Selection.class);
	    job.setMapperClass(SelectionMapper.class);

	    
	    job.setNumReduceTasks(0);
	    
	    FileInputFormat.setInputDirRecursive(job, true);
        FileInputFormat.setInputPaths(job, new Path(inputPath));
        FileOutputFormat.setOutputPath(job, new Path(outputPath));
		
        return job.waitForCompletion(true) ? 0 : 1;
		 
	}
	
    public static void main(String[] args) throws Exception {
    	String path [] = new String [2];
    	path[0] = inputPath;
    	path[1] = outputPath;
    	
    	
        int res = ToolRunner.run(new Selection(), path);
        System.exit(res);
    }
	
}
