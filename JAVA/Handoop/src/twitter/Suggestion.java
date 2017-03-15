package twitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class Suggestion extends Configured implements Tool {

	public static class MapperDifferentUser extends Mapper<LongWritable, Text, Text, NullWritable> {
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

	public static class ReducerDifferentUser extends Reducer<Text, NullWritable, Text, NullWritable> {

		@Override
		protected void reduce(Text key, Iterable<NullWritable> values, Context context)
				throws IOException, InterruptedException {
			context.write(key, NullWritable.get());
		}
	}

	public static int runDifferentUser(String nodesLocation, String DifferentUser)
			throws IllegalArgumentException, IOException, ClassNotFoundException, InterruptedException {

		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "JobDifferentUser");
		job.setJarByClass(Suggestion.class);

		job.setMapperClass(MapperDifferentUser.class);
		job.setReducerClass(ReducerDifferentUser.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);

		FileInputFormat.setInputDirRecursive(job, true);
		FileInputFormat.setInputPaths(job, new Path(nodesLocation));
		FileOutputFormat.setOutputPath(job, new Path(DifferentUser));

		return job.waitForCompletion(true) ? 0 : 1;

	}

	public static class MapperFillFollowers extends Mapper<LongWritable, Text, Text, Text> {

		@Override
		public void map(LongWritable key, Text value, Context context) {

			try {
				String Relation[] = value.toString().split(",");
				context.write(new Text(Relation[0]), new Text(Relation[1]));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static class ReducerFillFollowers extends Reducer<Text, Text, Text, Text> {

		MultipleOutputs<Text, Text> multipleout;
		@Override
		public void setup(Context context) {
			multipleout = new MultipleOutputs<Text, Text>(context);
		}
		@Override
		protected void reduce(Text key, Iterable<Text> values, Context context){
			try {
				for (Text val : values)   multipleout.write(key.toString(), key, val);
				} catch (Exception e) {}
		}
		@Override
		protected void cleanup(Context context) throws IOException, InterruptedException {
			multipleout.close();
		}

	}

	public static int runFillFollowers(String nodesLocation, String DifferentUser, String FillFollowers)
			throws IOException, ClassNotFoundException, InterruptedException {

		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "jobFillFollowers");

		job.setJarByClass(Suggestion.class);

		FileInputFormat.setInputDirRecursive(job, true);
		FileInputFormat.setInputPaths(job, new Path(nodesLocation));

		Path pt = new Path(DifferentUser + "//part-r-00000");
		FileSystem fs = FileSystem.get(new Configuration());
		BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(pt)));
		String line = br.readLine();
		while (line != null) {
			System.out.println("User : "+line);
			MultipleOutputs.addNamedOutput(job, line, TextOutputFormat.class, Text.class, Text.class);
			line = br.readLine();
		}

		FileOutputFormat.setOutputPath(job, new Path(FillFollowers));

		job.setMapperClass(MapperFillFollowers.class);
		job.setReducerClass(ReducerFillFollowers.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		return (job.waitForCompletion(true) ? 0 : 1);
	}

	public static class MapperSameFollowers extends Mapper<LongWritable, Text, Text, IntWritable> {
		private final static IntWritable one = new IntWritable(1);
		private final static IntWritable zero = new IntWritable(50);
		// protected to allow unit testing
		public Text word = new Text();

		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

			String line = value.toString();
			try {
				String t[] = line.split("\t");

				String g = t[1].toLowerCase();
				if (!g.equals("")) {
					word.set(g);

					if (context.getConfiguration().get("selection").equals(t[0])
							|| context.getConfiguration().get("selection").equals(t[1]))
						context.write(word, zero);
					else
						context.write(word, one);
				}

			} catch (Exception e) {
			}

		}
	}

	public static class ReducerSameFollowers extends Reducer<Text, IntWritable, Text, IntWritable> {
		@Override
		protected void reduce(Text key, Iterable<IntWritable> values, Context context)
				throws IOException, InterruptedException {
			int sum = 0;
			for (IntWritable val : values) {
				if (val.get() == 50 || val.get() == 0)
					return;
				sum += val.get();
			}
			context.write(key, new IntWritable(sum));
		}
	}

	public static int runSameFollowers(String user, String FillFollowers, String SameFollowers)
			throws IOException, ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		conf.set("selection", user);
		Job job1 = Job.getInstance(conf, "SameFollowers");
		job1.setJarByClass(Suggestion.class);

		MultipleInputs.addInputPath(job1, new Path(FillFollowers + "/part-r-00000"), TextInputFormat.class,
				MapperSameFollowers.class);

		Path pt = new Path(FillFollowers + "/" + user + "-r-00000");
		MultipleInputs.addInputPath(job1, pt, TextInputFormat.class, MapperSameFollowers.class);
		FileSystem fs = FileSystem.get(new Configuration());
		BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(pt)));
		String line= br.readLine();
		while (line != null) {
			String[] u = line.split("\t");
			try {
				Path p = new Path(FillFollowers + "/" + u[1] + "-r-00000");
				if (fs.exists(p)) {
					MultipleInputs.addInputPath(job1, p, TextInputFormat.class, MapperSameFollowers.class);
					System.out.println("Added user = " + u[1]);
				}
			} catch (Exception e) {

			}
			line = br.readLine();
		}

		job1.setOutputKeyClass(Text.class);
		job1.setOutputValueClass(IntWritable.class);
		job1.setMapperClass(MapperSameFollowers.class);

		job1.setReducerClass(ReducerSameFollowers.class);
		job1.setOutputFormatClass(TextOutputFormat.class);
		FileOutputFormat.setOutputPath(job1, new Path(SameFollowers));

		boolean succ = job1.waitForCompletion(true);
		if (!succ) {
			System.out.println("Job1 failed, exiting");
			return -1;
		}

		return 0;
	}

	public static class MapperFollowersSorted extends Mapper<LongWritable, Text, LongWritable, Text> {

		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String line = value.toString(); // agilencr 4
			StringTokenizer tokenizer = new StringTokenizer(line);
			while (tokenizer.hasMoreTokens()) {
				String token = tokenizer.nextToken();
				context.write(new LongWritable(Long.parseLong(tokenizer.nextToken().toString())), new Text(token));
			}
		}

	}

	public static class ReducerFollowersSorted extends Reducer<LongWritable, Text, Text, Text> {

		@Override
		protected void reduce(LongWritable key, Iterable<Text> trends, Context context)
				throws IOException, InterruptedException {
			for (Text val : trends) {
				context.write(new Text(val.toString()), new Text(key.toString()));
			}
		}
	}

	public static int runFollowersSorted(String user, String SameFollowers, String SameFollowersSorted)
			throws IOException, ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		Job job2 = Job.getInstance(conf, "jobSameFollowersSorted");
		job2.setJarByClass(Suggestion.class);
		FileInputFormat.setInputPaths(job2, new Path(SameFollowers));
		FileOutputFormat.setOutputPath(job2, new Path(SameFollowersSorted));
		job2.setMapperClass(MapperFollowersSorted.class);
		job2.setReducerClass(ReducerFollowersSorted.class);

		job2.setInputFormatClass(TextInputFormat.class);
		job2.setMapOutputKeyClass(LongWritable.class);
		job2.setMapOutputValueClass(Text.class);
		job2.setSortComparatorClass(LongWritable.DecreasingComparator.class);
		job2.setOutputFormatClass(TextOutputFormat.class);
		boolean succ = job2.waitForCompletion(true);
		Utils.deleteDestinationHDFS(SameFollowers);
		if (!succ) {
			System.out.println("Job2 failed, exiting");
			return -1;
		}

		return 0;
	}

	@Override
	public int run(String[] arg0) throws Exception {

		String user = "739";
		String nodesLocation = "mininodes";
		String DifferentUser = "DifferentUser";
		String FillFollowers = "FillFollowers";
		String SameFollowers = "SameFollowers";
		String SameFollowersSorted = "SameFollowersSorted";

		runDifferentUser(nodesLocation, DifferentUser);
		runFillFollowers(nodesLocation, DifferentUser, FillFollowers);
		runSameFollowers(user, FillFollowers, SameFollowers);
		runFollowersSorted(user, SameFollowers, SameFollowersSorted);
		
		return 0;
	}

	
	public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new Suggestion(), args);
		System.exit(res);
	}

}
