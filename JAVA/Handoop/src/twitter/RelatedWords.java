package twitter;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import org.json.JSONObject;

public class RelatedWords extends Configured implements Tool {

	public static class MapperRelatedWords extends Mapper<LongWritable, Text, Text, IntWritable> {

		public Text word = new Text();

		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			try {
			
				String search = context.getConfiguration().get("key");
				String tweet = new JSONObject(value.toString()).getString("text");
				
				boolean flag = false;

				String[] se = search.split(" ");
				for (int i = 0 ; i < (se.length )&& !flag ; i++)
				{
					if (tweet.contains(se[i])) flag =true;
				}
				
				if (!flag) return;
				
				
				
				
					
					String tw = "";
					for (int i = 0 ; i < tweet.length() ; i++)
					{
						if ( tweet.charAt(i) == ' ' ||  Utils.isAlpha(tweet.charAt(i)+"") )
					   tw = tw+tweet.charAt(i);
						else
							tw = tw+" ";
					}	
					String[] wo = tw.split(" ");
					for (String wor : wo)
					{
						if (!wor.equals(""))
						{
							word.set(wor); 
							context.write(word, new IntWritable(1));	
						}	
					}
					
					
				
				
			} catch (Exception e) {
			}

		}

	}

	public static class ReducerRelatedWords extends Reducer<Text, IntWritable, Text, IntWritable> {
		@Override
		protected void reduce(Text key, Iterable<IntWritable> values, Context context)
				throws IOException, InterruptedException {
			int sum = 0;
			for (IntWritable val : values) {
				sum += val.get();
			}
			context.write(key, new IntWritable(sum));
		}
	}

	public static int runStep1(String inputPath, String outputPath, String key)
			throws IOException, ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		conf.set("key", key);
		Job job1 = Job.getInstance(conf, "jobRelatedHashtags");
		job1.setJarByClass(RelatedWords.class);

		job1.setMapperClass(MapperRelatedWords.class);
		//job1.setCombinerClass(ReducerRelatedWords.class);
		job1.setReducerClass(ReducerRelatedWords.class);

		job1.setOutputKeyClass(Text.class);
		job1.setOutputValueClass(IntWritable.class);

		FileInputFormat.setInputDirRecursive(job1, true);
		FileInputFormat.setInputPaths(job1, new Path(inputPath));
		FileOutputFormat.setOutputPath(job1, new Path(outputPath));

		boolean succ = job1.waitForCompletion(true);
		if (!succ) {
			System.out.println("Job failed, exiting");
			return -1;
		}
		return 0;
	}

	@Override
	public int run(String[] arg0) throws Exception {
		return runStep1(arg0[0], arg0[1], arg0[2]);
	}

	public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new RelatedWords(), args);
		System.exit(res);
	}

}
