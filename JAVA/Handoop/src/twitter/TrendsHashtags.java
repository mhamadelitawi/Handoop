package twitter;


import java.io.IOException;
import java.util.StringTokenizer;

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
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import org.json.JSONArray;
import org.json.JSONObject;

public class TrendsHashtags  extends Configured implements Tool{

	
	public static class TrendMapper1 extends Mapper<LongWritable, Text, Text, IntWritable> {
		private final static IntWritable one = new IntWritable(1);
		public Text word = new Text();

		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {


			String line = value.toString();

			try {

				JSONArray js = new JSONObject(line).getJSONObject("entities").getJSONArray("hashtags");
				for (int i = 0; i < js.length(); i++) {
					String g = js.getJSONObject(i).getString("text").toLowerCase();
					if (Utils.isAlpha(g)) {
						word.set(g);
						context.write(word, one);
					}
				}
			} catch (Exception e) {
			}

		}
	}

	public static class TrendReducer1 extends Reducer<Text, IntWritable, Text, IntWritable> {
	    
	    @Override
	    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
	        int sum = 0;
	        for (IntWritable val : values) {
	            sum += val.get();
	        }
	        context.write(key, new IntWritable(sum));
	    }
	}
	
	public static int runStep1(String inputPath , String tempPath ) throws IOException, ClassNotFoundException, InterruptedException
	{
		Configuration conf = new Configuration();
        Job job1 = Job.getInstance(conf, "jobTrend");
       job1.setJarByClass(TrendsHashtags.class);       
        
        job1.setOutputKeyClass(Text.class);
        job1.setOutputValueClass(IntWritable.class);
        
        job1.setMapperClass(TrendsHashtags.TrendMapper1.class);
        job1.setCombinerClass(TrendsHashtags.TrendReducer1.class);
        job1.setReducerClass(TrendsHashtags.TrendReducer1.class);
        
        job1.setInputFormatClass(TextInputFormat.class);
        job1.setOutputFormatClass(TextOutputFormat.class);
        
        FileInputFormat.setInputDirRecursive(job1, true);
        FileInputFormat.setInputPaths(job1, new Path(inputPath));
        FileOutputFormat.setOutputPath(job1, new Path(tempPath));

        boolean succ = job1.waitForCompletion(true);
        if (! succ) {
          System.out.println("Job1 failed, exiting");
          return -1;
        }
        return 0;
	}
	
	public static class TrendMapper2 extends Mapper<LongWritable, Text, LongWritable, Text>{
		

	    @Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
	    	
	        String line = value.toString(); // agilencr 4
	        StringTokenizer tokenizer = new StringTokenizer(line);
	        while (tokenizer.hasMoreTokens()) {
	        	
	        	String token = tokenizer.nextToken();	
	        
	        // Context here is like a multi set which allocates value "one" for key "word".
	        	
	        	context.write(new LongWritable(Long.parseLong(tokenizer.nextToken().toString())), new Text(token));      	
	        	
	        }
	    }

	}

	public static class TrendReducer2 extends Reducer<LongWritable, Text, Text, Text> {

		@Override
		protected void reduce(LongWritable key, Iterable<Text> trends, Context context)
				throws IOException, InterruptedException {

			 for (Text val : trends) {
				 context.write(new Text(val.toString()),new Text(key.toString()));
		        }
		}
	}
	
	public static int runStep2(String tempPath ,  String outputPath) throws IOException, ClassNotFoundException, InterruptedException
	{
		Configuration conf = new Configuration();
	        Job job2 =Job.getInstance(conf, "jobTrendSorted");
	        job2.setJarByClass(TrendsHashtags.class);
	        FileInputFormat.setInputPaths(job2, new Path(tempPath));
	        FileOutputFormat.setOutputPath(job2, new Path(outputPath));
	        job2.setMapperClass(TrendsHashtags.TrendMapper2.class);
	        job2.setReducerClass(TrendsHashtags.TrendReducer2.class);
	        job2.setInputFormatClass(TextInputFormat.class);
	        job2.setMapOutputKeyClass(LongWritable.class);
	        job2.setMapOutputValueClass(Text.class);
	        job2.setSortComparatorClass(LongWritable.DecreasingComparator.class);
	        job2.setOutputFormatClass(TextOutputFormat.class);
	        boolean succ = job2.waitForCompletion(true);
	        if (! succ) {
	          System.out.println("Job2 failed, exiting");
	          return -1;
	        }
	        
	        return 0;
	}
	
	
	@Override
	public int run(String[] arg0) throws Exception {
		
		if ( runStep1(arg0[0] , arg0[1]) == -1) return -1 ;
		return runStep2(arg0[1] , arg0[2]);
		
	}
	
	
	
	public static int runTrendingHashtag(String inputJson, String tempPathHashtag, String outputJson) throws ClassNotFoundException, IOException, InterruptedException
	{
		if ( runStep1(inputJson , tempPathHashtag) == -1) return -1 ;
		return runStep2(tempPathHashtag , outputJson);
	}
	
	
	

    public static void main(String[] args) throws Exception {
    	
    
    	
        int res = ToolRunner.run(new TrendsHashtags(), args);
        System.exit(res);
    }
	
	
	
	
}
