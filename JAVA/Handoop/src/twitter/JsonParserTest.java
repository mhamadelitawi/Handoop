package twitter;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.json.*;

public class JsonParserTest {

	public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {
		private final static IntWritable one = new IntWritable(1);
		Text word = new Text();

		@Override
		public void map(LongWritable key, Text value, Context context) {

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

	public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {

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

	static Configuration  activateJsonParser(String inputPath, String outputPath) {
		try {
			Configuration conf = new Configuration();
			Job job = Job.getInstance(conf, "sonParser");
			
			job.setJarByClass(JsonParserTest.class);
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(IntWritable.class);
			
			job.setMapperClass(JsonParserTest.Map.class);
			job.setCombinerClass(JsonParserTest.Reduce.class);
			job.setReducerClass(JsonParserTest.Reduce.class);
			
			FileInputFormat.setInputDirRecursive(job, true);
			FileInputFormat.addInputPath(job, new Path(inputPath));
			FileOutputFormat.setOutputPath(job, new Path(outputPath));
			// System.exit(job.waitForCompletion(true) ? 0 : 1);
			 job.waitForCompletion(true);
			 return conf;
		} catch (IOException | ClassNotFoundException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
