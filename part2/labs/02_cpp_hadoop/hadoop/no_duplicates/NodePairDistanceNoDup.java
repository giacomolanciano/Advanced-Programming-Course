import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.ToolRunner;


public class NodePairDistanceNoDup {
	
	private static final String TEMP_DIR = "/outDistanceNoDupTemp";

	static int printUsage() {
		System.out.println("NodePairDistanceNoDup [-r <reduces>] <input> <output>");
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
		
		//tmp folder for first pass output
		Path tmpOut = new Path(TEMP_DIR);
		
		Job job = Job.getInstance(conf);
		job.setJarByClass(NodePairDistanceNoDup.class);
		job.setJobName("NodePairDistanceNoDup");
		
		FileInputFormat.addInputPath(job, input);
		FileOutputFormat.setOutputPath(job, tmpOut);

		job.setMapperClass(MyMapper.class);
		//job.setCombinerClass(MyReducer.class);
		job.setReducerClass(MyReducer.class);

		// An InputFormat for plain text files. 
		// Files are broken into lines. Either linefeed or carriage-return are used 
		// to signal end of line. Keys are the position in the file, and values 
		// are the line of text.
		job.setInputFormatClass(TextInputFormat.class);
		
		//to specify the types of intermediate result key and value
		job.setMapOutputKeyClass(IntWritable.class);
		job.setMapOutputValueClass(EdgeWritable.class);	 //it is the custom type defined below

		//to specify the types of output key and value
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		job.waitForCompletion(true);
		
		
		/*
		 * begin of second pass, duplicates elimination */
		 
		job = Job.getInstance(conf);
		job.setJarByClass(NodePairDistanceNoDup.class);
		job.setJobName("NodePairDistanceNoDup");
		
		FileInputFormat.addInputPath(job, tmpOut);
		FileOutputFormat.setOutputPath(job, output);

		job.setMapperClass(MyDupRemMapper.class);
		//job.setCombinerClass(MyReducer.class);
		job.setReducerClass(MyDupRemReducer.class);

		/* 
		 * An InputFormat for plain text files.
		 * Lines are borken in tokens, using space as delimiters */
		job.setInputFormatClass(KeyValueTextInputFormat.class);

		//to specify the types of output key and value
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		job.waitForCompletion(true);
	}
	
	
	public static class MyMapper extends Mapper<LongWritable, Text, IntWritable, EdgeWritable>{
		
		private IntWritable mapOutKey = new IntWritable();
		
		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
					
			String edge = value.toString();   
			String[] tok = edge.split(" ");
			String start = tok[0];
			String end = tok[1];
			int startInt = Integer.parseInt(start);
			int endInt = Integer.parseInt(end);
			
			mapOutKey.set(startInt);
			context.write(mapOutKey,							   //a
							new EdgeWritable(startInt, endInt));   //(a b)
			
			mapOutKey.set(endInt);
			context.write(mapOutKey,							   //b
							new EdgeWritable(startInt, endInt));   //(a b)
			
	   	}
	}
	
	
	public static class MyReducer extends Reducer<IntWritable, EdgeWritable, Text, Text>{

		private List<Integer> S = new ArrayList<Integer>();
		private List<Integer> E = new ArrayList<Integer>();
		private Text startText = new Text();
		private Text endText = new Text();
		
		@Override
		protected void reduce(IntWritable key, Iterable<EdgeWritable> values, Context context)
				throws IOException, InterruptedException {
					
			S.clear();
			E.clear();
			
			Iterator<EdgeWritable> it = values.iterator();
			
			/* 
			 * necessary to verify equality between node and key.
			 * without declaring it outside the loop, output seems empty */ 
			int keyInt = key.get();
			int start, end;
			EdgeWritable v;
			
			while (it.hasNext()) {
				v = it.next();
				start = v.getX();
				end = v.getY();
				
				if (start == keyInt)
					E.add(end);
				else 
					S.add(start);
			}
			
			for (Integer a : S)
				for(Integer b : E) {
					startText.set(a.toString());
					endText.set(b.toString());
					context.write(startText, endText);
				}
			
			
		}
	}
	
	
	public static class MyDupRemMapper extends Mapper<Text,Text,Text,Text> {
		private Text dummy = new Text();
		private Text edge = new Text();

		@Override
		protected void map(Text x, Text y, Context context)
				throws IOException, InterruptedException {
			edge.set(x.toString()+";"+y.toString());
			context.write(edge, dummy);
		}
	}


	public static class MyDupRemReducer extends Reducer<Text,Text,Text,Text> {
		private Text x = new Text();
		private Text y = new Text();

		@Override
		protected void reduce(Text key, Iterable<Text> dummy, Context context)
				throws IOException, InterruptedException {
			String[] edge = key.toString().split(";");
			x.set(edge[0]);
			y.set(edge[1]);
			context.write(x, y);
		}
	}
	
	public static class EdgeWritable implements Writable {
		
		private int x, y;
		
		public EdgeWritable(){
			 /*
			  * NECESSARY, defining a non-empty constructor we "lose" the default one.
			  * when it is called by the reducer a NoSuchMethodException arise,
			  * if not defined */
			 x = 0;
			 y = 0;
		}
		
		public EdgeWritable(int a, int b) {
			x = a;
			y = b;
		}
		
		@Override
		public void readFields(DataInput in) throws IOException {
			x = in.readInt();
			y = in.readInt();
		}
		
		@Override
		public void write(DataOutput out) throws IOException {
			out.writeInt(x);
			out.writeInt(y);
		}
		
		public int getX() {
			return x;
		}
		
		public int getY() {
			return y;
		}
		
	}
	
}
