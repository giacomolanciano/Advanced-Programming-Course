import java.io.DataInput;
import java.io.DataOutput;
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



public class Undirected {
	
	private static final String JOB_NAME = "Undirected";
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
		job.setJarByClass(Undirected.class);
		job.setJobName(JOB_NAME);
		
		FileInputFormat.addInputPath(job, input);
		//FileOutputFormat.setOutputPath(job, tmpOut);
		FileOutputFormat.setOutputPath(job, output);

		job.setMapperClass(MyMapper.class);
		//job.setCombinerClass(MyReducer.class);
		job.setReducerClass(MyReducer.class);

	
		job.setInputFormatClass(KeyValueTextInputFormat.class);
		
		//to specify the types of intermediate result key and value
		job.setMapOutputKeyClass(EdgeWritable.class);
		job.setMapOutputValueClass(DontCareWritable.class);

		//to specify the types of output key and value
		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(IntWritable.class);

		job.waitForCompletion(true);
		
	}

	
	public static class MyMapper extends Mapper<Text, Text, EdgeWritable, DontCareWritable> {

		private EdgeWritable key = new EdgeWritable();
		private DontCareWritable value = new DontCareWritable();
		int xi, yi;

		@Override
		protected void map(Text x, Text y, Context context)
				throws IOException, InterruptedException {
			
			xi = Integer.parseInt(x.toString());
			yi = Integer.parseInt(y.toString());
			
			if(xi < yi) {
				key.setX(xi);		// boxing
				key.setY(yi);		// boxing
			} else {
				key.setX(yi);		// boxing
				key.setY(xi);		// boxing
			}
			
			context.write(key, value);
			
		}
	}

	public static class MyReducer extends Reducer<EdgeWritable, DontCareWritable, IntWritable, IntWritable> {

		private IntWritable xt = new IntWritable();
		private IntWritable yt = new IntWritable();
		private HashSet<EdgeWritable> edges = new HashSet<EdgeWritable>();
		private EdgeWritable auxEdge = new EdgeWritable();
		boolean in;

		@Override
		protected void reduce(EdgeWritable key, Iterable<DontCareWritable> values, Context context)
						throws IOException, InterruptedException {
				
			xt.set(key.getX());
			yt.set(key.getY());
			
			context.write(xt, yt);
			
			/*
			 * NOTE: do not write the inverse edge if x = y
			 * (otherwise a duplicate is created)
			 * */
			if(xt.get() != yt.get())
				context.write(yt, xt);
			
		}
	}
	
	public static class EdgeWritable implements WritableComparable<EdgeWritable> {
		
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
		
		@Override
		public int compareTo(EdgeWritable o) {
			if(this.x < o.getX()) return -1;
			if(this.x > o.getX()) return 1;
			return new Integer(this.y).compareTo(o.getY());
		}
		
		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			EdgeWritable pair = (EdgeWritable) o;
			
			return (getX() == pair.getX() && getY() == pair.getY())
					|| (getX() == pair.getY() && getY() == pair.getX());

		}
		
		public int getX() {
			return x;
		}
		
		public int getY() {
			return y;
		}
		
		public void setX(int x) {
			this.x = x;
		}
		
		public void setY(int y) {
			this.y = y;
		}
		
	}
	
	public static class DontCareWritable implements WritableComparable<DontCareWritable> {
		
		/*
		 * "dumb" class, map out value is not relevant in this implementation
		 * */
		
		private int x;
		
		public DontCareWritable(){
			 x = 0;
		}
		
		@Override
		public void readFields(DataInput in) throws IOException {
			x = in.readInt();
		}
		
		@Override
		public void write(DataOutput out) throws IOException {
			out.writeInt(x);
		}
		
		@Override
		public int compareTo(DontCareWritable o) {
			return 0;
		}
		
		public int getX() {
			return x;
		}
		
		public void setX(int x) {
			this.x = x;
		}
		
	}
	
}
