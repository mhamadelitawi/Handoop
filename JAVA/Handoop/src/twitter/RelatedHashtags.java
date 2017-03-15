package twitter;


import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;


import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import org.json.JSONArray;
import org.json.JSONObject;

public class RelatedHashtags  extends Configured implements Tool{

	
	public static class MapperRelatedHashtags extends Mapper<LongWritable, Text, Text, NullWritable> {

		public Text word = new Text();

		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {


			String line = value.toString();

			try {
				
				String hash = context.getConfiguration().get("hash").toLowerCase();

				JSONArray js = new JSONObject(line).getJSONObject("entities").getJSONArray("hashtags");
				boolean flag = true;
				
				for (int i = 0; (i < js.length()) && flag  ; i++) {
					String g = js.getJSONObject(i).getString("text").toLowerCase();
					if (Utils.isAlpha(g)) 
						if (hash.contains(g)) 
							flag=false;
				}
				if (flag) return;  
				word.set(new JSONObject(line).getString("text"));
				context.write(word, NullWritable.get());	
			} catch (Exception e) {
			}

		}
	
	
	}

	
	public static class ReducerRelatedHashtags extends Reducer<Text, NullWritable, Text, NullWritable>
	{
		@Override
		protected void reduce(Text key, Iterable<NullWritable> values, Context context)
				throws IOException, InterruptedException {
			context.write(key, NullWritable.get());
		}
	}
	
	
	public static int runStep1(String inputPath , String outputPath  , String hash) throws IOException, ClassNotFoundException, InterruptedException
	{
		Configuration conf = new Configuration();
		conf.set("hash", hash);
        Job job1 = Job.getInstance(conf, "jobRelatedHashtags");
        job1.setJarByClass(RelatedHashtags.class);       
        
        job1.setMapperClass(MapperRelatedHashtags.class); 
        job1.setReducerClass(ReducerRelatedHashtags.class);
        
        job1.setOutputKeyClass(Text.class);
		job1.setOutputValueClass(NullWritable.class);
        
        FileInputFormat.setInputDirRecursive(job1, true);
        FileInputFormat.setInputPaths(job1, new Path(inputPath));
        FileOutputFormat.setOutputPath(job1, new Path(outputPath));

        boolean succ = job1.waitForCompletion(true);
        if (! succ) {
          System.out.println("Job failed, exiting");
          return -1;
        }
        return 0;
	}
	
	@Override
	public int run(String[] arg0) throws Exception {
		return  runStep1(arg0[0] , arg0[1] , arg0[2]) ;	
	}
	
    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new RelatedHashtags(), args);
        System.exit(res);
    }
	
	
	
	
}
