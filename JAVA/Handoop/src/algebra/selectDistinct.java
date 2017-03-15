package algebra;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;


public class selectDistinct extends Configured implements Tool{
	public static class MapperselectDistinct extends Mapper<LongWritable, Text, Text, NullWritable> {
		@Override
		public void map(LongWritable key, Text value, Context context) {

			try {
				String Relation[] = value.toString().split(",");
				context.write(new Text(Relation[0]), NullWritable.get());
				context.write(new Text(Relation[1]), NullWritable.get());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static class ReducerselectDistinct extends Reducer<Text, NullWritable, Text, NullWritable> {

		@Override
		protected void reduce(Text key, Iterable<NullWritable> values, Context context)
				throws IOException, InterruptedException {
			context.write(key, NullWritable.get());
		}
	}
	
	
	
	public static int runselectDistinct(String input, String output)
			throws IllegalArgumentException, IOException, ClassNotFoundException, InterruptedException {

		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "JobselectDistinct"+input);
		job.setJarByClass(selectDistinct.class);

		job.setMapperClass(MapperselectDistinct.class);
		job.setReducerClass(ReducerselectDistinct.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);

		FileInputFormat.setInputDirRecursive(job, true);
		FileInputFormat.setInputPaths(job, new Path(input));
		FileOutputFormat.setOutputPath(job, new Path(output));

		return job.waitForCompletion(true) ? 0 : 1;

	}
	
	
	
	@Override
	public int run(String[] arg0) throws Exception {


		runselectDistinct(arg0[0], arg0[1]);

		
		return 0;
	}

	
	
}
