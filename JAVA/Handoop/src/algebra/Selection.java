package algebra;

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


//Select word from a file
public class Selection extends Configured implements Tool {

	public static class SelectionMapper extends Mapper<LongWritable, Text, Text, NullWritable> {
		@Override
		public void map(LongWritable key, Text value, Context context) {
			try {

			//	if (value.toString().contains(context.getConfiguration().get("selection")))
				if (value.toString().contains(context.getConfiguration().get("selection")))
				context.write(value, NullWritable.get());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public int run(String[] arg0) throws Exception {

		Configuration conf = new Configuration();
		conf.set("selection", arg0[2]);
		Job job = Job.getInstance(conf, "Selection"+arg0[0]);
		job.setJarByClass(Selection.class);
		job.setMapperClass(SelectionMapper.class);

		job.setNumReduceTasks(0);

		FileInputFormat.setInputDirRecursive(job, true);
		FileInputFormat.setInputPaths(job, new Path(arg0[0]));
		FileOutputFormat.setOutputPath(job, new Path(arg0[1]));

		return job.waitForCompletion(true) ? 0 : 1;

	}

	

}
