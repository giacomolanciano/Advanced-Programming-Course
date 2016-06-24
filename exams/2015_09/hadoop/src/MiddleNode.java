import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Collections;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.ToolRunner;



public class MiddleNode {
	
	private static final String JOB_NAME = "MiddleNode";
	private static final String MAP_TAG_1 = "[mapper1] ";
	private static final String RED_TAG_1 = "[reducer1] ";
	
	static int printUsage() {
		System.out.println(JOB_NAME + " [-r <reducers>] <input> <output>");
		ToolRunner.printGenericCommandUsage(System.out);
		return -1;
	}

	public static void main(String[] args) throws Exception {
		
		List<String> otherArgs = new ArrayList<String>();

		Configuration conf = new Configuration();
		
		for(int i=0; i < args.length; ++i) {
			try {
				if ("-r".equals(args[i])) { 
					//to customize the number of reducers
					conf.setInt("mapreduce.job.reduces", Integer.parseInt(args[++i]));
				} else {
					otherArgs.add(args[i]);
				}
			} catch (NumberFormatException except) {
				System.out.println("ERROR: Integer expected instead of " + args[i]);
				System.exit(printUsage());
			} catch (ArrayIndexOutOfBoundsException except) {
				System.out.println("ERROR: Required parameter missing from " +
						args[i-1]);
				System.exit(printUsage());
			}
		}

		// Make sure there are exactly 2 parameters left.
		if (otherArgs.size() != 2) {
			System.out.println("ERROR: Wrong number of parameters: " +
					otherArgs.size() + " instead of 2.");
			System.exit(printUsage());
		}
		
		//take input and output folders from command line
		Path input = new Path(otherArgs.get(0));
		Path output =new Path(otherArgs.get(1));
		
		
		
		Job job = Job.getInstance(conf);
		job.setJarByClass(MiddleNode.class);
		job.setJobName(JOB_NAME);
		
		FileInputFormat.addInputPath(job, input);
		//FileOutputFormat.setOutputPath(job, tmpOut);
		FileOutputFormat.setOutputPath(job, output);

		job.setMapperClass(MyMapper.class);
		//job.setCombinerClass(MyReducer.class);
		job.setReducerClass(MyReducer.class);

	
		job.setInputFormatClass(KeyValueTextInputFormat.class);
		
		//to specify the types of intermediate result key and value
		job.setMapOutputKeyClass(IntWritable.class);
		job.setMapOutputValueClass(IntWritable.class);

		//to specify the types of output key and value
		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(Text.class);

		job.waitForCompletion(true);
		
	}

	
	public static class MyMapper extends Mapper<Text, Text, IntWritable, IntWritable> {

		private IntWritable key = new IntWritable();
		private IntWritable zero = new IntWritable(0);
		private IntWritable one = new IntWritable(1);
		int xi, yi;

		@Override
		protected void map(Text x, Text y, Context context)
				throws IOException, InterruptedException {
			
			xi = Integer.parseInt(x.toString());
			yi = Integer.parseInt(y.toString());
			
			if (xi < yi) {
				key.set(yi);		// boxing
				context.write(key, zero);
				
				key.set(xi);		// boxing
				context.write(key, one);
			}
			
		}
	}

	public static class MyReducer extends Reducer<IntWritable, IntWritable, IntWritable, Text> {

		private Text dontCare = new Text();
		boolean oneFound, zeroFound;

		@Override
		protected void reduce(IntWritable key, Iterable<IntWritable> values, Context context)
						throws IOException, InterruptedException {
			
			oneFound = false;
			zeroFound = false;
			
			for(IntWritable x : values) {
				
				if(x.get() == 0) zeroFound = true;
				else if(x.get() == 1) oneFound = true;
				
				if(oneFound && zeroFound) break;
			}
			
			if(oneFound && zeroFound) 
				context.write(key, dontCare);
		}
	}
	
}
