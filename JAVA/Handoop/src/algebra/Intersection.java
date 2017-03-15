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

public class Intersection  extends Configured implements Tool{
	Intersection in = this;
	
	public  class MapperIntesection extends Mapper<LongWritable, Text, Text, NullWritable> {
		@Override
		public void map(LongWritable key, Text value, Context context) {

			try {
				String Words[] = in.decomposeline(value.toString());
				for (String word : Words)
				context.write(new Text(word), NullWritable.get());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static class ReducerIntesection extends Reducer<Text, NullWritable, Text, NullWritable> {

		@Override
		protected void reduce(Text key, Iterable<NullWritable> values, Context context)
				throws IOException, InterruptedException {
			int count = 0;
			for (NullWritable val : values) 
			{
				count++;
				if (count == 2)
				{

					context.write(key, NullWritable.get());
					return;
				}
			}
			
		}
	}

	@Override
	public int run(String[] arg0) throws Exception {

		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "JobIntesection");
		job.setJarByClass(Intersection.class);

		job.setMapperClass(MapperIntesection.class);
		job.setReducerClass(ReducerIntesection.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);

		FileInputFormat.setInputDirRecursive(job, true);
		FileInputFormat.setInputPaths(job, new Path(arg0[0]));
		FileOutputFormat.setOutputPath(job, new Path(arg0[1]));

		return job.waitForCompletion(true) ? 0 : 1;


	}

	
	public  String[] decomposeline(String line)
	{
		return line.split(" ");
	}
	
	
}
